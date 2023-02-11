package com.odc.odctrackingcommercial.lib.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.location.Priority
import com.odc.odctrackingcommercial.MainActivity
import com.odc.odctrackingcommercial.models.LocationModel
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@ActivityScoped
class LocationHelper @Inject constructor(@ActivityContext private val context: Context) {
    val client = LocationServices.getFusedLocationProviderClient(context)
    val userCoord = mutableStateOf<Location?>(null)
    val isGranted = mutableStateOf(false)
    val locationFlow = MutableSharedFlow<Location>()
    private lateinit var locationRequest: LocationRequest


    init {
        val interval = 5 * 1000L
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, interval).build()


    }

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(l: LocationResult) {
            super.onLocationResult(l)
            for (newData in l.locations) {

                //Log.d("OE", "onLocationResult: ${newData.longitude}- ${newData.latitude}")
                setLocationData(newData)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates(activity: MainActivity) {
        Log.d("", "startLocationUpdates: ")
        openGpsService(activity)
        client.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    fun openGpsService(activity: MainActivity) {
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)

        val result =
            LocationServices.getSettingsClient(context).checkLocationSettings(builder.build())

        result.addOnCompleteListener { task ->
            Log.d("TAG", "startLocationUpdates: ASK LOCATION")
            try {
                val response = task.getResult(ApiException::class.java)
                // All location settings are satisfied. The client can initialize location
                // requests here.
            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->                         // Location settings are not satisfied. But could be fixed by showing the
                        // user a dialog.
                        try {
                            Log.d("TAG", "startLocationUpdates: OPEN DIALOG")
                            val resolvable = exception as ResolvableApiException
                            resolvable.startResolutionForResult(
                                activity,
                                990
                            )
                        } catch (e: IntentSender.SendIntentException) {
                            e.printStackTrace()
                            // Ignore the error.
                        } catch (e: ClassCastException) {
                            e.printStackTrace()

                            // Ignore, should be an impossible error.
                        }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
                }
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun setLocationData(location: Location?) {
        location?.let {

            GlobalScope.launch {
                locationFlow.emit(it)
                //Log.d("TAG", "setLocationData: Emitting data....")
            }
            userCoord.value = it
        }
    }

    fun checkLocationPermission(activity: MainActivity) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            isGranted.value = true
            client.lastLocation.addOnSuccessListener { location ->
                //Log.d("OE", "addOnSuccessListener: ${location?.longitude}- ${location?.latitude}")
                setLocationData(location)
            }

            startLocationUpdates(activity)
        }
    }
}