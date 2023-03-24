package com.h_fahmy.composecanvas.extenstions.netgrid

import android.graphics.Paint
import android.graphics.Rect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.sp

fun Modifier.displayNetGrid(
    netGridStyle: NetGridStyle = NetGridStyle()
) = drawBehind {
    val width = size.width
    val height = size.height

    val majorXLinesStyle = netGridStyle.majorXLineStyle
    val majorYLinesStyle = netGridStyle.majorYLineStyle
    val minorXLinesStyle = netGridStyle.minorXLineStyle
    val minorYLinesStyle = netGridStyle.minorYLineStyle

    val xNumbersStyle = netGridStyle.xNumbersStyle
    val yNumbersStyle = netGridStyle.yNumbersStyle

    // draw the x lines
    for (i in 0.rangeTo(width.toInt())) {

        val lineType = when {
            i % majorXLinesStyle.stepSize == 0 -> NetGridStyle.LineStyle.Type.Major
            i % minorXLinesStyle.stepSize == 0 -> NetGridStyle.LineStyle.Type.Minor
            else -> null
        } ?: continue

        val lineStyle = when (lineType) {
            NetGridStyle.LineStyle.Type.Major -> majorXLinesStyle
            NetGridStyle.LineStyle.Type.Minor -> minorXLinesStyle
        }

        val x = i.toFloat()
        drawLine(
            color = lineStyle.color,
            start = Offset(x, 0f),
            end = Offset(x, height),
            strokeWidth = lineStyle.stroke
        )

        // draw the numbers
        if (lineStyle.displayNumbers) {
            val textPaint = Paint().apply {
                color = xNumbersStyle.color.toArgb()
                textSize = xNumbersStyle.textSize.toPx()
                textAlign = Paint.Align.CENTER
                isFakeBoldText = true
            }
            val textBounds = Rect()
            textPaint.getTextBounds(i.toString(), 0, i.toString().length, textBounds)

            val padding = 5.sp.toPx()
            drawRect(
                color = xNumbersStyle.backgroundColor,
                topLeft = Offset(
                    x - textBounds.width() / 2f - padding / 2,
                    xNumbersStyle.offset.toPx() - textBounds.height() - padding / 2
                ),
                size = Size(
                    textBounds.width().toFloat() + padding,
                    textBounds.height().toFloat() + padding
                )
            )
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    i.toString(),
                    x,
                    xNumbersStyle.offset.toPx(),
                    textPaint
                )
            }
        }
    }

    // draw the y lines
    for (i in 0.rangeTo(height.toInt())) {
        val lineType = when {
            i % majorYLinesStyle.stepSize == 0 -> NetGridStyle.LineStyle.Type.Major
            i % minorYLinesStyle.stepSize == 0 -> NetGridStyle.LineStyle.Type.Minor
            else -> null
        } ?: continue

        val lineStyle = when (lineType) {
            NetGridStyle.LineStyle.Type.Major -> majorYLinesStyle
            NetGridStyle.LineStyle.Type.Minor -> minorYLinesStyle
        }

        val y = i.toFloat()
        drawLine(
            color = lineStyle.color,
            start = Offset(0f, y),
            end = Offset(width, y),
            strokeWidth = lineStyle.stroke
        )

        // draw the numbers
        if (lineStyle.displayNumbers) {
            val textPaint = Paint().apply {
                color = yNumbersStyle.color.toArgb()
                textSize = yNumbersStyle.textSize.toPx()
                textAlign = Paint.Align.CENTER
                isFakeBoldText = true
            }
            val textBounds = Rect()
            textPaint.getTextBounds(i.toString(), 0, i.toString().length, textBounds)

            val padding = 5.sp.toPx()
            drawRect(
                color = yNumbersStyle.backgroundColor,
                topLeft = Offset(
                    yNumbersStyle.offset.toPx() - textBounds.width() / 2 - padding / 2,
                    y - textBounds.height() / 2 - padding / 2
                ),
                size = Size(
                    textBounds.width().toFloat() + padding,
                    textBounds.height().toFloat() + padding
                )
            )
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    i.toString(),
                    yNumbersStyle.offset.toPx(),
                    y + padding / 2,
                    textPaint
                )
            }
        }
    }
}