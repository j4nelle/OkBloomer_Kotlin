package es.uc3m.android.okbloomer_kotlin

import android.app.Activity
import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.FeatureInfo
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.objectListOf
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import es.uc3m.android.okbloomer_kotlin.ui.theme.OkBloomer_KotlinTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import es.uc3m.android.okbloomer_kotlin.datas.Plant_data
import java.io.File
import coil.compose.AsyncImage
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

//old version
class Adding_Plant_activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            // Initiation of the variables
            var plant_nickname by remember { mutableStateOf("") }
            var plantID by remember { mutableStateOf("") }
            var plant_specie by remember { mutableStateOf("") }
            var watering_frequency by remember { mutableStateOf("") }
            var imageUri by remember { mutableStateOf<Uri?>(null) }
            var photo_path by remember { mutableStateOf("") }

            //context variable
            var context = LocalContext.current

            // variables for launching the camera access
            // camera paths are stored in xml file
            var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }

            // variables to get access and store the photo's path
            val uri = remember { mutableStateOf<Uri?>(null) }
            val photoFile = File(context.filesDir, "plant_${System.currentTimeMillis()}.jpg")

            val uriForFile = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                photoFile
            )

            //Camera launcher
            val cameraLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.TakePicture()
            ) { success ->
                if (success) {
                    imageUri = uriForFile
                } else {
                    Toast.makeText(context, "Failed to capture image", Toast.LENGTH_SHORT).show()
                }
            }

            //Permission launcher
            val permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission()
            ) { isGranted ->
                if (isGranted) {
                    cameraLauncher.launch(uriForFile)
                } else {
                    Toast.makeText(context, "Camera permission is required!", Toast.LENGTH_SHORT).show()
                }
            }

            //Gallery launcher
            // gallery paths are translated to absolute file paths

            val galleryLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.OpenDocument() // Open Document helps keep the URI when relaunching the app
            ) { uri: Uri? ->
                uri?.let {
                    try {
                        context.contentResolver.takePersistableUriPermission(
                            it,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION //changing permissions to still have access to the photo when relaunching
                        )
                        imageUri = it
                        photo_path = it.toString()
                        Log.d("Gallery Image", "Persisted URI: $photo_path")
                    } catch (e: SecurityException) {
                        Toast.makeText(context, "Unable to persist permission", Toast.LENGTH_SHORT).show()
                        e.printStackTrace()
                    }
                }
            }


            //function to be able to change Uri into files : files are what we send to the AI
            // as files cannot be directly created from content:// paths (the ones from the gallery pictures), we need to copy the content into tempFile
            fun uriToFile(context: Context, uri: Uri):File?{
                val inputStream = context.contentResolver.openInputStream(uri) ?: return null //gets the input stream of the uri if there is one
                val tempFile = File.createTempFile("upload_",".jpg", context.cacheDir)
                tempFile.outputStream().use { output->
                    inputStream.copyTo(output)
                }
                return tempFile

            }





            //function needed to send the picture of the plant to PlantNet
            fun FindSpecieWithPlantNet(context: Context, imageUri : Uri, onResult : (String?)-> Unit){
                val apikey = "2b10sy6Lu5wZTfeJ9uiZInTNIu"
                val imagefile = uriToFile(context, imageUri)

                //bonus lines : we send a message in the logcat in case imageUri is null
                if (imagefile == null) {
                    Log.e("PlantNet", "Failed to create file from URI")
                    onResult(null)
                    return
                }


                val client = OkHttpClient()
                val mediatype = "image/jpeg".toMediaTypeOrNull()

                //building the content of the request to send to the IA
                val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                    //.addFormDataPart("organs", "leaf") //recognition of plants according to their leaves
                    .addFormDataPart("organs", "flower") //recognition if flowers are in the photo
                    //.addFormDataPart("organs", "fruit") //if fruits are in the photo
                    .addFormDataPart("images",imagefile.name, imagefile.asRequestBody(mediatype))
                    .build()

                // building the request itself
                val request = Request.Builder()
                    .url("https://my-api.plantnet.org/v2/identify/all?api-key=$apikey")
                    .post(requestBody)
                    .build()

                client.newCall(request).enqueue( object :Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.e("PlantNet", "Request to the API failed",e)
                        onResult(null)
                    }


                    override fun onResponse(call: Call, response: Response) {
                        val responseBody = response.body?.string()

                        //Log to see if there's a response from the API
                        Log.d("PlantNet", "Raw Response: $responseBody")

                        if (!response.isSuccessful) {
                            //message in the logcat if there is no response (not working)
                            Log.e("PlantNet", "Request failed with code: ${response.code}")
                            onResult(null)
                            return
                        }

                        if (responseBody == null) {
                            //message in the logcat if the response exists but is null
                            Log.e("PlantNet", "Response body is null")
                            onResult(null)
                            return
                        }

                        try {
                            val jsonObject = JSONObject(responseBody)
                            val resultsArray = jsonObject.getJSONArray("results")

                            if (resultsArray.length() > 0) {
                                //if there are results, we need to manage the response from the API in a form we can comprehend
                                val firstResult = resultsArray.getJSONObject(0)
                                val species = firstResult.getJSONObject("species")
                                val scientificName = species.getString("scientificNameWithoutAuthor")

                                //logcat message to show the scientific name found for the plant
                                Log.d("PlantNet", "Identified specie: $scientificName")
                                onResult(scientificName)
                            } else {
                                //if no specie is found
                                Log.w("PlantNet", "No species found in response")
                                onResult(null)
                            }
                        } catch (e: Exception) {
                            //If there is a problem formating the answer
                            Log.e("PlantNet", "Error parsing JSON", e)
                            onResult(null)
                        }
                    }

                }
                )





            }



            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                //putting a title on top of the activity
                Text(fontSize = 22.sp, text = "Describe your new plant :", color = Color.Blue)

                //add space between elements
                Spacer(modifier = Modifier.size(12.dp))

                //creating a TextField for each variable
                TextField(value = plant_nickname,
                    label = ({ Text("Enter your plant's nickname") }),
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = {
                        plant_nickname = it
                    })

                Spacer(modifier = Modifier.size(12.dp))

                TextField(
                    value = plant_specie,
                    onValueChange = { plant_specie = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = ({ Text("Enter your plant's specie") })
                )

                Spacer(modifier = Modifier.size(12.dp))

                TextField(
                    value = watering_frequency,
                    onValueChange = { watering_frequency = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "What is the watering frequency ?") }
                )

                Spacer(modifier = Modifier.size(12.dp))

                Row {
                    Button(
                        onClick = {
                            //launches the access to camera
                            if (ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.CAMERA
                                ) == PackageManager.PERMISSION_GRANTED
                            ) {
                                cameraLauncher.launch(uriForFile)
                                Log.d("Photo Path", "Captured image URI: $uriForFile")

                                //updating the value of the path AS A STRING
                                photo_path = uriForFile.toString()
                            } else {
                                permissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(56.dp)
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        Text(text = "Take a picture", fontSize = 16.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    //displaying the image once you took it from camera
                    imageUri?.let { uri ->
                        AsyncImage(
                            model = uri,
                            contentDescription = "Plant Image",
                            modifier = Modifier.size(250.dp)
                        )
                    }



                    Button(
                    onClick = {
                        galleryLauncher.launch(arrayOf("image/*")) // This part changed to adapt to the new camera launcher

                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(56.dp)
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    Text(text = "From gallery", fontSize = 16.sp)
                }
                    Spacer(modifier = Modifier.height(16.dp))

                    //displaying the image once you chose it from gallery
                    imageUri?.let { uri ->
                        AsyncImage(
                            model = uri,
                            contentDescription = "Selected Image",
                            modifier = Modifier.size(250.dp)
                        )
                    }





                }




                Button(
                    onClick = {
                        keep_data(plant_nickname,
                            plant_specie,
                            watering_frequency,
                            imageUri?.toString() ?: "")

                        // gets the IA recognition of the specie when the picture is taken from the GALLERY
                        imageUri?.let {
                            FindSpecieWithPlantNet(context, imageUri!!) { iaSpecie ->
                                runOnUiThread {
                                    if (iaSpecie != null) {
                                        Log.d("PlantNet", "Identified specie: $iaSpecie")
                                        // TO DO : store iaSpecie somewhere
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Plant couldn't be identified",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }

                        }
                    },

                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(56.dp)
                        .clip(RoundedCornerShape(12.dp))

                ) {
                    Text("Add to Garden")
                    }
            }
        }
    }

    //saving image to internal storage
    private fun saveImage(bitmap: Bitmap?): File? {
        val fileName = "gallery_${System.currentTimeMillis()}.jpg"
        val file = File(applicationContext.filesDir, fileName)

        return try {
            val outputStream = FileOutputStream(file)
            if (bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            }
            outputStream.close()
            file
        } catch (e: IOException){
            e.printStackTrace()
            null
        }

    }


    private fun keep_data(plantNickname: String, plantSpecie: String, wateringFrequency: String, photoPath: String) {
        val plant_data = Plant_data(this)
        val autonumeric = plant_data.adding_new_plant(plantNickname, plantSpecie, wateringFrequency.toFloat(), -1, photoPath)

        Log.d("Photo Path", "Saving photo path: $photoPath")


        //creating a message to make sure the data is saved
        Toast.makeText(this, "id : $autonumeric", Toast.LENGTH_SHORT).show()

        //passing the id of the plant we just added to the activity Plant_display
        val intent = Intent(this, Plant_display::class.java).apply {
            putExtra("idplant", autonumeric.toString())
        }
        //making sure we start the garden activity to display the new plants once it's added
        startActivity(Intent(this, MyGarden_activity::class.java ))

    }

}


