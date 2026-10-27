package com.example.domain

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class PiiScrubResult(
    val scrubbedText: String,
    val hasPiiDetected: Boolean,
    val detectedTypes: List<String>
)

data class SafetyClassificationResult(
    val isHardRuleTriggered: Boolean,
    val triggerType: SafetyTriggerType,
    val clinicalResponse: String,
    val requiresActiveLinkageCard: Boolean
)

enum class SafetyTriggerType {
    NONE,
    CRISIS_SELF_HARM,
    REACTIVE_TEST_REPORT,
    INVALID_TEST_REPORT
}

data class JitaiRuleOutput(
    val decisionPoint: String,
    val activeRuleCode: String,
    val categoryName: String,
    val recommendationTitle: String,
    val recommendationSummary: String,
    val recommendedActionRoute: String,
    val targetVideoId: String? = null,
    val targetModuleId: String? = null
)

data class WindowPeriodResult(
    val exposureDateString: String,
    val windowEndString: String,
    val daysPassed: Int,
    val isWindowPeriodCompleted: Boolean,
    val daysRemaining: Int,
    val clinicalRecommendation: String
)

object SecurityAndJitaiEngine {

    // 1. PII Scrubber Otomatis
    fun scrubPii(input: String): PiiScrubResult {
        var scrubbed = input
        val detected = mutableListOf<String>()

        // Deteksi Pola No HP Indonesia (08xx atau +628xx)
        val phoneRegex = Regex("""(\+62|62|0)8[1-9][0-9]{6,11}""")
        if (phoneRegex.containsMatchIn(scrubbed)) {
            scrubbed = phoneRegex.replace(scrubbed, "[NOMOR_HP_DISENSOR]")
            detected.add("Nomor Telepon")
        }

        // Deteksi Pola NISN (10 digit angka)
        val nisnRegex = Regex("""\b\d{10}\b""")
        if (nisnRegex.containsMatchIn(scrubbed)) {
            scrubbed = nisnRegex.replace(scrubbed, "[NISN_DISENSOR]")
            detected.add("NISN")
        }

        // Deteksi Pola Email
        val emailRegex = Regex("""[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,6}""")
        if (emailRegex.containsMatchIn(scrubbed)) {
            scrubbed = emailRegex.replace(scrubbed, "[EMAIL_DISENSOR]")
            detected.add("Email")
        }

        // Deteksi Pola Nama Riil "nama saya [X]" atau "namaku [X]"
        val nameRegex = Regex("""(?i)\b(nama saya|namaku|panggil aku|nama ku)\s+([A-Za-z]{2,20}(\s+[A-Za-z]{2,20})?)""")
        if (nameRegex.containsMatchIn(scrubbed)) {
            scrubbed = nameRegex.replace(scrubbed, "$1 [NAMA_DISENSOR_PRIVASI]")
            detected.add("Identitas Nama")
        }

        return PiiScrubResult(
            scrubbedText = scrubbed,
            hasPiiDetected = detected.isNotEmpty(),
            detectedTypes = detected
        )
    }

    // 2. Pre-LLM Safety Classifier & Rule 3B Escalation (Bypass LLM)
    fun evaluateSafetyAndCrisis(input: String): SafetyClassificationResult {
        val lower = input.lowercase()

        // Evaluasi Krisis / Menyakiti Diri
        val crisisKeywords = listOf(
            "bunuh diri", "ingin mati", "mengakhiri hidup", "menyakiti diri",
            "putus asa", "tidak sanggup hidup", "mau mati", "mengakhiri hidupku"
        )
        if (crisisKeywords.any { lower.contains(it) }) {
            return SafetyClassificationResult(
                isHardRuleTriggered = true,
                triggerType = SafetyTriggerType.CRISIS_SELF_HARM,
                clinicalResponse = "Perasaan Anda saat ini sangat penting dan Anda tidak sendirian. Tolong tarik napas perlahan. Kesulitan apa pun yang Anda hadapi, ada pertolongan dan harapan nyata. Konselor ramah remaja di PKPR Puskesmas Tempuran siap mendengarkan serta mendampingi Anda sekarang juga secara rahasia dan tanpa menghakimi.",
                requiresActiveLinkageCard = true
            )
        }

        // Evaluasi Laporan Hasil Tes Reaktif (2 Garis)
        val reactiveKeywords = listOf(
            "muncul 2 garis", "dua garis", "garis c dan t", "garis t samar",
            "hasilku reaktif", "hasilku positif", "reaktif", "tesku 2 garis",
            "ada dua garis", "garis merah ada dua"
        )
        if (reactiveKeywords.any { lower.contains(it) }) {
            return SafetyClassificationResult(
                isHardRuleTriggered = true,
                triggerType = SafetyTriggerType.REACTIVE_TEST_REPORT,
                clinicalResponse = "Tarik napas dalam dan tetap tenang. Pertama, ingatlah bahwa kit HIVST cairan oral adalah instrumen skrining awal, BUKAN vonis diagnosis definitif. Garis kedua (meskipun samar) memerlukan pemeriksaan konfirmasi serial 3 reagen darah di Puskesmas Tempuran untuk memastikan kondisi tubuh yang sesungguhnya. Layanan ini GRATIS, rahasia, dan konselor PKPR siap mendampingi Anda langkah demi langkah.",
                requiresActiveLinkageCard = true
            )
        }

        // Evaluasi Laporan Hasil Invalid
        val invalidKeywords = listOf(
            "garis c tidak muncul", "tidak ada garis c", "alat invalid",
            "garisnya hilang", "tidak muncul garis sama sekali", "rusak alatnya"
        )
        if (invalidKeywords.any { lower.contains(it) }) {
            return SafetyClassificationResult(
                isHardRuleTriggered = true,
                triggerType = SafetyTriggerType.INVALID_TEST_REPORT,
                clinicalResponse = "Jika garis Kontrol (C) tidak muncul sama sekali, hasil tes dinyatakan INVALID (tidak sah). Hal ini biasanya terjadi karena cairan penyangga kurang meresap atau volume usapan tidak memadai. Jangan cemas; Anda berhak mengajukan klaim penggantian kit baru secara anonim atau datang untuk tes dengan pendampingan petugas PKPR.",
                requiresActiveLinkageCard = true
            )
        }

        return SafetyClassificationResult(
            isHardRuleTriggered = false,
            triggerType = SafetyTriggerType.NONE,
            clinicalResponse = "",
            requiresActiveLinkageCard = false
        )
    }

    // 3. Rule-Based Trigger Engine untuk Repeated Decision Points (Tabel 3.2 Proposal)
    fun evaluateJitaiRule(
        decisionPointStep: Int, // 1: Hari 1 (DP1), 2: Hari 4 (DP2), 3: Hari 8 (DP3)
        pr: Float, // Perceived Risk
        pc: Float, // Privacy Concern
        st: Float, // Stigma
        se: Float, // Self-Efficacy
        deltaSe: Float = 0f, // SE2 - SE1
        anxietyLevel: Float = 2.5f // Tingkat kecemasan DP3
    ): JitaiRuleOutput {
        return when (decisionPointStep) {
            1 -> {
                // Decision Point 1 (Hari ke-1)
                if (pc >= 3.5f || st >= 3.5f) {
                    JitaiRuleOutput(
                        decisionPoint = "DP1 (Hari ke-1)",
                        activeRuleCode = "Aturan 1A",
                        categoryName = "Privacy & Stigma Reassurance",
                        recommendationTitle = "Penguatan Kerahasiaan & Ketahanan Emosional",
                        recommendationSummary = "Tingkat kekhawatiran privasi atau stigma Anda berada pada level tinggi (>= 3.5). Sistem memprioritaskan rasa aman dengan edukasi perlindungan data dan pemutaran video kisah teman sebaya.",
                        recommendedActionRoute = "video",
                        targetVideoId = "vid_2",
                        targetModuleId = "mod_4"
                    )
                } else if (se < 3.5f) {
                    JitaiRuleOutput(
                        decisionPoint = "DP1 (Hari ke-1)",
                        activeRuleCode = "Aturan 1B",
                        categoryName = "Foundational Mastery & Education",
                        recommendationTitle = "Penguasaan Keterampilan Prosedural OraQuick",
                        recommendationSummary = "Privasi Anda dirasa aman, namun efikasi diri masih perlu ditingkatkan (< 3.5). Sistem memandu Anda mempelajari langkah demi langkah penggunaan kit cairan oral.",
                        recommendedActionRoute = "video",
                        targetVideoId = "vid_1",
                        targetModuleId = "mod_2"
                    )
                } else {
                    JitaiRuleOutput(
                        decisionPoint = "DP1 (Hari ke-1)",
                        activeRuleCode = "Aturan 1C",
                        categoryName = "Action Preparation",
                        recommendationTitle = "Persiapan Rencana Mandiri & Pemahaman Periode Jendela",
                        recommendationSummary = "Efikasi diri Anda tinggi dan privasi aman. Sistem merekomendasikan pendalaman pemahaman masa jendela 12 minggu dan persiapan waktu privat.",
                        recommendedActionRoute = "modul",
                        targetVideoId = "vid_3",
                        targetModuleId = "mod_3"
                    )
                }
            }
            2 -> {
                // Decision Point 2 (Hari ke-4)
                if (deltaSe <= 0f || se < 3.5f) {
                    JitaiRuleOutput(
                        decisionPoint = "DP2 (Hari ke-4)",
                        activeRuleCode = "Aturan 2A",
                        categoryName = "Targeted Mastery & Vicarious Modeling",
                        recommendationTitle = "Penguatan Prioritas: Modeling Teman Sebaya & Lab Virtual",
                        recommendationSummary = "Efikasi diri belum meningkat signifikan (ΔSE <= 0). Direkomendasikan menonton Video 2 (Cerita Teman Sebaya) dan mencoba Simulator Lab OraQuick 5 Langkah.",
                        recommendedActionRoute = "video",
                        targetVideoId = "vid_2",
                        targetModuleId = "mod_2"
                    )
                } else if (pc >= 3.5f) {
                    JitaiRuleOutput(
                        decisionPoint = "DP2 (Hari ke-4)",
                        activeRuleCode = "Aturan 2B",
                        categoryName = "Device Privacy Protection Guide",
                        recommendationTitle = "Panduan Proteksi Ponsel Berbagi & Mode Samaran",
                        recommendationSummary = "Kekhawatiran perangkat dipakai bersama masih terdeteksi tinggi. Pelajari panduan perlindungan gawai dan manfaatkan Mode Samaran Biologi.",
                        recommendedActionRoute = "modul",
                        targetVideoId = "vid_2",
                        targetModuleId = "mod_4"
                    )
                } else {
                    JitaiRuleOutput(
                        decisionPoint = "DP2 (Hari ke-4)",
                        activeRuleCode = "Aturan 2C",
                        categoryName = "Window Period & Kit Readiness",
                        recommendationTitle = "Kesiapan Menjelang Penerimaan Kit & Kalkulasi Waktu",
                        recommendationSummary = "Efikasi diri stabil tinggi. Pelajari kalkulator periode jendela dan pastikan Anda siap menerima kit dalam kemasan netral.",
                        recommendedActionRoute = "modul",
                        targetVideoId = "vid_3",
                        targetModuleId = "mod_3"
                    )
                }
            }
            3 -> {
                // Decision Point 3 (Hari ke-8)
                if (anxietyLevel >= 3.5f) {
                    JitaiRuleOutput(
                        decisionPoint = "DP3 (Hari ke-8)",
                        activeRuleCode = "Aturan 3A",
                        categoryName = "Affective Regulation & Assisted Option",
                        recommendationTitle = "Regulasi Kecemasan Pra-Tes & Opsi Pendampingan",
                        recommendationSummary = "Terdeteksi kecemasan menghadapi pembacaan hasil. Sistem menyediakan teknik pernapasan menenangkan dan opsi melakukan tes bersama konselor PKPR.",
                        recommendedActionRoute = "chatbot",
                        targetVideoId = "vid_2",
                        targetModuleId = "mod_5"
                    )
                } else {
                    JitaiRuleOutput(
                        decisionPoint = "DP3 (Hari ke-8)",
                        activeRuleCode = "Aturan 3C",
                        categoryName = "TACT Action Plan & Active Linkage Readiness",
                        recommendationTitle = "Finalisasi Komitmen TACT 14 Hari & Akses PKPR",
                        recommendationSummary = "Kesiapan Anda optimal. Tetapkan tanggal, jam, dan lokasi privat di Perencana TACT, serta ketahui bahwa layanan rujukan PKPR siap mendukung Anda.",
                        recommendedActionRoute = "kuesioner",
                        targetVideoId = "vid_4",
                        targetModuleId = "mod_5"
                    )
                }
            }
            else -> {
                JitaiRuleOutput(
                    decisionPoint = "DP Umum",
                    activeRuleCode = "Aturan Edukasi",
                    categoryName = "Edukasi Komprehensif",
                    recommendationTitle = "Modul Belajar Mandiri Kesehatan Remaja",
                    recommendationSummary = "Jelajahi materi dan video edukasi untuk meningkatkan pengetahuan pencegahan HIV.",
                    recommendedActionRoute = "modul"
                )
            }
        }
    }

    // 4. Lapisan 2 Generative AI Contextual Responder dengan 4 Negative Constraints
    fun generateContextualAiResponse(
        userMessage: String,
        activeRule: JitaiRuleOutput?,
        userClusterX: Int
    ): String {
        // Kontrol cluster: Kontrol (X = 0) diarahkan ke modul statis
        if (userClusterX == 0) {
            return "Halo! Anda berada dalam kelompok panduan mandiri terstandar. Silakan membaca materi komprehensif pada menu Modul Bacaan dan memutar Video Edukasi yang tersedia."
        }

        val scrubbedResult = scrubPii(userMessage)
        val text = scrubbedResult.scrubbedText.lowercase()

        // Evaluasi Safety Classifier
        val safetyResult = evaluateSafetyAndCrisis(text)
        if (safetyResult.isHardRuleTriggered) {
            return safetyResult.clinicalResponse
        }

        // 4 Negative Constraints Enforcement:
        // Constraint 1: Menolak Mendiagnosis Gejala Fisik
        val symptomWords = listOf("demam", "sariawan", "ruam", "bintik merah", "batuk", "berat badan turun", "keringat malam", "gejala hiv")
        if (symptomWords.any { text.contains(it) }) {
            return "Penting untuk dipahami bahwa gejala fisik seperti demam, sariawan, atau ruam TIDAK BISA digunakan untuk mendiagnosis apakah seseorang terinfeksi HIV atau tidak. Gejala-gejala tersebut sangat umum dan dapat disebabkan oleh infeksi biasa (seperti flu atau radang tenggorokan). Satu-satunya cara memastikan status HIV adalah melalui tes skrining darah atau cairan oral setelah melewati periode jendela (window period 12 minggu), dilanjutkan dengan tes konfirmasi resmi di Puskesmas Tempuran. Jangan mendiagnosis diri sendiri, ya."
        }

        // Constraint 2: Menolak Meresepkan Obat
        val medicationWords = listOf("obat hiv", "resep obat", "minum obat apa", "beli obat", "dosis", "antibiotik", "terapi arv")
        if (medicationWords.any { text.contains(it) }) {
            return "Sebagai asisten edukasi digital, saya tidak memiliki wewenang medis untuk meresepkan obat atau menentukan dosis terapi apa pun. Pengobatan pencegahan (seperti PrEP/PEP) atau pengobatan antiretroviral (ARV) hanya dapat ditentukan secara tepat dan aman oleh dokter di fasilitas layanan kesehatan seperti Puskesmas Tempuran setelah melalui pemeriksaan laboratorium resmi. Layanan ini tersedia gratis dan terjamin kerahasiaannya."
        }

        // Constraint 3: Menolak Topik di Luar Domain Penelitian
        val offTopicWords = listOf("pr matematika", "game", "mobile legends", "tugas biologi besok", "cuaca", "presiden", "film", "resep masakan")
        if (offTopicWords.any { text.contains(it) }) {
            return "Maaf, ruang belajar ini didedikasikan khusus untuk topik kesehatan reproduksi remaja, perlindungan privasi, serta edukasi skrining mandiri HIVST di SMAN 1 Tempuran. Ada pertanyaan seputar cara penggunaan kit cairan oral, perlindungan privasi di ponsel, periode jendela 12 minggu, atau layanan konselor PKPR yang ingin kita bahas bersama?"
        }

        // Respon Kontekstual Berdasarkan Topik Pertanyaan Ramah Remaja:
        if (text.contains("usap gusi") || text.contains("cara pakai") || text.contains("spatula") || text.contains("oraquick")) {
            return "Untuk menggunakan kit OraQuick cairan oral dengan benar:\n" +
                    "1. Puasa makan, minum air dingin, dan permen karet selama 15 menit sebelum tes.\n" +
                    "2. Buka tutup tabung buffer dan letakkan tegak lurus di dudukannya agar tidak tumpah.\n" +
                    "3. Ambil spatula dan usapkan bantalannya 1 kali sepanjang gusi luar atas, lalu balik dan usapkan 1 kali sepanjang gusi luar bawah (bukan pada lidah atau air liur biasa).\n" +
                    "4. Masukkan spatula ke tabung buffer dan tunggu selama 20 hingga maksimal 40 menit untuk membaca garis C dan T.\n" +
                    "Anda juga bisa mencoba langkah interaktifnya di menu 'Modul' -> sub-tab 'Virtual Lab'!"
        }

        if (text.contains("hp dipinjam") || text.contains("privasi") || text.contains("keluarga") || text.contains("aman")) {
            return "Kekhawatiran Anda sangat wajar, terutama jika menggunakan ponsel bersama keluarga di rumah. Sistem ini telah dirancang dengan prinsip Privacy-by-Design:\n" +
                    "• Anda menggunakan Token Pseudonim acak tanpa nama asli atau NISN.\n" +
                    "• Ada Tombol Merah 'Mode Samaran' di atas layar yang seketika mengubah tampilan menjadi materi Biologi SMA jika ada orang mendekat.\n" +
                    "• Notifikasi disamarkan dengan teks netral 'Pembaruan materi belajar mandiri'.\n" +
                    "• Riwayat chat dan sesi akan logout otomatis dalam 5 menit jika tidak ada aktivitas."
        }

        if (text.contains("periode jendela") || text.contains("window period") || text.contains("12 minggu") || text.contains("3 bulan")) {
            return "Periode jendela (Window Period) adalah waktu yang dibutuhkan tubuh untuk menghasilkan antibodi HIV dalam jumlah yang cukup agar terbaca oleh kit tes. Untuk kit cairan oral, periode jendelanya adalah 12 minggu (sekitar 84 hari). Jika Anda melakukan tes sebelum 12 minggu pasca pajanan dan hasilnya nonreaktif (1 garis), hasil tersebut belum konklusif karena antibodi mungkin belum terbentuk optimal (dikenal sebagai false reassurance). Pastikan melakukan tes ulang setelah genap 12 minggu."
        }

        if (text.contains("konselor") || text.contains("pkpr") || text.contains("puskesmas") || text.contains("tempuran")) {
            return "Layanan PKPR Puskesmas Tempuran adalah mitra resmi penelitian ini. Konselor PKPR (seperti perawat dan dokter terlatih) menyediakan ruang konsultasi khusus remaja yang ramah, tanpa menghakimi, dan 100% rahasia tanpa melapor ke sekolah atau orang tua. Anda bisa menghubungi mereka melalui formulir rahasia di menu 'Modul' -> sub-tab 'Rujukan PKPR' dengan jaminan respon maksimal 1x24 jam."
        }

        // Respon default yang disesuaikan dengan aturan aktif JITAI
        val ruleContext = activeRule?.let { " Saat ini Anda berada dalam bimbingan ${it.activeRuleCode} (${it.categoryName})." } ?: ""
        return "Terima kasih atas pertanyaan Anda.$ruleContext Ingatlah bahwa menjaga kesehatan diri adalah langkah berani dan bijak. Pastikan selalu memperhatikan waktu puasa 15 menit sebelum usap gusi, memahami masa jendela 12 minggu, dan memanfaatkan tombol Mode Samaran jika membutuhkan privasi mendadak. Ada hal lain yang ingin Anda ketahui seputar kit tes atau layanan konseling?"
    }

    // 5. Kalkulator Interaktif Periode Jendela (12 Minggu / 84 Hari)
    fun calculateWindowPeriod(exposureDate: Date): WindowPeriodResult {
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
        val exposureStr = dateFormat.format(exposureDate)

        val calendar = Calendar.getInstance()
        calendar.time = exposureDate
        calendar.add(Calendar.DAY_OF_YEAR, 84) // 12 minggu = 84 hari
        val windowEndDate = calendar.time
        val windowEndStr = dateFormat.format(windowEndDate)

        val today = Calendar.getInstance().time
        val diffMillis = today.time - exposureDate.time
        val daysPassed = (diffMillis / (1000 * 60 * 60 * 24)).toInt().coerceAtLeast(0)
        val isCompleted = daysPassed >= 84
        val remaining = (84 - daysPassed).coerceAtLeast(0)

        val recommendation = if (isCompleted) {
            "Masa jendela 12 minggu (84 hari) telah terpenuhi. Hasil tes HIVST cairan oral saat ini memiliki tingkat akurasi konklusif (sensitivitas >=99%). Jika hasilnya 1 garis (nonreaktif), Anda dinyatakan bebas risiko dari pajanan tanggal tersebut. Jika 2 garis (reaktif), segera lakukan tes konfirmasi darah di Puskesmas Tempuran."
        } else {
            "Saat ini baru berlalu $daysPassed hari dari perkiraan pajanan. Masa jendela masih tersisa $remaining hari lagi. Anda tetap dapat melakukan tes awal mandiri untuk ketenangan pikiran, tetapi jika hasilnya nonreaktif (1 garis), Anda WAJIB menjadwalkan tes ulang pada tanggal $windowEndStr guna mencegah rasa aman semu (false reassurance)."
        }

        return WindowPeriodResult(
            exposureDateString = exposureStr,
            windowEndString = windowEndStr,
            daysPassed = daysPassed,
            isWindowPeriodCompleted = isCompleted,
            daysRemaining = remaining,
            clinicalRecommendation = recommendation
        )
    }
}
