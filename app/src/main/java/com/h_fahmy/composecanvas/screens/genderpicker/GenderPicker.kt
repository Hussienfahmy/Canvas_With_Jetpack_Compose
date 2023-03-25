package com.h_fahmy.composecanvas.screens.genderpicker

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.h_fahmy.composecanvas.R

@Composable
fun GenderPicker(
    modifier: Modifier = Modifier,
    maleGradient: List<Color> = listOf(Color(0xFF6D6DFF), Color.Blue),
    femaleGradient: List<Color> = listOf(Color(0xFFEA76FF), Color.Magenta),
    distanceBetweenGenders: Dp = 50.dp,
    pathScaleFactor: Float = 9f,
    onGenderSelected: (Gender) -> Unit
) {
    val malePathString = stringResource(id = R.string.male_path)
    val femalePathString = stringResource(id = R.string.female_path)

    var selectedGender by remember { mutableStateOf<Gender>(Gender.Female) }

    var center by remember { mutableStateOf(Offset.Unspecified) }

    val malePath = remember { PathParser().parsePathString(malePathString).toPath() }
    val femalePath = remember { PathParser().parsePathString(femalePathString).toPath() }

    val malePathBounds = remember { malePath.getBounds() }

    val femalePathBounds = remember { malePath.getBounds() }

    var maleTranslationOffset by remember { mutableStateOf(Offset.Zero) }
    var femaleTranslationOffset by remember { mutableStateOf(Offset.Zero) }

    var currentLickOffset by remember { mutableStateOf(Offset.Zero) }

    val maleSelectionRadius by animateFloatAsState(
        targetValue = if (selectedGender is Gender.Male) 80f else 0f,
        animationSpec = tween(durationMillis = 1000)
    )

    val femaleSelectionRadius by animateFloatAsState(
        targetValue = if (selectedGender is Gender.Female) 80f else 0f,
        animationSpec = tween(durationMillis = 1000)
    )

    Canvas(
        modifier = modifier.pointerInput(Unit) {
            detectTapGestures {
                val transformedMaleRect = Rect(
                    offset = maleTranslationOffset,
                    size = malePathBounds.size * pathScaleFactor
                )

                val transformedFemaleRect = Rect(
                    offset = femaleTranslationOffset,
                    size = femalePathBounds.size * pathScaleFactor
                )

                if (selectedGender !is Gender.Male && transformedMaleRect.contains(it)) {
                    // male clicked
                    currentLickOffset = it
                    selectedGender = Gender.Male
                    onGenderSelected(Gender.Male)
                } else if (selectedGender !is Gender.Female && transformedFemaleRect.contains(it)) {
                    // female clicked
                    currentLickOffset = it
                    selectedGender = Gender.Female
                    onGenderSelected(Gender.Female)
                }
            }
        }
    ) {
        center = this.center

        maleTranslationOffset = Offset(
            x = center.x - malePathBounds.width * pathScaleFactor - distanceBetweenGenders.toPx() / 2,
            y = center.y - malePathBounds.height * pathScaleFactor / 2
        )

        femaleTranslationOffset = Offset(
            x = center.x + distanceBetweenGenders.toPx() / 2,
            y = center.y - femalePathBounds.height * pathScaleFactor / 2
        )

        val untransformedMaleClickOffset = if (currentLickOffset == Offset.Zero) {
            // the initial state
            malePathBounds.center
        } else {
            (currentLickOffset - maleTranslationOffset) / pathScaleFactor
        }

        val untransformedFemaleClickOffset = if (currentLickOffset == Offset.Zero) {
            // the initial state
            femalePathBounds.center
        } else {
            (currentLickOffset - femaleTranslationOffset) / pathScaleFactor
        }

        translate(
            left = maleTranslationOffset.x,
            top = maleTranslationOffset.y
        ) {
            scale(
                scale = pathScaleFactor,
                pivot = malePathBounds.topLeft
            ) {
                // draw the male
                drawPath(
                    path = malePath,
                    color = Color.LightGray
                )
                // draw the color inside the male
                clipPath(
                    path = malePath
                ) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = maleGradient,
                            center = untransformedMaleClickOffset,
                            radius = maleSelectionRadius + 1 // gradient can't have radius of zero
                        ),
                        center = untransformedMaleClickOffset,
                        radius = maleSelectionRadius
                    )
                }
            }
        }

        translate(
            left = femaleTranslationOffset.x,
            top = femaleTranslationOffset.y
        ) {
            scale(
                scale = pathScaleFactor,
                pivot = femalePathBounds.topLeft
            ) {
                // draw female
                drawPath(
                    path = femalePath,
                    color = Color.LightGray
                )
                // draw the color inside the femal
                clipPath(
                    path = femalePath
                ) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = femaleGradient,
                            center = untransformedFemaleClickOffset,
                            radius = femaleSelectionRadius + 1 // gradient can't have radius of zero
                        ),
                        center = untransformedFemaleClickOffset,
                        radius = femaleSelectionRadius
                    )
                }
            }
        }
    }
}