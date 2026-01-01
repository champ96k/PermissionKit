package com.champ96k.permissionkit.activity

import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.champ96k.permissionkit.core.PermissionController
import com.champ96k.permissionkit.core.PermissionResult

/**
 * Handles native permission request for Activity.
 *
 * This is the ONLY place where Android shows
 * the system permission dialog for Activity.
 */
 class ActivityPermissionRequester(
    private val activity: ComponentActivity
) {

    private val controller = PermissionController()

    private lateinit var permissions: List<String>
    private var callback: ((PermissionResult) -> Unit)? = null

    /**
     * Native Android permission launcher.
     *
     * THIS triggers the system permission dialog.
     */
    private val permissionLauncher =
        activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result: Map<String, Boolean> ->

            // Convert raw Android result to PermissionResult
            val permissionResult = controller.resolve(
                permissions = permissions,
                result = result,
                shouldShowRationale = { permission ->
                    // SAFE for API 21+
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        activity.shouldShowRequestPermissionRationale(permission)
                    } else {
                        false
                    }
                }
            )

            callback?.invoke(permissionResult)
        }

    /**
     * Requests permissions from Activity.
     *
     * @param permissions list of permission strings
     * @param callback result callback
     */
    fun request(
        permissions: List<String>,
        callback: (PermissionResult) -> Unit
    ) {
        this.permissions = permissions
        this.callback = callback

        // ACTUAL NATIVE REQUEST
        permissionLauncher.launch(permissions.toTypedArray())
    }
}
