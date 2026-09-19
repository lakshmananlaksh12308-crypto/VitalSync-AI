package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Biomarker
import com.example.data.model.BiomarkerStatus
import com.example.ui.components.DisclaimerBanner
import com.example.ui.components.InteractiveTrendChart
import com.example.ui.theme.MedicalPrimary
import com.example.ui.theme.StatusCritical
import com.example.ui.theme.StatusHigh
import com.example.ui.theme.StatusLow
import com.example.ui.theme.StatusNormal

@Composable
fun TrendAnalysisScreen(
    allBiomarkers: List<Biomarker>,
    selectedBiomarkerName: String,
    onSelectBiomarkerName: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val distinctNames = allBiomarkers.map { it.name }.distinct()
    val currentBiomarker = if (distinctNames.contains(selectedBiomarkerName)) {
        selectedBiomarkerName
    } else {
        distinctNames.firstOrNull() ?: "Fasting Blood Glucose"
    }

    val pointsForBiomarker = allBiomarkers
        .filter { it.name.equals(currentBiomarker, ignoreCase = true) }
        .sortedBy { it.timestamp }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            // Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MedicalPrimary.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Timeline,
                        contentDescription = null,
                        tint = MedicalPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Longitudinal Biomarker Trends",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Track health parameters across multiple diagnostic reports",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Biomarker Selector Chips
        item {
            Column {
                Text(
                    text = "Select Biomarker to Inspect:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(distinctNames) { name ->
                        val isSelected = name.equals(currentBiomarker, ignoreCase = true)
                        FilterChip(
                            selected = isSelected,
                            onClick = { onSelectBiomarkerName(name) },
                            label = {
                                Text(
                                    text = name,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MedicalPrimary,
                                selectedLabelColor = androidx.compose.ui.graphics.Color.White
                            ),
                            modifier = Modifier.testTag("trend_chip_${name.lowercase().replace(" ", "_")}")
                        )
                    }
                }
            }
        }

        // The Interactive Trend Chart
        item {
            InteractiveTrendChart(
                biomarkerName = currentBiomarker,
                dataPoints = pointsForBiomarker
            )
        }

        // Historical Data Records Table
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Historical Measurements (${pointsForBiomarker.size})",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Real records extracted from uploaded and demo diagnostic files without fabricated interpolation.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    pointsForBiomarker.forEach { pt ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = pt.date.ifEmpty { "Diagnostic Record" },
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "Ref: ${pt.referenceRange}",
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "${pt.valueString} ${pt.unit}",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = when (pt.status) {
                                            BiomarkerStatus.NORMAL -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                                            BiomarkerStatus.HIGH -> StatusHigh.copy(alpha = 0.15f)
                                            BiomarkerStatus.LOW -> StatusLow.copy(alpha = 0.15f)
                                            BiomarkerStatus.CRITICAL -> StatusCritical.copy(alpha = 0.15f)
                                        }
                                    ) {
                                        Text(
                                            text = pt.status.displayName,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = when (pt.status) {
                                                BiomarkerStatus.NORMAL -> StatusNormal
                                                BiomarkerStatus.HIGH -> StatusHigh
                                                BiomarkerStatus.LOW -> StatusLow
                                                BiomarkerStatus.CRITICAL -> StatusCritical
                                            },
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Educational Disclaimer
        item {
            DisclaimerBanner()
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
