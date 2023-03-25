package com.h_fahmy.composecanvas.screens.clock

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import kotlin.math.cos
import kotlin.math.sin

@Destination
@Composable
fun ClockScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        var time by remember { mutableStateOf(LocalDateTime.now()) }
        LaunchedEffect(time) {
            delay(1000L)
            time = LocalDateTime.now()
        }

        Clock(
            modifier = Modifier.align(Alignment.Center),
            time = time
        )
    }
}

@Composable
fun Clock(
    modifier: Modifier,
    clockStyle: ClockStyle = ClockStyle(),
    time: LocalDateTime = LocalDateTime.now()
) {
    Canvas(modifier = modifier) {
        val hour = time.hour
        val minutes = time.minute
        val seconds = time.second

        val clockRadius = clockStyle.radius.toPx()
        val hoursLineLength = clockStyle.hoursLineLength.toPx()
        val minutesLineLength = clockStyle.minutesLineLength.toPx()

        // draw the circle of clock
        drawContext.canvas.nativeCanvas.apply {
            drawCircle(
                center.x,
                center.y,
                clockRadius,
                Paint().apply {
                    color = android.graphics.Color.WHITE
                    setShadowLayer(
                        60f,
                        0f,
                        0f,
                        android.graphics.Color.argb(30, 0, 0, 0)
                    )
                }
            )
        }

        // draw hours and minutes lines
        for (i in 0..360) {
            val lineType = when {
                i % 30 == 0 -> LineType.Hours
                i % 6 == 0 -> LineType.Minutes
                else -> continue
            }

            val lineLength = when (lineType) {
                LineType.Hours -> hoursLineLength
                LineType.Minutes -> minutesLineLength
            }

            val lineColor = when (lineType) {
                LineType.Hours -> clockStyle.hoursLineColor
                LineType.Minutes -> clockStyle.minutesLineColor
            }

            val angleInRad = Math.toRadians(i.toDouble())

            val lineStart = Offset(
                x = (clockRadius - lineLength) * cos(angleInRad).toFloat(),
                y = (clockRadius - lineLength) * sin(angleInRad).toFloat()
            )
            val lineEnd = Offset(
                x = clockRadius * cos(angleInRad).toFloat(),
                y = clockRadius * sin(angleInRad).toFloat()
            )

            drawLine(
                start = lineStart,
                end = lineEnd,
                color = lineColor,
                strokeWidth = 1.dp.toPx(),
                cap = StrokeCap.Round
            )
        }

        // draw hour line
        val hourAngleInRad = Math.toRadians((hour * 30.0) - 90)
        val hourLineEnd = Offset(
            x = (clockRadius - 35.dp.toPx()) * cos(hourAngleInRad).toFloat(),
            y = (clockRadius - 35.dp.toPx()) * sin(hourAngleInRad).toFloat()
        )
        drawLine(
            start = center,
            end = hourLineEnd,
            color = Color.Black,
            strokeWidth = 5.dp.toPx(),
            cap = StrokeCap.Round
        )

        // draw minute line
        val minuteAngleInRad = Math.toRadians((minutes * 6.0) - 90)
        val minuteLineEnd = Offset(
            x = (clockRadius - 25.dp.toPx()) * cos(minuteAngleInRad).toFloat(),
            y = (clockRadius - 25.dp.toPx()) * sin(minuteAngleInRad).toFloat()
        )
        drawLine(
            start = center,
            end = minuteLineEnd,
            color = Color.Black,
            strokeWidth = 3.dp.toPx(),
            cap = StrokeCap.Round
        )

        // draw seconds line
        val secondsAngleInRad = Math.toRadians((seconds * 6.0) - 90)
        val secondsLineEnd = Offset(
            x = (clockRadius - 15.dp.toPx()) * cos(secondsAngleInRad).toFloat(),
            y = (clockRadius - 15.dp.toPx()) * sin(secondsAngleInRad).toFloat()
        )
        drawLine(
            start = center,
            end = secondsLineEnd,
            color = Color.Red,
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}