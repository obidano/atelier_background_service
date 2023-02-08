@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalPermissionsApi::class)

package com.odc.odctrackingcommercial.vues


import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.odc.odctrackingcommercial.composants.ActivitesMarkerList
import com.odc.odctrackingcommercial.composants.HandleLocalisationPermisssion
import com.odc.odctrackingcommercial.composants.PositionsMarkerList
import com.odc.odctrackingcommercial.lib.utils.RequestState
import com.odc.odctrackingcommercial.models.ActivitesModel
import com.odc.odctrackingcommercial.models.LocationModel
import com.odc.odctrackingcommercial.models.UserModel
import com.odc.odctrackingcommercial.vue_models.SharedVueModel

interface ICartePage {
    val naviguer: (String) -> Any
    val retourArriere: () -> Unit
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun CartePage(
    navC: NavHostController,
    shareVM: SharedVueModel,
    navCB: NavHostController,
    scaffoldState: ScaffoldState,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = scaffoldState.snackbarHostState
    val keyboardController = LocalSoftwareKeyboardController.current

    val allLocations = shareVM.allLocations.collectAsState().value
    val allActivites=shareVM.allActivites.collectAsState().value
    val user=shareVM.identifiant

    val fn = object : ICartePage {
        override val naviguer = { route: String ->
            navC.navigate(route)
        }

        override val retourArriere: () -> Unit = {
            navC.popBackStack()
        }
    }
  /*  HandleLocalisationPermisssion() {
        shareVM.locationH.checkLocationPermission()
    }*/

    CarteBody(scaffoldState, fn, allLocations,allActivites, user)

}


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
private fun CarteBody(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    fn: ICartePage? = null,
    allLocations: RequestState<List<LocationModel>>,
    allActivites: RequestState<List<ActivitesModel>>,
    user: UserModel?,
) {
    val context = LocalContext.current
  /*  val centerPosition = remember(allLocations) {
        if (allLocations is RequestState.Success && allLocations.data.isNotEmpty()) {
            val a = allLocations.data.maxBy { it.id }
            LatLng(a.latitude, a.longitude)
        } else {
            LatLng(-4.3308706, 15.30636)

        }
    }*/

  /*  val cameraPosition = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(centerPosition, 16f)
    }*/

   /* LaunchedEffect(centerPosition) {
        cameraPosition.animate(
            update = CameraUpdateFactory.newCameraPosition(
                CameraPosition(centerPosition, 16f, 0f, 0f)
            ),
            durationMs = 700
        )
    }*/




    Scaffold(
        scaffoldState = scaffoldState,
        //topBar = { AppBar(fn) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(1.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
           /* GoogleMap(cameraPositionState = cameraPosition, modifier = Modifier.fillMaxSize()) {


            }*/

        }
    }

}




