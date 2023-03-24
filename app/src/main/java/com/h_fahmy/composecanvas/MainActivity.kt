package com.h_fahmy.composecanvas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.h_fahmy.composecanvas.ui.theme.ComposeCanvasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeCanvasTheme(darkTheme = false) {

            }
        }
    }
}