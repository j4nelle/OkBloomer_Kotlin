package es.uc3m.android.okbloomer_kotlin


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import coil.compose.AsyncImage
import es.uc3m.android.okbloomer_kotlin.datas.Plant_data


class AddingPlantActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            // Initiation of the variables
            var plant_nickname by remember { mutableStateOf("") }
            var plant_specie by remember { mutableStateOf("") }
            var watering_frequency by remember { mutableStateOf("") }
            var imageUri by remember { mutableStateOf<Uri?>(null) }
            var photo_path by remember { mutableStateOf("") }

            //context variable
            val context = LocalContext.current

            // variables to get access and store the photo's path
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
                        keepdata(plant_nickname,
                            plant_specie,
                            watering_frequency,
                            imageUri?.toString() ?: "")
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


    private fun keepdata(plantNickname: String, plantSpecie: String, wateringFrequency: String, photoPath: String) {
        val plantdata = Plant_data(this)
        val autonumeric = plantdata.adding_new_plant(plantNickname, plantSpecie, wateringFrequency.toFloat(), -1, photoPath)

        Log.d("Photo Path", "Saving photo path: $photoPath")


        //creating a message to make sure the data is saved
        Toast.makeText(this, "id : $autonumeric", Toast.LENGTH_SHORT).show()

        //passing the id of the plant we just added to the activity Plant_display
        /*val intent = Intent(this, PlantDisplay::class.java).apply {
            putExtra("idplant", autonumeric.toString())
        }*/
        //making sure we start the garden activity to display the new plants once it's added
        startActivity(Intent(this, MyGardenActivity::class.java ))

    }
}



