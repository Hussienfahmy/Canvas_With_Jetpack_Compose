package com.h_fahmy.composecanvas.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.random.Random

@Composable
fun RandomMovingBox() {
    BoxWithConstraints {
        val boxSize = Size(300f, 300f)

        var randomPosition by remember {
            mutableStateOf(
                Offset(
                    x = Random.nextInt(
                        from = boxSize.width.toInt(),
                        until = constraints.maxWidth - boxSize.width.toInt()
                    ).toFloat(),
                    y = Random.nextInt(
                        from = boxSize.height.toInt(),
                        constraints.maxHeight - boxSize.height.toInt()
                    ).toFloat()
                )
            )
        }

        val infiniteAnimation = rememberInfiniteTransition()
        val rotationProgress by infiniteAnimation.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = InfiniteRepeatableSpec(
                animation = tween(durationMillis = 3000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )

        val position by animateOffsetAsState(targetValue = randomPosition)

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures {
                        randomPosition = Offset(
                            x = Random
                                .nextInt(
                                    from = boxSize.width.toInt(),
                                    until = constraints.maxWidth - boxSize.width.toInt()
                                )
                                .toFloat(),
                            y = Random
                                .nextInt(
                                    from = boxSize.height.toInt(),
                                    constraints.maxHeight - boxSize.height.toInt()
                                )
                                .toFloat()
                        )
                    }
                }
        ) {
            rotate(
                degrees = rotationProgress,
                pivot = Offset(
                    x = position.x + boxSize.width / 2,
                    y = position.y + boxSize.height / 2
                )
            ) {
                drawRect(
                    color = Color.Red,
                    size = boxSize,
                    topLeft = position
                )
            }
        }
    }
}