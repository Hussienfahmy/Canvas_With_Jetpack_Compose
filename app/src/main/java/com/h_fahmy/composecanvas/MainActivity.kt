@file:OptIn(ExperimentalMaterialApi::class)

package com.h_fahmy.composecanvas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.h_fahmy.composecanvas.destinations.*
import com.h_fahmy.composecanvas.ui.theme.ComposeCanvasTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeCanvasTheme(darkTheme = false) {
                Scaffold() {
                    DestinationsNavHost(
                        navGraph = NavGraphs.root,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it)
                    )
                }
            }
        }
    }
}

@Composable
fun ScreenItem(
    title: String,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        shape = RoundedCornerShape(10.dp),
        onClick = onClick,
    ) {
        Box {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = title,
                style = MaterialTheme.typography.h4
            )
        }
    }
}

@RootNavGraph(start = true)
@Destination
@Composable
fun MainScree(
    navigator: DestinationsNavigator
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(items) { item ->
            ScreenItem(title = item.title, onClick = {
                navigator.navigate(item.destination)
            })
        }
    }
}

data class Item(
    val title: String,
    val destination: DirectionDestination
)

private val items = listOf(
    Item(
        title = "Academic Grades",
        destination = AcademicGradeSelectorScreenDestination
    ),
    Item(
        title = "Clock",
        destination = ClockScreenDestination
    ),
    Item(
        title = "Gender Picker",
        destination = GenderPickerScreenDestination
    ),
    Item(
        title = "Weight Picker",
        destination = WeightScaleScreenDestination
    ),
    Item(
        title = "Animated Arrow",
        destination = AnimatedArrowDestination
    ),
    Item(
        title = "Shape Clicker",
        destination = ShapeClickerGameDestination
    ),
    Item(
        title = "Moving Box",
        destination = RandomMovingBoxScreenDestination
    )
)