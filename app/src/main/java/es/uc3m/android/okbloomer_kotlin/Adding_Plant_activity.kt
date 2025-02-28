package es.uc3m.android.okbloomer_kotlin

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import es.uc3m.android.okbloomer_kotlin.ui.theme.OkBloomer_KotlinTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp


class Adding_Plant_activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OkBloomer_KotlinTheme {
                AddingPreview_plant()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddingPreview_plant(modifier : Modifier = Modifier) {
    //function that gives the option of entering plant infos (should lead to the creation of an instance of Plant)

    // Initiation of the variables
    var plant_name by rememberSaveable{ mutableStateOf("") }
    var specie by rememberSaveable { mutableStateOf("") }
    var watering_frequency by rememberSaveable { mutableStateOf("") }

    //context variable
    var context = LocalContext.current

    Column (
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
    )

    {
        Text(fontSize = 22.sp, text ="Describe your new plant :", color = Color.Blue)
        TextField(
            value = plant_name,
            onValueChange = { plant_name = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text("Enter your plant's nickname")
            }
        )

        TextField(
            value = specie,
            onValueChange = { specie = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text("Enter your plant's specie")
            }
        )

        TextField(
            value = watering_frequency,
            onValueChange = { watering_frequency = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text("What is the watering frequency ?")
            }
        )

        Button(
            onClick ={
                val intent = Intent(context, MyGarden_activity::class.java)
                context.startActivity(intent)
            }

            //colors =
        ) {
        Text("Add to Garden")
            // will have to switch to a text_button variable that says "added" when you click it
        }
    }



}