package com.champ96k.permissionkit

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.fragment.app.Fragment
import com.champ96k.permissionkit.compose.rememberPermissionRequester
import com.champ96k.permissionkit.core.PermissionResult

/**
 * PermissionKit public entry point.
 *
 * This is the ONLY class users need.
 */
object PermissionKit {

    /**
     * Start permission request from Activity
     */
    fun from(activity: ComponentActivity): ActivityRequestBuilder {
        return ActivityRequestBuilder(activity)
    }

    /**
     * Start permission request from Fragment
     */
    fun from(fragment: Fragment): FragmentRequestBuilder {
        return FragmentRequestBuilder(fragment)
    }

    /**
     * Permission request for Jetpack Compose
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
