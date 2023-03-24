package com.h_fahmy.composecanvas.extenstions.netgrid

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class NetGridStyle(
    val majorXLineStyle: LineStyle = LineStyle.Type.Major.lineStyle,
    val majorYLineStyle: LineStyle = LineStyle.Type.Major.lineStyle,
    val minorXLineStyle: LineStyle = LineStyle.Type.Minor.lineStyle,
    val minorYLineStyle: LineStyle = LineStyle.Type.Minor.lineStyle,
    val xNumbersStyle: NumbersStyle = NumbersStyle(),
    val yNumbersStyle: NumbersStyle = NumbersStyle(),
) {
    data class LineStyle(
        val stroke: Float,
        val color: Color,
        val stepSize: Int,
        val displayNumbers: Boolean = true,
    ) {
        sealed class Type(val lineStyle: LineStyle) {
            object Major : Type(LineStyle(5f, Color.Black, 100))
            object Minor : Type(LineStyle(2f, Color.Gray, 50, false))
        }
    }

    data class NumbersStyle(
        val color: Color = Color.Black,
        val backgroundColor: Color = Color.White,
        val textSize: TextUnit = 10.sp,
        val offset: Dp = 15.dp
    )
}