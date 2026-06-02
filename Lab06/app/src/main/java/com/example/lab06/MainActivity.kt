package com.example.lab06

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.core.ui.SecAlertsTheme
import com.example.feature.alerts.AddTechnologyScreen
import com.example.feature.alerts.AlertScreen
import com.example.feature.alerts.AlertsViewModel
import com.example.feature.alerts.MyStackScreen
import com.example.feature.alerts.TechnologiesViewModel
import com.example.feature.dashboard.DashboardScreen
import com.example.feature.dashboard.DashboardViewModel
import dagger.hilt.android.AndroidEntryPoint

enum class AppTab {
    ALERTS, ADD_TECH, MY_STACK, DASHBOARD
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SecAlertsTheme {
                var currentTab by remember { mutableStateOf(AppTab.ALERTS) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        NavigationBar {
                            NavigationBarItem(
                                selected = currentTab == AppTab.ALERTS,
                                onClick = { currentTab = AppTab.ALERTS },
                                icon = { Icon(Icons.Default.List, contentDescription = "Alertas") },
                                label = { Text("Alertas") }
                            )
                            NavigationBarItem(
                                selected = currentTab == AppTab.ADD_TECH,
                                onClick = { currentTab = AppTab.ADD_TECH },
                                icon = { Icon(Icons.Default.Add, contentDescription = "Registrar") },
                                label = { Text("Registrar") }
                            )
                            NavigationBarItem(
                                selected = currentTab == AppTab.MY_STACK,
                                onClick = { currentTab = AppTab.MY_STACK },
                                icon = { Icon(Icons.Default.Build, contentDescription = "Mi Stack") },
                                label = { Text("Mi Stack") }
                            )
                            NavigationBarItem(
                                selected = currentTab == AppTab.DASHBOARD,
                                onClick = { currentTab = AppTab.DASHBOARD },
                                icon = { Icon(Icons.Default.Info, contentDescription = "Dashboard") },
                                label = { Text("Dashboard") }
                            )
                        }
                    }
                ) { innerPadding ->
                    val contentModifier = Modifier.padding(innerPadding)
                    when (currentTab) {
                        AppTab.ALERTS -> {
                            val viewModel: AlertsViewModel = hiltViewModel()
                            val uiState by viewModel.uiState.collectAsState()
                            AlertScreen(
                                state = uiState,
                                onRefresh = viewModel::syncAlerts,
                                onSeveritySelected = viewModel::selectSeverity,
                                onDismissError = viewModel::dismissError,
                                onResolveAlert = viewModel::toggleAlertResolution,
                                modifier = contentModifier
                            )
                        }
                        AppTab.ADD_TECH -> {
                            val viewModel: TechnologiesViewModel = hiltViewModel()
                            val uiState by viewModel.uiState.collectAsState()
                            AddTechnologyScreen(
                                state = uiState,
                                onCheckCves = viewModel::checkCves,
                                onAddTechnology = viewModel::addTechnology,
                                onClearSearchResults = viewModel::clearSearchResults,
                                onDismissError = viewModel::dismissError,
                                modifier = contentModifier
                            )
                        }
                        AppTab.MY_STACK -> {
                            val viewModel: TechnologiesViewModel = hiltViewModel()
                            val uiState by viewModel.uiState.collectAsState()
                            MyStackScreen(
                                state = uiState,
                                onDeleteTechnology = viewModel::deleteTechnology,
                                onDismissError = viewModel::dismissError,
                                modifier = contentModifier
                            )
                        }
                        AppTab.DASHBOARD -> {
                            val viewModel: DashboardViewModel = hiltViewModel()
                            val uiState by viewModel.uiState.collectAsState()
                            DashboardScreen(
                                state = uiState,
                                modifier = contentModifier
                            )
                        }
                    }
                }
            }
        }
    }
}