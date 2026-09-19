package com.example.data.sample

import com.example.data.model.Biomarker
import com.example.data.model.BiomarkerStatus
import com.example.data.model.ClinicalReference
import com.example.data.model.Report

object SampleMedicalData {
    const val DEMO_LABEL = "Synthetic Demo Data – Not Real Patient Information"

    fun getClinicalReferences(): List<ClinicalReference> {
        return listOf(
            ClinicalReference(
                title = "2024 ADA Standards of Care in Diabetes",
                organization = "American Diabetes Association",
                guideline = "Fasting plasma glucose 100–125 mg/dL defines impaired fasting glucose (prediabetes). Repeat testing or HbA1c recommended.",
                citation = "Diabetes Care 2024;47(Suppl. 1):S20–S42",
                relevance = "Relevant to elevated Fasting Blood Glucose (118 mg/dL)."
            ),
            ClinicalReference(
                title = "2018 AHA/ACC Multisociety Guideline on Blood Cholesterol",
                organization = "American College of Cardiology / AHA",
                guideline = "Total cholesterol >200 mg/dL warrants lifestyle modification and ASCVD risk stratification.",
                citation = "Circulation 2019;139:e1082–e1143",
                relevance = "Relevant to Total Cholesterol (224 mg/dL)."
            ),
            ClinicalReference(
                title = "KDIGO 2023 Clinical Practice Guideline for Kidney Evaluation",
                organization = "Kidney Disease: Improving Global Outcomes (KDIGO)",
                guideline = "Serum creatinine elevation above 1.2 mg/dL in adults warrants calculation of eGFR and assessment of hydration status.",
                citation = "Kidney Int Suppl 2023;13(1):1–125",
                relevance = "Relevant to Serum Creatinine (1.3 mg/dL)."
            ),
            ClinicalReference(
                title = "WHO Reference Intervals for Complete Blood Count",
                organization = "World Health Organization",
                guideline = "Adult hemoglobin 12.0–17.5 g/dL, WBC 4.0–11.0 10^3/uL, Platelets 150–450 10^3/uL are normal baseline intervals.",
                citation = "WHO Guidelines on Diagnostic Clinical Chemistry, 2022",
                relevance = "Relevant to Hemoglobin, WBC, and Platelet evaluations."
            )
        )
    }

    fun getPrimaryDemoReport(): Report {
        val now = System.currentTimeMillis()
        val biomarkers = listOf(
            Biomarker(
                name = "Fasting Blood Glucose",
                value = 118.0,
                valueString = "118",
                unit = "mg/dL",
                referenceRange = "70 - 99 mg/dL",
                minRef = 70.0,
                maxRef = 99.0,
                status = BiomarkerStatus.HIGH,
                patientExplanation = "Glucose is the main type of sugar in your blood. Your level is slightly higher than the standard normal fasting range (70–99 mg/dL), which can indicate early changes in how your body handles sugar.",
                category = "Metabolic",
                clinicalSignificance = "Impaired fasting glucose (prediabetic range). Suggest verifying with HbA1c and checking fasting compliance.",
                date = "Sep 19, 2026",
                timestamp = now
            ),
            Biomarker(
                name = "Total Cholesterol",
                value = 224.0,
                valueString = "224",
                unit = "mg/dL",
                referenceRange = "125 - 200 mg/dL",
                minRef = 125.0,
                maxRef = 200.0,
                status = BiomarkerStatus.HIGH,
                patientExplanation = "Cholesterol is a waxy substance used to build cells. Your level is mildly elevated above the recommended 200 mg/dL ceiling. Often influenced by diet, activity, and genetics.",
                category = "Lipids",
                clinicalSignificance = "Mild hypercholesterolemia. Evaluate full lipid fraction (LDL-C, HDL-C, Triglycerides) and 10-year ASCVD risk score.",
                date = "Sep 19, 2026",
                timestamp = now
            ),
            Biomarker(
                name = "Serum Creatinine",
                value = 1.3,
                valueString = "1.3",
                unit = "mg/dL",
                referenceRange = "0.6 - 1.2 mg/dL",
                minRef = 0.6,
                maxRef = 1.2,
                status = BiomarkerStatus.HIGH,
                patientExplanation = "Creatinine is a natural waste product filtered by your kidneys. A value of 1.3 mg/dL is slightly above the typical upper limit (1.2 mg/dL). Dehydration or vigorous exercise can also cause transient elevations.",
                category = "Renal",
                clinicalSignificance = "Borderline elevated serum creatinine. Recommend calculating eGFR, repeating after proper hydration, and checking urinalysis for proteinuria.",
                date = "Sep 19, 2026",
                timestamp = now
            ),
            Biomarker(
                name = "Hemoglobin",
                value = 13.8,
                valueString = "13.8",
                unit = "g/dL",
                referenceRange = "12.0 - 16.0 g/dL",
                minRef = 12.0,
                maxRef = 16.0,
                status = BiomarkerStatus.NORMAL,
                patientExplanation = "Hemoglobin is the iron-rich protein in red blood cells that carries oxygen to all organs. Your result is well within the healthy normal range.",
                category = "Hematology",
                clinicalSignificance = "Normocytic, normochromic oxygen-carrying capacity well preserved. No evidence of anemia.",
                date = "Sep 19, 2026",
                timestamp = now
            ),
            Biomarker(
                name = "White Blood Cells (WBC)",
                value = 7.2,
                valueString = "7.2",
                unit = "10^3/uL",
                referenceRange = "4.0 - 11.0 10^3/uL",
                minRef = 4.0,
                maxRef = 11.0,
                status = BiomarkerStatus.NORMAL,
                patientExplanation = "White blood cells defend your body against infections. A count of 7.2 is healthy and suggests no active acute bacterial infection or immune suppression.",
                category = "Hematology",
                clinicalSignificance = "Total leukocyte count within normal physiological limits.",
                date = "Sep 19, 2026",
                timestamp = now
            ),
            Biomarker(
                name = "Platelets",
                value = 240.0,
                valueString = "240",
                unit = "10^3/uL",
                referenceRange = "150 - 450 10^3/uL",
                minRef = 150.0,
                maxRef = 450.0,
                status = BiomarkerStatus.NORMAL,
                patientExplanation = "Platelets are tiny cell fragments that help your blood clot normally if you get a cut or injury. Your level is right in the ideal healthy zone.",
                category = "Hematology",
                clinicalSignificance = "Normal thrombocyte count. Coagulation support intact.",
                date = "Sep 19, 2026",
                timestamp = now
            )
        )

        val questions = listOf(
            "My fasting blood sugar was 118 mg/dL. Should we check an HbA1c test or discuss nutrition changes?",
            "Total cholesterol is 224 mg/dL. Would you recommend a full lipid panel including LDL and HDL?",
            "My creatinine is 1.3 mg/dL, which is slightly above range. Could dehydration have affected this, and should we recheck?",
            "Are there any specific lifestyle modifications (exercise or dietary adjustments) you'd like me to start before our next checkup?"
        )

        return Report(
            title = "Comprehensive Metabolic & CBC Panel",
            reportType = "Routine Outpatient Laboratory Panel",
            uploadTimestamp = now,
            dateFormatted = "September 19, 2026",
            patientName = "Synthetic Demo Patient (Alex M.)",
            patientAge = "46 Years",
            patientGender = "Male",
            overallSummary = "This diagnostic report evaluates vital metabolic markers, kidney filtration, and complete blood count. Overall, hematology parameters (Hemoglobin, WBC, Platelets) are within healthy limits. Three markers show mild elevations: Fasting Blood Glucose (118 mg/dL), Total Cholesterol (224 mg/dL), and Serum Creatinine (1.3 mg/dL). These are informative indicators for preventative discussion with your healthcare provider.",
            patientExplanation = "Your red and white blood cells and clotting platelets look great and well within normal ranges. Your blood sugar, total cholesterol, and kidney filtration waste marker are mildly above target thresholds. These values do not mean an emergency, but they are great markers to review proactively with your physician to plan simple dietary, hydration, and exercise steps.",
            questionsForDoctor = questions,
            soapSubjective = "Patient (Alex M., 46yo) presents for routine annual outpatient laboratory evaluation. Denies acute fatigue, polyuria, polydipsia, dysuria, chest discomfort, or short breath. Reports adherence to overnight 10-hour fast prior to venipuncture.",
            soapObjective = "Vitals & Labs from analyzed panel (09/19/2026):\n• Fasting Blood Glucose: 118 mg/dL [H] (Ref: 70-99)\n• Total Cholesterol: 224 mg/dL [H] (Ref: 125-200)\n• Serum Creatinine: 1.3 mg/dL [H] (Ref: 0.6-1.2)\n• Hemoglobin: 13.8 g/dL (Ref: 12.0-16.0)\n• WBC: 7.2 x 10^3/uL (Ref: 4.0-11.0)\n• Platelets: 240 x 10^3/uL (Ref: 150-450)",
            soapAssessment = "1. Impaired Fasting Glucose (FPG 118 mg/dL): Consistent with prediabetic metabolic profile; warrants HbA1c confirmation.\n2. Borderline Hypercholesterolemia (Total Chol 224 mg/dL): Requires complete lipid subfractionation and ASCVD risk calculation.\n3. Borderline Renal Parameter Elevation (Creatinine 1.3 mg/dL): Mild elevation; consider pre-renal hydration effect versus early intrinsic nephron decline.",
            soapPlan = "1. Diagnostic Follow-up: Order Glycated Hemoglobin (HbA1c), Comprehensive Lipid Panel (LDL-C, HDL-C, Triglycerides), Spot Urine Albumin-to-Creatinine Ratio (uACR).\n2. Hydration & Recheck: Advise adequate oral hydration (64 oz water daily); repeat basic metabolic panel in 8–12 weeks.\n3. Lifestyle Intervention: Medical nutrition therapy counseling targeting lower refined carbohydrate intake and Mediterranean-style cardioprotective diet.\n4. Follow-up: Clinic appointment in 3 months with repeat lab results.",
            abnormalCount = 3,
            criticalCount = 0,
            hasCritical = false,
            isDemo = true,
            confidenceScore = "98% (High Confidence – Grounded in ADA & KDIGO Standards)",
            biomarkers = biomarkers,
            clinicalReferences = getClinicalReferences()
        )
    }

    fun getHistoricalDemoReports(): List<Report> {
        val now = System.currentTimeMillis()
        val dayMillis = 86_400_000L

        // Report from ~6 months ago
        val report6MonthsAgoTime = now - (180 * dayMillis)
        val biomarkers6Months = listOf(
            Biomarker(
                name = "Fasting Blood Glucose",
                value = 106.0,
                valueString = "106",
                unit = "mg/dL",
                referenceRange = "70 - 99 mg/dL",
                minRef = 70.0,
                maxRef = 99.0,
                status = BiomarkerStatus.HIGH,
                patientExplanation = "Mildly elevated fasting blood sugar.",
                category = "Metabolic",
                date = "Mar 23, 2026",
                timestamp = report6MonthsAgoTime
            ),
            Biomarker(
                name = "Total Cholesterol",
                value = 210.0,
                valueString = "210",
                unit = "mg/dL",
                referenceRange = "125 - 200 mg/dL",
                minRef = 125.0,
                maxRef = 200.0,
                status = BiomarkerStatus.HIGH,
                patientExplanation = "Mildly elevated total cholesterol.",
                category = "Lipids",
                date = "Mar 23, 2026",
                timestamp = report6MonthsAgoTime
            ),
            Biomarker(
                name = "Serum Creatinine",
                value = 1.1,
                valueString = "1.1",
                unit = "mg/dL",
                referenceRange = "0.6 - 1.2 mg/dL",
                minRef = 0.6,
                maxRef = 1.2,
                status = BiomarkerStatus.NORMAL,
                patientExplanation = "Normal kidney filtration marker.",
                category = "Renal",
                date = "Mar 23, 2026",
                timestamp = report6MonthsAgoTime
            ),
            Biomarker(
                name = "Hemoglobin",
                value = 14.1,
                valueString = "14.1",
                unit = "g/dL",
                referenceRange = "12.0 - 16.0 g/dL",
                minRef = 12.0,
                maxRef = 16.0,
                status = BiomarkerStatus.NORMAL,
                patientExplanation = "Healthy oxygen-carrying capacity.",
                category = "Hematology",
                date = "Mar 23, 2026",
                timestamp = report6MonthsAgoTime
            ),
            Biomarker(
                name = "White Blood Cells (WBC)",
                value = 6.8,
                valueString = "6.8",
                unit = "10^3/uL",
                referenceRange = "4.0 - 11.0 10^3/uL",
                minRef = 4.0,
                maxRef = 11.0,
                status = BiomarkerStatus.NORMAL,
                patientExplanation = "Normal immune white blood cell count.",
                category = "Hematology",
                date = "Mar 23, 2026",
                timestamp = report6MonthsAgoTime
            ),
            Biomarker(
                name = "Platelets",
                value = 248.0,
                valueString = "248",
                unit = "10^3/uL",
                referenceRange = "150 - 450 10^3/uL",
                minRef = 150.0,
                maxRef = 450.0,
                status = BiomarkerStatus.NORMAL,
                patientExplanation = "Normal clotting platelet count.",
                category = "Hematology",
                date = "Mar 23, 2026",
                timestamp = report6MonthsAgoTime
            )
        )

        val report6MonthsAgo = Report(
            title = "Routine Semi-Annual Health Checkup",
            reportType = "Laboratory Diagnostic Panel",
            uploadTimestamp = report6MonthsAgoTime,
            dateFormatted = "March 23, 2026",
            patientName = "Synthetic Demo Patient (Alex M.)",
            patientAge = "45 Years",
            patientGender = "Male",
            overallSummary = "Semi-annual laboratory checkup showing stable hematology and initial mild elevations in metabolic markers.",
            patientExplanation = "Most biomarkers were normal, with slight elevation in blood sugar and cholesterol noted for observation.",
            questionsForDoctor = listOf("How do my cholesterol numbers compare to my previous test?"),
            soapSubjective = "Outpatient follow-up. Patient asymptomatic.",
            soapObjective = "Fasting Glucose: 106 mg/dL [H], Total Chol: 210 mg/dL [H], Creatinine: 1.1 mg/dL [Normal].",
            soapAssessment = "Impaired fasting glucose and mild dyslipidemia.",
            soapPlan = "Emphasize dietary counseling; recheck in 6 months.",
            abnormalCount = 2,
            criticalCount = 0,
            hasCritical = false,
            isDemo = true,
            confidenceScore = "97%",
            biomarkers = biomarkers6Months,
            clinicalReferences = getClinicalReferences()
        )

        // Report from ~12 months ago
        val report12MonthsAgoTime = now - (365 * dayMillis)
        val biomarkers12Months = listOf(
            Biomarker(
                name = "Fasting Blood Glucose",
                value = 95.0,
                valueString = "95",
                unit = "mg/dL",
                referenceRange = "70 - 99 mg/dL",
                minRef = 70.0,
                maxRef = 99.0,
                status = BiomarkerStatus.NORMAL,
                patientExplanation = "Healthy normal fasting glucose level.",
                category = "Metabolic",
                date = "Sep 19, 2025",
                timestamp = report12MonthsAgoTime
            ),
            Biomarker(
                name = "Total Cholesterol",
                value = 192.0,
                valueString = "192",
                unit = "mg/dL",
                referenceRange = "125 - 200 mg/dL",
                minRef = 125.0,
                maxRef = 200.0,
                status = BiomarkerStatus.NORMAL,
                patientExplanation = "Optimal cholesterol level below 200 mg/dL.",
                category = "Lipids",
                date = "Sep 19, 2025",
                timestamp = report12MonthsAgoTime
            ),
            Biomarker(
                name = "Serum Creatinine",
                value = 0.95,
                valueString = "0.95",
                unit = "mg/dL",
                referenceRange = "0.6 - 1.2 mg/dL",
                minRef = 0.6,
                maxRef = 1.2,
                status = BiomarkerStatus.NORMAL,
                patientExplanation = "Excellent normal kidney filtration level.",
                category = "Renal",
                date = "Sep 19, 2025",
                timestamp = report12MonthsAgoTime
            ),
            Biomarker(
                name = "Hemoglobin",
                value = 14.3,
                valueString = "14.3",
                unit = "g/dL",
                referenceRange = "12.0 - 16.0 g/dL",
                minRef = 12.0,
                maxRef = 16.0,
                status = BiomarkerStatus.NORMAL,
                patientExplanation = "Normal hemoglobin level.",
                category = "Hematology",
                date = "Sep 19, 2025",
                timestamp = report12MonthsAgoTime
            ),
            Biomarker(
                name = "White Blood Cells (WBC)",
                value = 6.4,
                valueString = "6.4",
                unit = "10^3/uL",
                referenceRange = "4.0 - 11.0 10^3/uL",
                minRef = 4.0,
                maxRef = 11.0,
                status = BiomarkerStatus.NORMAL,
                patientExplanation = "Normal white blood cell count.",
                category = "Hematology",
                date = "Sep 19, 2025",
                timestamp = report12MonthsAgoTime
            ),
            Biomarker(
                name = "Platelets",
                value = 252.0,
                valueString = "252",
                unit = "10^3/uL",
                referenceRange = "150 - 450 10^3/uL",
                minRef = 150.0,
                maxRef = 450.0,
                status = BiomarkerStatus.NORMAL,
                patientExplanation = "Normal platelet count.",
                category = "Hematology",
                date = "Sep 19, 2025",
                timestamp = report12MonthsAgoTime
            )
        )

        val report12MonthsAgo = Report(
            title = "Annual Comprehensive Diagnostic Panel",
            reportType = "Laboratory Diagnostic Panel",
            uploadTimestamp = report12MonthsAgoTime,
            dateFormatted = "September 19, 2025",
            patientName = "Synthetic Demo Patient (Alex M.)",
            patientAge = "45 Years",
            patientGender = "Male",
            overallSummary = "Baseline annual diagnostic screening. All primary metabolic, renal, and hematologic biomarkers within optimal reference ranges.",
            patientExplanation = "All tested parameters were completely normal.",
            questionsForDoctor = listOf("Everything looks normal. When should I schedule my next regular checkup?"),
            soapSubjective = "Annual preventative screening examination.",
            soapObjective = "All metabolic and CBC markers within reference boundaries.",
            soapAssessment = "Normal preventative laboratory evaluation.",
            soapPlan = "Maintain routine wellness and active lifestyle; return in 1 year.",
            abnormalCount = 0,
            criticalCount = 0,
            hasCritical = false,
            isDemo = true,
            confidenceScore = "99%",
            biomarkers = biomarkers12Months,
            clinicalReferences = getClinicalReferences()
        )

        return listOf(report6MonthsAgo, report12MonthsAgo)
    }
}
