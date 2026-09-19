package com.example.data.model

enum class UserRole(val label: String) {
    PATIENT("Patient Mode"),
    CLINICIAN("Clinician Mode")
}

enum class BiomarkerStatus(val displayName: String) {
    NORMAL("Normal"),
    HIGH("High"),
    LOW("Low"),
    CRITICAL("Critical")
}

data class Biomarker(
    val id: Long = 0,
    val reportId: Long = 0,
    val name: String,
    val value: Double?,
    val valueString: String,
    val unit: String,
    val referenceRange: String,
    val minRef: Double? = null,
    val maxRef: Double? = null,
    val status: BiomarkerStatus = BiomarkerStatus.NORMAL,
    val patientExplanation: String,
    val category: String = "General",
    val clinicalSignificance: String = "",
    val date: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

data class ClinicalReference(
    val title: String,
    val organization: String,
    val guideline: String,
    val citation: String,
    val relevance: String
)

data class Report(
    val id: Long = 0,
    val title: String,
    val reportType: String,
    val uploadTimestamp: Long = System.currentTimeMillis(),
    val dateFormatted: String,
    val patientName: String = "Patient Record",
    val patientAge: String = "Adult",
    val patientGender: String = "Not Specified",
    val overallSummary: String,
    val patientExplanation: String,
    val questionsForDoctor: List<String> = emptyList(),
    val soapSubjective: String = "",
    val soapObjective: String = "",
    val soapAssessment: String = "",
    val soapPlan: String = "",
    val abnormalCount: Int = 0,
    val criticalCount: Int = 0,
    val hasCritical: Boolean = false,
    val isDemo: Boolean = false,
    val confidenceScore: String = "High",
    val biomarkers: List<Biomarker> = emptyList(),
    val clinicalReferences: List<ClinicalReference> = emptyList()
)
