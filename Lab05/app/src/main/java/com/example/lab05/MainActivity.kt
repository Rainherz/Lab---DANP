package com.example.lab05

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import com.example.lab05.navigation.AppNavigation
import com.example.lab05.ui.theme.*
import com.example.lab05.ui.viewmodel.StoreViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: StoreViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeMode by viewModel.themeMode.collectAsState()
            val isDarkMode by viewModel.isDarkMode.collectAsState()

            ModularStoreTheme(
                themeMode = themeMode,
                isDarkMode = isDarkMode
            ) {
                AppNavigation(
                    viewModel = viewModel,
                    onThemeChange = { mode ->
                        viewModel.setThemeMode(mode)
                    }
                )
            }
        }
    }
}