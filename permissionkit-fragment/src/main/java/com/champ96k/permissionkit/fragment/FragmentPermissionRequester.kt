package com.champ96k.permissionkit.fragment

import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.champ96k.permissionkit.core.PermissionController
import com.champ96k.permissionkit.core.PermissionResult

/**
 * Handles native permission request for Fragment.
 *
 * This is the ONLY place where Android shows
 * the system permission dialog for Fragment.
 */
 class FragmentPermissionRequester(
    private val fragment: Fragment
) {

    private val controller = PermissionController()

    private lateinit var permissions: List<String>
    private var callback: ((PermissionResult) -> Unit)? = null

    /**
     * Native Android permission launcher bound to Fragment lifecycle.
     */
    private val permissionLauncher =
        fragment.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result ->

            val permissionResult = controller.resolve(
                permissions = permissions,
                result = result,
                shouldShowRationale = { permission ->
                    //  API 21 SAFE
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        fragment.shouldShowRequestPermissionRationale(permission)
                    } else {
                        false
                    }
                }
            )

            callback?.invoke(permissionResult)
        }

    /**
     * Requests permissions from Fragment.
     *
     * @param permissions list of permission strings
     * @param callback permission result callback
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
