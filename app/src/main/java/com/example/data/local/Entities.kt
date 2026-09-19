package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.Biomarker
import com.example.data.model.BiomarkerStatus
import com.example.data.model.ClinicalReference
import com.example.data.model.Report

@Entity(tableName = "reports")
data class ReportEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val reportType: String,
    val uploadTimestamp: Long,
    val dateFormatted: String,
    val patientName: String,
    val patientAge: String,
    val patientGender: String,
    val overallSummary: String,
    val patientExplanation: String,
    val questionsForDoctorJoined: String, // newline-separated questions
    val soapSubjective: String,
    val soapObjective: String,
    val soapAssessment: String,
    val soapPlan: String,
    val abnormalCount: Int,
    val criticalCount: Int,
    val hasCritical: Boolean,
    val isDemo: Boolean,
    val confidenceScore: String
)

@Entity(tableName = "biomarkers")
data class BiomarkerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val reportId: Long,
    val name: String,
    val value: Double?,
    val valueString: String,
    val unit: String,
    val referenceRange: String,
    val minRef: Double?,
    val maxRef: Double?,
    val status: String, // NORMAL, HIGH, LOW, CRITICAL
    val patientExplanation: String,
    val category: String,
    val clinicalSignificance: String,
    val date: String,
    val timestamp: Long
)
