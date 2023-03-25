package com.h_fahmy.composecanvas.screens.weightpicker

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.withRotation
import com.ramcosta.composedestinations.annotation.Destination
import kotlin.math.*

@Destination
@Composable
fun WeightScaleScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        var weight by remember {
            mutableStateOf(80)
        }

        Text(
            modifier = Modifier.align(Alignment.Center),
            text = buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 45.sp
                    )
                ) {
                    append(weight.toString())
                }
                append(" ")
                withStyle(
                    SpanStyle(
                        color = Color.Green,
                        fontSize = 25.sp
                    )
                ) {
                    append("Kg")
                }
            }
        )

        WeightScale(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .align(Alignment.BottomCenter),
            initialWeight = 70,
            onWeightChanged = {
                weight = it
            }
        )
    }
}

@Composable
fun WeightScale(
    modifier: Modifier = Modifier,
    style: ScaleStyle = ScaleStyle(),
    minWeight: Int = 20,
    maxWeight: Int = 250,
    initialWeight: Int = 80,
    onWeightChanged: (Int) -> Unit = { }
) {
    val scaleRadius = style.radius
    val scaleWidth = style.width
    var center by remember { mutableStateOf(Offset.Zero) }
    var circleCenter by remember { mutableStateOf(Offset.Zero) }
    // the angle we currently rotating the scale with
    var angle by remember { mutableStateOf(0f) }
    var oldAngle by remember { mutableStateOf(angle) }
    var dragStartedAngle by remember { mutableStateOf(0f) }

    Canvas(
        modifier = modifier
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        // calculate the angle from the circleCenter to the point the user touches his finger on
                        dragStartedAngle = Math.toDegrees(
                            atan2(
                                y = circleCenter.y - offset.y,
                                x = circleCenter.x - offset.x
                            ).toDouble()
                        ).toFloat()
                    },
                    onDragEnd = {
                        oldAngle = angle
                    }
                ) { change, _ ->
                    val touchAngle = Math.toDegrees(
                        atan2(
                            y = circleCenter.y - change.position.y,
                            x = circleCenter.x - change.position.x
                        ).toDouble()
                    ).toFloat()

                    val newAngle = (oldAngle + (touchAngle - dragStartedAngle)).roundToInt()
                    angle = newAngle.coerceIn(
                        minimumValue = initialWeight - maxWeight,
                        maximumValue = initialWeight - minWeight
                    ).toFloat()
                    onWeightChanged((initialWeight - angle).roundToInt())
                }
            }
    ) {
        center = this.center
        circleCenter = Offset(
            x = center.x,
            y = scaleWidth.toPx() / 2f + scaleRadius.toPx()
        )
        val outerRadius = scaleRadius.toPx() + scaleWidth.toPx() / 2f
        val innerRadius = scaleRadius.toPx() - scaleWidth.toPx() / 2f

        // draw the scale area, (stroke)
        drawContext.canvas.nativeCanvas.apply {
            drawCircle(
                circleCenter.x,
                circleCenter.y,
                scaleRadius.toPx(),
                Paint().apply {
                    strokeWidth = scaleWidth.toPx()
                    color = android.graphics.Color.WHITE
                    setStyle(Paint.Style.STROKE)
                    setShadowLayer(
                        60f,
                        0f,
                        0f,
                        android.graphics.Color.argb(50, 0, 0, 0)
                    )
                }
            )
        }
        // Draw Lines
        for (i in minWeight..maxWeight) {
            val angleInRad = Math.toRadians((i - initialWeight + angle - 90).toDouble()).toFloat()
            val lineType = when {
                i % 10 == 0 -> LineType.TenStep
                i % 5 == 0 -> LineType.FiveStep
                else -> LineType.Normal
            }
            val lineColor = when (lineType) {
                LineType.TenStep -> style.tenStepLineColor
                LineType.FiveStep -> style.fiveStepLineColor
                LineType.Normal -> style.normalLineColor
            }
            val lineLength = when (lineType) {
                LineType.TenStep -> style.tenStepLineLength
                LineType.FiveStep -> style.fiveStepLineLength
                LineType.Normal -> style.normalLineLength
            }.toPx()

            val lineStart = Offset(
                x = (outerRadius - lineLength) * cos(angleInRad) + circleCenter.x,
                y = (outerRadius - lineLength) * sin(angleInRad) + circleCenter.y
            )

            val lineEnd = Offset(
                x = outerRadius * cos(angleInRad) + circleCenter.x,
                y = outerRadius * sin(angleInRad) + circleCenter.y
            )

            drawLine(
                color = lineColor,
                start = lineStart,
                end = lineEnd,
                strokeWidth = 1.dp.toPx()
            )

            // draw text number for only TenStep lines
            if (lineType is LineType.TenStep || lineType is LineType.FiveStep) {
                val textSize = if (lineType is LineType.FiveStep) style.textSize.toPx() / 2
                else style.textSize.toPx()

                val textRadius = outerRadius - lineLength - 5.dp.toPx() - textSize
                val x = textRadius * cos(angleInRad) + circleCenter.x
                val y = textRadius * sin(angleInRad) + circleCenter.y

                drawContext.canvas.nativeCanvas.run {
                    withRotation(
                        degrees = Math.toDegrees(angleInRad.toDouble()).toFloat() + 90f,
                        pivotX = x,
                        pivotY = y
                    ) {
                        drawText(
                            abs(i).toString(),
                            x,
                            y,
                            Paint().apply {
                                this.textSize = textSize
                                textAlign = Paint.Align.CENTER
                            }
                        )
                    }
                }
            }
        }

        // draw the weight line indicator
        val middleTop = Offset(
            x = circleCenter.x,
            y = circleCenter.y - innerRadius - style.indicatorLength.toPx()
        )
        val indicatorBaseOffset = 4f
        val bottomLeft = Offset(
            x = circleCenter.x - indicatorBaseOffset,
            y = circleCenter.y - innerRadius
        )
        val bottomRight = Offset(
            x = circleCenter.x + indicatorBaseOffset,
            y = circleCenter.y - innerRadius
        )

        val indicator = Path().apply {
            moveTo(middleTop.x, middleTop.y)
            lineTo(bottomLeft.x, bottomLeft.y)
            lineTo(bottomRight.x, bottomRight.y)
            lineTo(middleTop.x, middleTop.y)
        }
        drawPath(
            path = indicator,
            color = style.indicatorColor
        )
    }
}