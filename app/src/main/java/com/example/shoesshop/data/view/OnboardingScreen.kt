package com.example.shoesshop.data.view

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shoesshop.R
import com.example.shoesshop.ui.theme.AppTypography
import com.example.shoesshop.ui.theme.Typography
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

enum class OnboardingType {
    FIRST, SECOND, THIRD
}

data class OnboardingSlide(
    val type: OnboardingType,
    val titleRes: Int,
    val subtitleRes: Int? = null,
    val topImageRes: Int? = null,
    val tabsImageRes: Int
)

@OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
@Composable
fun OnboardingScreen(
    onFinish: () -> Unit
) {

    val slides = remember {
        listOf(
            OnboardingSlide(
                type = OnboardingType.FIRST,
                titleRes = R.string.welcome,
                topImageRes = R.drawable.image_1,
                tabsImageRes = R.drawable.dots_1
            ),
            OnboardingSlide(
                type = OnboardingType.SECOND,
                titleRes = R.string.journey,
                subtitleRes = R.string.collection,
                topImageRes = R.drawable.image_2,
                tabsImageRes = R.drawable.dots_2
            ),
            OnboardingSlide(
                type = OnboardingType.THIRD,
                titleRes = R.string.power,
                subtitleRes = R.string.plants,
                topImageRes = R.drawable.image_3,
                tabsImageRes = R.drawable.dots_3
            )
        )
    }

    // состояние пейджера (для свайпа)
    val pagerState = rememberPagerState(initialPage = 0)
    val coroutineScope = rememberCoroutineScope()

    // логический currentPage, синхронизированный с pagerState
    var currentPage by remember { mutableIntStateOf(0) }
    LaunchedEffect(pagerState.currentPage) {
        currentPage = pagerState.currentPage
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        colorResource(R.color.Accent),
                        colorResource(R.color.Disable)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset.Infinite
                )
            )
    ) {

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            // Листание свайпом между слайдами
            HorizontalPager(
                count = slides.size,
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                // Внутри оставляем ту же анимацию и разметку, что и была
                AnimatedContent(
                    targetState = page,
                    transitionSpec = {
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            tween(300)
                        ) with slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            tween(300)
                        )
                    }
                ) { targetPage ->
                    when (slides[targetPage].type) {
                        OnboardingType.FIRST -> FirstSlide(slides[targetPage])
                        OnboardingType.SECOND -> SecondSlide(slides[targetPage])
                        OnboardingType.THIRD -> ThirdSlide(slides[targetPage])
                    }
                }
            }
        }

        Button(
            onClick = {
                if (currentPage < slides.lastIndex) {
                    // листаем на следующую страницу анимацией
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(currentPage + 1)
                    }
                } else {
                    onFinish()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.white)
            )
        ) {
            Text(
                text = stringResource(R.string.next),
                color = colorResource(R.color.Text),
                style = AppTypography.bodyMedium14
            )
        }
    }
}

@Composable
fun FirstSlide(slide: OnboardingSlide) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = stringResource(slide.titleRes),
            modifier = Modifier
                .padding(top = 39.dp, start = 54.dp, end = 54.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = Typography.headlineSmall,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(122.dp))

        Image(
            painter = painterResource(slide.topImageRes!!),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )

        Spacer(modifier = Modifier.height(26.dp))

        Image(
            painter = painterResource(slide.tabsImageRes),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(36.dp))
    }
}

@Composable
fun SecondSlide(slide: OnboardingSlide) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(37.dp))

        Image(
            painter = painterResource(slide.topImageRes!!),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )

        Spacer(modifier = Modifier.height(60.dp))

        Text(
            text = stringResource(slide.titleRes),
            style = Typography.headlineLarge,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(13.dp))

        Text(
            text = stringResource(slide.subtitleRes!!),
            style = Typography.bodySmall,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        Image(
            painter = painterResource(slide.tabsImageRes),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
fun ThirdSlide(slide: OnboardingSlide) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(50.dp))

        Image(
            painter = painterResource(slide.topImageRes!!),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = stringResource(slide.titleRes),
            style = Typography.headlineLarge,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(slide.subtitleRes!!),
            style = Typography.bodySmall,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        Spacer(modifier = Modifier.height(36.dp))

        Image(
            painter = painterResource(slide.tabsImageRes),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

/* -------------------- PREVIEW -------------------- */

@Preview(showBackground = true, heightDp = 800)
@Composable
fun OnboardingPreview() {
    OnboardingScreen {}
}
