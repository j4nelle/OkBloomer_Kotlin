package es.uc3m.android.okbloomer_kotlin

import android.app.Activity
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
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
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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

class Adding_Plant_activity : ComponentActivity() {
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
            var context = LocalContext.current

            // variables for launching the camera access
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

                Button(
                    onClick = {
                        //launches the access to camera
                        if(ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CAMERA
                            ) == PackageManager.PERMISSION_GRANTED
                            ){
                            cameraLauncher.launch(uriForFile)
                            Log.d("Photo Path", "Captured image URI: $uriForFile")

                            //updating the value of the path AS A STRING
                            photo_path = uriForFile.toString()
                            }
                        else{
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(56.dp)
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    Text(text= "Take a picture", fontSize = 18.sp)
                }


                Spacer(modifier = Modifier.height(16.dp))

                //displaying the image once you took it
                imageUri?.let { uri ->
                    AsyncImage(
                        model = uri,
                        contentDescription = "Plant Image",
                        modifier = Modifier.size(250.dp)
                    )
                }


                Button(
                    onClick = {
                        keep_data(plant_nickname, plant_specie, watering_frequency, photo_path)
                    }
                    //colors =
                ) {
                    Text("Add to Garden")
                    // will have to switch to a text_button variable that says "added" when you click it
                }
            }
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


