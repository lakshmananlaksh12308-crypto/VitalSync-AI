package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Report
import com.example.ui.components.DisclaimerBanner
import com.example.ui.theme.MedicalPrimary
import com.example.ui.theme.MedicalSecondary
import com.example.ui.theme.StatusHigh
import com.example.ui.theme.StatusHighBg

@Composable
fun SoapNoteScreen(
    report: Report?,
    onSaveSoapNote: (String, String, String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    if (report == null) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No report selected for SOAP note generation.",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }

    var subjective by remember(report.id) { mutableStateOf(report.soapSubjective) }
    var objective by remember(report.id) { mutableStateOf(report.soapObjective) }
    var assessment by remember(report.id) { mutableStateOf(report.soapAssessment) }
    var plan by remember(report.id) { mutableStateOf(report.soapPlan) }

    fun copyToClipboard() {
        val fullNote = """
            ========================================
            DRAFT CLINICAL SOAP NOTE (AI-GENERATED)
            Report: ${report.title}
            Date: ${report.dateFormatted}
            Patient: ${report.patientName} (${report.patientAge}, ${report.patientGender})
            NOTE: This is an AI-generated draft decision-support note that requires clinician review and verification.
            ========================================
            
            [S] SUBJECTIVE:
            $subjective
            
            [O] OBJECTIVE:
            $objective
            
            [A] ASSESSMENT:
            $assessment
            
            [P] PLAN:
            $plan
            ========================================
        """.trimIndent()

        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("VitalSync SOAP Note", fullNote)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "DRAFT SOAP Note copied to clipboard", Toast.LENGTH_SHORT).show()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Clinical SOAP Note",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Patient: ${report.patientName} • ${report.dateFormatted}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            OutlinedButton(
                onClick = { copyToClipboard() },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("copy_soap_note_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Copy Note", fontSize = 12.sp)
            }
        }

        // Mandatory AI-Draft Warning Banner
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = StatusHighBg),
            border = BorderStroke(1.dp, StatusHigh),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = StatusHigh,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "AI-GENERATED DRAFT — REQUIRES CLINICIAN REVIEW",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = StatusHigh
                    )
                    Text(
                        text = "This document is an AI-assisted initial draft generated from extracted report data. The attending clinician must review, edit, and sign before clinical documentation.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // Section: Subjective
        SoapSectionField(
            title = "Subjective (S)",
            description = "Patient history, symptoms, fasting status, or reason for testing as noted.",
            value = subjective,
            onValueChange = { subjective = it },
            tag = "soap_subjective_field"
        )

        // Section: Objective
        SoapSectionField(
            title = "Objective (O)",
            description = "Extracted diagnostic measurements, biomarker values, reference ranges, and flags.",
            value = objective,
            onValueChange = { objective = it },
            tag = "soap_objective_field"
        )

        // Section: Assessment
        SoapSectionField(
            title = "Assessment (A)",
            description = "Clinical interpretation of findings and potential abnormalities (informational decision-support).",
            value = assessment,
            onValueChange = { assessment = it },
            tag = "soap_assessment_field"
        )

        // Section: Plan
        SoapSectionField(
            title = "Plan (P)",
            description = "Proposed confirmatory testing, lifestyle interventions, and follow-up intervals.",
            value = plan,
            onValueChange = { plan = it },
            tag = "soap_plan_field"
        )

        // Save & Copy Actions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = {
                    onSaveSoapNote(subjective, objective, assessment, plan)
                    Toast.makeText(context, "SOAP Note saved successfully", Toast.LENGTH_SHORT).show()
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MedicalSecondary),
                modifier = Modifier
                    .weight(1f)
                    .height(46.dp)
                    .testTag("save_soap_note_btn")
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Save SOAP Draft", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }

            OutlinedButton(
                onClick = { copyToClipboard() },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(46.dp)
            ) {
                Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Copy Note", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }
        }

        DisclaimerBanner(isDemo = report.isDemo)
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun SoapSectionField(
    title: String,
    description: String,
    value: String,
    onValueChange: (String) -> Unit,
    tag: String
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MedicalPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(tag),
                shape = RoundedCornerShape(8.dp),
                minLines = 3
            )
        }
    }
}
