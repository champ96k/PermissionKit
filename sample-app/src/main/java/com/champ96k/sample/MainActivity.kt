package com.champ96k.sample

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.champ96k.permissionkit.PermissionKit
import com.champ96k.permissionkit.core.PermissionResult
import com.champ96k.sample.ui.theme.PermissionKitSampleTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PermissionKitSampleTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PermissionScreen()
                }
            }
        }
    }
}

@Composable
fun PermissionScreen() {
    val context = LocalContext.current

    // Track latest permission result
    var lastResult by remember { mutableStateOf<PermissionResult?>(null) }

    val permissions = remember {
        listOf(
            Manifest.permission.CAMERA to "Camera",
            Manifest.permission.RECORD_AUDIO to "Microphone",
            Manifest.permission.ACCESS_FINE_LOCATION to "Location"
        )
    }

    val requestPermissions = PermissionKit.rememberRequester(
        permissions = permissions.map { it.first }.toTypedArray(),
        onRationale = { deniedPermissions, proceed ->
            showRationaleDialog(
                context = context, permissions = deniedPermissions, proceed = proceed
            )
        },
        onDenied = { result ->
            result.openAppSettings(context)
        }) { result ->
        lastResult = result
        showToast(context, result)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        Text(
            text = "PermissionKit Sample",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Manage multiple permissions with a clean, single-dependency API.",
            style = MaterialTheme.typography.bodyMedium
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        permissions.forEach { (permission, label) ->
            PermissionRow(
                label = label, permission = permission, result = lastResult
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = requestPermissions,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Request Permissions")
        }

        OutlinedButton(
            onClick = {
                lastResult?.openAppSettings(context)
            }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)
        ) {
            Text("Open App Settings (Revoke Permissions)")
        }
    }
}

private fun showRationaleDialog(
    context: android.content.Context,
    permissions: List<String>,
    proceed: () -> Unit,
) {
    android.app.AlertDialog.Builder(context).setTitle("Permission Required").setMessage(
        "We need the following permissions to continue:\n\n" + permissions.joinToString("\n")
    ).setPositiveButton("Allow") { _: android.content.DialogInterface, _: Int ->
        proceed()
    }.setNegativeButton("Cancel", null).show()
}

private fun showToast(
    context: android.content.Context,
    result: PermissionResult,
) {
    when {
        result.allGranted -> {
            Toast.makeText(context, "All permissions granted ✅", Toast.LENGTH_SHORT).show()
        }

        result.permanentlyDenied.isNotEmpty() -> {
            Toast.makeText(context, "Some permissions permanently denied ❌", Toast.LENGTH_SHORT)
                .show()
        }

        else -> {
            Toast.makeText(context, "Some permissions denied ⚠️", Toast.LENGTH_SHORT).show()
        }
    }
}

