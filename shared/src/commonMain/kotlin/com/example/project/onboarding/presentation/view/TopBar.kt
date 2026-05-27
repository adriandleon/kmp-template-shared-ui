package com.example.project.onboarding.presentation.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.project.resources.Res
import com.example.project.resources.skip
import org.jetbrains.compose.resources.stringResource

@Composable
fun TopBar(onSkip: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Skip button
        FilledTonalButton(
            onClick = onSkip,
            shape = RoundedCornerShape(24.dp),
            colors =
                ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
            modifier = Modifier.testTag("skip_onboarding_button"),
        ) {
            Text(
                text = stringResource(Res.string.skip),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
    }
}
