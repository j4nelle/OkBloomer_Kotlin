package es.uc3m.android.okbloomer_kotlin

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Paint.Align
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import coil.compose.AsyncImage
import es.uc3m.android.okbloomer_kotlin.GreetingPreview
import es.uc3m.android.okbloomer_kotlin.R
import es.uc3m.android.okbloomer_kotlin.datas.Plant_data
import es.uc3m.android.okbloomer_kotlin.ui.theme.OkBloomer_KotlinTheme
import es.uc3m.android.okbloomer_kotlin.MyGarden_activity

class Plant_display : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //get the plant id to display it
        val plantID = intent.getStringExtra("idplant")

        // get the rest of the info directly from the database
        val plantData = Plant_data(this)

        var plant_nickname = "Unknown"
        var plant_specie = "Unknown"
        var watering_frequency = "Unknown"
        var typo = "Unknown"
        var photo_path = "Unknowm"


        // reading the database :
        val cursor = plantData.readableDatabase.rawQuery(
            "SELECT * FROM mygarden WHERE idplant = ?", arrayOf(plantID ?:"-1")
        )
        if (cursor.moveToFirst()) {
            plant_nickname = cursor.getString(cursor.getColumnIndexOrThrow("plant_nickname"))
            plant_specie = cursor.getString(cursor.getColumnIndexOrThrow("plant_specie"))
            watering_frequency = cursor.getString(cursor.getColumnIndexOrThrow("watering_frequency"))
            typo = cursor.getString(cursor.getColumnIndexOrThrow("typo"))
            photo_path = cursor.getString(cursor.getColumnIndexOrThrow("photo_path"))
        }

        Log.d("Plant Info", "Retrieved photo path: $photo_path")

        cursor.close()
        setContent {
        Displaying_info(plantID.toString(), plant_nickname, plant_specie, watering_frequency, typo, photo_path)
        }

    }
}

@Composable
fun Displaying_info(
    plantID: String,
    plantNickname: String,
    plantSpecie: String,
    wateringFrequency: String,
    typo: String,
    photo_path:String // this parameter is now nullable
) {
    var context = LocalContext.current

    val imageUri = if (photo_path.isNotEmpty()) Uri.parse(photo_path) else null

    Box(
        modifier = Modifier.fillMaxSize()
    )
    {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
        Column (modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start)
        {
            Text(text = "Nickname : $plantNickname", fontSize = 18.sp)
            Text(text = "Specie : $plantSpecie",fontSize = 18.sp)
            Text(text = "Watering frequency :$wateringFrequency",fontSize = 18.sp)

            Spacer(modifier = Modifier.height(12.dp))

            //displaying the image + testing if the Uri is ok / exists
            if(imageUri!=null){
                Log.d("Plant Info", "Valid image URI: $imageUri")
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Plant Image",
                    modifier = Modifier.size(250.dp)
                )
            } else {
                Log.d("Plant Info", "Invalid or empty image URI")
                Text(text = "no image available given",
                    fontSize = 16.sp,
                    color = Color.Gray)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ){
                //delete button
                Button(
                    onClick = {
                        delete_data(plantID = plantID, context)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(56.dp)
                        .clip(RoundedCornerShape(12.dp))

                )
                {
                    Text(text = "Delete plant", fontSize = 18.sp)
                }


                //watering button
                Button(
                    onClick = {/*function that turns the button green when clicked + update the last watering date*/
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),//this color or red when it needs to be watered
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(56.dp)
                        .clip(RoundedCornerShape(12.dp)),

                    ) {
                    Text(text = "Watered", fontSize = 18.sp) //"Watered" or "Needs to be watered" according to the watering frequency
                }
            }
        }

    }
}


fun delete_data(plantID: String, context: Context) {
    val plant_data = Plant_data(context)
    val success = plant_data.deleting_a_plant(plantID)

    if (success) {
        // Message to see if the deletion worked :
        Toast.makeText(context, "plant deleted", Toast.LENGTH_SHORT).show()

        //getting back to the garden after deletion
        val intent = Intent(context, MyGarden_activity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        context.startActivity(intent)

    }
    else{
        //Message if the deletion failed
        Toast.makeText(context, "deletion failed", Toast.LENGTH_SHORT).show()
    }
}

