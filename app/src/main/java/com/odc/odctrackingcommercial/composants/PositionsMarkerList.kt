package com.odc.odctrackingcommercial.composants

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState
import com.odc.odctrackingcommercial.lib.utils.RequestState
import com.odc.odctrackingcommercial.models.LocationModel

@Composable
fun PositionsMarkerList(allLocations: RequestState<List<LocationModel>>) {
    if (allLocations is RequestState.Success && allLocations.data.isNotEmpty()) {
        Log.d("MAXTAG", "Max ID ${allLocations.data.maxBy { it.id }.id}")
        val maxPos = allLocations.data.maxBy { it.id }
        allLocations.data.forEachIndexed { index, it ->
            PositionMarker(maxPos, it)
        }
        //}
    }
}

@Composable
fun PositionMarker(maxPos: LocationModel, position: LocationModel) {
    val pos = LatLng(position.latitude + 0.000008f, position.longitude+ 0.000008f)
    val isAlpha = if (position.id == maxPos.id) .8f else 0.5f
    val color =
        if (position.id == maxPos.id) BitmapDescriptorFactory.HUE_YELLOW
        else BitmapDescriptorFactory.HUE_BLUE

    val markerState = rememberMarkerState()
    // https://github.com/googlemaps/android-maps-compose/issues/198#issuecomment-1240847555
    LaunchedEffect(pos) { markerState.position = pos }
    Marker(
        state = markerState,
        icon = BitmapDescriptorFactory.defaultMarker(color),
        alpha = isAlpha,
        title = if (position.id == maxPos.id) "Votre position actuelle" else "",
        snippet = ""
    )
    if (position.id == maxPos.id) {
        markerState.showInfoWindow()
        Circle(
            center = pos,
            fillColor = Color(0x4BCDE1EB),
            strokeColor = Color(0x60FFFFFF),
            radius = 50.00
        )
    }
}

