package com.h_fahmy.composecanvas.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.h_fahmy.composecanvas.extenstions.netgrid.displayNetGrid
import com.ramcosta.composedestinations.annotation.Destination
import kotlin.math.atan2

@Destination
@Composable
fun AnimatedArrow() {
    val pathProgress = remember {
        Animatable(0f)
    }

    LaunchedEffect(key1 = true) {
        pathProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 5000
            )
        )
    }

    val path = Path().apply {
        moveTo(100f, 100f)
        quadraticBezierTo(100f, 400f, 400f, 400f)
        quadraticBezierTo(900f, 200f, 900f, 700f)
        lineTo(100f, 2000f)
        cubicTo(1700f, 2000f, 1000f, 700f, 200f, 700f)
    }

    val outPath = android.graphics.Path()
    val pos = FloatArray(2)
    val tan = FloatArray(2)

    android.graphics.PathMeasure().apply {
        setPath(path.asAndroidPath(), false)
        val currentDistance = pathProgress.value * length
        getSegment(0f, currentDistance, outPath, true)
        getPosTan(currentDistance, pos, tan)
    }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .displayNetGrid()
    ) {
        // draw the segment progress path
        drawPath(
            path = outPath.asComposePath(),
            color = Color.Red,
            style = Stroke(
                width = 5.dp.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        // Draw the triangle arrow
        val x = pos[0]
        val y = pos[1]
        val degrees =
            (Math.toDegrees((-atan2(tan[0], tan[1])).toDouble()) - 180).toFloat()

        rotate(degrees = degrees, pivot = Offset(x, y)) {
            drawPath(
                path = Path().apply {
                    moveTo(x, y - 30f)
                    lineTo(x - 30f, y + 60)
                    lineTo(x + 30f, y + 60f)
                    close()
                },
                color = Color.Red
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnimatedArrowPreview() {
    AnimatedArrow()
}