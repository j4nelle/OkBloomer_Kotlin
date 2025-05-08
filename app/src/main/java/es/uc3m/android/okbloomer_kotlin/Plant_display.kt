package es.uc3m.android.okbloomer_kotlin

import android.app.Activity
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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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


//old code version
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
        var last_watered = "Unknown"
        var typo = "Unknown"
        var photo_path = "Unknown"

//
        // reading the database :
        val cursor = plantData.readableDatabase.rawQuery(
            "SELECT * FROM mygarden WHERE idplant = ?", arrayOf(plantID ?:"-1")
        )
        if (cursor.moveToFirst()) {
            plant_nickname = cursor.getString(cursor.getColumnIndexOrThrow("plant_nickname"))
            plant_specie = cursor.getString(cursor.getColumnIndexOrThrow("plant_specie"))
            watering_frequency = cursor.getString(cursor.getColumnIndexOrThrow("watering_frequency"))
            last_watered = cursor.getString(cursor.getColumnIndexOrThrow("last_watered"))
            typo = cursor.getString(cursor.getColumnIndexOrThrow("typo"))
            photo_path = cursor.getString(cursor.getColumnIndexOrThrow("photo_path"))
        }

        Log.d("Plant Info", "Retrieved photo path: $photo_path")

        cursor.close()
        setContent {
        Displaying_info(plantID.toString(), plant_nickname, plant_specie, watering_frequency, last_watered, typo, photo_path)
        }

    }
}


@Composable
fun Displaying_info(
    plantID: String,
    plantNickname: String,
    plantSpecie: String,
    wateringFrequency: String,
    lastWatered: String,
    typo: String,
    photo_path:String // this parameter is now nullable
) {
    var context = LocalContext.current

    val imageUri = if (photo_path.isNotEmpty()) Uri.parse(photo_path)
    else null

    var refresh by remember { mutableStateOf(false) }
    if (refresh) {
        refresh = false
    }

    val needsWatering = needsWater(lastWatered.toLong(), wateringFrequency.toFloat())
    val buttonText = if (needsWatering) "Needs Water" else "Watered"
    val buttonColor = if (needsWatering) Color(0xD9EF5350) else Color(0xFF810C784)

    Box(
        modifier = Modifier.fillMaxSize()
    )
    {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
        Column (modifier = Modifier
            .padding(16.dp)
            .align(Alignment.Center)
            .background(Color.White.copy(alpha = 0.7f), shape = RoundedCornerShape(8.dp))
            .padding(12.dp) ,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start)
        {
            Text(text = "Nickname : $plantNickname", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = "Specie : $plantSpecie",fontSize = 20.sp)
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = "Watering frequency :$wateringFrequency",fontSize = 20.sp)

            Spacer(modifier = Modifier.height(15.dp))

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
                Spacer(modifier = Modifier.height(15.dp))
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
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xD9EF5350)),
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(56.dp)
                        .clip(RoundedCornerShape(12.dp))

                )
                {
                    Text(text = "Delete plant", fontSize = 18.sp)
                }


                //watering button - turns the button green when clicked + update the last watering date (?)
                Button(
                    onClick = {
                        val success = Plant_data(context).plant_watered(plantID)
                        if (success) {
                            scheduleWateringNotification(
                                context,
                                plantID.toInt(),
                                plantNickname,
                                wateringFrequency.toFloat(),
                            )
                            Toast.makeText(context, "Marked as watered!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(context, MyGarden_activity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            context.startActivity(intent)
                        } else {
                            Toast.makeText(context, "Failed to update watering time", Toast.LENGTH_SHORT).show()
                        }

                    },
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(56.dp)
                        .clip(RoundedCornerShape(12.dp)),

                    ) {
                    Text(text = buttonText, fontSize = 18.sp)
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            //back button
            Button(onClick = { (context as? Activity)?.finish() }) {
                Text("Back")
            }
            //home button
            Button(onClick = {
                val intent = Intent(context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                context.startActivity(intent)
            }) {
                Text("Home")
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
@Preview(showBackground = true)
@Composable
fun DisplayingInfoPreview() {
    Displaying_info(
        plantID = "1",
        plantNickname = "Test",
        plantSpecie = "Test",
        wateringFrequency = "2",
        lastWatered = System.currentTimeMillis().toString(),
        typo = "0",
        photo_path = "" // leave blank for preview or add a test URI string
    )
}
