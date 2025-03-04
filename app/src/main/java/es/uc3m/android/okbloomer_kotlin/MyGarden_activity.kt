package es.uc3m.android.okbloomer_kotlin

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import es.uc3m.android.okbloomer_kotlin.GreetingPreview
import es.uc3m.android.okbloomer_kotlin.R
import es.uc3m.android.okbloomer_kotlin.datas.Plant_data
import es.uc3m.android.okbloomer_kotlin.ui.theme.OkBloomer_KotlinTheme

class MyGarden_activity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = this
            val plantList = remember { mutableStateListOf<HashMap<String,String>>() }
            // Load data whenComposable is created
            LaunchedEffect(Unit) {
                val data = readData(context)
                plantList.clear()
                plantList.addAll(data)
            }
            Displayingplants(plantList)
        }
    }
}


private fun readData(context : Context): List<HashMap<String,String>>{
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
    Box(modifier = Modifier.fillMaxSize()){
        LazyColumn {
            items(plantList){ plant -> PlantItem(plant) }
        }
    }
}


//single plant item as a composable
@Composable
fun PlantItem(plant: HashMap<String, String>) {
    val context = LocalContext.current
    Column (modifier = Modifier.fillMaxWidth()
        .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Button(onClick = {
            val intent = Intent(context, Plant_display::class.java)
            context.startActivity(intent)
        }) { Text("view plant") }// change the text for it to display the plant's nickname
    }

}




