package com.example.project.onboarding.presentation.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.project.onboarding.domain.entity.SlideEntity
import com.example.project.onboarding.presentation.component.OnboardingComponent

/** Content composable for the onboarding screen */
@Composable
fun OnboardingView(component: OnboardingComponent, modifier: Modifier = Modifier) {
    val state by component.state.subscribeAsState()

    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        } else {
            OnboardingPager(
                slides = state.slides,
                currentSlide = state.currentSlide,
                totalSlides = state.totalSlides,
                onNext = { component.nextSlide() },
                onPrevious = { component.previousSlide() },
                onSkip = { component.skipOnboarding() },
                onComplete = { component.completeOnboarding() },
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun OnboardingPager(
    slides: List<SlideEntity>,
    currentSlide: Int,
    totalSlides: Int,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onSkip: () -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState(pageCount = { totalSlides })

    // Sync pager state with component state
    LaunchedEffect(currentSlide) { pagerState.animateScrollToPage(currentSlide) }

    // Sync component state with pager state
    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage != currentSlide) {
            // Handle page change from pager
            when {
                pagerState.currentPage > currentSlide -> onNext()
                pagerState.currentPage < currentSlide -> onPrevious()
            }
        }
    }

    Column(modifier = modifier.fillMaxSize().systemBarsPadding()) {
        // Top bar with skip button
        TopBar(onSkip = onSkip, modifier = Modifier.fillMaxWidth().padding(16.dp))

        // Horizontal pager content
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxWidth().weight(1f)) { page ->
            if (slides.isNotEmpty()) {
                OnboardingPage(
                    slide = slides[page],
                    modifier = Modifier.padding(horizontal = 32.dp),
                )
            }
        }

        // Page indicator - positioned outside pager content
        PageIndicator(
            pagerState = pagerState,
            modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(bottom = 16.dp),
        )

        // Bottom navigation
        BottomNavigation(
            currentPage = pagerState.currentPage,
            totalPages = totalSlides,
            onNext = onNext,
            onPrevious = onPrevious,
            onComplete = onComplete,
            modifier = Modifier.fillMaxWidth().padding(32.dp),
        )
    }
}

@Composable
private fun PageIndicator(pagerState: PagerState, modifier: Modifier = Modifier) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.Center) {
        repeat(pagerState.pageCount) { iteration ->
            val color =
                if (pagerState.currentPage == iteration) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                }

            Box(modifier = Modifier.padding(2.dp).clip(CircleShape).background(color).size(16.dp))
        }
    }
}
