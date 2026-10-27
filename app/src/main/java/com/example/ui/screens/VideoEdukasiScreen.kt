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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ClosedCaption
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ResearchData
import com.example.data.VideoLesson
import com.example.viewmodel.AppUiState

@Composable
fun VideoEdukasiScreen(
    uiState: AppUiState,
    onSelectVideo: (String) -> Unit,
    onTogglePlay: () -> Unit,
    onAnswerQuiz: (String, Int) -> Unit,
    onSubmitQuiz: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isMuted by remember { mutableStateOf(true) } // Muted-by-default for privacy!
    var showSubtitle by remember { mutableStateOf(true) }

    val activeLesson: VideoLesson = ResearchData.videoLessons.find { it.id == uiState.activeVideoId }
        ?: ResearchData.videoLessons[0]

    // Cari slide aktif berdasarkan videoCurrentSeconds
    val currentSlide = activeLesson.slides.lastOrNull { uiState.videoCurrentSeconds >= it.secondTimestamp }
        ?: activeLesson.slides[0]

    val isVideoCompleted = uiState.videoCompletedMap[activeLesson.id] == true
    val isQuizSubmitted = uiState.videoQuizSubmitted[activeLesson.id] == true

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Discreet Media Session Banner
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
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
                    text = "Discreet Media Session: \"Modul Belajar Mandiri SMA\"",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Canvas Player Animasi Interaktif Ramah Privasi
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)), // Cinema dark slate
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Background visual gradient & graphic cue
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0xFF0369A1).copy(alpha = 0.5f), Color(0xFF0F172A))
                            )
                        )
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = currentSlide.headline,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF38BDF8)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White.copy(alpha = 0.1f))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "⏱ Detik ${uiState.videoCurrentSeconds} / ${activeLesson.durationSeconds}s",
                                fontSize = 11.sp,
                                color = Color.White
                            )
                        }
                    }
                }

                // Subtitle Bahasa Indonesia Besar Sinkron Detik Demi Detik
                if (showSubtitle) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 44.dp, start = 12.dp, end = 12.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Black.copy(alpha = 0.85f))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = currentSlide.subtitleText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFFEF08A), // High contrast yellow subtitle
                            lineHeight = 16.sp
                        )
                    }
                }

                // Control Bar (Bawah Player)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(Color.Black.copy(alpha = 0.7f))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Tombol Play / Pause
                    IconButton(
                        onClick = onTogglePlay,
                        modifier = Modifier.size(32.dp).testTag("btn_play_pause_video")
                    ) {
                        Icon(
                            imageVector = if (uiState.videoIsPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (uiState.videoIsPlaying) "Jeda" else "Putar",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Progress Slider Bar
                    LinearProgressIndicator(
                        progress = { uiState.videoCurrentSeconds.toFloat() / activeLesson.durationSeconds },
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp)
                            .padding(horizontal = 8.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = Color(0xFF38BDF8),
                        trackColor = Color.White.copy(alpha = 0.3f)
                    )

                    // Subtitle Toggle
                    IconButton(
                        onClick = { showSubtitle = !showSubtitle },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ClosedCaption,
                            contentDescription = "Subtitle",
                            tint = if (showSubtitle) Color(0xFF38BDF8) else Color.Gray,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Muted-by-Default Toggle
                    IconButton(
                        onClick = { isMuted = !isMuted },
                        modifier = Modifier.size(32.dp).testTag("btn_toggle_mute")
                    ) {
                        Icon(
                            imageVector = if (isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                            contentDescription = if (isMuted) "Bisu (Aman Privasi)" else "Suara Aktif",
                            tint = if (isMuted) Color(0xFFF87171) else Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Info Video Aktif
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = activeLesson.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = activeLesson.category + " • Durasi: ${activeLesson.durationSeconds} detik",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (isVideoCompleted) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFD1FAE5)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF059669), modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Selesai Ditonton", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF065F46))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = activeLesson.summary,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Daftar 4 Video Edukasi
        Text(
            text = "Daftar 4 Video Pembelajaran:",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        ResearchData.videoLessons.forEach { lesson ->
            val isSelected = lesson.id == activeLesson.id
            val isLessonDone = uiState.videoCompletedMap[lesson.id] == true

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { onSelectVideo(lesson.id) }
                    .testTag("item_video_${lesson.id}"),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                    else MaterialTheme.colorScheme.surface
                ),
                border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isLessonDone) Icons.Default.Check else Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = lesson.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                        if (lesson.isPriorityRule2A) {
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "⭐ Rekomendasi Prioritas Aturan 2A (Penguatan Efikasi Diri)",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFD97706)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Kuis Kilat Evaluasi Pemahaman Video
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Quiz, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Kuis Kilat Pemahaman: ${activeLesson.title}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
                Text(
                    text = "Jawab 3 pertanyaan kilat berikut untuk menguji pemahaman Anda:",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                activeLesson.quizQuestions.forEachIndexed { qIdx, q ->
                    Text(
                        text = "${qIdx + 1}. ${q.question}",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    val selectedAns = uiState.videoQuizAnswers[q.id]

                    q.options.forEachIndexed { optIdx, optText ->
                        val isSelected = selectedAns == optIdx
                        val isCorrect = optIdx == q.correctIndex

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (!isQuizSubmitted) onAnswerQuiz(q.id, optIdx)
                                }
                                .padding(vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { if (!isQuizSubmitted) onAnswerQuiz(q.id, optIdx) }
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = optText,
                                fontSize = 11.sp,
                                color = if (isQuizSubmitted && isCorrect) Color(0xFF059669) else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    // Tampilkan penjelasan jika sudah disubmit
                    if (isQuizSubmitted) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = "Penjelasan: ${q.explanation}",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { onSubmitQuiz(activeLesson.id) },
                    enabled = !isQuizSubmitted,
                    modifier = Modifier.fillMaxWidth().testTag("btn_submit_video_quiz")
                ) {
                    Text(
                        text = if (isQuizSubmitted) "Kuis Sudah Dinilai ✓" else "Kirim Jawaban Kuis Kilat",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
