package com.h_fahmy.composecanvas.screens.weightpicker

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ScaleStyle(
    val width: Dp = 150.dp,
    val radius: Dp = 550.dp,
    val normalLineColor: Color = Color.Gray,
    val fiveStepLineColor: Color = Color.Green,
    val tenStepLineColor: Color = Color.Black,
    val normalLineLength: Dp = 15.dp,
    val fiveStepLineLength: Dp = 25.dp,
    val tenStepLineLength: Dp = 35.dp,
    val indicatorColor: Color = Color.Green,
    val indicatorLength: Dp = 60.dp,
    val textSize: TextUnit = 18.sp,
)
