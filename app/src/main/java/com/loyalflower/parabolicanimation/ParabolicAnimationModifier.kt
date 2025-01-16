package com.loyalflower.parabolicanimation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.pow
import kotlin.math.roundToInt

/**
 * 포물선 애니메이션 Modifier 확장 함수
 *
 * @param targetY 애니메이션 이동 거리
 * @param animationDuration 애니메이션 속도
 * @param startDelay 애니메이션 시작 딜레이
 * @param onAnimationComplete 애니메이션 끝나고 호출되는 콜백함수
 * @param isReversed 애니메이션 좌우 반전
 */
fun Modifier.parabolicAnimation(
    targetY: Float,
    animationDuration: Int = 2000,
    startDelay: Long = 100,
    onAnimationComplete: () -> Unit = {},
    isReversed: Boolean = false
) = composed {
    // 애니메이션 상태
    var isAnimating by remember { mutableStateOf(false) }

    // 컴포넌트의 크기 정보를 얻기 위한 onGloballyPositioned
    var componentSize by remember { mutableStateOf(IntSize.Zero) }

    val density = LocalDensity.current

    // dp를 px로 변환
    val screenWidth = with(density) { LocalConfiguration.current.screenWidthDp.dp.toPx() }

    /**
     * 이차방정식을 활용한 포물선 애니메이션
     */
    val parabolaWidth = screenWidth / 2.0f - componentSize.width / 2.0f
    val parabolaCoefficient = 4.0f * parabolaWidth / targetY / targetY
    val vertexY = targetY / 2.0f

    // Y축 애니메이션
    val verticalProgress by animateFloatAsState(
        targetValue = if (isAnimating) targetY else 0f,
        animationSpec = tween(
            durationMillis = animationDuration,
            easing = FastOutSlowInEasing
        ),
        finishedListener = { onAnimationComplete() }, label = "vertical_movement"
    )

    // X축 포물선 이동 계산
    val horizontalOffset = if (isReversed) {
        -parabolaCoefficient * (-verticalProgress + vertexY).pow(2) + parabolaWidth
    } else {
        parabolaCoefficient * (-verticalProgress + vertexY).pow(2) - parabolaWidth
    }

    LaunchedEffect(key1 = Unit) {
        delay(startDelay)
        isAnimating = true
    }

    this
        .onGloballyPositioned { coordinates ->
            componentSize = coordinates.size
        }
        .offset {
            IntOffset(
                x = horizontalOffset.roundToInt(),
                y = verticalProgress.roundToInt()
            )
        }
}