package com.h_fahmy.composecanvas.screens.academicgrade

import android.graphics.Paint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.h_fahmy.composecanvas.extenstions.netgrid.displayNetGrid
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun AcademicGradeSelectorScreen(
    modifier: Modifier = Modifier
) {
    val grades = listOf(
        AcademicGrade("A"),
        AcademicGrade("A-"),
        AcademicGrade("B+"),
        AcademicGrade("B"),
        AcademicGrade("C+"),
        AcademicGrade("C"),
        AcademicGrade("D"),
        AcademicGrade("F"),
    )

    var selectedGrade by remember {
        mutableStateOf(null as AcademicGrade?)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .displayNetGrid()
    ) {
        AcademicGradeSelector(
            modifier = Modifier.align(Alignment.Center),
            items = grades,
            selectedItem = selectedGrade,
            onSelectedItem = { selectedGrade = it }
        )
    }
}

@Composable
fun <T : AcademicGradeLabelInterface> AcademicGradeSelector(
    modifier: Modifier = Modifier,
    spacing: Dp = 15.dp,
    selectedItemColor: Color = Color.Blue,
    items: List<T>,
    selectedItem: T?,
    onSelectedItem: (T) -> Unit = {},
) {
    val spacingPx = with(LocalDensity.current) { spacing.toPx() }

    var canvasSize by remember { mutableStateOf(Size.Zero) }

    val rectWidth = (canvasSize.width - (spacingPx * (items.size - 1))) / items.size

    val allRectToItem by produceState(initialValue = emptyList(), key1 = canvasSize) {
        this.value = items.mapIndexed { index, item ->
            Rect(
                Offset(
                    index * (rectWidth + spacingPx),
                    0f
                ),
                Size(
                    rectWidth,
                    canvasSize.height
                )
            ) to item
        }
    }

    val selectedItemState by rememberUpdatedState(newValue = selectedItem)

    var oldSelectedItem by remember { mutableStateOf(null as T?) }

    val pathPortion = remember { Animatable(0f) }
    LaunchedEffect(selectedItemState, oldSelectedItem) {
        pathPortion.snapTo(0f)
        pathPortion.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 700)
        )
    }
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .clip(RoundedCornerShape(10.dp))
            .padding(horizontal = 15.dp)
            .height(50.dp)
            .pointerInput(Unit) {
                detectTapGestures {
                    allRectToItem.forEach { (rect, item) ->
                        if (rect.contains(it)) {
                            if (selectedItemState != item) {
                                // only trigger the callback if the item is different
                                onSelectedItem(item)
                                oldSelectedItem = selectedItemState
                            }
                        }
                    }
                }
            }
    ) {
        canvasSize = size

        val paint = Paint().apply {
            textAlign = Paint.Align.CENTER
            textSize = 20.sp.toPx()
        }

        allRectToItem.forEach { (rect, item) ->
            // draw the label
            drawContext.canvas.nativeCanvas.apply {
                val textHeight = paint.fontMetrics.bottom - paint.fontMetrics.top
                drawText(
                    item.label,
                    rect.center.x,
                    rect.center.y + (textHeight / 4),
                    paint
                )
            }

            // draw the circle
            val circlePath = Path().apply {
                moveTo(rect.center.x, rect.center.y)
                addOval(Rect(center = rect.center, radius = rect.width / 2))
            }

            val circlePathItem = when (item) {
                selectedItemState -> {
                    val outPath = Path()
                    PathMeasure().apply {
                        setPath(circlePath, false)
                        getSegment(0f, length * pathPortion.value, outPath, true)
                    }
                    outPath
                }
                oldSelectedItem -> {
                    val outPath = Path()
                    PathMeasure().apply {
                        setPath(circlePath, false)
                        getSegment(0f, length - (length * pathPortion.value), outPath, true)
                    }
                    outPath
                }
                else -> {
                    null
                }
            }

            // draw the selected item circle
            if (circlePathItem != null) {
                drawPath(
                    path = circlePathItem,
                    color = selectedItemColor,
                    style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                )

                // draw the animated background for selected item only
                if (item == selectedItemState) {
                    drawCircle(
                        color = selectedItemColor,
                        radius = (rect.width / 2) * pathPortion.value,
                        center = rect.center,
                        alpha = 1 - pathPortion.value
                    )
                }
            }
        }
    }
}
