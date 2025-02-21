package es.uc3m.android.okbloomer_kotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import es.uc3m.android.okbloomer_kotlin.GreetingPreview
import es.uc3m.android.okbloomer_kotlin.R
import es.uc3m.android.okbloomer_kotlin.ui.theme.OkBloomer_KotlinTheme

class MyGarden_activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OkBloomer_KotlinTheme {
                Gardenlist()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Gardenlist(modifier : Modifier = Modifier){

    val context = LocalContext.current
    // Create a launcher for starting the activity
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // Handle the result of the activity here
        // For example, you can retrieve data from the activity result
        val data = result.data
        // Handle the data accordingly
    }



    Column {
        // to see the plants of our garden in a column view
        Button(onClick ={/*go to the activity displaying the plant infos, instance of the class plant*/})
        {
            Text("Plant 1")
        }

        Button(onClick = {/*go to the activity displaying the plant infos, instance of the class plant*/})
        {
            Text("Plant 2")
        }
        /*We need to find a way to add as many buttons as possible to this activity */
    }
}