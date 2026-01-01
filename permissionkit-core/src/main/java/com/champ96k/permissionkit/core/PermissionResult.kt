package com.champ96k.permissionkit.core

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings

/**
 * Represents final permission state after request.
 */
data class PermissionResult(
    val granted: List<String>,
    val denied: List<String>,
    val permanentlyDenied: List<String>
) {

    /**
     * True if ALL permissions are granted
     */
    val allGranted: Boolean
        get() = denied.isEmpty() && permanentlyDenied.isEmpty()

    /**
     * Convenience helper to open app settings
     * when permissions are permanently denied.
     */
    fun openAppSettings(context: Context) {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", context.packageName, null)
        )
        context.startActivity(intent)
    }
}
