package com.h_fahmy.composecanvas.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.delay
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

@Destination
@Composable
fun ShapeClickerGame() {
    var points by remember { mutableStateOf(0) }
    var isTimerRunning by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Points: $points")
            Button(onClick = {
                isTimerRunning = !isTimerRunning
                points = 0
            }) {
                Text(text = if (isTimerRunning) "Reset" else "Start")
            }
            CountdownTimer(isTimerRunning = isTimerRunning, onTimerFinish = {
                isTimerRunning = false
            })
        }
        BallClicker(
            enabled = isTimerRunning,
            onBallClick = { points++ }
        )
    }
}

@Composable
fun CountdownTimer(
    time: Int = 30,
    isTimerRunning: Boolean = false,
    onTimerFinish: () -> Unit = {},
) {
    var currentTime by remember {
        mutableStateOf(time)
    }
    LaunchedEffect(currentTime, isTimerRunning) {
        if (!isTimerRunning) {
            currentTime = time
            return@LaunchedEffect
        }
        if (currentTime == 0) {
            onTimerFinish()
        } else {
            delay(1000L)
            currentTime--
        }
    }
    Text(text = currentTime.toString())
}

@Composable
fun BallClicker(
    enabled: Boolean = false,
    onBallClick: () -> Unit = {},
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        var shape by remember {
            mutableStateOf(
                Shape.random(
                    canvasWidth = constraints.maxWidth,
                    canvasHeight = constraints.maxHeight
                )
            )
        }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(enabled) {
                    if (!enabled) return@pointerInput
                    detectTapGestures {
                        if (shape.containsPoint(it)) {
                            shape = Shape.random(
                                canvasWidth = constraints.maxWidth,
                                canvasHeight = constraints.maxHeight
                            )
                            onBallClick()
                        }
                    }
                }
        ) {
            shape.drawOn(this)
        }
    }
}

sealed class Shape {
    protected abstract val canvasWidth: Int
    protected abstract val canvasHeight: Int
    abstract var position: Offset

    protected abstract fun randomPosition(): Offset
    abstract fun drawOn(canvas: DrawScope)
    abstract fun containsPoint(point: Offset): Boolean

    fun nextPosition() {
        position = randomPosition()
    }

    data class Circle(
        val radius: Float = 100f,
        val color: Color = Color.Red,
        override val canvasWidth: Int,
        override val canvasHeight: Int,
    ) : Shape() {
        override var position by mutableStateOf(randomPosition())

        override fun drawOn(canvas: DrawScope) {
            canvas.drawCircle(
                color = color,
                radius = radius,
                center = position
            )
        }

        override fun randomPosition(): Offset {
            return Offset(
                x = Random.nextInt(from = radius.toInt(), until = canvasWidth - radius.toInt())
                    .toFloat(),
                y = Random.nextInt(from = radius.toInt(), until = canvasHeight - radius.toInt())
                    .toFloat()
            )
        }

        override fun containsPoint(point: Offset): Boolean {
            val distance = sqrt(
                (point.x - position.x).pow(2)
                        + (point.y - position.y).pow(2)
            )
            return distance <= radius
        }
    }

    data class Square(
        val side: Float = 250f,
        val color: Color = Color.Red,
        override val canvasWidth: Int,
        override val canvasHeight: Int,
    ) : Shape() {
        override var position by mutableStateOf(randomPosition())

        override fun drawOn(canvas: DrawScope) {
            canvas.drawRect(
                color = color,
                topLeft = position,
                size = Size(side, side)
            )
        }

        override fun randomPosition(): Offset {
            return Offset(
                x = Random.nextInt(from = side.toInt(), until = canvasWidth - side.toInt())
                    .toFloat(),
                y = Random.nextInt(from = side.toInt(), until = canvasHeight - side.toInt())
                    .toFloat()
            )
        }

        override fun containsPoint(point: Offset): Boolean {
            return point.x in position.x..(position.x + side)
                    && point.y in position.y..(position.y + side)
        }
    }

    data class Rectangle(
        val width: Float = 350f,
        val height: Float = 200f,
        val color: Color = Color.Red,
        override val canvasWidth: Int,
        override val canvasHeight: Int,
    ) : Shape() {
        override var position by mutableStateOf(randomPosition())

        override fun drawOn(canvas: DrawScope) {
            canvas.drawRect(
                color = color,
                topLeft = position,
                size = Size(width, height)
            )
        }

        override fun randomPosition(): Offset {
            return Offset(
                x = Random.nextInt(from = width.toInt(), until = canvasWidth - width.toInt())
                    .toFloat(),
                y = Random.nextInt(from = height.toInt(), until = canvasHeight - height.toInt())
                    .toFloat()
            )
        }

        override fun containsPoint(point: Offset): Boolean {
            return point.x in position.x..(position.x + width)
                    && point.y in position.y..(position.y + height)
        }
    }

    companion object {
        private val shapes = mutableListOf<Shape>()

        fun random(canvasWidth: Int, canvasHeight: Int): Shape {
            if (shapes.isEmpty()) {
                shapes.add(Circle(canvasWidth = canvasWidth, canvasHeight = canvasHeight))
                shapes.add(Square(canvasWidth = canvasWidth, canvasHeight = canvasHeight))
                shapes.add(Rectangle(canvasWidth = canvasWidth, canvasHeight = canvasHeight))
            }
            val shape = shapes.random()
            shape.nextPosition()
            return shape
        }
    }
}