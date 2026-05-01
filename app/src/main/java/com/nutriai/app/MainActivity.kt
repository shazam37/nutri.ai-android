package com.nutriai.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nutriai.app.ui.NutriAiApp
import com.nutriai.app.ui.NutriTheme
import com.nutriai.app.ui.NutriViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NutriTheme {
                val viewModel: NutriViewModel = viewModel()
                NutriAiApp(viewModel = viewModel)
            }
        }
    }
}
