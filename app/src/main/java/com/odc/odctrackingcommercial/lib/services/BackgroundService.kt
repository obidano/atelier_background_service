package com.odc.odctrackingcommercial.lib.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.NotificationCompat
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.odc.odctrackingcommercial.MainActivity
import com.odc.odctrackingcommercial.R
import com.odc.odctrackingcommercial.lib.broadcast.MyReceiver
import com.odc.odctrackingcommercial.lib.depot.ActivitesDepot
import com.odc.odctrackingcommercial.lib.depot.DataStoreDepot
import com.odc.odctrackingcommercial.lib.utils.LocationHelper
import com.odc.odctrackingcommercial.lib.utils.SocketUtils
import com.odc.odctrackingcommercial.models.ActivitesModel
import com.odc.odctrackingcommercial.models.UserModel
import dagger.hilt.android.AndroidEntryPoint
import io.socket.client.Socket
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

@AndroidEntryPoint
class BackgroundService : Service() {
    @Inject
    lateinit var socketUtils: SocketUtils

    @Inject
    lateinit var activitesDepot: ActivitesDepot

    @Inject
    lateinit var dataStoreDepot: DataStoreDepot

    var isForeGround = false
    var identifiant by mutableStateOf<UserModel?>(null)

    override fun onCreate() {
        super.onCreate()
    }

    fun readUSerState() {
        GlobalScope.launch {
            dataStoreDepot.readUserState.collect {
                if (it.isNotEmpty()) {
                    identifiant = Gson().fromJson(it, UserModel::class.java)
                }
            }
        }
    }


    fun socketConnection() {
        GlobalScope.launch { }
        socketUtils.startConnection()
        socketUtils.socket?.let {
            it.on(Socket.EVENT_CONNECT) {
                socketUtils.showNotifs("Connecté au Serveur ODC")
                Log.d("EVENT_CONNECT", ": Connect socket")
                socketUtils.socket?.emit("souscrire", 1)

            }

            /* it.on("new_espace") {
                 Log.d("new_espace", "${it[0]}")
                 Log.d("new_space_type", "${it[0]::class.java}")
                 val new_msg = it[0].toString()
                 socketUtils.showNotifs(new_msg)
             }*/

            it.on("new_message") {
                Log.d("new_message", "${it[0]}")
                try {
                    val newOp =
                        Gson().fromJson<ActivitesModel>(
                            it[0].toString(),
                            ActivitesModel::class.java
                        )
                    if (newOp.identifiant.id == identifiant?.id) return@on
                    socketUtils.showNotifs("Nouvelle ${newOp.category.display} de ${newOp.identifiant.name}")
                    GlobalScope.launch {
                        if (isForeGround) {
                            activitesDepot.addActivite(newOp)
                        } else {
                            socketUtils.messageFlow.emit(newOp)
                        }

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }

    private fun startMyOwnForeground() {
        val NOTIFICATION_CHANNEL_ID = "com.odc.notif"
        val channelName = "Background Service"
        val chan = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            channelName,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        //chan.lightColor = Color(0xFF00000)
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val manager = (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
        manager.createNotificationChannel(chan)

        val intent = Intent(this, MainActivity::class.java)
        //intent.putExtra("","")
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent =
            PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        val notification: Notification = notificationBuilder
            .setOngoing(true)
            .setContentTitle("Compose APP")
            .setSmallIcon(R.drawable.ic_notif)
            .setContentText("Recherche des activités...")
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setContentIntent(pendingIntent)
            .build()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(2, notification, FOREGROUND_SERVICE_TYPE_LOCATION)
        } else {
            startForeground(2, notification)

        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        isForeGround = intent?.getBooleanExtra("IS_FOREGROUND", false) ?: false
        Log.d("isForeGround", "onStartCommand: $isForeGround")
        if (isForeGround) {
            Log.i("SERVICE", "DEMARRAGE FOREGROUND")
            startMyOwnForeground()

        } else {
            Log.i("SERVICE", "DEMARRAGE BACKGROUND")
        }
        readUSerState()
        socketConnection()

        //super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

     override fun onDestroy() {
         super.onDestroy()
         Log.d("restartservice", "Service onDestroy: ")
         socketUtils.socket?.disconnect()

         /*val broadcastIntent = Intent()
         broadcastIntent.action = "restartservice"
         broadcastIntent.setClass(this, MyReceiver::class.java)
         this.sendBroadcast(broadcastIntent)*/
     }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}