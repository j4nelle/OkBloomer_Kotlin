package es.uc3m.android.okbloomer_kotlin

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import es.uc3m.android.okbloomer_kotlin.datas.PlantData

class PlantDisplay : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //get the plant id to display it
        val plantID = intent.getStringExtra("idplant")

        // get the rest of the info directly from the database
        val plantData = PlantData(this)

        var plantnickname = "Unknown"
        var plantspecie = "Unknown"
        var wateringfrequency = "Unknown"
        //var typo = "Unknown"
        var photopath = "Unknown"

//
        // reading the database :
        val cursor = plantData.readableDatabase.rawQuery(
            "SELECT * FROM mygarden WHERE idplant = ?", arrayOf(plantID ?:"-1")
        )
        if (cursor.moveToFirst()) {
            plantnickname = cursor.getString(cursor.getColumnIndexOrThrow("plant_nickname"))
            plantspecie = cursor.getString(cursor.getColumnIndexOrThrow("plant_specie"))
            wateringfrequency = cursor.getString(cursor.getColumnIndexOrThrow("watering_frequency"))
            //typo = cursor.getString(cursor.getColumnIndexOrThrow("typo"))
            photopath = cursor.getString(cursor.getColumnIndexOrThrow("photo_path"))
        }

        Log.d("Plant Info", "Retrieved photo path: $photopath")

        cursor.close()
        setContent {
        Displaying_info(plantID.toString(), plantnickname, plantspecie, wateringfrequency, photopath)
        }

    }
}

@Composable
fun Displaying_info(
    plantID: String,
    plantNickname: String,
    plantSpecie: String,
    wateringFrequency: String,
    photopath:String // this parameter is now nullable
) {
    val context = LocalContext.current

    val imageUri = if (photopath.isNotEmpty()) Uri.parse(photopath)
    else null

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
                        deletedata(plantID = plantID, context)
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


fun deletedata(plantID: String, context: Context) {
    val plantdata = PlantData(context)
    val success = plantdata.deletingaplant(plantID)

    if (success) {
        // Message to see if the deletion worked :
        Toast.makeText(context, "plant deleted", Toast.LENGTH_SHORT).show()

        //getting back to the garden after deletion
        val intent = Intent(context, MyGardenActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        context.startActivity(intent)

    }
    else{
        //Message if the deletion failed
        Toast.makeText(context, "deletion failed", Toast.LENGTH_SHORT).show()
    }
}

