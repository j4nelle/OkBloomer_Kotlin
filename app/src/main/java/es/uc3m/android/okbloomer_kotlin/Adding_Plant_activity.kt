package es.uc3m.android.okbloomer_kotlin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import es.uc3m.android.okbloomer_kotlin.ui.theme.OkBloomer_KotlinTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.uc3m.android.okbloomer_kotlin.datas.Plant_data


class Adding_Plant_activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent{

    // Initiation of the variables
    var plant_nickname by remember { mutableStateOf("") }
    var plant_specie by remember { mutableStateOf("") }
    var watering_frequency by remember { mutableStateOf("") }

    //context variable
    var context = LocalContext.current

    Box(
    modifier = Modifier.fillMaxSize()
    )
    {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            //putting a title on top of the activity
            Text(fontSize = 22.sp, text = "Describe your new plant :", color = Color.Blue)

            //add space between elements
            Spacer(modifier = Modifier.size(12.dp))

            //creating a textfield for each variable
            TextField(value = plant_nickname,
                label = (
                        { Text("Enter your plant's nickname")
                        }),
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { plant_nickname = it }
            )

            Spacer(modifier = Modifier.size(12.dp))

            TextField(
                value = plant_specie,
                onValueChange = { plant_specie = it },
                modifier = Modifier.fillMaxWidth(),
                label = ({ Text("Enter your plant's specie") })
            )

            Spacer(modifier = Modifier.size(12.dp))

            TextField(
                value = watering_frequency,
                onValueChange = { watering_frequency = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "What is the watering frequency ?") }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick =
                { keep_data(plant_nickname, plant_specie, watering_frequency)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                 )
                {
                Text("Add to Garden", fontSize = 18.sp)
                }
        }
    }
        }
    }

    private fun keep_data(plantNickname: String, plantSpecie: String, wateringFrequency: String) {
        val plant_data = Plant_data(this)
        val autonumeric = plant_data.adding_new_plant(plantNickname, plantSpecie, wateringFrequency.toFloat(), -1)

        //creating a message to make sure the data is saved
        Toast.makeText(this, "id : $autonumeric", Toast.LENGTH_SHORT).show()

        //making sure we start the garden activity to display the new plants once it's added
        val intent = Intent(this, Plant_display::class.java).apply {
            putExtra("idplant", autonumeric.toString())
        }
        startActivity(Intent(this, MyGarden_activity::class.java ))

    }
}
