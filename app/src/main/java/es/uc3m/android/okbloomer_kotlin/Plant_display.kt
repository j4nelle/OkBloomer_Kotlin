package es.uc3m.android.okbloomer_kotlin

import android.database.Cursor
import android.os.Bundle
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

class Plant_display : ComponentActivity() {}



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



