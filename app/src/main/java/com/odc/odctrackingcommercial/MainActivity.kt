package com.odc.odctrackingcommercial

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.odc.odctrackingcommercial.lib.broadcast.MyReceiver
import com.odc.odctrackingcommercial.lib.services.BackgroundService
import com.odc.odctrackingcommercial.ui.theme.ODCTrackingCommercialTheme
import com.odc.odctrackingcommercial.vue_models.SharedVueModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.odc.odctrackingcommercial.lib.Navigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController
    private val shareVM: SharedVueModel by viewModels()

    fun arreterServiceBackground() {
        val intent = Intent(this, BackgroundService::class.java)
        stopService(intent)
    }

    fun demarrerServiceBackground() {
        val mServiceIntent = Intent(this, BackgroundService::class.java)
        mServiceIntent.putExtra("IS_FOREGROUND", false)
        if (!isMyServiceRunning(BackgroundService::class.java)) {
            startService(mServiceIntent)
        }
    }

    fun demarrerServiceForeground() {
        val mServiceIntent = Intent(this, BackgroundService::class.java)
        mServiceIntent.putExtra("IS_FOREGROUND", true)
        if (!isMyServiceRunning(BackgroundService::class.java)) {
            startForegroundService(mServiceIntent)
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GlobalScope.launch {
            delay(10000L)
            arreterServiceBackground()
            demarrerServiceBackground()
        }

        setContent {
            ODCTrackingCommercialTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    navController = rememberNavController()
                    Navigation(navC = navController, shareVM)
                }
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { view, insets ->
            val top = insets.getInsets(WindowInsetsCompat.Type.ime()).top
            val bottom = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
            view.updatePadding(top = top, bottom = bottom)
            insets
        }
    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                Log.i("Service status", "Running")
                return true
            }
        }
        Log.i("Service status", "Not running")
        return false
    }

    override fun onDestroy() {
       /* arreterServiceBackground()
        Log.d("restartservice", "Activity onDestroy: ")
        val broadcastIntent = Intent()
        broadcastIntent.action = "restartservice"
        broadcastIntent.setClass(this, MyReceiver::class.java)
        this.sendBroadcast(broadcastIntent)
        */
       arreterServiceBackground()
        Toast.makeText(this, "Lancement Foreground Service", Toast.LENGTH_LONG).show()
        demarrerServiceForeground()
        super.onDestroy()
    }
}

