package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface VitalSyncDao {
    @Query("SELECT * FROM reports ORDER BY uploadTimestamp DESC")
    fun getAllReports(): Flow<List<ReportEntity>>

    @Query("SELECT * FROM reports WHERE id = :id LIMIT 1")
    fun getReportById(id: Long): Flow<ReportEntity?>

    @Query("SELECT * FROM biomarkers WHERE reportId = :reportId ORDER BY id ASC")
    fun getBiomarkersForReport(reportId: Long): Flow<List<BiomarkerEntity>>

    @Query("SELECT * FROM biomarkers ORDER BY timestamp ASC")
    fun getAllBiomarkers(): Flow<List<BiomarkerEntity>>

    @Query("SELECT * FROM biomarkers WHERE LOWER(name) LIKE LOWER(:name) ORDER BY timestamp ASC")
    fun getBiomarkersByName(name: String): Flow<List<BiomarkerEntity>>

    @Query("SELECT DISTINCT name FROM biomarkers ORDER BY name ASC")
    fun getDistinctBiomarkerNames(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: ReportEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBiomarkers(biomarkers: List<BiomarkerEntity>)

    @Query("DELETE FROM reports WHERE id = :id")
    suspend fun deleteReportById(id: Long)

    @Query("DELETE FROM biomarkers WHERE reportId = :reportId")
    suspend fun deleteBiomarkersForReport(reportId: Long)

    @Query("DELETE FROM reports")
    suspend fun deleteAllReports()

    @Query("DELETE FROM biomarkers")
    suspend fun deleteAllBiomarkers()
}
