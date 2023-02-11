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


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun HandleNotificationPermisssion( callback: () -> Unit) {

    var permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.POST_NOTIFICATIONS,

        )
    ) {
        if (it[Manifest.permission.POST_NOTIFICATIONS] == true
        ) {
            callback()
        }
    }



    LaunchedEffect(Unit) {
        delay(1000L)
            permissionState.launchMultiplePermissionRequest()
    }
}