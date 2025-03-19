package es.uc3m.android.okbloomer_kotlin

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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


    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.background2),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Button(
            onClick = {/*make the link to google maps or another gps service (going directly to MAPS ?)*/
        },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .height(56.dp)
            .clip(RoundedCornerShape(12.dp))
        ) {
            Text("I want to buy a new plant", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
            val intent = Intent(context, Adding_Plant_activity::class.java)
            context.startActivity (intent)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(56.dp)
                .clip(RoundedCornerShape(12.dp))

        ){
        Text("I already have a new plant", fontSize = 18.sp)
        }
    }
}
}
