package es.uc3m.android.okbloomer_kotlin

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import es.uc3m.android.okbloomer_kotlin.ui.theme.MyGarden_activity
import es.uc3m.android.okbloomer_kotlin.ui.theme.OkBloomer_KotlinTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
           OkBloomer_KotlinTheme {
               GreetingPreview()
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview(modifier : Modifier = Modifier) {

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
        Text(
            text = "Greetings user !",
            modifier = modifier
        )
        Button(onClick = { // Create an intent to start the activity
            val intent = Intent(context, MyGarden_activity::class.java)
            // Start the activity using the launcher
            launcher.launch(intent)
            context.startActivity (intent) })
        {
            Text("Go to my garden")
        }

        Button(
            onClick = {
                val intent = Intent(context, Adding_Plant_question::class.java)
                launcher.launch(intent)
                context.startActivity (intent) }
            )
        {
            Text("Add a new plant")
        }

    }
}