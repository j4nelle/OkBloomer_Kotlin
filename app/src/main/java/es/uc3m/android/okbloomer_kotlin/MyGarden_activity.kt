package es.uc3m.android.okbloomer_kotlin

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import es.uc3m.android.okbloomer_kotlin.datas.PlantData
import es.uc3m.android.okbloomer_kotlin.ui.theme.OkBloomer_KotlinTheme

class MyGardenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = this
            val plantList = remember { mutableStateListOf<HashMap<String,String>>() }

            // Load data whenComposable is created
            LaunchedEffect(Unit) {
                val data = readData(context)
                plantList.clear()
                plantList.addAll(data)
            }
            OkBloomer_KotlinTheme {
                Displayingplants(plantList)
            }
        }
    }
}

fun readData(context : Context): List<HashMap<String,String>>{
        // function that get the data from the database
    val plantList = mutableListOf<HashMap<String, String>>()
    val plantdata = PlantData(context)
    val cursor: Cursor = plantdata.plantSelect(plantdata)

    if (cursor.moveToFirst()) {
        do {
            val map = hashMapOf(
                "idplant" to cursor.getString(cursor.getColumnIndexOrThrow("idplant")),
                "plant_nickname" to cursor.getString(cursor.getColumnIndexOrThrow("plant_nickname")),
                "plant_specie" to cursor.getString(cursor.getColumnIndexOrThrow("plant_specie")),
                "watering_frequency" to cursor.getString(cursor.getColumnIndexOrThrow("watering_frequency")),
                "typo" to cursor.getString(cursor.getColumnIndexOrThrow("typo"))
            )
            plantList.add(map)
        } while (cursor.moveToNext())
    }
    cursor.close()
    return plantList
}

@Composable
fun Displayingplants(plantList: List<HashMap<String, String>>){
    //function that display the plants in the garden


    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Image(
            painter = painterResource(id = R.drawable.background2),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )

        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(plantList){ plant -> PlantItem(plant)
            }
        }
    }
}

//single plant item as a composable

@Composable
fun PlantItem(plant: HashMap<String, String>) {
    val context = LocalContext.current

    val plantID = plant["idplant"]
    val plantnickname = plant["plant_nickname"]
    val plantspecie = plant["plant_specie"]
    val wateringfrequency = plant["watering_frequency"]
    val typo = plant["typo"]
    val photopath = plant["photo_path"]



    Column (modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Button(onClick = {
            val intent = Intent(context, PlantDisplay::class.java).apply {
                putExtra( "idplant", plantID ?:-1) // -1 in case idplant is null
                putExtra(plant["plant_nickname"], plantnickname)
                putExtra(plant["plant_specie"], plantspecie)
                putExtra(plant["watering_frequency"], wateringfrequency)
                putExtra(plant["typo"], typo)
                putExtra(plant["photo_path"], photopath)
            }
            context.startActivity(intent)
            }
            ,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(56.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            Text(text = plant["plant_nickname"].toString())
        }// change the text for it to display the plant's nickname
        Spacer(modifier = Modifier.height(12.dp))
    }
}

