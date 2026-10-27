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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ResearchData
import kotlin.random.Random

@Composable
fun AuthPortalScreen(
    onLoginSuccess: (String, String) -> Boolean,
    onQuickFill: (Int) -> Unit,
    onCompleteSignUp: (String, String, Int, Int, Int, Map<String, String>) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedAuthTab by remember { mutableIntStateOf(0) } // 0: Login, 1: Sign Up

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Header Lambang & Judul Edukasi
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Shield,
                contentDescription = "Shield Logo",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(34.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Ruang Belajar Mandiri Remaja",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Edukasi Pencegahan & Skrining Mandiri SMAN 1 Tempuran",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Badge Perlindungan Privasi
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Prinsip Privasi Penuh: Tanpa Nama, NISN, atau No HP",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Tab Autentikasi (LOGIN vs SIGN UP)
        TabRow(
            selectedTabIndex = selectedAuthTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
        ) {
            Tab(
                selected = selectedAuthTab == 0,
                onClick = { selectedAuthTab = 0 },
                text = { Text("MASUK (LOGIN)", fontWeight = FontWeight.Bold, fontSize = 13.sp) },
                modifier = Modifier.testTag("tab_auth_login")
            )
            Tab(
                selected = selectedAuthTab == 1,
                onClick = { selectedAuthTab = 1 },
                text = { Text("DAFTAR BARU (SIGN UP)", fontWeight = FontWeight.Bold, fontSize = 13.sp) },
                modifier = Modifier.testTag("tab_auth_signup")
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedAuthTab == 0) {
            // ==========================================
            // TAB LOGIN PESERTA
            // ==========================================
            LoginTabContent(
                onLogin = onLoginSuccess,
                onQuickFill = onQuickFill
            )
        } else {
            // ==========================================
            // TAB SIGN UP 4 TAHAP WIZARD
            // ==========================================
            SignUpWizardContent(
                onComplete = onCompleteSignUp,
                onSwitchToLogin = { selectedAuthTab = 0 }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun LoginTabContent(
    onLogin: (String, String) -> Boolean,
    onQuickFill: (Int) -> Unit
) {
    var tokenInput by remember { mutableStateOf("") }
    var pinInput by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Masuk Menggunakan Token Pseudonim",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Gunakan kode token KRW-SMAN1T-XXX dan PIN 4-digit yang telah Anda buat.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = tokenInput,
                onValueChange = {
                    tokenInput = it.uppercase()
                    loginError = false
                },
                label = { Text("Kode Token Pseudonim (Contoh: KRW-SMAN1T-042)") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_login_token"),
                leadingIcon = {
                    Icon(Icons.Default.VpnKey, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = pinInput,
                onValueChange = {
                    if (it.length <= 6) {
                        pinInput = it
                        loginError = false
                    }
                },
                label = { Text("PIN Keamanan (4-6 Digit)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_login_pin"),
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                }
            )

            if (loginError) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Token atau PIN tidak valid. Pastikan format token benar dan PIN minimal 4 digit.",
                    color = Color.Red,
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = {
                    val success = onLogin(tokenInput, pinInput)
                    if (!success) loginError = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("btn_submit_login"),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Masuk ke Ruang Belajar", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))
            Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "⚡ Akses Cepat Simulasi Pengujian Kelompok:",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Quick Fill 1: Intervensi 18 th
            OutlinedButton(
                onClick = { onQuickFill(1) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .testTag("btn_quick_fill_1")
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("• KRW-SMAN1T-042 (Intervensi X=1, Usia 18 th)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text("Pretest selesai, Hari ke-4, siap uji Micro-Check-In & Chatbot AI", fontSize = 10.sp, color = Color.Gray)
                }
            }

            // Quick Fill 2: Intervensi 16 th (Assent)
            OutlinedButton(
                onClick = { onQuickFill(2) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .testTag("btn_quick_fill_2")
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("• KRW-SMAN1T-015 (Intervensi X=1, Usia 16 th)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text("Peserta Assent, mulai dari Pretest Hari ke-0", fontSize = 10.sp, color = Color.Gray)
                }
            }

            // Quick Fill 3: Kontrol 17 th (Waitlist)
            OutlinedButton(
                onClick = { onQuickFill(3) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .testTag("btn_quick_fill_3")
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("• KRW-SMAN1T-088 (Kontrol X=0, Usia 17 th)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text("Kelompok Kontrol edukasi statis, fitur AI terbuka pasca Posttest Hari-10", fontSize = 10.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
private fun SignUpWizardContent(
    onComplete: (String, String, Int, Int, Int, Map<String, String>) -> Unit,
    onSwitchToLogin: () -> Unit
) {
    var wizardStep by remember { mutableIntStateOf(1) } // 1..4

    // Tahap 1 State: Token & PIN & Klaster
    var generatedToken by remember { mutableStateOf("KRW-SMAN1T-" + String.format("%03d", Random.nextInt(100, 999))) }
    var selectedCluster by remember { mutableIntStateOf(1) } // 1 = Intervensi, 0 = Kontrol
    var signupPin by remember { mutableStateOf("1234") }

    // Tahap 2 State: Skrining Inklusi/Eksklusi
    var isStudentSman1 by remember { mutableStateOf(true) }
    var isHivStatusUnknown by remember { mutableStateOf(true) }
    var hasSmartphoneAccess by remember { mutableStateOf(true) }
    var noAcuteCrisis by remember { mutableStateOf(true) }

    // Tahap 3 State: Assent & Consent
    var participantAge by remember { mutableIntStateOf(16) }
    var assentParentOption by remember { mutableIntStateOf(1) } // 1: Umbrella term, 2: Waiver KEPK
    var assentAgreed by remember { mutableStateOf(false) }
    var researchVsClinicalSeparationAcknowledged by remember { mutableStateOf(false) }

    // Tahap 4 State: Kovariat A4-A8
    val covariateSelections = remember {
        mutableStateMapOf(
            "A4" to "16 tahun",
            "A5" to "Perempuan",
            "A6" to "Kelas X (Fase E)",
            "A7" to "Belum Pernah Sama Sekali",
            "A8" to "Perangkat Pribadi Penuh (Milik Sendiri)"
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Indikator Wizard 4 Tahap
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tahap $wizardStep dari 4",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = when (wizardStep) {
                        1 -> "Token & PIN"
                        2 -> "Skrining Kelayakan"
                        3 -> "Persetujuan Etik"
                        else -> "Data Karakteristik"
                    },
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { wizardStep / 4f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Spacer(modifier = Modifier.height(18.dp))

            when (wizardStep) {
                1 -> {
                    // ==========================================
                    // TAHAP 1: GENERATOR TOKEN & PIN (TANPA PII)
                    // ==========================================
                    Text(
                        text = "1. Generator Token Pseudonim & PIN Keamanan",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Sesuai prinsip perlindungan privasi, sistem tidak pernah meminta nama asli, NISN, atau no HP.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Kode Token Pseudonim Anda:", fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                                Text(generatedToken, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                            }
                            IconButton(
                                onClick = {
                                    generatedToken = "KRW-SMAN1T-" + String.format("%03d", Random.nextInt(100, 999))
                                }
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = "Acak Token Baru", tint = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Pilih Klaster Penelitian:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedCluster == 1,
                            onClick = { selectedCluster = 1 }
                        )
                        Text("Kelompok Intervensi (X = 1)", fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        RadioButton(
                            selected = selectedCluster == 0,
                            onClick = { selectedCluster = 0 }
                        )
                        Text("Kelompok Kontrol (X = 0)", fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = signupPin,
                        onValueChange = { if (it.length <= 6) signupPin = it },
                        label = { Text("Buat PIN Keamanan (4-6 Angka)") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth().testTag("input_signup_pin")
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = { if (signupPin.length >= 4) wizardStep = 2 },
                        enabled = signupPin.length >= 4,
                        modifier = Modifier.fillMaxWidth().testTag("btn_wizard_next_1")
                    ) {
                        Text("Lanjut ke Skrining Kelayakan")
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                }
                2 -> {
                    // ==========================================
                    // TAHAP 2: SKRINING INKLUSI & EKSKLUSI
                    // ==========================================
                    Text(
                        text = "2. Skrining Kriteria Inklusi & Eksklusi",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Pastikan Anda memenuhi persyaratan kepesertaan penelitian berikut:",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    val screeningItems = listOf(
                        Triple("Siswa aktif SMAN 1 Tempuran Karawang berusia 15–19 tahun.", isStudentSman1, { v: Boolean -> isStudentSman1 = v }),
                        Triple("Status HIV belum diketahui / belum pernah terdiagnosis positif HIV (bukan ALHIV terapi ARV).", isHivStatusUnknown, { v: Boolean -> isHivStatusUnknown = v }),
                        Triple("Memiliki akses ke perangkat smartphone secara mandiri atau berbagi.", hasSmartphoneAccess, { v: Boolean -> hasSmartphoneAccess = v }),
                        Triple("Tidak sedang dalam krisis psikologis akut yang memerlukan penanganan gawat darurat.", noAcuteCrisis, { v: Boolean -> noAcuteCrisis = v })
                    )

                    screeningItems.forEach { (label, checked, onCheckedChange) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onCheckedChange(!checked) }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = checked,
                                onCheckedChange = onCheckedChange,
                                colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = label, fontSize = 12.sp, lineHeight = 16.sp)
                        }
                    }

                    val allScreeningPassed = isStudentSman1 && isHivStatusUnknown && hasSmartphoneAccess && noAcuteCrisis

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        OutlinedButton(onClick = { wizardStep = 1 }) { Text("Kembali") }
                        Button(
                            onClick = { wizardStep = 3 },
                            enabled = allScreeningPassed,
                            modifier = Modifier.testTag("btn_wizard_next_2")
                        ) {
                            Text("Lanjut ke Persetujuan Etik")
                        }
                    }
                }
                3 -> {
                    // ==========================================
                    // TAHAP 3: INFORMED ASSENT & CONSENT BERTINGKAT
                    // ==========================================
                    Text(
                        text = "3. Lembar Penjelasan & Persetujuan Etik (PSP)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Kotak Naskah PSP
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(130.dp).fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(10.dp)
                                .verticalScroll(rememberScrollState())
                        ) {
                            Text(
                                text = "Penjelasan Sebelum Persetujuan (PSP):\n" +
                                        "Penelitian ini bertujuan menguji efektivitas program edukasi digital adaptif pencegahan HIV dan pengenalan kit skrining cairan oral di SMAN 1 Tempuran. Partisipasi Anda bersifat sukarela. Identitas Anda dilindungi secara anonim menggunakan kode token. Anda berhak menghentikan keikutsertaan kapan pun tanpa sanksi akademik apa pun.",
                                fontSize = 11.sp,
                                lineHeight = 15.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Pilihan Usia untuk Cabang Assent vs Consent
                    Text("Pilih Usia Anda Saat Ini:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        listOf(15, 16, 17, 18, 19).forEach { age ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable {
                                    participantAge = age
                                    covariateSelections["A4"] = "$age tahun"
                                }
                            ) {
                                RadioButton(
                                    selected = participantAge == age,
                                    onClick = {
                                        participantAge = age
                                        covariateSelections["A4"] = "$age tahun"
                                    }
                                )
                                Text("$age", fontSize = 11.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (participantAge < 18) {
                        // Jalur Assent Remaja (< 18 tahun)
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = "📋 Jalur Persetujuan Remaja (Informed Assent < 18 Th):",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(
                                        selected = assentParentOption == 1,
                                        onClick = { assentParentOption = 1 }
                                    )
                                    Text(
                                        "Izin Wali/Ortu Menggunakan Terminologi Payung: 'Edukasi Kesehatan Remaja'",
                                        fontSize = 11.sp
                                    )
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(
                                        selected = assentParentOption == 2,
                                        onClick = { assentParentOption = 2 }
                                    )
                                    Text(
                                        "Waiver of Parental Consent KEPK didampingi Adolescent Advocate (Konselor UKS/PKPR)",
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    } else {
                        // Jalur Adult Consent (18-19 tahun)
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = "✍️ Jalur Persetujuan Mandiri Dewasa (Informed Consent 18-19 Th):",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                Text(
                                    text = "Anda telah mencapai usia dewasa mandiri secara hukum untuk memberikan persetujuan penelitian secara langsung.",
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Checklist Pemisahan Research Consent vs Clinical Consent
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable {
                            researchVsClinicalSeparationAcknowledged = !researchVsClinicalSeparationAcknowledged
                        },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = researchVsClinicalSeparationAcknowledged,
                            onCheckedChange = { researchVsClinicalSeparationAcknowledged = it }
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Saya memahami pemisahan tegas antara Persetujuan Penelitian Aplikasi ini dengan Persetujuan Tindakan Medis Klinis (yang dilakukan terpisah di Puskesmas jika memerlukan tes darah konfirmasi).",
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth().clickable { assentAgreed = !assentAgreed },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = assentAgreed,
                            onCheckedChange = { assentAgreed = it }
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Saya secara sukarela setuju untuk berpartisipasi dalam penelitian ini.",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        OutlinedButton(onClick = { wizardStep = 2 }) { Text("Kembali") }
                        Button(
                            onClick = { wizardStep = 4 },
                            enabled = assentAgreed && researchVsClinicalSeparationAcknowledged,
                            modifier = Modifier.testTag("btn_wizard_next_3")
                        ) {
                            Text("Lanjut ke Data Awal")
                        }
                    }
                }
                4 -> {
                    // ==========================================
                    // TAHAP 4: DATA KOVARIAT AWAL (ITEM A4 - A8)
                    // ==========================================
                    Text(
                        text = "4. Karakteristik & Kovariat Awal (Item A4–A8)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Data statistik dasar untuk mengendalikan faktor perancu penelitian:",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ResearchData.covariateItems.forEach { item ->
                        Text(
                            text = "${item.code}: ${item.label}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        item.options.forEach { opt ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { covariateSelections[item.code] = opt }
                                    .padding(vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = covariateSelections[item.code] == opt,
                                    onClick = { covariateSelections[item.code] = opt }
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(opt, fontSize = 11.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        OutlinedButton(onClick = { wizardStep = 3 }) { Text("Kembali") }
                        Button(
                            onClick = {
                                onComplete(
                                    generatedToken,
                                    signupPin,
                                    selectedCluster,
                                    participantAge,
                                    assentParentOption,
                                    covariateSelections.toMap()
                                )
                            },
                            modifier = Modifier.testTag("btn_complete_signup")
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Selesaikan & Mulai Pretest")
                        }
                    }
                }
            }
        }
    }
}
