package com.example.ui.navigation

sealed class Screen(val route: String, val title: String) {
    object Landing : Screen("landing", "Welcome")
    object Dashboard : Screen("dashboard", "Dashboard")
    object Upload : Screen("upload", "Upload Report")
    object PatientResults : Screen("patient_results", "Patient Insights")
    object ClinicianDashboard : Screen("clinician_dashboard", "Clinical Review")
    object TrendAnalysis : Screen("trend_analysis", "Trend Analysis")
    object SoapNote : Screen("soap_note", "Draft SOAP Note")
    object History : Screen("history", "Report History")
    object Settings : Screen("settings", "Settings & Privacy")
}
