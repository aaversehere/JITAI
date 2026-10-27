package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.DataRightsDialog
import com.example.ui.components.DecoyBiologiScreen
import com.example.ui.components.GlobalPrivacyBar
import com.example.ui.components.Navbar
import com.example.ui.screens.AuthPortalScreen
import com.example.ui.screens.ChatbotAiScreen
import com.example.ui.screens.KuesionerScreen
import com.example.ui.screens.ModulScreen
import com.example.ui.screens.VideoEdukasiScreen
import com.example.ui.theme.HealthTechTheme
import com.example.viewmodel.HealthTechViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: HealthTechViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            HealthTechTheme(darkTheme = uiState.isDarkMode) {
                // Tangani Mode Samaran Biologi Darurat (Hapus Cepat Riwayat Layar)
                if (uiState.isDecoyCamouflageActive) {
                    DecoyBiologiScreen(
                        onUnlockWithPin = { pin -> viewModel.dismissDecoyWithPin(pin) }
                    )
                } else if (!uiState.isLoggedIn) {
                    // Layar Autentikasi: Sign Up & Login
                    AuthPortalScreen(
                        onLoginSuccess = { token, pin -> viewModel.login(token, pin) },
                        onQuickFill = { scenario -> viewModel.quickFillLoginAccount(scenario) },
                        onCompleteSignUp = { token, pin, cluster, age, assentType, covariates ->
                            viewModel.completeSignUp(token, pin, cluster, age, assentType, covariates)
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .statusBarsPadding()
                    )
                } else {
                    // Layar Utama Peserta Terotentikasi
                    MainResearchAppContent(
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun MainResearchAppContent(
    viewModel: HealthTechViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.statusNoticeMessage) {
        uiState.statusNoticeMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.dismissToast()
        }
    }

    // Back Handler: kembali ke tab Kuesioner (Home) jika di sub-tab atau sub-menu
    BackHandler(enabled = uiState.currentTab != 0) {
        viewModel.setMainTab(0)
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding(),
        topBar = {
            Column {
                GlobalPrivacyBar(
                    uiState = uiState,
                    onEmergencyCamouflageClick = { viewModel.toggleDecoyCamouflage(true) },
                    onSimulateDiscreetNotification = { viewModel.showDiscreetNotificationSimulation() },
                    onToggleDataRights = { viewModel.toggleDataRightsDialog(true) },
                    onToggleDarkMode = { viewModel.toggleDarkMode() }
                )

                // Floating Demo Bar untuk Simulasi Hari Program (Hari 0 s.d. 10)
                ProgramDaySimulationBar(
                    currentDay = uiState.programDay,
                    onSelectDay = { day -> viewModel.setProgramDaySimulated(day) }
                )
            }
        },
        bottomBar = {
            Navbar(
                selectedTabIndex = uiState.currentTab,
                onTabSelected = { tabIndex -> viewModel.setMainTab(tabIndex) }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState.currentTab) {
                0 -> KuesionerScreen(
                    uiState = uiState,
                    selectedSubTab = uiState.kuesionerSubTab,
                    onSubTabSelected = { viewModel.setKuesionerSubTab(it) },
                    onAnswerItem = { id, score, isPost -> viewModel.answerSurveyItem(id, score, isPost) },
                    onQuickFill = { isPost -> viewModel.quickFillSurvey(isPost) },
                    onSubmitPretest = { viewModel.submitPretest() },
                    onSubmitPosttest = { viewModel.submitPosttest() },
                    onUpdateMicroCheckIn = { step, pr, pc, st, se, anx, kit ->
                        viewModel.updateMicroCheckIn(step, pr, pc, st, se, anx, kit)
                    },
                    onSaveTactPlan = { loc, dt, tm -> viewModel.saveTactPlan(loc, dt, tm) },
                    onNavigateToRoute = { route ->
                        when (route) {
                            "video" -> viewModel.setMainTab(1)
                            "chatbot" -> viewModel.setMainTab(2)
                            "modul" -> viewModel.setMainTab(3)
                            else -> viewModel.setMainTab(0)
                        }
                    }
                )
                1 -> VideoEdukasiScreen(
                    uiState = uiState,
                    onSelectVideo = { viewModel.selectVideo(it) },
                    onTogglePlay = { viewModel.toggleVideoPlay() },
                    onAnswerQuiz = { qId, ansIdx -> viewModel.answerVideoQuiz(qId, ansIdx) },
                    onSubmitQuiz = { lId -> viewModel.submitVideoQuiz(lId) }
                )
                2 -> ChatbotAiScreen(
                    uiState = uiState,
                    onSendMessage = { viewModel.sendUserChatMessage(it) },
                    onNavigateToModul = {
                        viewModel.setMainTab(3)
                        viewModel.setModulSubTab(3) // Buka Sub-Tab Rujukan PKPR
                    }
                )
                3 -> ModulScreen(
                    uiState = uiState,
                    selectedSubTab = uiState.modulSubTab,
                    onSubTabSelected = { viewModel.setModulSubTab(it) },
                    onSelectModule = { viewModel.selectModule(it) },
                    onSetLabStep = { viewModel.setVirtualLabStep(it) },
                    onStartLabTimer = { viewModel.startLabReactionTimer() },
                    onSelectLabScenario = { viewModel.selectLabScenario(it) },
                    onUpdateWindowPeriodDays = { viewModel.updateWindowPeriodDaysAgo(it) },
                    onSubmitCallbackRequest = { opt, tm, urg -> viewModel.submitCallbackRequest(opt, tm, urg) },
                    onDismissToast = { viewModel.dismissToast() }
                )
            }

            // Simulasi Pop-up Discreet Notification di Layar Kunci
            uiState.discreetNotificationMessage?.let { msg ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.TopCenter),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Notifications, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Pemberitahuan Sistem (Netral)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text(msg, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                        }
                        IconButton(onClick = { viewModel.dismissDiscreetNotification() }) {
                            Icon(Icons.Default.Close, contentDescription = "Tutup", modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }

    // Dialog Hak Subjek Data UU PDP
    if (uiState.isDataRightsDialogVisible) {
        DataRightsDialog(
            onDismiss = { viewModel.toggleDataRightsDialog(false) },
            onClearLocalChat = { viewModel.clearLocalChatAndCache() },
            onWithdrawConsent = { viewModel.withdrawConsentAndReset() }
        )
    }
}

@Composable
fun ProgramDaySimulationBar(
    currentDay: Int,
    onSelectDay: (Int) -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("simulation_day_bar")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "⚡ Simulasi Hari:",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            listOf(0, 1, 4, 8, 10).forEach { day ->
                val isSelected = currentDay == day
                val label = when (day) {
                    0 -> "H0 (Pre)"
                    1 -> "H1 (DP1)"
                    4 -> "H4 (DP2)"
                    8 -> "H8 (DP3)"
                    else -> "H10 (Post)"
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
                        .clickable { onSelectDay(day) }
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = label,
                        fontSize = 9.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
