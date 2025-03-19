package es.uc3m.android.okbloomer_kotlin

import android.content.Intent
import android.os.Bundle
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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

@Composable
fun AddingPreview_plant(modifier: Modifier = Modifier) {
    // Variables de estado
    var plantName by rememberSaveable { mutableStateOf("") }
    var species by rememberSaveable { mutableStateOf("") }
    var wateringFrequency by rememberSaveable { mutableStateOf("") }

    // Contexto para iniciar actividades
    val context = LocalContext.current

    Box(modifier = modifier.fillMaxSize()) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.background), // Asegúrate de tener una imagen en resources
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Contenido principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                fontSize = 24.sp,
                text = "Describe your new plant:",
                color = Color(0xFF4CAF50),
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Campo para el nombre de la planta
            TextField(
                value = plantName,
                onValueChange = { plantName = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                placeholder = { Text("Enter your plant's nickname") }
            )

            // Campo para la especie
            TextField(
                value = species,
                onValueChange = { species = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                placeholder = { Text("Enter your plant's species") }
            )

            // Campo para la frecuencia de riego
            TextField(
                value = wateringFrequency,
                onValueChange = { wateringFrequency = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                placeholder = { Text("What is the watering frequency?") }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Botón para añadir la planta al jardín
            Button(
                onClick = {
                    val intent = Intent(context, MyGarden_activity::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(8.dp),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50),
                    contentColor = Color.White
                )
            ) {
                Text("Add to Garden", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}