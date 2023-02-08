@file:OptIn(ExperimentalPermissionsApi::class)

package com.odc.odctrackingcommercial.composants

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.odc.odctrackingcommercial.vue_models.SharedVueModel
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun HandleLocalisationPermisssion(shareVM: SharedVueModel, callback: () -> Unit) {

    var permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
    ) {
        if (it[Manifest.permission.ACCESS_FINE_LOCATION] == true || it[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        ) {
            callback()
        }
    }



    LaunchedEffect(shareVM.identifiant) {
        delay(1700L)
        if (shareVM.identifiant != null && !shareVM.locationH.isGranted.value)
            permissionState.launchMultiplePermissionRequest()
    }
}