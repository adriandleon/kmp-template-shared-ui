package com.example.project.onboarding.presentation.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project.onboarding.domain.entity.SlideEntity

@Composable
fun OnboardingPage(slide: SlideEntity, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // Icon with animated entrance
        AnimatedVisibility(
            visible = true,
            enter =
                fadeIn(animationSpec = tween(600)) +
                    slideInVertically(animationSpec = tween(600), initialOffsetY = { -it / 2 }),
            exit = fadeOut(animationSpec = tween(300)),
        ) {
            Card(
                modifier = Modifier.size(120.dp),
                shape = CircleShape,
                colors =
                    CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = slide.icon, fontSize = 48.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Title with animated entrance
        AnimatedVisibility(
            visible = true,
            enter =
                fadeIn(animationSpec = tween(600, delayMillis = 200)) +
                    slideInVertically(
                        animationSpec = tween(600, delayMillis = 200),
                        initialOffsetY = { -it / 2 },
                    ),
            exit = fadeOut(animationSpec = tween(300)),
        ) {
            Text(
                text = slide.title,
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Description with animated entrance
        AnimatedVisibility(
            visible = true,
            enter =
                fadeIn(animationSpec = tween(600, delayMillis = 400)) +
                    slideInVertically(
                        animationSpec = tween(600, delayMillis = 400),
                        initialOffsetY = { -it / 2 },
                    ),
            exit = fadeOut(animationSpec = tween(300)),
        ) {
            Text(
                text = slide.description,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
    }
}
