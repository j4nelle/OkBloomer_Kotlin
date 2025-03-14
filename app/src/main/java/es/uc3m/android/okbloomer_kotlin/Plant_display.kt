package es.uc3m.android.okbloomer_kotlin

import android.database.Cursor
import android.graphics.Paint.Align
import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import es.uc3m.android.okbloomer_kotlin.GreetingPreview
import es.uc3m.android.okbloomer_kotlin.R
import es.uc3m.android.okbloomer_kotlin.datas.Plant_data
import es.uc3m.android.okbloomer_kotlin.ui.theme.OkBloomer_KotlinTheme
import es.uc3m.android.okbloomer_kotlin.MyGarden_activity

class Plant_display : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    setContent {
        val plantID = intent.getStringExtra("plant_id")
        val plant_nickname = intent.getStringExtra("plant_nickname")
        val plant_specie = intent.getStringExtra("plant_specie")
        val watering_frequency = intent.getStringExtra("watering_frequency")
        val typo = intent.getStringExtra("typo")

        Displaying_info(plantID.toString(), plant_nickname.toString(), plant_specie.toString(), watering_frequency.toString(), typo.toString())
        Displaying_buttons()
    }

    }
}

@Composable
fun Displaying_info(plantID: String, plantNickname: String, plantSpecie: String, wateringFrequency: String, typo: String) {
Box(modifier = Modifier.fillMaxSize()){
    Column (modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End) {
        Text( text = "Nickname :" + plantNickname)
        Text(text = "Specie :" + plantSpecie)
        Text(text = "Watering frequency :" + wateringFrequency)
    }

}

}

@Composable
fun Displaying_buttons(){
    Box(modifier = Modifier.fillMaxSize()){
        //watering button
        Button() {Text(text = "watering button") }

        //delete button
        Button(onClick = delete_data() ){ Text(text = "Delete plant")}

    }


}

fun delete_data(): () -> Unit {
    val autonumeric =
}

/* code previously used to get plant infos and display it as text zones
private fun draw(plantList: ArrayList<HashMap<String, String>>) { // function that "draws" the button of the different plants
    setContent{
        Box(modifier = Modifier.fillMaxSize()){
            LazyColumn (
                content = {
                    items(items = plantList, itemContent = {

                        Column (
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        )
                        {
                            Button(onClick ={}
                            ) { }

                        }

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
*/



