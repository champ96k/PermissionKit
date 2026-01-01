package com.champ96k.permissionkit.core

/**
 * Core permission decision engine.
 *
 * This class does NOT request permissions.
 * It only interprets Android's permission results.
 */
class PermissionController {

    /**
     * Resolves raw Android permission results into PermissionResult.
     *
     * @param permissions List of requested permissions
     * @param result Android permission result map
     * @param shouldShowRationale Callback from Activity/Fragment
     */
    fun resolve(
        permissions: List<String>,
        result: Map<String, Boolean>,
        shouldShowRationale: (String) -> Boolean
    ): PermissionResult {

        val granted = mutableListOf<String>()
        val denied = mutableListOf<String>()
        val permanentlyDenied = mutableListOf<String>()

        for (permission in permissions) {
            when {
                // User clicked "Allow"
                result[permission] == true -> {
                    granted.add(permission)
                }

                // User clicked "Deny" (can ask again)
                shouldShowRationale(permission) -> {
                    denied.add(permission)
                }

                // User clicked "Don't ask again"
                else -> {
                    permanentlyDenied.add(permission)
                }
            }
        }

        return PermissionResult(
            granted = granted,
            denied = denied,
            permanentlyDenied = permanentlyDenied
        )
    }
}
