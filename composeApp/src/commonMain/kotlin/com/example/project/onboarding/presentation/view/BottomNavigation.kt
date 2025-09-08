package com.example.project.onboarding.presentation.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import apptemplate.composeapp.generated.resources.Res
import apptemplate.composeapp.generated.resources.get_started
import apptemplate.composeapp.generated.resources.next
import apptemplate.composeapp.generated.resources.previous
import org.jetbrains.compose.resources.stringResource

@Composable
fun BottomNavigation(
    currentPage: Int,
    totalPages: Int,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isFirstPage = currentPage == 0
    val isLastPage = currentPage == totalPages - 1

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Previous button
        AnimatedVisibility(
            visible = !isFirstPage,
            enter = fadeIn(animationSpec = tween(300)),
            exit = fadeOut(animationSpec = tween(300)),
        ) {
            OutlinedButton(onClick = onPrevious, shape = RoundedCornerShape(24.dp)) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = stringResource(Res.string.previous),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(Res.string.previous))
            }
        }

        // Spacer for first page
        if (isFirstPage) {
            Spacer(modifier = Modifier.weight(1f))
        }

        // Next/Complete button
        Button(
            onClick = if (isLastPage) onComplete else onNext,
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        ) {
            Text(
                text = stringResource(if (isLastPage) Res.string.get_started else Res.string.next),
                style = MaterialTheme.typography.labelLarge,
            )
            if (!isLastPage) {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = stringResource(Res.string.next),
                    modifier = Modifier.size(20.dp),
                )
            }
        }
    }
}
