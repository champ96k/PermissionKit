package com.champ96k.sample

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.champ96k.permissionkit.core.PermissionResult

@Composable
fun PermissionRow(
    label: String,
    permission: String,
    result: PermissionResult?,
) {
    val status = when {
        result == null -> "Not Requested"
        result.granted.contains(permission) -> "Granted"
        result.permanentlyDenied.contains(permission) -> "Permanently Denied"
        result.denied.contains(permission) -> "Denied"
        else -> "Unknown"
    }

    Card(
        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column {
                Text(text = label, fontWeight = FontWeight.Medium)
                Text(
                    text = status,
                    style = MaterialTheme.typography.bodySmall,
                    color = when (status) {
                        "Granted" -> MaterialTheme.colorScheme.primary
                        "Permanently Denied" -> MaterialTheme.colorScheme.error
                        "Denied" -> MaterialTheme.colorScheme.tertiary
                        else -> MaterialTheme.colorScheme.outline
                    }
                )
            }
        }
    }
}
