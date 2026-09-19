package com.example.ui.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.gemini.MedicalExtractionEngine
import com.example.data.local.VitalSyncDatabase
import com.example.data.model.Biomarker
import com.example.data.model.Report
import com.example.data.model.UserRole
import com.example.data.repository.VitalSyncRepository
import com.example.data.sample.SampleMedicalData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class VitalSyncViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: VitalSyncRepository
    private val extractionEngine: MedicalExtractionEngine

    private val _userRole = MutableStateFlow(UserRole.PATIENT)
    val userRole: StateFlow<UserRole> = _userRole.asStateFlow()

    private val _selectedReport = MutableStateFlow<Report?>(null)
    val selectedReport: StateFlow<Report?> = _selectedReport.asStateFlow()

    private val _selectedBiomarkerName = MutableStateFlow<String>("Fasting Blood Glucose")
    val selectedBiomarkerName: StateFlow<String> = _selectedBiomarkerName.asStateFlow()

    // Upload & Analysis State
    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing: StateFlow<Boolean> = _isAnalyzing.asStateFlow()

    private val _currentStage = MutableStateFlow<MedicalExtractionEngine.ProcessingStage?>(null)
    val currentStage: StateFlow<MedicalExtractionEngine.ProcessingStage?> = _currentStage.asStateFlow()

    private val _analysisError = MutableStateFlow<String?>(null)
    val analysisError: StateFlow<String?> = _analysisError.asStateFlow()

    // Selected file for upload view
    private val _stagedFileUri = MutableStateFlow<Uri?>(null)
    val stagedFileUri: StateFlow<Uri?> = _stagedFileUri.asStateFlow()

    private val _stagedFileName = MutableStateFlow<String>("")
    val stagedFileName: StateFlow<String> = _stagedFileName.asStateFlow()

    private val _stagedFileSize = MutableStateFlow<String>("")
    val stagedFileSize: StateFlow<String> = _stagedFileSize.asStateFlow()

    private val _stagedFileMime = MutableStateFlow<String>("")
    val stagedFileMime: StateFlow<String> = _stagedFileMime.asStateFlow()

    init {
        val db = VitalSyncDatabase.getDatabase(application)
        repository = VitalSyncRepository(db.vitalSyncDao())
        extractionEngine = MedicalExtractionEngine(application)

        // Preload sample data if empty so the user immediately has rich, interactive demonstration data
        viewModelScope.launch {
            repository.seedDemoDataIfEmpty()
        }
    }

    val reports: StateFlow<List<Report>> = repository.getAllReports()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val allBiomarkers: StateFlow<List<Biomarker>> = repository.getAllBiomarkers()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun setUserRole(role: UserRole) {
        _userRole.value = role
    }

    fun selectReport(report: Report) {
        viewModelScope.launch {
            repository.getReportWithBiomarkers(report.id).collect { (fullReport, biomarkers) ->
                if (fullReport != null) {
                    _selectedReport.value = fullReport.copy(biomarkers = biomarkers)
                } else {
                    _selectedReport.value = report
                }
            }
        }
    }

    fun selectReportById(reportId: Long) {
        viewModelScope.launch {
            repository.getReportWithBiomarkers(reportId).collect { (fullReport, biomarkers) ->
                if (fullReport != null) {
                    _selectedReport.value = fullReport.copy(biomarkers = biomarkers)
                }
            }
        }
    }

    fun setStagedFile(uri: Uri, name: String, size: String, mime: String) {
        _stagedFileUri.value = uri
        _stagedFileName.value = name
        _stagedFileSize.value = size
        _stagedFileMime.value = mime
        _analysisError.value = null
    }

    fun clearStagedFile() {
        _stagedFileUri.value = null
        _stagedFileName.value = ""
        _stagedFileSize.value = ""
        _stagedFileMime.value = ""
        _analysisError.value = null
    }

    fun analyzeStagedFile(onSuccess: (Long) -> Unit) {
        val uri = _stagedFileUri.value
        val name = _stagedFileName.value.ifEmpty { "Diagnostic_Report.pdf" }
        val mime = _stagedFileMime.value.ifEmpty { "application/pdf" }

        viewModelScope.launch {
            _isAnalyzing.value = true
            _analysisError.value = null

            try {
                val report = extractionEngine.processDocument(
                    fileUri = uri,
                    fileName = name,
                    mimeType = mime,
                    onProgress = { stage ->
                        _currentStage.value = stage
                    }
                )

                val newId = repository.saveReport(report)
                _selectedReport.value = report.copy(id = newId)
                _isAnalyzing.value = false
                _currentStage.value = null
                clearStagedFile()
                onSuccess(newId)
            } catch (e: Exception) {
                _isAnalyzing.value = false
                _currentStage.value = null
                _analysisError.value = "Unable to process document: ${e.message ?: "Please ensure the file is a readable medical report or try demo mode."}"
            }
        }
    }

    fun loadDemoReport(onSuccess: (Long) -> Unit) {
        viewModelScope.launch {
            _isAnalyzing.value = true
            _analysisError.value = null

            try {
                // Emulate the 5 processing steps for the demo report demonstration
                _currentStage.value = MedicalExtractionEngine.ProcessingStage.ReadingDocument
                kotlinx.coroutines.delay(400)
                _currentStage.value = MedicalExtractionEngine.ProcessingStage.ExtractingInfo
                kotlinx.coroutines.delay(400)
                _currentStage.value = MedicalExtractionEngine.ProcessingStage.IdentifyingBiomarkers
                kotlinx.coroutines.delay(400)
                _currentStage.value = MedicalExtractionEngine.ProcessingStage.ValidatingRanges
                kotlinx.coroutines.delay(400)
                _currentStage.value = MedicalExtractionEngine.ProcessingStage.GeneratingExplanations
                kotlinx.coroutines.delay(300)

                val demoReport = SampleMedicalData.getPrimaryDemoReport()
                val id = repository.saveReport(demoReport)
                _selectedReport.value = demoReport.copy(id = id)

                _isAnalyzing.value = false
                _currentStage.value = null
                onSuccess(id)
            } catch (e: Exception) {
                _isAnalyzing.value = false
                _currentStage.value = null
                _analysisError.value = "Failed to load demo report: ${e.message}"
            }
        }
    }

    fun deleteReport(reportId: Long) {
        viewModelScope.launch {
            repository.deleteReport(reportId)
            if (_selectedReport.value?.id == reportId) {
                _selectedReport.value = null
            }
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            repository.clearAllData()
            _selectedReport.value = null
        }
    }

    fun resetDemoData() {
        viewModelScope.launch {
            repository.clearAllData()
            repository.seedSampleReports()
        }
    }

    fun setSelectedBiomarkerName(name: String) {
        _selectedBiomarkerName.value = name
    }

    fun updateSoapNote(s: String, o: String, a: String, p: String) {
        val current = _selectedReport.value ?: return
        val updated = current.copy(
            soapSubjective = s,
            soapObjective = o,
            soapAssessment = a,
            soapPlan = p
        )
        _selectedReport.value = updated
        viewModelScope.launch {
            repository.saveReport(updated)
        }
    }
}
