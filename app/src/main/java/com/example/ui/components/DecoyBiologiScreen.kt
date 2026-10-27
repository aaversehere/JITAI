package com.example.ui.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DecoyGreen

@Composable
fun DecoyBiologiScreen(
    onUnlockWithPin: (String) -> Boolean,
    modifier: Modifier = Modifier
) {
    var showPinDialog by remember { mutableStateOf(false) }
    var enteredPin by remember { mutableStateOf("") }
    var isPinError by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier
            .fillMaxSize()
            .testTag("screen_decoy_biologi"),
        color = Color(0xFFF0FDF4) // Soft green academic notebook background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header Materi Sekolah
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DecoyGreen, RoundedCornerShape(12.dp))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = "Logo Sekolah",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "SMAN 1 TEMPURAN - KURIKULUM MERDEKA",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFDCFCE7)
                    )
                    Text(
                        text = "Biologi Kelas XI: Sistem Imun & Pertahanan Tubuh",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Rangkuman Bab
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Book, contentDescription = null, tint = DecoyGreen, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Bab 5: Mekanisme Pertahanan Tubuh Non-Spesifik & Spesifik",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color(0xFF14532D)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Sistem kekebalan tubuh (imunitas) berfungsi mengenali, merespon, dan menetralisir patogen asing seperti bakteri, jamur, serta virus sebelum menimbulkan kerusakan jaringan seluler.",
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        color = Color(0xFF374151)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Poin-poin Pelajaran Biologi
            val biologyPoints = listOf(
                "Lini Pertama: Pertahanan Fisik & Kimiawi (Kulit, lapisan mukosa gusi dan mulut, cairan saliva, asam lambung, serta lisozim).",
                "Lini Kedua: Pertahanan Internal Non-Spesifik (Fagositosis oleh makrofag, sel Natural Killer, dan respon inflamasi lokal).",
                "Lini Ketiga: Pertahanan Adaptif Spesifik (Limfosit T-Helper pembawa reseptor CD4 dan Limfosit B penghasil Antibodi spesifik).",
                "Sintesis Antibodi: Tubuh membutuhkan waktu beberapa minggu (fase laten imunologis) untuk membentuk antibodi spesifik dalam jumlah optimal setelah terpapar antigen baru."
            )

            biologyPoints.forEachIndexed { idx, point ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = DecoyGreen,
                            modifier = Modifier.size(16.dp).padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = point,
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            color = Color(0xFF1F2937)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Catatan Guru / Buku Catatan
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "💡 Catatan Mandiri Siswa:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color(0xFF92400E)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Pastikan memahami perbedaan peran Limfosit CD4 dan peran antibodi untuk persiapan asesmen sumatif tengah semester.",
                        fontSize = 11.sp,
                        color = Color(0xFF78350F)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Tombol Navigasi Penyamaran (Terlihat seperti tombol bab selanjutnya, namun membuka dialog PIN)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Halaman 1 dari 4 (Materi Daring)",
                    fontSize = 11.sp,
                    color = Color(0xFF6B7280)
                )

                Button(
                    onClick = { showPinDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = DecoyGreen),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("btn_decoy_resume")
                ) {
                    Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Lanjutkan Sesi", fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    // Dialog Verifikasi PIN untuk keluar dari Mode Samaran
    if (showPinDialog) {
        AlertDialog(
            onDismissRequest = {
                showPinDialog = false
                isPinError = false
                enteredPin = ""
            },
            title = {
                Text("Verifikasi Keamanan Layar", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            },
            text = {
                Column {
                    Text(
                        text = "Masukkan PIN 4-digit Anda untuk membuka kembali antarmuka penelitian secara privat.",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = enteredPin,
                        onValueChange = {
                            if (it.length <= 6) {
                                enteredPin = it
                                isPinError = false
                            }
                        },
                        label = { Text("PIN Keamanan (Default: 1234)") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        visualTransformation = PasswordVisualTransformation(),
                        isError = isPinError,
                        modifier = Modifier.fillMaxWidth().testTag("input_decoy_pin")
                    )
                    if (isPinError) {
                        Text(
                            text = "PIN salah. Silakan coba lagi.",
                            color = Color.Red,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val success = onUnlockWithPin(enteredPin)
                        if (success) {
                            showPinDialog = false
                            enteredPin = ""
                        } else {
                            isPinError = true
                        }
                    },
                    modifier = Modifier.testTag("btn_confirm_decoy_pin")
                ) {
                    Text("Buka Sesi")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPinDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }
}
