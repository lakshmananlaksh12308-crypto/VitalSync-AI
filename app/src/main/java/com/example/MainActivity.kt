package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.example.ui.navigation.VitalSyncApp
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.VitalSyncViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: VitalSyncViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                VitalSyncApp(
                    viewModel = viewModel,
                    navController = navController
                )
            }
        }
    }
}

