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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ResearchData
import com.example.data.SurveyItem
import com.example.viewmodel.AppUiState

@Composable
fun KuesionerScreen(
    uiState: AppUiState,
    selectedSubTab: Int,
    onSubTabSelected: (Int) -> Unit,
    onAnswerItem: (String, Int, Boolean) -> Unit,
    onQuickFill: (Boolean) -> Unit,
    onSubmitPretest: () -> Unit,
    onSubmitPosttest: () -> Unit,
    onUpdateMicroCheckIn: (Int, Float, Float, Float, Float, Float, Boolean) -> Unit,
    onSaveTactPlan: (String, String, String) -> Unit,
    onNavigateToRoute: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Sub-Tabs Header
        TabRow(
            selectedTabIndex = selectedSubTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth()
        ) {
            Tab(
                selected = selectedSubTab == 0,
                onClick = { onSubTabSelected(0) },
                text = { Text("1. Pre/Posttest", fontWeight = FontWeight.Bold, fontSize = 12.sp) },
                modifier = Modifier.testTag("subtab_survey_main")
            )
            Tab(
                selected = selectedSubTab == 1,
                onClick = { onSubTabSelected(1) },
                text = { Text("2. Micro-Check-In", fontWeight = FontWeight.Bold, fontSize = 12.sp) },
                modifier = Modifier.testTag("subtab_survey_micro")
            )
            Tab(
                selected = selectedSubTab == 2,
                onClick = { onSubTabSelected(2) },
                text = { Text("3. Rencana TACT", fontWeight = FontWeight.Bold, fontSize = 12.sp) },
                modifier = Modifier.testTag("subtab_survey_tact")
            )
        }

        when (selectedSubTab) {
            0 -> MainSurveySubTab(
                uiState = uiState,
                onAnswer = onAnswerItem,
                onQuickFill = onQuickFill,
                onSubmitPretest = onSubmitPretest,
                onSubmitPosttest = onSubmitPosttest
            )
            1 -> MicroCheckInSubTab(
                uiState = uiState,
                onUpdate = onUpdateMicroCheckIn,
                onNavigate = onNavigateToRoute
            )
            2 -> TactPlannerSubTab(
                uiState = uiState,
                onSave = onSaveTactPlan
            )
        }
    }
}

// ==========================================
// SUB-TAB 1: KUESIONER UTAMA 30 BUTIR ASLI
// ==========================================
@Composable
private fun MainSurveySubTab(
    uiState: AppUiState,
    onAnswer: (String, Int, Boolean) -> Unit,
    onQuickFill: (Boolean) -> Unit,
    onSubmitPretest: () -> Unit,
    onSubmitPosttest: () -> Unit
) {
    var isViewingPosttest by remember { mutableStateOf(uiState.isPosttestActive || uiState.isPretestCompleted) }
    var showWaitlistDialog by remember { mutableStateOf(false) }

    val currentAnswers = if (isViewingPosttest) uiState.posttestAnswers else uiState.pretestAnswers
    val answeredCount = currentAnswers.size
    val totalCount = ResearchData.all30SurveyItems.size
    val isComplete = answeredCount == totalCount

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Toggle Switch Pretest (Hari 0) vs Posttest (Hari 10)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (!isViewingPosttest) MaterialTheme.colorScheme.surface else Color.Transparent)
                    .clickable { isViewingPosttest = false }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Pretest (Hari ke-0)" + if (uiState.isPretestCompleted) " ✓" else "",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = if (!isViewingPosttest) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isViewingPosttest) MaterialTheme.colorScheme.surface else Color.Transparent)
                    .clickable { isViewingPosttest = true }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Posttest (Hari ke-10)" + if (uiState.isPosttestCompleted) " ✓" else "",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = if (isViewingPosttest) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Progress Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Kemajuan Pengisian: $answeredCount dari $totalCount Butir",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "${(answeredCount * 100) / totalCount}%",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        LinearProgressIndicator(
            progress = { answeredCount.toFloat() / totalCount },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Tombol Isi Cepat Jawaban Simulasi
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            OutlinedButton(
                onClick = { onQuickFill(isViewingPosttest) },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("btn_quick_fill_survey")
            ) {
                Icon(Icons.Default.Bolt, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("⚡ Isi Cepat Jawaban Simulasi", fontSize = 11.sp)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Seksi A: Privacy Trust (PT1 - PT8)
        SurveySectionHeader(
            sectionCode = "Bagian A",
            title = "Privacy Trust (PT1–PT8)",
            description = "Tingkat kepercayaan terhadap jaminan keamanan teknis dan kerahasiaan data sistem (Skor 8–40)."
        )
        ResearchData.privacyTrustItems.forEachIndexed { idx, item ->
            SurveyItemCard(
                item = item,
                selectedScore = currentAnswers[item.id],
                onScoreSelected = { score -> onAnswer(item.id, score, isViewingPosttest) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Seksi B: HIVST Self-Efficacy (SE1 - SE8)
        SurveySectionHeader(
            sectionCode = "Bagian B",
            title = "HIVST Self-Efficacy (SE1–SE8)",
            description = "Keyakinan kapasitas diri dalam menyiapkan, mengusap gusi, dan membaca kit OraQuick (Skor 8–40)."
        )
        ResearchData.selfEfficacyItems.forEachIndexed { idx, item ->
            SurveyItemCard(
                item = item,
                selectedScore = currentAnswers[item.id],
                onScoreSelected = { score -> onAnswer(item.id, score, isViewingPosttest) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Seksi C: Intensi HIVST Berbasis TACT (INT1 - INT6)
        SurveySectionHeader(
            sectionCode = "Bagian C",
            title = "Intensi HIVST Berbasis TACT (INT1–INT6)",
            description = "Niat terencana menggunakan kit cairan oral dalam 14 hari ke depan (Skor 6–30)."
        )
        ResearchData.intentionTactItems.forEachIndexed { idx, item ->
            SurveyItemCard(
                item = item,
                selectedScore = currentAnswers[item.id],
                onScoreSelected = { score -> onAnswer(item.id, score, isViewingPosttest) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Seksi D: Help-Seeking Intention / Readiness (HS1 - HS8)
        SurveySectionHeader(
            sectionCode = "Bagian D",
            title = "Help-Seeking Intention & Readiness (HS1–HS8)",
            description = "Kesiapan mencari pertolongan dan konsultasi ke PKPR Puskesmas Tempuran (Skor 8–40)."
        )
        ResearchData.helpSeekingItems.forEachIndexed { idx, item ->
            SurveyItemCard(
                item = item,
                selectedScore = currentAnswers[item.id],
                onScoreSelected = { score -> onAnswer(item.id, score, isViewingPosttest) }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Tombol Submit
        if (!isViewingPosttest) {
            Button(
                onClick = onSubmitPretest,
                enabled = isComplete && !uiState.isPretestCompleted,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("btn_submit_pretest")
            ) {
                Text(
                    text = if (uiState.isPretestCompleted) "Pretest Sudah Disimpan (Hari ke-0 Selesai)" else "Simpan Jawaban Pretest & Masuk Hari ke-1",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        } else {
            Button(
                onClick = {
                    onSubmitPosttest()
                    if (uiState.clusterX == 0) showWaitlistDialog = true
                },
                enabled = isComplete && !uiState.isPosttestCompleted,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("btn_submit_posttest")
            ) {
                Text(
                    text = if (uiState.isPosttestCompleted) "Posttest Sudah Disimpan (Selesai)" else "Kirim Jawaban Posttest (Hari ke-10)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }

    // Modal Waitlist Crossover Khusus Kelompok Kontrol (X=0)
    if (showWaitlistDialog) {
        AlertDialog(
            onDismissRequest = { showWaitlistDialog = false },
            icon = {
                Icon(Icons.Default.LockOpen, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(36.dp))
            },
            title = {
                Text("Waitlist Crossover Access Terbuka!", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            },
            text = {
                Column {
                    Text(
                        text = "Selamat! Anda dari Kelompok Kontrol (X = 0) telah berhasil menyelesaikan kuesioner Posttest Hari ke-10.",
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Sesuai etika penelitian 'Waitlist Crossover Design', seluruh fitur canggih Chatbot AI adaptif dan Micro-Check-In JITAI kini telah DIBUKA LENGKAP untuk akun Anda.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showWaitlistDialog = false },
                    modifier = Modifier.testTag("btn_dismiss_waitlist_modal")
                ) {
                    Text("Jelajahi Fitur AI Sekarang")
                }
            }
        )
    }
}

@Composable
private fun SurveySectionHeader(
    sectionCode: String,
    title: String,
    description: String
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = "$sectionCode: $title",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SurveyItemCard(
    item: SurveyItem,
    selectedScore: Int?,
    onScoreSelected: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Text(
                    text = item.id,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.width(36.dp)
                )
                Text(
                    text = item.text,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Skala Likert 1..5
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                (1..5).forEach { score ->
                    val isSelected = selectedScore == score
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.surfaceVariant
                            )
                            .clickable { onScoreSelected(score) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$score",
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Sangat Tidak Setuju", fontSize = 9.sp, color = Color.Gray)
                Text("Sangat Setuju", fontSize = 9.sp, color = Color.Gray)
            }
        }
    }
}

// ==========================================
// SUB-TAB 2: MICRO-CHECK-IN (< 60 DETIK) DP1-DP3
// ==========================================
@Composable
private fun MicroCheckInSubTab(
    uiState: AppUiState,
    onUpdate: (Int, Float, Float, Float, Float, Float, Boolean) -> Unit,
    onNavigate: (String) -> Unit
) {
    var selectedDpStep by remember {
        mutableIntStateOf(
            when {
                uiState.programDay <= 2 -> 1
                uiState.programDay <= 6 -> 2
                else -> 3
            }
        )
    }

    // State sliders
    var prVal by remember { mutableFloatStateOf(uiState.dp1Record.pr) }
    var pcVal by remember { mutableFloatStateOf(uiState.dp1Record.pc) }
    var stVal by remember { mutableFloatStateOf(uiState.dp1Record.st) }
    var seVal by remember { mutableFloatStateOf(uiState.dp1Record.se) }
    var anxietyVal by remember { mutableFloatStateOf(uiState.dp3Record.anxietyLevel) }
    var kitReceivedVal by remember { mutableStateOf(uiState.dp3Record.isKitReceived) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Pilihan Decision Point (DP1, DP2, DP3)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf(
                1 to "DP1 (Hari 1)",
                2 to "DP2 (Hari 4)",
                3 to "DP3 (Hari 8)"
            ).forEach { (step, label) ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (selectedDpStep == step) MaterialTheme.colorScheme.surface else Color.Transparent)
                        .clickable {
                            selectedDpStep = step
                            when (step) {
                                1 -> {
                                    prVal = uiState.dp1Record.pr
                                    pcVal = uiState.dp1Record.pc
                                    stVal = uiState.dp1Record.st
                                    seVal = uiState.dp1Record.se
                                }
                                2 -> {
                                    prVal = uiState.dp2Record.pr
                                    pcVal = uiState.dp2Record.pc
                                    stVal = uiState.dp2Record.st
                                    seVal = uiState.dp2Record.se
                                }
                                3 -> {
                                    prVal = uiState.dp3Record.pr
                                    pcVal = uiState.dp3Record.pc
                                    stVal = uiState.dp3Record.st
                                    seVal = uiState.dp3Record.se
                                }
                            }
                        }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (selectedDpStep == step) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Speed, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Micro-Check-In Dinamis (< 60 Detik)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
                Text(
                    text = "Bantu sistem memetakan kebutuhan intervensi adaptif waktu nyata (JITAI) Anda hari ini:",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Item 1: Perceived Risk (PR)
                SliderItemView(
                    label = "1. Persepsi Risiko Pribadi (Perceived Risk)",
                    desc = "Sejauh mana Anda merasa perlu mengetahui status kekebalan tubuh?",
                    value = prVal,
                    onValueChange = { prVal = it }
                )

                // Item 2: Privacy Concern (PC)
                SliderItemView(
                    label = "2. Kekhawatiran Privasi (Privacy Concern)",
                    desc = "Tingkat kekhawatiran informasi Anda dibuka orang lain di rumah/sekolah?",
                    value = pcVal,
                    onValueChange = { pcVal = it }
                )

                // Item 3: Stigma (ST)
                SliderItemView(
                    label = "3. Persepsi Stigma Sosial (HIV-Related Stigma)",
                    desc = "Kekhawatiran terhadap penilaian negatif teman sebaya?",
                    value = stVal,
                    onValueChange = { stVal = it }
                )

                // Item 4: Self-Efficacy (SE)
                SliderItemView(
                    label = "4. Efikasi Diri Penggunaan Kit (HIVST Self-Efficacy)",
                    desc = "Keyakinan Anda mampu mengusap gusi dan membaca alat secara benar?",
                    value = seVal,
                    onValueChange = { seVal = it }
                )

                // Khusus DP3 (Hari 8): Kecemasan & Konfirmasi Kit
                if (selectedDpStep == 3) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(10.dp))

                    SliderItemView(
                        label = "5. Tingkat Kecemasan Pra-Tes",
                        desc = "Rasa tegang menjelang pembacaan hasil mandiri?",
                        value = anxietyVal,
                        onValueChange = { anxietyVal = it }
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { kitReceivedVal = !kitReceivedVal }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = kitReceivedVal,
                            onCheckedChange = { kitReceivedVal = it }
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Konfirmasi: Saya telah menerima fisik kit HIVST dalam kemasan tertutup netral.",
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        onUpdate(selectedDpStep, prVal, pcVal, stVal, seVal, anxietyVal, kitReceivedVal)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("btn_submit_micro_checkin")
                ) {
                    Text("Perbarui Intervensi Adaptif JITAI", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Kartu Aturan Aktif Terkini
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Bolt, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Keputusan JITAI Aktif: ${uiState.activeJitaiRule.activeRuleCode}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = uiState.activeJitaiRule.recommendationTitle,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                )
                Text(
                    text = uiState.activeJitaiRule.recommendationSummary,
                    fontSize = 11.sp,
                    lineHeight = 15.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = { onNavigate(uiState.activeJitaiRule.recommendedActionRoute) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Buka Rekomendasi (${uiState.activeJitaiRule.recommendedActionRoute.uppercase()})", fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun SliderItemView(
    label: String,
    desc: String,
    value: Float,
    onValueChange: (Float) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            Text(String.format("%.1f", value), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }
        Text(desc, fontSize = 10.sp, color = Color.Gray)
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 1.0f..5.0f,
            steps = 7,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}

// ==========================================
// SUB-TAB 3: PERENCANA KOMITMEN TACT 14 HARI & RINGKASAN
// ==========================================
@Composable
private fun TactPlannerSubTab(
    uiState: AppUiState,
    onSave: (String, String, String) -> Unit
) {
    var locationInput by remember { mutableStateOf(uiState.tactPlan.privateLocation) }
    var dateInput by remember { mutableStateOf(uiState.tactPlan.plannedDate) }
    var timeInput by remember { mutableStateOf(uiState.tactPlan.plannedTime) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Kartu Rencana Aksi TACT
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.DateRange, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Perencana Komitmen TACT (14 Hari)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
                Text(
                    text = "Kerangka TACT (Target, Action, Context, Time) terbukti ilmiah meningkatkan realisasi niat skrining mandiri:",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = locationInput,
                    onValueChange = { locationInput = it },
                    label = { Text("Konteks / Tempat Privat Aman (Context)") },
                    leadingIcon = { Icon(Icons.Default.Place, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth().testTag("input_tact_context")
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = dateInput,
                    onValueChange = { dateInput = it },
                    label = { Text("Rencana Tanggal Pelaksanaan (Time)") },
                    leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth().testTag("input_tact_date")
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = timeInput,
                    onValueChange = { timeInput = it },
                    label = { Text("Rencana Jam Pelaksanaan (Time)") },
                    leadingIcon = { Icon(Icons.Default.Schedule, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth().testTag("input_tact_time")
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onSave(locationInput, dateInput, timeInput) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("btn_save_tact_plan")
                ) {
                    Text("Tetapkan Komitmen Skrining Mandiri", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }

                if (uiState.tactPlan.isSaved) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Komitmen TACT berhasil tersimpan dalam sistem.", fontSize = 11.sp, color = Color(0xFF10B981), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Kartu Ringkasan Perbandingan Skor Pretest vs Posttest
        Text(
            text = "📊 Ringkasan Skor Psikometrik Penelitian:",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Kalkulasi Skor
        fun calculateSubscale(items: List<SurveyItem>, map: Map<String, Int>): Int {
            return items.sumOf { map[it.id] ?: 3 }
        }

        val prePt = calculateSubscale(ResearchData.privacyTrustItems, uiState.pretestAnswers)
        val postPt = calculateSubscale(ResearchData.privacyTrustItems, uiState.posttestAnswers)

        val preSe = calculateSubscale(ResearchData.selfEfficacyItems, uiState.pretestAnswers)
        val postSe = calculateSubscale(ResearchData.selfEfficacyItems, uiState.posttestAnswers)

        val preInt = calculateSubscale(ResearchData.intentionTactItems, uiState.pretestAnswers)
        val postInt = calculateSubscale(ResearchData.intentionTactItems, uiState.posttestAnswers)

        val preHs = calculateSubscale(ResearchData.helpSeekingItems, uiState.pretestAnswers)
        val postHs = calculateSubscale(ResearchData.helpSeekingItems, uiState.posttestAnswers)

        ScoreComparisonCard("Privacy Trust (PT1–PT8)", prePt, postPt, 8, 40)
        ScoreComparisonCard("HIVST Self-Efficacy (SE1–SE8)", preSe, postSe, 8, 40)
        ScoreComparisonCard("Intensi HIVST TACT (INT1–INT6)", preInt, postInt, 6, 30)
        ScoreComparisonCard("Help-Seeking Readiness (HS1–HS8)", preHs, postHs, 8, 40)

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun ScoreComparisonCard(
    variableTitle: String,
    preScore: Int,
    postScore: Int,
    minVal: Int,
    maxVal: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(variableTitle, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Pretest (Hari 0)", fontSize = 10.sp, color = Color.Gray)
                    Text("$preScore / $maxVal", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Posttest (Hari 10)", fontSize = 10.sp, color = Color.Gray)
                    Text("$postScore / $maxVal", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                }
            }
        }
    }
}
