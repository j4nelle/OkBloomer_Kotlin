package es.uc3m.android.okbloomer_kotlin

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Paint.Align
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
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

        //get the plant id to display it
        val plantID = intent.getStringExtra("idplant")

        // get the rest of the info directly from the database
        val plantData = Plant_data(this)

        var plant_nickname = "Unknown"
        var plant_specie = "Unknown"
        var watering_frequency = "Unknown"
        var typo = "Unknown"

        // reading the database :
        val cursor = plantData.readableDatabase.rawQuery(
            "SELECT * FROM mygarden WHERE idplant = ?", arrayOf(plantID ?:"-1")
        )
        if (cursor.moveToFirst()) {
            plant_nickname = cursor.getString(cursor.getColumnIndexOrThrow("plant_nickname"))
            plant_specie = cursor.getString(cursor.getColumnIndexOrThrow("plant_specie"))
            watering_frequency = cursor.getString(cursor.getColumnIndexOrThrow("watering_frequency"))
            typo = cursor.getString(cursor.getColumnIndexOrThrow("typo"))

        }
        cursor.close()
        setContent {
        Displaying_info(plantID.toString(), plant_nickname, plant_specie, watering_frequency, typo)
        }

    }
}

@Composable
fun Displaying_info(plantID: String, plantNickname: String, plantSpecie: String, wateringFrequency: String, typo: String) {
    var context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()){
    Column (modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start) {
        Text(text = "Nickname : $plantNickname")
        Text(text = "Specie : $plantSpecie")
        Text(text = "Watering frequency :$wateringFrequency")

        //delete button
        Button(onClick = {delete_data(plantID = plantID, context)} )
        { Text(text = "Delete plant")}

        //watering button
        //Button() {Text(text = "watering button") }
    }

}
}


fun delete_data(plantID: String, context: Context) {
    val plant_data = Plant_data(context)
    val success = plant_data.deleting_a_plant(plantID)

    if (success) {
        // Message to see if the deletion worked :
        Toast.makeText(context, "plant deleted", Toast.LENGTH_SHORT).show()

        //getting back to the garden after deletion
        val intent = Intent(context, MyGarden_activity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        context.startActivity(intent)

    }
    else{
        //Message if the deletion failed
        Toast.makeText(context, "deletion failed", Toast.LENGTH_SHORT).show()
    }
}

