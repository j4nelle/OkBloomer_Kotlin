package es.uc3m.android.okbloomer_kotlin

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import es.uc3m.android.okbloomer_kotlin.ui.theme.OkBloomer_KotlinTheme

class Adding_Plant_question : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OkBloomer_KotlinTheme {
                AddingPreview()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddingPreview(modifier : Modifier = Modifier){
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
        //test

    Column {

    Button(onClick = {/*make the link to google maps or another gps service (going directly to MAPS ?)*/}) {
    Text("I want to buy a new plant !")}

    Button(onClick = {
        val intent = Intent(context, Adding_Plant_activity::class.java)
        context.startActivity (intent)

    }) {
        Text("I already have a new plant")}
}
}