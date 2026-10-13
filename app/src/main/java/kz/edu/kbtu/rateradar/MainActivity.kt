package kz.edu.kbtu.rateradar

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import kz.edu.kbtu.rateradar.navigation.RateRadarNavGraph
import kz.edu.kbtu.rateradar.ui.theme.RateRadarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logLifecycle("onCreate")
        enableEdgeToEdge()
        setContent {
            RateRadarTheme {
                RateRadarNavGraph()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        logLifecycle("onStart")
    }

    override fun onResume() {
        super.onResume()
        logLifecycle("onResume")
    }

    override fun onPause() {
        super.onPause()
        logLifecycle("onPause")
    }

    override fun onStop() {
        super.onStop()
        logLifecycle("onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        logLifecycle("onDestroy")
    }

    private fun logLifecycle(method: String) {
        Log.d("RateRadarActivity", "$method instance=${System.identityHashCode(this)}")
    }
}
