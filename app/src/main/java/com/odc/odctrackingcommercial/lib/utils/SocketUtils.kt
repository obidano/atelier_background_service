package com.odc.odctrackingcommercial.lib.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.odc.odctrackingcommercial.R
import com.odc.odctrackingcommercial.models.ActivitesModel
import com.odc.odctrackingcommercial.models.LocationModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.flow.MutableSharedFlow
import java.net.URI
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocketUtils @Inject constructor(@ApplicationContext val context: Context) {
    var socket: Socket? = null
    val CHANNEL_ID = "channelID"
    val CHANNEL_NAME = "channelName"
    val NOTIF_ID = 0
    val messageFlow = MutableSharedFlow<ActivitesModel>()


    init {
        val chan = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        //chan.lightColor = Color(0xFF00000)
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val manager = (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
        manager.createNotificationChannel(chan)
    }

    fun startConnection() {
        val uri: URI = URI.create(Constantes.BASE_URL)
        val options = IO.Options.builder()//.setTransports(arrayOf("websocket"))
            .build()
        Log.d("", "startConnection: SOCKET")

        try {
            socket = IO.socket(uri, options)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        socket?.connect()
    }

    fun showNotifs(msg: String) {

        val notif = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Notification")
            .setContentText(msg)
            .setSmallIcon(R.drawable.ic_shop)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        Log.d("TAG", "showNotifs: msg")

        val notifManger = NotificationManagerCompat.from(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notifManger.notify(NOTIF_ID, notif)
    }
}