package com.odc.odctrackingcommercial.lib.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import com.odc.odctrackingcommercial.lib.services.BackgroundService

class MyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.i("Broadcast Listened", "Service tried to stop")
        Toast.makeText(context, "Service redemarré", Toast.LENGTH_SHORT).show()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val i = Intent(context, BackgroundService::class.java)
            i.putExtra("IS_FOREGROUND", true)
            context.startForegroundService(i)
        } else {
            context.startService(Intent(context, BackgroundService::class.java))
        }
    }
}