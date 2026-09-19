package com.example.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.data.model.UserRole
import com.example.ui.components.TopAppBarHeader
import com.example.ui.screens.ClinicianDashboardScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.LandingScreen
import com.example.ui.screens.PatientResultsScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SoapNoteScreen
import com.example.ui.screens.TrendAnalysisScreen
import com.example.ui.screens.UploadScreen
import com.example.ui.theme.MedicalPrimary
import com.example.ui.viewmodel.VitalSyncViewModel

@Composable
fun VitalSyncApp(
    viewModel: VitalSyncViewModel,
    navController: NavHostController
) {
    val userRole by viewModel.userRole.collectAsState()
    val reports by viewModel.reports.collectAsState()
    val selectedReport by viewModel.selectedReport.collectAsState()
    val allBiomarkers by viewModel.allBiomarkers.collectAsState()
    val selectedBiomarkerName by viewModel.selectedBiomarkerName.collectAsState()

    val stagedUri by viewModel.stagedFileUri.collectAsState()
    val stagedFileName by viewModel.stagedFileName.collectAsState()
    val stagedFileSize by viewModel.stagedFileSize.collectAsState()
    val stagedFileMime by viewModel.stagedFileMime.collectAsState()

    val isAnalyzing by viewModel.isAnalyzing.collectAsState()
    val currentStage by viewModel.currentStage.collectAsState()
    val analysisError by viewModel.analysisError.collectAsState()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val isLanding = currentRoute == Screen.Landing.route

    val bottomNavItems = listOf(
        Triple(Screen.Dashboard.route, "Dashboard", Icons.Default.Dashboard),
        Triple(Screen.Upload.route, "Upload", Icons.Default.CloudUpload),
        Triple(
            if (userRole == UserRole.CLINICIAN) Screen.ClinicianDashboard.route else Screen.PatientResults.route,
            if (userRole == UserRole.CLINICIAN) "Clinical" else "Insights",
            if (userRole == UserRole.CLINICIAN) Icons.Default.MedicalServices else Icons.Default.Assessment
        ),
        Triple(Screen.TrendAnalysis.route, "Trends", Icons.Default.Timeline),
        Triple(Screen.History.route, "History", Icons.Default.History)
    )

    Scaffold(
        topBar = {
            if (!isLanding) {
                TopAppBarHeader(
                    currentRole = userRole,
                    onRoleToggle = { newRole ->
                        viewModel.setUserRole(newRole)
                        // If currently on a results page, route to the corresponding view for the new role
                        if (currentRoute == Screen.PatientResults.route && newRole == UserRole.CLINICIAN) {
                            navController.navigate(Screen.ClinicianDashboard.route) {
                                launchSingleTop = true
                            }
                        } else if (currentRoute == Screen.ClinicianDashboard.route && newRole == UserRole.PATIENT) {
                            navController.navigate(Screen.PatientResults.route) {
                                launchSingleTop = true
                            }
                        }
                    },
                    onSettingsClick = {
                        navController.navigate(Screen.Settings.route)
                    }
                )
            }
        },
        bottomBar = {
            if (!isLanding) {
                NavigationBar(
                    containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface
                ) {
                    bottomNavItems.forEach { (route, label, icon) ->
                        val isSelected = currentRoute == route ||
                                (route == Screen.PatientResults.route && currentRoute == Screen.PatientResults.route) ||
                                (route == Screen.ClinicianDashboard.route && currentRoute == Screen.ClinicianDashboard.route)

                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                if (route == Screen.PatientResults.route || route == Screen.ClinicianDashboard.route) {
                                    // Make sure a report is selected
                                    if (selectedReport == null && reports.isNotEmpty()) {
                                        viewModel.selectReport(reports.first())
                                    }
                                }
                                navController.navigate(route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = label
                                )
                            },
                            label = {
                                Text(
                                    text = label,
                                    fontSize = 11.sp
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MedicalPrimary,
                                selectedTextColor = MedicalPrimary,
                                indicatorColor = MedicalPrimary.copy(alpha = 0.15f)
                            ),
                            modifier = Modifier.testTag("nav_item_${label.lowercase()}")
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Landing.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Landing.route) {
                LandingScreen(
                    onGetStarted = {
                        navController.navigate(Screen.Dashboard.route)
                    },
                    onEnterPatientMode = {
                        viewModel.setUserRole(UserRole.PATIENT)
                        navController.navigate(Screen.Dashboard.route)
                    },
                    onEnterClinicianMode = {
                        viewModel.setUserRole(UserRole.CLINICIAN)
                        navController.navigate(Screen.Dashboard.route)
                    },
                    onTryDemoReport = {
                        viewModel.loadDemoReport {
                            val target = if (userRole == UserRole.CLINICIAN) Screen.ClinicianDashboard.route else Screen.PatientResults.route
                            navController.navigate(target)
                        }
                    }
                )
            }

            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    userRole = userRole,
                    reports = reports,
                    allBiomarkers = allBiomarkers,
                    onUploadClick = {
                        navController.navigate(Screen.Upload.route)
                    },
                    onTryDemoClick = {
                        viewModel.loadDemoReport {
                            val target = if (userRole == UserRole.CLINICIAN) Screen.ClinicianDashboard.route else Screen.PatientResults.route
                            navController.navigate(target)
                        }
                    },
                    onSelectReport = { report ->
                        viewModel.selectReport(report)
                        val target = if (userRole == UserRole.CLINICIAN) Screen.ClinicianDashboard.route else Screen.PatientResults.route
                        navController.navigate(target)
                    },
                    onViewTrends = {
                        navController.navigate(Screen.TrendAnalysis.route)
                    },
                    onViewSoapNotes = {
                        if (selectedReport == null && reports.isNotEmpty()) {
                            viewModel.selectReport(reports.first())
                        }
                        navController.navigate(Screen.SoapNote.route)
                    },
                    onViewHistory = {
                        navController.navigate(Screen.History.route)
                    }
                )
            }

            composable(Screen.Upload.route) {
                UploadScreen(
                    stagedUri = stagedUri,
                    stagedFileName = stagedFileName,
                    stagedFileSize = stagedFileSize,
                    stagedFileMime = stagedFileMime,
                    isAnalyzing = isAnalyzing,
                    currentStage = currentStage,
                    analysisError = analysisError,
                    onFileSelected = { uri, name, size, mime ->
                        viewModel.setStagedFile(uri, name, size, mime)
                    },
                    onClearStagedFile = {
                        viewModel.clearStagedFile()
                    },
                    onAnalyzeClick = {
                        viewModel.analyzeStagedFile { reportId ->
                            val target = if (userRole == UserRole.CLINICIAN) Screen.ClinicianDashboard.route else Screen.PatientResults.route
                            navController.navigate(target)
                        }
                    },
                    onTryDemoClick = {
                        viewModel.loadDemoReport { reportId ->
                            val target = if (userRole == UserRole.CLINICIAN) Screen.ClinicianDashboard.route else Screen.PatientResults.route
                            navController.navigate(target)
                        }
                    }
                )
            }

            composable(Screen.PatientResults.route) {
                val reportToDisplay = selectedReport ?: reports.firstOrNull()
                PatientResultsScreen(
                    report = reportToDisplay,
                    onSwitchToClinicianMode = {
                        viewModel.setUserRole(UserRole.CLINICIAN)
                        navController.navigate(Screen.ClinicianDashboard.route)
                    },
                    onViewTrends = { biomarkerName ->
                        viewModel.setSelectedBiomarkerName(biomarkerName)
                        navController.navigate(Screen.TrendAnalysis.route)
                    }
                )
            }

            composable(Screen.ClinicianDashboard.route) {
                val reportToDisplay = selectedReport ?: reports.firstOrNull()
                ClinicianDashboardScreen(
                    report = reportToDisplay,
                    onViewSoapNote = {
                        navController.navigate(Screen.SoapNote.route)
                    },
                    onViewTrends = { biomarkerName ->
                        viewModel.setSelectedBiomarkerName(biomarkerName)
                        navController.navigate(Screen.TrendAnalysis.route)
                    },
                    onSwitchToPatientView = {
                        viewModel.setUserRole(UserRole.PATIENT)
                        navController.navigate(Screen.PatientResults.route)
                    }
                )
            }

            composable(Screen.TrendAnalysis.route) {
                TrendAnalysisScreen(
                    allBiomarkers = allBiomarkers,
                    selectedBiomarkerName = selectedBiomarkerName,
                    onSelectBiomarkerName = { name ->
                        viewModel.setSelectedBiomarkerName(name)
                    }
                )
            }

            composable(Screen.SoapNote.route) {
                val reportToDisplay = selectedReport ?: reports.firstOrNull()
                SoapNoteScreen(
                    report = reportToDisplay,
                    onSaveSoapNote = { s, o, a, p ->
                        viewModel.updateSoapNote(s, o, a, p)
                    }
                )
            }

            composable(Screen.History.route) {
                HistoryScreen(
                    reports = reports,
                    onSelectPatientView = { report ->
                        viewModel.selectReport(report)
                        viewModel.setUserRole(UserRole.PATIENT)
                        navController.navigate(Screen.PatientResults.route)
                    },
                    onSelectClinicianView = { report ->
                        viewModel.selectReport(report)
                        viewModel.setUserRole(UserRole.CLINICIAN)
                        navController.navigate(Screen.ClinicianDashboard.route)
                    },
                    onDeleteReport = { reportId ->
                        viewModel.deleteReport(reportId)
                    }
                )
            }

            composable(Screen.Settings.route) {
                SettingsScreen(
                    currentRole = userRole,
                    onRoleChange = { newRole ->
                        viewModel.setUserRole(newRole)
                    },
                    onClearAllData = {
                        viewModel.clearAllData()
                    },
                    onResetDemoData = {
                        viewModel.resetDemoData()
                    }
                )
            }
        }
    }
}
