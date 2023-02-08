package com.odc.odctrackingcommercial.composants

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState
import com.odc.odctrackingcommercial.R
import com.odc.odctrackingcommercial.lib.utils.BitmapDescriptorUtils
import com.odc.odctrackingcommercial.lib.utils.RequestState
import com.odc.odctrackingcommercial.models.ActivitesModel
import com.odc.odctrackingcommercial.models.UserModel

@Composable
fun ActivitesMarkerList(allActivites: RequestState<List<ActivitesModel>>, identifiant: UserModel?) {
    if (allActivites is RequestState.Success && allActivites.data.isNotEmpty()) {
        allActivites.data.forEachIndexed { index, it ->
            if (it.localisation != null && identifiant != null) APositionMarker(it, identifiant)
        }
    }
}

@Composable
fun APositionMarker(activite: ActivitesModel, identifiant: UserModel) {
    val context = LocalContext.current
    val icon = BitmapDescriptorUtils(
        context, R.drawable.ic_activite
    )
    val icon2 = BitmapDescriptorUtils(
        context, R.drawable.ic_activite_2
    )
    val position = activite.localisation!!
    val pos = LatLng(position.latitude, position.longitude)
    val isAlpha = 0.5f
    val markerIcon =
        if (identifiant.id == activite.identifiant.id) icon
        else icon2

    val markerState = rememberMarkerState()
    LaunchedEffect(pos) { markerState.position = pos }
    Marker(
        state = markerState,
        //icon = BitmapDescriptorFactory.defaultMarker(color),
        alpha = isAlpha,
        icon = markerIcon,
        title = activite.category.display,
        snippet = "${activite.article.nom} - ${activite.quantite}"
    )

}

