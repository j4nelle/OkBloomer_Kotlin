package es.uc3m.android.okbloomer_kotlin

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import es.uc3m.android.okbloomer_kotlin.GreetingPreview
import es.uc3m.android.okbloomer_kotlin.R
import es.uc3m.android.okbloomer_kotlin.datas.Plant_data
import es.uc3m.android.okbloomer_kotlin.ui.theme.OkBloomer_KotlinTheme

//old code version
class MyGarden_activity : ComponentActivity() {
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
//
fun readData(context : Context): List<HashMap<String,String>>{
        // function that get the data from the database
    val plantList = mutableListOf<HashMap<String, String>>()
    val plant_data = Plant_data(context)
    val cursor: Cursor = plant_data.plantSelect(plant_data)

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

    //changing the color values
    val green = Color(0xFF810C784)
    val red = Color(0xD9EF5350)


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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(60.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
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

//single plant item as a composable

@Composable
fun PlantItem(plant: HashMap<String, String>) {
    val context = LocalContext.current

    val plantID = plant["idplant"]
    val plant_nickname = plant["plant_nickname"]
    val plant_specie = plant["plant_specie"]
    val watering_frequency = plant["watering_frequency"]
    val typo = plant["typo"]
    val photo_path = plant["photo_path"]

    //converting back the string photo_path as a Uri
    val imageUri = if (!photo_path.isNullOrEmpty()) Uri.parse(photo_path) else null

    Column (modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Button(onClick = {
            val intent = Intent(context, Plant_display::class.java).apply {
                putExtra( "idplant", plantID ?:-1) // -1 in case idplant is null
                putExtra(plant.get("plant_nickname"), plant_nickname)
                putExtra(plant.get("plant_specie"), plant_specie)
                putExtra(plant.get("watering_frequency"), watering_frequency)
                putExtra(plant.get("typo"), typo)
                putExtra(plant.get("photo_path"), photo_path)
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
            Text(text = plant.get("plant_nickname").toString())
        }// change the text for it to display the plant's nickname
        Spacer(modifier = Modifier.height(12.dp))
    }
}
@Preview(showBackground = true)
@Composable
fun MyGardenPreview() {
    val mockPlants = listOf(
        hashMapOf(
            "idplant" to "1",
            "plant_nickname" to "test",
            "plant_specie" to "test",
            "watering_frequency" to "1",
            "last_watered" to System.currentTimeMillis().toString(),
            "typo" to "0",
            "photo_path" to ""
        ),
        hashMapOf(
            "idplant" to "2",
            "plant_nickname" to "test1",
            "plant_specie" to "test1",
            "watering_frequency" to "2",
            "last_watered" to System.currentTimeMillis().toString(),
            "typo" to "0",
            "photo_path" to ""
        )
    )

    Displayingplants(plantList = mockPlants)
}


