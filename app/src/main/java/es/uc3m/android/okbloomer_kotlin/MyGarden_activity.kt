package es.uc3m.android.okbloomer_kotlin

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

        //get the information of the database
        readData()
    }

private fun readData() {
    val plant_list = ArrayList<HashMap<String, String>>()
    val plant_data = Plant_data(this)
    val cursor : Cursor = plant_data.plantSelect(plant_data)

    if (cursor.moveToFirst()){
        do {
            //getting each info for each variable
            val idplant = cursor.getString(cursor.getColumnIndexOrThrow("idplant"))
            val plant_nickname = cursor.getString(cursor.getColumnIndexOrThrow("plant_nickname"))
            val plant_specie = cursor.getString(cursor.getColumnIndexOrThrow("plant_specie"))
            val watering_frequency = cursor.getString(cursor.getColumnIndexOrThrow("watering_frequency"))
            val typo = cursor.getString(cursor.getColumnIndexOrThrow("typo"))

            //putting it as a list:
            val map = HashMap<String, String>()
            map.put("idplant", idplant)
            map.put("plant_nickname", plant_nickname)
            map.put("plant_specie", plant_specie)
            map.put("watering_frequency", watering_frequency)
            map.put("typo", typo)

            plant_list.add(map)
        }while (cursor.moveToNext())
        draw(plant_list)
    }
}

private fun draw(plantList: ArrayList<HashMap<String, String>>) { // function that "draws" the button of the different plants
    setContent{
        Box(modifier = Modifier.fillMaxSize()){
            LazyColumn (
                content = {
                    items(items = plantList, itemContent = {

                        Column (
                            modifier = Modifier.
                            fillMaxWidth()
                                .padding(4.dp)
                                .border(width = 1.dp, color = Color.Green, shape = RectangleShape)
                                .padding(24.dp)
                        ) {}

                        Text(text = it.get("idplant").toString())
                        Text(text = it.get("plant_nickname").toString())
                        Text(text = it.get("plant_specie").toString())
                        Text(text = it.get("watering_frequency").toString())
                        Text(text = it.get("typo").toString())
                    })
                }
            )//we use a lazy column so that the list is scrollable
        }
    }

}

}




