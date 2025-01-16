package com.loyalflower.parabolicanimation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.pow


/**
 * 포물선 + 크기 조절 애니메이션 예제
 * 화면 크기에 따라 맞춰지는 애니메이션
 *
 * @param painter 애니메이션이 적용되는 이미지 페인터
 * @param initialImageSize 처음 이미지 사이즈
 * @param targetImageSize 목표 이미지 사이즈
 * @param targetY 포물선의 세로 이동 거리
 * @param animationDuration 애니메이션 속도
 * @param startDelay 애니메이션 시작 딜레이
 * @param onAnimationComplete 애니메이션 끝나고 호출되는 콜백함수
 * @param isReversed 애니메이션 좌우 반전
 */
@Composable
fun ParabolicScaleAnimation(
    painter: Painter,
    initialImageSize: Int = 600,
    targetImageSize: Int = 128,
    animationDuration: Int = 2000,
    startDelay: Long = 100,
    onAnimationComplete: () -> Unit = {},
    isReversed:Boolean = false
) {
    // 화면 크기 계산
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp

    // 애니메이션 상태
    var isAnimating by remember { mutableStateOf(false) }

    /**
     * 이차방정식을 활용한 포물선 애니메이션
     *
     * X = a(Y+b)*(Y+b) - c 기본 식 (왼쪽으로 가는 포물선 그래프)
     * parabolaCoefficient * (-verticalProgress(아래로의 이동 방향이 그래프로 치면 음수) + vertexY).pow(2) - parabolaWidth
     */
    val targetY = screenHeight / 2.0f - 64

    val parabolaWidth = screenWidth / 2.0f - targetImageSize / 2.0f // 포물선의 최대 가로 이동 거리

    val parabolaCoefficient = 4.0f * parabolaWidth / targetY / targetY // 포물선의 곡률 계수

    val vertexY = targetY / 2.0f // 포물선의 중심점 Y좌표


    // Y축 애니메이션
    val verticalProgress by animateFloatAsState(
        targetValue = if (isAnimating) targetY else 0.0f,
        animationSpec = tween(
            durationMillis = animationDuration,
            easing = FastOutSlowInEasing
        ),
        finishedListener = { onAnimationComplete() }, label = "vertical_movement"
    )

    // X축 포물선 이동 계산
    val horizontalOffset:Float = if(isReversed){
            (-parabolaCoefficient * (-verticalProgress + vertexY).pow(2) + parabolaWidth)
    } else{
            (parabolaCoefficient * (-verticalProgress + vertexY).pow(2) - parabolaWidth)
    }

    // 크기 애니메이션
    val currentSize by animateDpAsState(
        targetValue = if (isAnimating) targetImageSize.dp else initialImageSize.dp,
        animationSpec = tween(animationDuration),
        label = "size_animation"
    )

    // LaunchedEffect로 애니메이션 시작
    LaunchedEffect(key1 = Unit) {
        delay(startDelay)
        isAnimating = true
    }

    // 이미지 표시
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
                .size(currentSize)
                .offset(
                    x = horizontalOffset.dp,
                    y = verticalProgress.dp
                )
        )
    }
}

