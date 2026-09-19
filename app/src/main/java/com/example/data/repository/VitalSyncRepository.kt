package com.example.data.repository

import com.example.data.local.BiomarkerEntity
import com.example.data.local.ReportEntity
import com.example.data.local.VitalSyncDao
import com.example.data.model.Biomarker
import com.example.data.model.BiomarkerStatus
import com.example.data.model.Report
import com.example.data.sample.SampleMedicalData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class VitalSyncRepository(private val dao: VitalSyncDao) {

    fun getAllReports(): Flow<List<Report>> {
        return dao.getAllReports().map { entities ->
            entities.map { entity ->
                entityToReport(entity, emptyList())
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getReportWithBiomarkers(reportId: Long): Flow<Pair<Report?, List<Biomarker>>> {
        return dao.getReportById(reportId).map { entity ->
            if (entity == null) {
                null to emptyList()
            } else {
                val biomarkerEntities = dao.getBiomarkersForReport(reportId).first()
                val biomarkers = biomarkerEntities.map { it.toBiomarker() }
                entityToReport(entity, biomarkers) to biomarkers
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getAllBiomarkers(): Flow<List<Biomarker>> {
        return dao.getAllBiomarkers().map { list ->
            list.map { it.toBiomarker() }
        }.flowOn(Dispatchers.IO)
    }

    fun getBiomarkersByName(name: String): Flow<List<Biomarker>> {
        return dao.getBiomarkersByName(name).map { list ->
            list.map { it.toBiomarker() }
        }.flowOn(Dispatchers.IO)
    }

    fun getDistinctBiomarkerNames(): Flow<List<String>> {
        return dao.getDistinctBiomarkerNames().flowOn(Dispatchers.IO)
    }

    suspend fun saveReport(report: Report): Long = withContext(Dispatchers.IO) {
        val reportEntity = ReportEntity(
            id = if (report.id > 0) report.id else 0,
            title = report.title,
            reportType = report.reportType,
            uploadTimestamp = report.uploadTimestamp,
            dateFormatted = report.dateFormatted,
            patientName = report.patientName,
            patientAge = report.patientAge,
            patientGender = report.patientGender,
            overallSummary = report.overallSummary,
            patientExplanation = report.patientExplanation,
            questionsForDoctorJoined = report.questionsForDoctor.joinToString("\n"),
            soapSubjective = report.soapSubjective,
            soapObjective = report.soapObjective,
            soapAssessment = report.soapAssessment,
            soapPlan = report.soapPlan,
            abnormalCount = report.abnormalCount,
            criticalCount = report.criticalCount,
            hasCritical = report.hasCritical,
            isDemo = report.isDemo,
            confidenceScore = report.confidenceScore
        )

        val reportId = dao.insertReport(reportEntity)
        val biomarkerEntities = report.biomarkers.map { bm ->
            BiomarkerEntity(
                id = 0,
                reportId = reportId,
                name = bm.name,
                value = bm.value,
                valueString = bm.valueString,
                unit = bm.unit,
                referenceRange = bm.referenceRange,
                minRef = bm.minRef,
                maxRef = bm.maxRef,
                status = bm.status.name,
                patientExplanation = bm.patientExplanation,
                category = bm.category,
                clinicalSignificance = bm.clinicalSignificance,
                date = bm.date.ifEmpty { report.dateFormatted },
                timestamp = if (bm.timestamp > 0) bm.timestamp else report.uploadTimestamp
            )
        }
        dao.insertBiomarkers(biomarkerEntities)
        reportId
    }

    suspend fun deleteReport(reportId: Long) = withContext(Dispatchers.IO) {
        dao.deleteBiomarkersForReport(reportId)
        dao.deleteReportById(reportId)
    }

    suspend fun clearAllData() = withContext(Dispatchers.IO) {
        dao.deleteAllBiomarkers()
        dao.deleteAllReports()
    }

    suspend fun seedDemoDataIfEmpty() = withContext(Dispatchers.IO) {
        val currentReports = dao.getAllReports().first()
        if (currentReports.isEmpty()) {
            seedSampleReports()
        }
    }

    suspend fun seedSampleReports() = withContext(Dispatchers.IO) {
        // Seed historical reports first so timestamps order naturally
        for (historical in SampleMedicalData.getHistoricalDemoReports()) {
            saveReport(historical)
        }
        // Seed primary demo report
        saveReport(SampleMedicalData.getPrimaryDemoReport())
    }

    private fun entityToReport(entity: ReportEntity, biomarkers: List<Biomarker>): Report {
        val questions = if (entity.questionsForDoctorJoined.isNotBlank()) {
            entity.questionsForDoctorJoined.split("\n").filter { it.isNotBlank() }
        } else {
            emptyList()
        }

        return Report(
            id = entity.id,
            title = entity.title,
            reportType = entity.reportType,
            uploadTimestamp = entity.uploadTimestamp,
            dateFormatted = entity.dateFormatted,
            patientName = entity.patientName,
            patientAge = entity.patientAge,
            patientGender = entity.patientGender,
            overallSummary = entity.overallSummary,
            patientExplanation = entity.patientExplanation,
            questionsForDoctor = questions,
            soapSubjective = entity.soapSubjective,
            soapObjective = entity.soapObjective,
            soapAssessment = entity.soapAssessment,
            soapPlan = entity.soapPlan,
            abnormalCount = entity.abnormalCount,
            criticalCount = entity.criticalCount,
            hasCritical = entity.hasCritical,
            isDemo = entity.isDemo,
            confidenceScore = entity.confidenceScore,
            biomarkers = biomarkers,
            clinicalReferences = SampleMedicalData.getClinicalReferences()
        )
    }

    private fun BiomarkerEntity.toBiomarker(): Biomarker {
        val bmStatus = try {
            BiomarkerStatus.valueOf(this.status)
        } catch (e: Exception) {
            BiomarkerStatus.NORMAL
        }
        return Biomarker(
            id = this.id,
            reportId = this.reportId,
            name = this.name,
            value = this.value,
            valueString = this.valueString,
            unit = this.unit,
            referenceRange = this.referenceRange,
            minRef = this.minRef,
            maxRef = this.maxRef,
            status = bmStatus,
            patientExplanation = this.patientExplanation,
            category = this.category,
            clinicalSignificance = this.clinicalSignificance,
            date = this.date,
            timestamp = this.timestamp
        )
    }
}
