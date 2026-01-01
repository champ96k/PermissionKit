package com.champ96k.sample

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.champ96k.permissionkit.PermissionKit
import com.champ96k.permissionkit.core.PermissionResult
import com.champ96k.sample.ui.theme.PermissionKitSampleTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PermissionKitSampleTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    PermissionScreen()
                }
            }
        }
    }
}

@Composable
fun PermissionScreen() {
    val context = LocalContext.current

    val requestPermission = PermissionKit.rememberRequester(
        permissions = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        ),
        onRationale = { permissions, proceed ->
            showRationale(context, permissions, proceed)
        },
        onDenied = { result ->
            result.openAppSettings(context)
        }
    ) { result ->
        handleResult(context, result)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "PermissionKit Sample App")
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = requestPermission) {
            Text("Request Permissions")
        }
    }
}

private fun showRationale(
    context: android.content.Context,
    permissions: List<String>,
    proceed: () -> Unit
) {
    android.app.AlertDialog.Builder(context)
        .setTitle("Permission Required")
        .setMessage(
            "We need the following permissions:\n\n" +
                    permissions.joinToString("\n")
        )
        .setPositiveButton("Allow") {  _, _ ->
            proceed()
        }
        .setNegativeButton("Cancel", null)
        .show()
}

private fun handleResult(
    context: android.content.Context,
    result: PermissionResult
) {
    when {
        result.allGranted -> {
            Toast.makeText(
                context,
                "All permissions granted ✅",
                Toast.LENGTH_SHORT
            ).show()
        }

        result.permanentlyDenied.isNotEmpty() -> {
            Toast.makeText(
                context,
                "Permission permanently denied ❌",
                Toast.LENGTH_SHORT
            ).show()
        }

        else -> {
            Toast.makeText(
                context,
                "Permission denied",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
