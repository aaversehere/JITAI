package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.data.EModule
import com.example.data.ResearchData
import com.example.ui.theme.EmergencyRed
import com.example.viewmodel.AppUiState

@Composable
fun ModulScreen(
    uiState: AppUiState,
    selectedSubTab: Int,
    onSubTabSelected: (Int) -> Unit,
    onSelectModule: (String) -> Unit,
    onSetLabStep: (Int) -> Unit,
    onStartLabTimer: () -> Unit,
    onSelectLabScenario: (String) -> Unit,
    onUpdateWindowPeriodDays: (Int) -> Unit,
    onSubmitCallbackRequest: (String, String, String) -> Unit,
    onDismissToast: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Sub-Tab Navigation Bar
        TabRow(
            selectedTabIndex = selectedSubTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth()
        ) {
            Tab(
                selected = selectedSubTab == 0,
                onClick = { onSubTabSelected(0) },
                text = { Text("E-Modul", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                modifier = Modifier.testTag("subtab_modul_library")
            )
            Tab(
                selected = selectedSubTab == 1,
                onClick = { onSubTabSelected(1) },
                text = { Text("Virtual Lab", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                modifier = Modifier.testTag("subtab_modul_lab")
            )
            Tab(
                selected = selectedSubTab == 2,
                onClick = { onSubTabSelected(2) },
                text = { Text("Masa Jendela", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                modifier = Modifier.testTag("subtab_modul_wp")
            )
            Tab(
                selected = selectedSubTab == 3,
                onClick = { onSubTabSelected(3) },
                text = { Text("Rujukan PKPR", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                modifier = Modifier.testTag("subtab_modul_linkage")
            )
        }

        when (selectedSubTab) {
            0 -> ModuleLibrarySubTab(
                uiState = uiState,
                onSelectModule = onSelectModule
            )
            1 -> VirtualLabSubTab(
                uiState = uiState,
                onSetStep = onSetLabStep,
                onStartTimer = onStartLabTimer,
                onSelectScenario = onSelectLabScenario,
                onGoToLinkage = { onSubTabSelected(3) }
            )
            2 -> WindowPeriodCalculatorSubTab(
                uiState = uiState,
                onUpdateDays = onUpdateWindowPeriodDays
            )
            3 -> ActiveLinkageSubTab(
                uiState = uiState,
                onSubmitCallback = onSubmitCallbackRequest,
                onDismissToast = onDismissToast
            )
        }
    }
}

// ==========================================
// SUB-TAB 1: PUSTAKA 5 E-MODUL BACAAN LENGKAP
// ==========================================
@Composable
private fun ModuleLibrarySubTab(
    uiState: AppUiState,
    onSelectModule: (String) -> Unit
) {
    var expandedModuleId by remember { mutableStateOf<String?>(uiState.selectedModuleId) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "📚 Pustaka E-Modul Kesehatan Remaja",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Materi berbasis rujukan ilmiah resmi Kemenkes RI & WHO (2024a):",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(14.dp))

        ResearchData.eModules.forEach { module ->
            val isExpanded = expandedModuleId == module.id

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clickable {
                        expandedModuleId = if (isExpanded) null else module.id
                        onSelectModule(module.id)
                    }
                    .testTag("card_module_${module.id}"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isExpanded) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
                    else MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.primary
                        ) {
                            Text(
                                text = module.code,
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Text(
                            text = "⏱ ~${module.estimatedReadMinutes} menit baca",
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = module.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Poin Kunci
                    module.keyPoints.forEach { point ->
                        Row(
                            modifier = Modifier.padding(vertical = 1.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text("• ", fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                            Text(point, fontSize = 11.sp, lineHeight = 15.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    // Tampilan Konten Bacaan Lengkap jika Expanded
                    AnimatedVisibility(visible = isExpanded) {
                        Column(modifier = Modifier.padding(top = 10.dp)) {
                            Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = module.fullContent,
                                fontSize = 12.sp,
                                lineHeight = 17.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Referensi Klinis: ${module.clinicalCitation}",
                                fontSize = 9.sp,
                                color = Color.Gray,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

// ==========================================
// SUB-TAB 2: VIRTUAL LAB ORAQUICK 5 LANGKAH
// ==========================================
@Composable
private fun VirtualLabSubTab(
    uiState: AppUiState,
    onSetStep: (Int) -> Unit,
    onStartTimer: () -> Unit,
    onSelectScenario: (String) -> Unit,
    onGoToLinkage: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "🧪 Simulator Laboratorium Virtual OraQuick",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
        Text(
            text = "Latih keterampilan Anda melalui simulasi 5 langkah mandiri:",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Indikator 5 Langkah
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            (1..5).forEach { stepNum ->
                val isCurrent = uiState.labStep == stepNum
                val isPast = uiState.labStep > stepNum
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                isCurrent -> MaterialTheme.colorScheme.primary
                                isPast -> Color(0xFF10B981)
                                else -> MaterialTheme.colorScheme.surfaceVariant
                            }
                        )
                        .clickable { onSetStep(stepNum) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$stepNum",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isCurrent || isPast) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                when (uiState.labStep) {
                    1 -> {
                        Text("Langkah 1: Persiapan Privasi & Puasa 15 Menit", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Pastikan Anda berada di ruang kamar privat yang aman. Puasa dari makan, minum, dan permen karet minimal 15 menit agar residu tidak memengaruhi spesimen.",
                            fontSize = 12.sp, lineHeight = 16.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { onSetStep(2) },
                            modifier = Modifier.fillMaxWidth().testTag("btn_lab_step_1_next")
                        ) {
                            Text("Saya Sudah Puasa 15 Menit -> Lanjut Langkah 2")
                        }
                    }
                    2 -> {
                        Text("Langkah 2: Membuka Tabung Buffer & Meletakkan di Stand", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Keluarkan tabung cairan penyangga (buffer). Buka tutupnya secara hati-hati agar tidak ada tetesan cairan yang tumpah, lalu pasang tegak lurus pada dudukan (stand).",
                            fontSize = 12.sp, lineHeight = 16.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { onSetStep(3) },
                            modifier = Modifier.fillMaxWidth().testTag("btn_lab_step_2_next")
                        ) {
                            Text("Tabung Buffer Terpasang Kokoh -> Lanjut Langkah 3")
                        }
                    }
                    3 -> {
                        Text("Langkah 3: Pengambilan Usapan Mukosa Oral (Oral Swab)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Keluarkan spatula pipih. Usapkan bantalan secara perlahan sepanjang gusi luar atas 1 kali, lalu balik bantalan dan usapkan sepanjang gusi luar bawah 1 kali. (Jangan usap pada lidah atau dinding pipi).",
                            fontSize = 12.sp, lineHeight = 16.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { onSetStep(4) },
                            modifier = Modifier.fillMaxWidth().testTag("btn_lab_step_3_next")
                        ) {
                            Text("Usapan Gusi Atas & Bawah Selesai -> Lanjut Langkah 4")
                        }
                    }
                    4 -> {
                        Text("Langkah 4: Reaksi Penyangga & Fast-Forward Timer (20 Menit)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Masukkan spatula ke dalam tabung buffer hingga menyentuh dasar tabung. Waktu pembacaan yang sah adalah antara menit ke-20 hingga menit ke-40.",
                            fontSize = 12.sp, lineHeight = 16.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.HourglassBottom, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = if (uiState.isLabTimerRunning) "Waktu Tunggu Simulasi: ${uiState.labTimerSeconds} detik tersisa" else "Siap Jalankan Timer 20 Menit",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = onStartTimer,
                            enabled = !uiState.isLabTimerRunning,
                            modifier = Modifier.fillMaxWidth().testTag("btn_lab_start_timer")
                        ) {
                            Text(if (uiState.isLabTimerRunning) "Reaksi Berlangsung..." else "⚡ Jalankan Fast-Forward Timer")
                        }
                    }
                    5 -> {
                        Text("Langkah 5: Simulator Pembacaan Garis Hasil (C & T)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Pilih salah satu dari 3 skenario hasil di bawah ini untuk mempelajari interpretasi klinisnya:",
                            fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf("NONREACTIVE" to "1 Garis (C)", "REACTIVE" to "2 Garis (C&T)", "INVALID" to "Invalid").forEach { (scen, label) ->
                                val isSelected = uiState.labSelectedScenario == scen
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
                                        .clickable { onSelectScenario(scen) }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = label,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Visual Strip Kit
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Garis Kontrol (C)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .width(24.dp)
                                            .height(4.dp)
                                            .clip(RoundedCornerShape(2.dp))
                                            .background(if (uiState.labSelectedScenario != "INVALID") Color(0xFFDC2626) else Color(0xFFE2E8F0))
                                    )
                                    Text(if (uiState.labSelectedScenario != "INVALID") "Muncul" else "Hilang", fontSize = 9.sp, color = Color.Gray)
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Garis Tes (T)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .width(24.dp)
                                            .height(4.dp)
                                            .clip(RoundedCornerShape(2.dp))
                                            .background(if (uiState.labSelectedScenario == "REACTIVE") Color(0xFFDC2626) else Color(0xFFE2E8F0))
                                    )
                                    Text(if (uiState.labSelectedScenario == "REACTIVE") "Muncul" else "Bersih", fontSize = 9.sp, color = Color.Gray)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Penjelasan Hasil Klinis
                        when (uiState.labSelectedScenario) {
                            "NONREACTIVE" -> {
                                Text("Artinya: Nonreaktif (Tidak Terdeteksi Antibodi HIV)", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF059669))
                                Text("Jika tes dilakukan setelah 12 minggu dari risiko pajanan terakhir, hasil ini konklusif. Namun bila pajanan masih baru (<12 minggu), lakukan tes ulang.", fontSize = 11.sp)
                            }
                            "REACTIVE" -> {
                                Text("Artinya: Reaktif (Skrining Awal Menunjukkan Kemungkinan Antibodi)", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = EmergencyRed)
                                Text("Ingat, ini BUKAN diagnosis definitif. Anda WAJIB melakukan tes konfirmasi 3 reagen di Puskesmas Tempuran. Layanan ini gratis dan konselor siap mendampingi Anda.", fontSize = 11.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = onGoToLinkage,
                                    colors = ButtonDefaults.buttonColors(containerColor = EmergencyRed),
                                    modifier = Modifier.fillMaxWidth().testTag("btn_lab_to_linkage")
                                ) {
                                    Text("Buka Active Linkage Rujukan Puskesmas", fontSize = 11.sp)
                                }
                            }
                            "INVALID" -> {
                                Text("Artinya: Tes Tidak Sah (Garis C Tidak Tampak)", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFFD97706))
                                Text("Terjadi kesalahan prosedur atau tabung buffer tidak cukup meresap. Silakan gunakan hak Anda untuk mengklaim kit baru gratis secara anonim.", fontSize = 11.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedButton(
                            onClick = { onSetStep(1) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Ulangi Simulasi dari Langkah 1", fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

// ==========================================
// SUB-TAB 3: KALKULATOR INTERAKTIF PERIODE JENDELA
// ==========================================
@Composable
private fun WindowPeriodCalculatorSubTab(
    uiState: AppUiState,
    onUpdateDays: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "📅 Kalkulator Periode Jendela (Window Period 12 Minggu)",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
        Text(
            text = "Hitung waktu akurasi pembentukan antibodi untuk mencegah salah tafsir (false reassurance):",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Berapa hari yang lalu perkiraan pajanan risiko terjadi?",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("${uiState.windowPeriodDaysAgo} hari yang lalu", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, fontSize = 14.sp)
                    Text("(Standar Jendela: 84 hari / 12 minggu)", fontSize = 10.sp, color = Color.Gray)
                }

                Slider(
                    value = uiState.windowPeriodDaysAgo.toFloat(),
                    onValueChange = { onUpdateDays(it.toInt()) },
                    valueRange = 1f..120f,
                    steps = 119,
                    modifier = Modifier.testTag("slider_window_period")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Ringkasan Hasil Kalkulator
                val result = uiState.windowPeriodResult

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (result.isWindowPeriodCompleted) Color(0xFFD1FAE5) else Color(0xFFFEF3C7)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (result.isWindowPeriodCompleted) Icons.Default.CheckCircle else Icons.Default.Warning,
                                contentDescription = null,
                                tint = if (result.isWindowPeriodCompleted) Color(0xFF059669) else Color(0xFFD97706),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (result.isWindowPeriodCompleted) "Masa Jendela Selesai (Konklusif)" else "Masih Dalam Masa Jendela (${result.daysRemaining} Hari Tersisa)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = if (result.isWindowPeriodCompleted) Color(0xFF065F46) else Color(0xFF92400E)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Tanggal Perkiraan Selesai Jendela: ${result.windowEndString}",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = result.clinicalRecommendation,
                            fontSize = 11.sp,
                            lineHeight = 15.sp,
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

// ==========================================
// SUB-TAB 4: PUSAT RUJUKAN RAHASIA PKPR
// ==========================================
@Composable
private fun ActiveLinkageSubTab(
    uiState: AppUiState,
    onSubmitCallback: (String, String, String) -> Unit,
    onDismissToast: () -> Unit
) {
    var contactMethod by remember { mutableStateOf("Chat Rahasia Aplikasi") }
    var preferredHour by remember { mutableStateOf("Pukul 14.00 - 16.00 WIB (Sepulang Sekolah)") }
    var urgencyLevel by remember { mutableStateOf("Konsultasi Hasil Reaktif") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Info Fasilitas PKPR Mitra
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocalHospital, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = ResearchData.contactInfo.clinicName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(ResearchData.contactInfo.address, fontSize = 10.sp, color = Color.Gray)
                Text(ResearchData.contactInfo.confidentialityAssurance, fontSize = 10.sp, color = MaterialTheme.colorScheme.primary, lineHeight = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "3 Jalur Resmi Active Linkage (WHO 2024a):",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Jalur 1: Hasil Reaktif / Cemas Berat
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("Jalur 1: Hasil Reaktif / Cemas Berat (SLA <= 1x24 Jam)", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = EmergencyRed)
                Text("Pendampingan tes konfirmasi 3 reagen darah di Puskesmas Tempuran secara cepat, ramah remaja, dan bebas biaya.", fontSize = 11.sp, color = Color.Gray)
            }
        }

        // Jalur 2: Hasil Nonreaktif
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("Jalur 2: Hasil Nonreaktif (Pengingat Retesting)", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF059669))
                Text("Pengingat jadwal tes ulang pasca-periode jendela 12 minggu dan edukasi pencegahan berkelanjutan.", fontSize = 11.sp, color = Color.Gray)
            }
        }

        // Jalur 3: Hasil Invalid
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("Jalur 3: Hasil Invalid (Klaim Kit Baru Anonim)", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFFD97706))
                Text("Penggantian kit baru tertutup netral atau permohonan tes langsung dengan pendampingan perawat PKPR.", fontSize = 11.sp, color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Formulir Callback Request Terenkripsi
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Formulir Permohonan Pendampingan Rahasia", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text("Dihubungkan hanya menggunakan Token: ${uiState.userToken}", fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)

                Spacer(modifier = Modifier.height(12.dp))

                Text("Tingkat Urgensi / Alasan:", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                listOf("Konsultasi Hasil Reaktif (Prioritas Cepat)", "Cemas Berat Menghadapi Hasil", "Klaim Penggantian Kit Invalid", "Pertanyaan Umum Periode Jendela").forEach { urg ->
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable { urgencyLevel = urg }.padding(vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = urgencyLevel == urg, onClick = { urgencyLevel = urg })
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(urg, fontSize = 11.sp)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text("Metode Kontak yang Nyaman Bagi Anda:", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                listOf("Chat Rahasia Aplikasi", "Panggilan Suara Samaran").forEach { m ->
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable { contactMethod = m }.padding(vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = contactMethod == m, onClick = { contactMethod = m })
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(m, fontSize = 11.sp)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        onSubmitCallback(contactMethod, preferredHour, urgencyLevel)
                    },
                    modifier = Modifier.fillMaxWidth().testTag("btn_submit_callback_request")
                ) {
                    Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Kirim Permohonan ke Konselor PKPR (SLA <= 24 Jam)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                if (uiState.callbackSuccessToast) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFD1FAE5)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF059669), modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Permohonan Anda berhasil diterima sistem! Konselor PKPR akan merespons maksimal 1x24 jam melalui saluran rahasia.",
                                fontSize = 11.sp,
                                color = Color(0xFF065F46)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
