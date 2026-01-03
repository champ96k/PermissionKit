package com.champ96k.permissionkit

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.fragment.app.Fragment
import com.champ96k.permissionkit.compose.rememberPermissionRequester
import com.champ96k.permissionkit.core.PermissionResult

/**
 * PermissionKit is the single public entry point for requesting
 * Android runtime permissions.
 *
 * Supports Activity, Fragment, and Jetpack Compose.
 */
object PermissionKit {

    /**
     * Create a permission request bound to an Activity lifecycle.
     */
    @Suppress("unused")
    fun from(activity: ComponentActivity): ActivityRequestBuilder =
        ActivityRequestBuilder(activity)

    /**
     * Create a permission request bound to a Fragment lifecycle.
     */
    @Suppress("unused")
    fun from(fragment: Fragment): FragmentRequestBuilder =
        FragmentRequestBuilder(fragment)

    /**
     * Create a permission requester for Jetpack Compose.
     *
     * @return A lambda that triggers the permission request.
     */
    @Composable
    fun rememberRequester(
        permissions: Array<String>,
        onRationale: ((List<String>, () -> Unit) -> Unit)? = null,
        onDenied: ((PermissionResult) -> Unit)? = null,
        onResult: (PermissionResult) -> Unit
    ): () -> Unit {
        return rememberPermissionRequester(
            permissions = permissions,
            onRationale = onRationale,
            onDenied = onDenied,
            onResult = onResult
        )
    }
}
