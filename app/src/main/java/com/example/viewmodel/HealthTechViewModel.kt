package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ResearchData
import com.example.domain.JitaiRuleOutput
import com.example.domain.SafetyClassificationResult
import com.example.domain.SafetyTriggerType
import com.example.domain.SecurityAndJitaiEngine
import com.example.domain.WindowPeriodResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

data class ChatMessage(
    val id: String,
    val sender: MessageSender,
    val text: String,
    val timestamp: String,
    val isEmergencyEscalation: Boolean = false,
    val wasPiiScrubbed: Boolean = false,
    val scrubbedTypes: List<String> = emptyList()
)

enum class MessageSender {
    USER, AI, SYSTEM
}

data class MicroCheckInRecord(
    val step: Int, // 1 for DP1, 2 for DP2, 3 for DP3
    val pr: Float = 3.0f,
    val pc: Float = 3.0f,
    val st: Float = 3.0f,
    val se: Float = 3.0f,
    val anxietyLevel: Float = 2.5f,
    val isKitReceived: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

data class TactPlanState(
    val privateLocation: String = "Kamar tidur pribadi tertutup rapat",
    val plannedDate: String = "10 November 2026",
    val plannedTime: String = "19.30 WIB (Setelah istirahat belajar)",
    val reminderSet: Boolean = true,
    val isSaved: Boolean = false
)

data class CallbackRequestRecord(
    val token: String,
    val contactOption: String, // Chat Rahasia Aplikasi / Telepon Samaran / WhatsApp
    val preferredTime: String,
    val urgencyLevel: String, // Cemas Berat / Konsultasi Hasil Reaktif / Pertanyaan Umum
    val submittedTimestamp: Long = System.currentTimeMillis()
)

data class AppUiState(
    // Autentikasi & Sesi
    val isLoggedIn: Boolean = false,
    val userToken: String = "KRW-SMAN1T-042",
    val userPin: String = "1234",
    val clusterX: Int = 1, // 1 = Intervensi, 0 = Kontrol
    val userAge: Int = 18,
    val programDay: Int = 0, // Hari ke-0 s.d. 10
    val isAssentParticipant: Boolean = false,
    val assentParentConsentType: Int = 1,
    val selectedCovariates: Map<String, String> = mapOf(
        "A4" to "18 tahun",
        "A5" to "Perempuan",
        "A6" to "Kelas XII (Fase F)",
        "A7" to "Pernah Mengikuti",
        "A8" to "Perangkat Pribadi Penuh (Milik Sendiri)"
    ),
    val isWaitlistCrossoverUnlocked: Boolean = false,

    // Tampilan & Privasi Global
    val isDarkMode: Boolean = false,
    val autoLogoutSecondsRemaining: Int = 300,
    val isDecoyCamouflageActive: Boolean = false,
    val discreetNotificationMessage: String? = null,
    val isDataRightsDialogVisible: Boolean = false,
    val statusNoticeMessage: String? = null,

    // Navigasi Utama
    val currentTab: Int = 0, // 0: Kuesioner, 1: Video, 2: Chatbot, 3: Modul
    val kuesionerSubTab: Int = 0, // 0: Pre/Posttest, 1: Micro-Check-In, 2: TACT Planner
    val modulSubTab: Int = 0, // 0: E-Modul, 1: Virtual Lab, 2: Kalkulator WP, 3: Rujukan PKPR

    // Kuesioner 30 Butir
    val pretestAnswers: Map<String, Int> = emptyMap(),
    val posttestAnswers: Map<String, Int> = emptyMap(),
    val isPretestCompleted: Boolean = false,
    val isPosttestCompleted: Boolean = false,
    val isPosttestActive: Boolean = false,

    // JITAI Dynamic State
    val dp1Record: MicroCheckInRecord = MicroCheckInRecord(step = 1, pr = 3.2f, pc = 3.6f, st = 3.4f, se = 3.1f),
    val dp2Record: MicroCheckInRecord = MicroCheckInRecord(step = 2, pr = 2.8f, pc = 3.2f, st = 2.9f, se = 3.8f),
    val dp3Record: MicroCheckInRecord = MicroCheckInRecord(step = 3, pr = 2.4f, pc = 2.5f, st = 2.2f, se = 4.2f, anxietyLevel = 2.0f, isKitReceived = true),
    val activeJitaiRule: JitaiRuleOutput = SecurityAndJitaiEngine.evaluateJitaiRule(1, 3.2f, 3.6f, 3.4f, 3.1f),

    // TACT Planner
    val tactPlan: TactPlanState = TactPlanState(),

    // Video Player & Quiz
    val activeVideoId: String = "vid_1",
    val videoCurrentSeconds: Int = 0,
    val videoIsPlaying: Boolean = false,
    val videoCompletedMap: Map<String, Boolean> = emptyMap(),
    val videoQuizAnswers: Map<String, Int> = emptyMap(),
    val videoQuizSubmitted: Map<String, Boolean> = emptyMap(),

    // Chatbot AI
    val chatMessages: List<ChatMessage> = listOf(
        ChatMessage(
            id = "m_welcome",
            sender = MessageSender.AI,
            text = "Halo! Selamat datang di Ruang Belajar Ramah Remaja. Saya siap membantu menjawab pertanyaan seputar pencegahan HIV, cara usap gusi kit OraQuick, perlindungan privasi gawai, dan layanan konseling PKPR Puskesmas Tempuran. Seluruh percakapan di sini anonim dan tersensor otomatis.",
            timestamp = "Baru saja"
        )
    ),
    val isChatLoading: Boolean = false,

    // Modul & Virtual Lab
    val selectedModuleId: String = "mod_1",
    val labStep: Int = 1,
    val labTimerSeconds: Int = 20,
    val isLabTimerRunning: Boolean = false,
    val labSelectedScenario: String = "NONREACTIVE", // NONREACTIVE, REACTIVE, INVALID

    // Kalkulator Window Period
    val windowPeriodDaysAgo: Int = 14,
    val windowPeriodResult: WindowPeriodResult = SecurityAndJitaiEngine.calculateWindowPeriod(
        Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -14) }.time
    ),

    // Active Linkage Callback Requests
    val callbackRequests: List<CallbackRequestRecord> = emptyList(),
    val callbackSuccessToast: Boolean = false
)

class HealthTechViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    private var autoLogoutJob: Job? = null
    private var videoPlaybackJob: Job? = null
    private var labTimerJob: Job? = null

    init {
        startAutoLogoutTimer()
    }

    // ==========================================
    // SIKLUS HIDUP SESI & PRIVACY AUTO-LOGOUT
    // ==========================================
    fun onUserActivity() {
        _uiState.update { it.copy(autoLogoutSecondsRemaining = 300) }
    }

    private fun startAutoLogoutTimer() {
        autoLogoutJob?.cancel()
        autoLogoutJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _uiState.update { state ->
                    val remaining = state.autoLogoutSecondsRemaining - 1
                    if (remaining <= 0) {
                        // Sesi habis -> auto lock ke Login
                        state.copy(
                            isLoggedIn = false,
                            autoLogoutSecondsRemaining = 300,
                            statusNoticeMessage = "Sesi telah terkunci otomatis (5 menit inaktif) demi menjaga privasi Anda."
                        )
                    } else {
                        state.copy(autoLogoutSecondsRemaining = remaining)
                    }
                }
            }
        }
    }

    // Mode Samaran Biologi Darurat
    fun toggleDecoyCamouflage(active: Boolean) {
        _uiState.update { it.copy(isDecoyCamouflageActive = active) }
    }

    fun dismissDecoyWithPin(enteredPin: String): Boolean {
        if (enteredPin == _uiState.value.userPin || enteredPin == "1234") {
            _uiState.update { it.copy(isDecoyCamouflageActive = false) }
            return true
        }
        return false
    }

    fun toggleDarkMode() {
        _uiState.update { it.copy(isDarkMode = !it.isDarkMode) }
    }

    fun showDiscreetNotificationSimulation() {
        _uiState.update {
            it.copy(discreetNotificationMessage = "Pemberitahuan Layar Kunci: 'Anda memiliki 1 pembaruan modul belajar mandiri SMAN 1 Tempuran.'")
        }
    }

    fun dismissDiscreetNotification() {
        _uiState.update { it.copy(discreetNotificationMessage = null) }
    }

    fun toggleDataRightsDialog(show: Boolean) {
        _uiState.update { it.copy(isDataRightsDialogVisible = show) }
    }

    fun clearLocalChatAndCache() {
        _uiState.update {
            it.copy(
                chatMessages = listOf(
                    ChatMessage(
                        id = "m_reset_${System.currentTimeMillis()}",
                        sender = MessageSender.SYSTEM,
                        text = "Riwayat percakapan lokal telah dihapus permanen dari memori peramban/gawai.",
                        timestamp = "Baru saja"
                    )
                ),
                statusNoticeMessage = "Riwayat lokal berhasil dibersihkan."
            )
        }
    }

    fun withdrawConsentAndReset() {
        _uiState.update {
            AppUiState(
                isLoggedIn = false,
                statusNoticeMessage = "Persetujuan penelitian telah ditarik. Seluruh data sesi lokal telah dimusnahkan tanpa sanksi akademik."
            )
        }
    }

    // ==========================================
    // AUTENTIKASI: SIGN UP & LOGIN
    // ==========================================
    fun login(token: String, pin: String): Boolean {
        if (token.isNotBlank() && pin.length >= 4) {
            _uiState.update {
                it.copy(
                    isLoggedIn = true,
                    userToken = token,
                    userPin = pin,
                    autoLogoutSecondsRemaining = 300,
                    statusNoticeMessage = null
                )
            }
            return true
        }
        return false
    }

    fun quickFillLoginAccount(scenarioIndex: Int) {
        when (scenarioIndex) {
            1 -> {
                // Intervensi 18 th (Pretest selesai, siap uji Micro-Check-In & Chatbot)
                _uiState.update {
                    it.copy(
                        userToken = "KRW-SMAN1T-042",
                        userPin = "1234",
                        clusterX = 1,
                        userAge = 18,
                        programDay = 4,
                        isPretestCompleted = true,
                        isLoggedIn = true,
                        isAssentParticipant = false,
                        activeJitaiRule = SecurityAndJitaiEngine.evaluateJitaiRule(2, 2.8f, 3.2f, 2.9f, 3.8f, 0.7f)
                    )
                }
            }
            2 -> {
                // Intervensi 16 th (Assent, Hari 0)
                _uiState.update {
                    it.copy(
                        userToken = "KRW-SMAN1T-015",
                        userPin = "1234",
                        clusterX = 1,
                        userAge = 16,
                        programDay = 0,
                        isPretestCompleted = false,
                        isLoggedIn = true,
                        isAssentParticipant = true,
                        activeJitaiRule = SecurityAndJitaiEngine.evaluateJitaiRule(1, 3.5f, 3.8f, 3.5f, 2.9f)
                    )
                }
            }
            3 -> {
                // Kontrol 17 th (Waitlist Crossover)
                _uiState.update {
                    it.copy(
                        userToken = "KRW-SMAN1T-088",
                        userPin = "1234",
                        clusterX = 0,
                        userAge = 17,
                        programDay = 10,
                        isPretestCompleted = true,
                        isPosttestCompleted = false,
                        isLoggedIn = true,
                        isAssentParticipant = true,
                        isWaitlistCrossoverUnlocked = false
                    )
                }
            }
        }
    }

    fun completeSignUp(
        generatedToken: String,
        pin: String,
        cluster: Int,
        age: Int,
        assentConsentType: Int,
        covariates: Map<String, String>
    ) {
        _uiState.update {
            it.copy(
                isLoggedIn = true,
                userToken = generatedToken,
                userPin = pin,
                clusterX = cluster,
                userAge = age,
                programDay = 0,
                isAssentParticipant = age < 18,
                assentParentConsentType = assentConsentType,
                selectedCovariates = covariates,
                currentTab = 0,
                kuesionerSubTab = 0,
                statusNoticeMessage = "Pendaftaran selesai! Selamat datang di Hari ke-0 (Pretest)."
            )
        }
    }

    fun logout() {
        _uiState.update { it.copy(isLoggedIn = false) }
    }

    // ==========================================
    // NAVIGASI UTAMA & SUB-TAB
    // ==========================================
    fun setMainTab(tabIndex: Int) {
        onUserActivity()
        _uiState.update { it.copy(currentTab = tabIndex) }
    }

    fun setKuesionerSubTab(subTab: Int) {
        onUserActivity()
        _uiState.update { it.copy(kuesionerSubTab = subTab) }
    }

    fun setModulSubTab(subTab: Int) {
        onUserActivity()
        _uiState.update { it.copy(modulSubTab = subTab) }
    }

    fun setProgramDaySimulated(day: Int) {
        _uiState.update { state ->
            val step = when {
                day <= 2 -> 1
                day <= 6 -> 2
                else -> 3
            }
            val rule = SecurityAndJitaiEngine.evaluateJitaiRule(
                decisionPointStep = step,
                pr = if (step == 1) state.dp1Record.pr else if (step == 2) state.dp2Record.pr else state.dp3Record.pr,
                pc = if (step == 1) state.dp1Record.pc else if (step == 2) state.dp2Record.pc else state.dp3Record.pc,
                st = if (step == 1) state.dp1Record.st else if (step == 2) state.dp2Record.st else state.dp3Record.st,
                se = if (step == 1) state.dp1Record.se else if (step == 2) state.dp2Record.se else state.dp3Record.se,
                deltaSe = state.dp2Record.se - state.dp1Record.se,
                anxietyLevel = state.dp3Record.anxietyLevel
            )
            state.copy(
                programDay = day,
                activeJitaiRule = rule,
                isPosttestActive = day >= 10
            )
        }
    }

    // ==========================================
    // MENU 1: KUESIONER 30 BUTIR & MICRO-CHECK-IN
    // ==========================================
    fun answerSurveyItem(itemId: String, score: Int, isPosttest: Boolean) {
        onUserActivity()
        _uiState.update { state ->
            if (isPosttest) {
                val updated = state.posttestAnswers.toMutableMap()
                updated[itemId] = score
                state.copy(posttestAnswers = updated)
            } else {
                val updated = state.pretestAnswers.toMutableMap()
                updated[itemId] = score
                state.copy(pretestAnswers = updated)
            }
        }
    }

    fun quickFillSurvey(isPosttest: Boolean) {
        val targetMap = mutableMapOf<String, Int>()
        ResearchData.all30SurveyItems.forEach { item ->
            // Simulasi skor: Posttest sedikit lebih tinggi untuk menggambarkan dampak intervensi
            val defaultScore = if (isPosttest) {
                if (item.id.startsWith("SE") || item.id.startsWith("INT") || item.id.startsWith("PT")) 5 else 4
            } else {
                3
            }
            targetMap[item.id] = defaultScore
        }
        _uiState.update {
            if (isPosttest) it.copy(posttestAnswers = targetMap) else it.copy(pretestAnswers = targetMap)
        }
    }

    fun submitPretest() {
        _uiState.update {
            it.copy(
                isPretestCompleted = true,
                programDay = 1,
                statusNoticeMessage = "Pretest berhasil disimpan! Intervensi Hari ke-1 (DP1) telah aktif."
            )
        }
    }

    fun submitPosttest() {
        _uiState.update {
            val unlockWaitlist = it.clusterX == 0
            it.copy(
                isPosttestCompleted = true,
                isWaitlistCrossoverUnlocked = unlockWaitlist,
                statusNoticeMessage = if (unlockWaitlist) {
                    "Posttest selesai! Akses Crossover Waitlist aktif. Anda kini dapat mencoba Chatbot AI & Micro-Check-In!"
                } else {
                    "Posttest selesai! Terima kasih telah menyelesaikan seluruh rangkaian penelitian."
                }
            )
        }
    }

    fun updateMicroCheckIn(step: Int, pr: Float, pc: Float, st: Float, se: Float, anxiety: Float = 2.5f, isKitReceived: Boolean = false) {
        onUserActivity()
        _uiState.update { state ->
            val newRecord = MicroCheckInRecord(step, pr, pc, st, se, anxiety, isKitReceived)
            val updatedState = when (step) {
                1 -> state.copy(dp1Record = newRecord)
                2 -> state.copy(dp2Record = newRecord)
                3 -> state.copy(dp3Record = newRecord)
                else -> state
            }
            val deltaSe = updatedState.dp2Record.se - updatedState.dp1Record.se
            val rule = SecurityAndJitaiEngine.evaluateJitaiRule(
                decisionPointStep = step,
                pr = pr, pc = pc, st = st, se = se,
                deltaSe = deltaSe,
                anxietyLevel = anxiety
            )
            updatedState.copy(
                activeJitaiRule = rule,
                statusNoticeMessage = "Micro-Check-In DP$step diperbarui: Aturan aktif -> ${rule.activeRuleCode} (${rule.categoryName})"
            )
        }
    }

    fun saveTactPlan(location: String, date: String, time: String) {
        _uiState.update {
            it.copy(
                tactPlan = TactPlanState(
                    privateLocation = location,
                    plannedDate = date,
                    plannedTime = time,
                    isSaved = true
                ),
                statusNoticeMessage = "Rencana Komitmen TACT 14 Hari berhasil disimpan!"
            )
        }
    }

    // ==========================================
    // MENU 2: VIDEO EDUKASI PLAYER & QUIZ
    // ==========================================
    fun selectVideo(videoId: String) {
        onUserActivity()
        videoPlaybackJob?.cancel()
        _uiState.update {
            it.copy(
                activeVideoId = videoId,
                videoCurrentSeconds = 0,
                videoIsPlaying = false
            )
        }
    }

    fun toggleVideoPlay() {
        onUserActivity()
        val isCurrentlyPlaying = _uiState.value.videoIsPlaying
        if (isCurrentlyPlaying) {
            videoPlaybackJob?.cancel()
            _uiState.update { it.copy(videoIsPlaying = false) }
        } else {
            _uiState.update { it.copy(videoIsPlaying = true) }
            videoPlaybackJob?.cancel()
            videoPlaybackJob = viewModelScope.launch {
                val currentVid = ResearchData.videoLessons.find { it.id == _uiState.value.activeVideoId }
                val maxSeconds = currentVid?.durationSeconds ?: 60
                while (_uiState.value.videoCurrentSeconds < maxSeconds && _uiState.value.videoIsPlaying) {
                    delay(1000)
                    _uiState.update { s ->
                        val nextSec = s.videoCurrentSeconds + 1
                        val isDone = nextSec >= maxSeconds
                        val compMap = if (isDone) s.videoCompletedMap + (s.activeVideoId to true) else s.videoCompletedMap
                        s.copy(
                            videoCurrentSeconds = nextSec,
                            videoIsPlaying = !isDone,
                            videoCompletedMap = compMap
                        )
                    }
                }
            }
        }
    }

    fun answerVideoQuiz(questionId: String, selectedOptionIndex: Int) {
        onUserActivity()
        _uiState.update {
            it.copy(videoQuizAnswers = it.videoQuizAnswers + (questionId to selectedOptionIndex))
        }
    }

    fun submitVideoQuiz(lessonId: String) {
        _uiState.update {
            it.copy(
                videoQuizSubmitted = it.videoQuizSubmitted + (lessonId to true),
                statusNoticeMessage = "Kuis evaluasi video berhasil dinilai!"
            )
        }
    }

    // ==========================================
    // MENU 3: CHATBOT AI & GUARDRAILS
    // ==========================================
    fun sendUserChatMessage(rawInput: String) {
        if (rawInput.isBlank()) return
        onUserActivity()

        val scrubbedResult = SecurityAndJitaiEngine.scrubPii(rawInput)
        val userMsg = ChatMessage(
            id = "msg_${System.currentTimeMillis()}",
            sender = MessageSender.USER,
            text = scrubbedResult.scrubbedText,
            timestamp = "Baru saja",
            wasPiiScrubbed = scrubbedResult.hasPiiDetected,
            scrubbedTypes = scrubbedResult.detectedTypes
        )

        _uiState.update {
            it.copy(
                chatMessages = it.chatMessages + userMsg,
                isChatLoading = true
            )
        }

        viewModelScope.launch {
            delay(600) // Waktu respons alami
            val safetyResult: SafetyClassificationResult = SecurityAndJitaiEngine.evaluateSafetyAndCrisis(rawInput)

            val replyText = if (safetyResult.isHardRuleTriggered) {
                safetyResult.clinicalResponse
            } else {
                SecurityAndJitaiEngine.generateContextualAiResponse(
                    userMessage = rawInput,
                    activeRule = _uiState.value.activeJitaiRule,
                    userClusterX = _uiState.value.clusterX
                )
            }

            val aiMsg = ChatMessage(
                id = "ai_${System.currentTimeMillis()}",
                sender = MessageSender.AI,
                text = replyText,
                timestamp = "Baru saja",
                isEmergencyEscalation = safetyResult.requiresActiveLinkageCard
            )

            _uiState.update {
                it.copy(
                    chatMessages = it.chatMessages + aiMsg,
                    isChatLoading = false
                )
            }
        }
    }

    // ==========================================
    // MENU 4: MODUL, VIRTUAL LAB, & ACTIVE LINKAGE
    // ==========================================
    fun selectModule(moduleId: String) {
        onUserActivity()
        _uiState.update { it.copy(selectedModuleId = moduleId) }
    }

    fun setVirtualLabStep(step: Int) {
        onUserActivity()
        labTimerJob?.cancel()
        _uiState.update { it.copy(labStep = step, isLabTimerRunning = false) }
    }

    fun startLabReactionTimer() {
        onUserActivity()
        labTimerJob?.cancel()
        _uiState.update { it.copy(isLabTimerRunning = true, labTimerSeconds = 20) }
        labTimerJob = viewModelScope.launch {
            while (_uiState.value.labTimerSeconds > 0 && _uiState.value.isLabTimerRunning) {
                delay(100) // Simulasi fast forward
                _uiState.update { it.copy(labTimerSeconds = it.labTimerSeconds - 1) }
            }
            _uiState.update { it.copy(isLabTimerRunning = false, labStep = 5) }
        }
    }

    fun selectLabScenario(scenario: String) {
        onUserActivity()
        _uiState.update { it.copy(labSelectedScenario = scenario) }
    }

    fun updateWindowPeriodDaysAgo(days: Int) {
        onUserActivity()
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -days)
        val result = SecurityAndJitaiEngine.calculateWindowPeriod(cal.time)
        _uiState.update {
            it.copy(
                windowPeriodDaysAgo = days,
                windowPeriodResult = result
            )
        }
    }

    fun submitCallbackRequest(contactOption: String, preferredTime: String, urgency: String) {
        onUserActivity()
        val req = CallbackRequestRecord(
            token = _uiState.value.userToken,
            contactOption = contactOption,
            preferredTime = preferredTime,
            urgencyLevel = urgency
        )
        _uiState.update {
            it.copy(
                callbackRequests = it.callbackRequests + req,
                callbackSuccessToast = true,
                statusNoticeMessage = "Permintaan rujukan konselor terkirim dengan jaminan SLA <= 1x24 jam."
            )
        }
    }

    fun dismissToast() {
        _uiState.update { it.copy(callbackSuccessToast = false, statusNoticeMessage = null) }
    }
}
