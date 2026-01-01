package com.champ96k.permissionkit.core

/**
 * Callback invoked when permissions need explanation.
 *
 * Library user decides HOW to show rationale.
 */
fun interface RationaleHandler {

    /**
     * @param permissions Permissions that need explanation
     * @param proceed Call this to continue permission request
     */
    fun onRationale(
        permissions: List<String>,
        proceed: () -> Unit
    )
}
