package es.uc3m.android.okbloomer_kotlin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.uc3m.android.okbloomer_kotlin.ui.theme.OkBloomer_KotlinTheme

class Find_Plant_activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OkBloomer_KotlinTheme {
                FindPlantScreen()
            }
        }
    }
}

@Composable
fun FindPlantScreen() {
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background2),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Find Plant Shops Near You",
                fontSize = 22.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    val gmmIntentUri = Uri.parse("geo:0,0?q=plant shops near me")
                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    mapIntent.setPackage("com.google.android.apps.maps")
                    context.startActivity(mapIntent)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(45.dp)
            ) {
                Text(text = "Open Google Maps", fontSize = 18.sp)
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            //back button
            Button(onClick = { (context as? Activity)?.finish() }) {
                Text("Back")
            }
            //home button
            Button(onClick = {
                val intent = Intent(context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                context.startActivity(intent)
            }) {
                Text("Home")
            }
        }
    }
}
