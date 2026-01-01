package com.champ96k.permissionkit

import androidx.fragment.app.Fragment
import com.champ96k.permissionkit.core.PermissionResult
import com.champ96k.permissionkit.fragment.FragmentPermissionRequester

/**
 * Fluent permission request builder for Fragment.
 */
class FragmentRequestBuilder(
    private val fragment: Fragment
) {

    private val requester = FragmentPermissionRequester(fragment)

    private var permissions: List<String> = emptyList()
    private var rationaleCallback: ((List<String>, () -> Unit) -> Unit)? = null
    private var deniedCallback: ((PermissionResult) -> Unit)? = null
    private var resultCallback: ((PermissionResult) -> Unit)? = null

    fun request(vararg permissions: String): FragmentRequestBuilder {
        this.permissions = permissions.toList()
        return this
    }

    fun onRationale(
        callback: (permissions: List<String>, proceed: () -> Unit) -> Unit
    ): FragmentRequestBuilder {
        this.rationaleCallback = callback
        return this
    }

    fun onDenied(
        callback: (PermissionResult) -> Unit
    ): FragmentRequestBuilder {
        this.deniedCallback = callback
        return this
    }

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
