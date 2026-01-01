package com.champ96k.permissionkit.compose

import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.champ96k.permissionkit.core.PermissionController
import com.champ96k.permissionkit.core.PermissionResult

@Composable
fun rememberPermissionRequester(
    permissions: Array<String>,
    onRationale: ((List<String>, proceed: () -> Unit) -> Unit)? = null,
    onDenied: ((PermissionResult) -> Unit)? = null,
    onResult: (PermissionResult) -> Unit
): () -> Unit {

    val context = LocalContext.current
    val activity = context as ComponentActivity

    val controller = remember { PermissionController() }

    // Controls when permission request should be triggered
    var triggerRequest by remember { mutableStateOf(false) }

    // Native permission launcher
    val launcher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result ->

            val permissionResult = controller.resolve(
                permissions = permissions.toList(),
                result = result,
                shouldShowRationale = { permission ->
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        activity.shouldShowRequestPermissionRationale(permission)
                    } else {
                        false
                    }
                }
            )

            when {
                permissionResult.denied.isNotEmpty() && onRationale != null -> {
                    onRationale(permissionResult.denied) {
                        // retry permission request
                        triggerRequest = true
                    }
                }

                permissionResult.permanentlyDenied.isNotEmpty() -> {
                    onDenied?.invoke(permissionResult)
                    onResult(permissionResult)
                }

                else -> {
                    onResult(permissionResult)
                }
            }
        }

    // Side-effect to trigger permission request safely
    LaunchedEffect(triggerRequest) {
        if (triggerRequest) {
            launcher.launch(permissions)
            triggerRequest = false
        }
    }

    // Initial request trigger (returned to user)
    return {
        triggerRequest = true
    }
}
