package com.h_fahmy.composecanvas.screens.academicgrade

data class AcademicGrade(
    val symbol: String,
    val points: Double = 3.33,
    val percentage: Double = 75.0,
) : AcademicGradeLabelInterface {
    override val label: String
        get() = symbol
}