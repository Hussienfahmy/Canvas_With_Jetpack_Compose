package com.h_fahmy.composecanvas.screens.clock

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ClockStyle(
    val radius: Dp = 120.dp,
    val hoursLineLength: Dp = 20.dp,
    val minutesLineLength: Dp = 15.dp,
    val hoursLineColor: Color = Color.Black,
    val minutesLineColor: Color = Color.Gray
)