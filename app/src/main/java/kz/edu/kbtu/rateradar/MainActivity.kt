package kz.edu.kbtu.rateradar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import kz.edu.kbtu.rateradar.navigation.RateRadarNavGraph
import kz.edu.kbtu.rateradar.ui.theme.RateRadarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RateRadarTheme {
                RateRadarNavGraph()
            }
        }
    }
}
