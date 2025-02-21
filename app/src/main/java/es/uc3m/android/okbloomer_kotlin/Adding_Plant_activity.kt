package es.uc3m.android.okbloomer_kotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import es.uc3m.android.okbloomer_kotlin.ui.theme.OkBloomer_KotlinTheme
import androidx.compose.ui.Modifier

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

}