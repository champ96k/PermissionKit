package com.champ96k.permissionkit

import androidx.activity.ComponentActivity
import com.champ96k.permissionkit.activity.ActivityPermissionRequester
import com.champ96k.permissionkit.core.PermissionResult

/**
 * Fluent permission request builder for Activity.
 */
class ActivityRequestBuilder(private val activity: ComponentActivity) {

    private val requester = ActivityPermissionRequester(activity)

    private var permissions: List<String> = emptyList()
    private var rationaleCallback: ((List<String>, () -> Unit) -> Unit)? = null
    private var deniedCallback: ((PermissionResult) -> Unit)? = null
    private var resultCallback: ((PermissionResult) -> Unit)? = null

    /**
     * Permissions to request
     */
    fun request(vararg permissions: String): ActivityRequestBuilder {
        this.permissions = permissions.toList()
        return this
    }

    /**
     * Called when rationale should be shown
     */
    fun onRationale(
        callback: (permissions: List<String>, proceed: () -> Unit) -> Unit
    ): ActivityRequestBuilder {
        this.rationaleCallback = callback
        return this
    }

    /**
     * Called when permission is permanently denied
     */
    fun onDenied(
        callback: (PermissionResult) -> Unit
    ): ActivityRequestBuilder {
        this.deniedCallback = callback
        return this
    }

    /**
     * Executes the permission request
     */
    fun execute(
        callback: (PermissionResult) -> Unit
    ) {
        this.resultCallback = callback

        requester.request(permissions) { result ->
            when {
                result.denied.isNotEmpty() && rationaleCallback != null -> {
                    rationaleCallback?.invoke(result.denied) {
                        requester.request(permissions, callback)
                    }
                }

                result.permanentlyDenied.isNotEmpty() -> {
                    deniedCallback?.invoke(result)
                    callback(result)
                }

                else -> {
                    callback(result)
                }
            }
        }
    }
}
