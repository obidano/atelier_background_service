package com.odc.odctrackingcommercial.composants

import android.util.Log
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ShowSnackBar(
    snackbarHostState: SnackbarHostState,
    msg: String,
    isSuccess: Boolean,
    callback: () -> Unit,
) {
    val scope = rememberCoroutineScope()

    LaunchedEffect(isSuccess, msg) {
        if (msg.isEmpty()) return@LaunchedEffect
        scope.launch {
            val snackbarResult = snackbarHostState.showSnackbar(
                message = msg,
                actionLabel = "Fermer",
                duration = SnackbarDuration.Short
            )
            when (snackbarResult) {
                SnackbarResult.Dismissed -> {
                    Log.d("TAG", "Dismissed: $snackbarResult")
                    callback()
                }
                SnackbarResult.ActionPerformed -> {
                    Log.d("TAG", "ActionPerformed: $snackbarResult")
                    callback()
                }
            }
        }
    }

}