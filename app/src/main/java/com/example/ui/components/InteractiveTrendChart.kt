package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Biomarker
import com.example.data.model.BiomarkerStatus
import com.example.ui.theme.MedicalPrimary
import com.example.ui.theme.StatusCritical
import com.example.ui.theme.StatusHigh
import com.example.ui.theme.StatusLow
import com.example.ui.theme.StatusNormal
import com.example.ui.theme.StatusNormalBg

@Composable
fun InteractiveTrendChart(
    biomarkerName: String,
    dataPoints: List<Biomarker>,
    modifier: Modifier = Modifier
) {
    if (dataPoints.isEmpty()) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            modifier = modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "No historical measurements recorded for $biomarkerName",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        return
    }

    // Sort by timestamp
    val sortedPoints = dataPoints.sortedBy { it.timestamp }
    val validNumericPoints = sortedPoints.filter { it.value != null }

    val minRef = validNumericPoints.firstOrNull { it.minRef != null }?.minRef ?: 0.0
    val maxRef = validNumericPoints.firstOrNull { it.maxRef != null }?.maxRef ?: 100.0
    val unit = validNumericPoints.firstOrNull()?.unit ?: ""

    // Calculate Y-scale min and max with padding
    val allValues = validNumericPoints.mapNotNull { it.value } + listOf(minRef, maxRef)
    val dataMin = allValues.minOrNull() ?: 0.0
    val dataMax = allValues.maxOrNull() ?: 100.0
    val rangeSpan = (dataMax - dataMin).coerceAtLeast(10.0)
    val yMin = (dataMin - (rangeSpan * 0.15)).coerceAtLeast(0.0)
    val yMax = dataMax + (rangeSpan * 0.20)

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 1.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Chart Header & Legend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = biomarkerName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Reference Interval: $minRef – $maxRef $unit",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Legend Pills
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(StatusNormalBg, RoundedCornerShape(2.dp))
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Target Zone", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(MedicalPrimary, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Reported", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Canvas Chart
            val pointCount = validNumericPoints.size
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                val canvasWidth = size.width
                val canvasHeight = size.height
                val paddingLeft = 45.dp.toPx()
                val paddingRight = 35.dp.toPx()
                val paddingTop = 25.dp.toPx()
                val paddingBottom = 35.dp.toPx()

                val plotWidth = canvasWidth - paddingLeft - paddingRight
                val plotHeight = canvasHeight - paddingTop - paddingBottom

                fun toY(value: Double): Float {
                    val normalized = ((value - yMin) / (yMax - yMin)).coerceIn(0.0, 1.0)
                    return (paddingTop + plotHeight * (1f - normalized.toFloat()))
                }

                fun toX(index: Int): Float {
                    return if (pointCount <= 1) {
                        paddingLeft + plotWidth / 2f
                    } else {
                        paddingLeft + (plotWidth / (pointCount - 1)) * index
                    }
                }

                // 1. Draw Normal Reference Range Shaded Area
                val refYTop = toY(maxRef)
                val refYBottom = toY(minRef)
                val refHeight = (refYBottom - refYTop).coerceAtLeast(4f)

                drawRect(
                    color = Color(0xFFD1FAE5).copy(alpha = 0.65f),
                    topLeft = Offset(paddingLeft, refYTop),
                    size = Size(plotWidth, refHeight)
                )

                // Reference Range Boundary Lines
                val dashEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                drawLine(
                    color = StatusNormal.copy(alpha = 0.8f),
                    start = Offset(paddingLeft, refYTop),
                    end = Offset(paddingLeft + plotWidth, refYTop),
                    strokeWidth = 1.5.dp.toPx(),
                    pathEffect = dashEffect
                )
                drawLine(
                    color = StatusNormal.copy(alpha = 0.8f),
                    start = Offset(paddingLeft, refYBottom),
                    end = Offset(paddingLeft + plotWidth, refYBottom),
                    strokeWidth = 1.5.dp.toPx(),
                    pathEffect = dashEffect
                )

                // 2. Draw Trend Line Connecting Points
                if (pointCount > 1) {
                    val linePath = Path()
                    validNumericPoints.forEachIndexed { index, pt ->
                        val x = toX(index)
                        val y = toY(pt.value ?: 0.0)
                        if (index == 0) linePath.moveTo(x, y) else linePath.lineTo(x, y)
                    }
                    drawPath(
                        path = linePath,
                        color = MedicalPrimary,
                        style = Stroke(width = 3.dp.toPx())
                    )
                }

                // 3. Draw Data Point Nodes, Values & Dates
                val textPaint = android.graphics.Paint().apply {
                    color = android.graphics.Color.DKGRAY
                    textSize = 28f
                    isAntiAlias = true
                    textAlign = android.graphics.Paint.Align.CENTER
                    typeface = android.graphics.Typeface.DEFAULT_BOLD
                }

                val datePaint = android.graphics.Paint().apply {
                    color = android.graphics.Color.GRAY
                    textSize = 24f
                    isAntiAlias = true
                    textAlign = android.graphics.Paint.Align.CENTER
                }

                validNumericPoints.forEachIndexed { index, pt ->
                    val x = toX(index)
                    val value = pt.value ?: 0.0
                    val y = toY(value)

                    val nodeColor = when (pt.status) {
                        BiomarkerStatus.NORMAL -> StatusNormal
                        BiomarkerStatus.HIGH -> StatusHigh
                        BiomarkerStatus.LOW -> StatusLow
                        BiomarkerStatus.CRITICAL -> StatusCritical
                    }

                    // Outer halo
                    drawCircle(
                        color = nodeColor.copy(alpha = 0.25f),
                        radius = 11.dp.toPx(),
                        center = Offset(x, y)
                    )
                    // Core point
                    drawCircle(
                        color = Color.White,
                        radius = 6.dp.toPx(),
                        center = Offset(x, y)
                    )
                    drawCircle(
                        color = nodeColor,
                        radius = 4.5.dp.toPx(),
                        center = Offset(x, y)
                    )

                    // Value text above point
                    drawContext.canvas.nativeCanvas.drawText(
                        pt.valueString,
                        x,
                        y - 14.dp.toPx(),
                        textPaint
                    )

                    // Date label below X axis
                    val dateLabel = if (pt.date.isNotBlank()) pt.date else "Visit ${index + 1}"
                    drawContext.canvas.nativeCanvas.drawText(
                        dateLabel,
                        x,
                        canvasHeight - 6.dp.toPx(),
                        datePaint
                    )
                }
            }
        }
    }
}
