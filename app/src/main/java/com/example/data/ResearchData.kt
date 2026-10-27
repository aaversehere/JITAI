package com.example.data

data class SurveyItem(
    val id: String,
    val text: String,
    val category: String,
    val minScore: Int = 1,
    val maxScore: Int = 5
)

data class CovariateOption(
    val code: String,
    val label: String,
    val description: String,
    val options: List<String>
)

data class VideoLesson(
    val id: String,
    val title: String,
    val category: String,
    val durationSeconds: Int,
    val isPriorityRule2A: Boolean = false,
    val summary: String,
    val slides: List<VideoSlide>,
    val quizQuestions: List<VideoQuizQuestion>
)

data class VideoSlide(
    val secondTimestamp: Int,
    val headline: String,
    val subtitleText: String,
    val graphicCue: String
)

data class VideoQuizQuestion(
    val id: String,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

data class EModule(
    val id: String,
    val code: String,
    val title: String,
    val estimatedReadMinutes: Int,
    val keyPoints: List<String>,
    val fullContent: String,
    val clinicalCitation: String
)

data class ContactPKPR(
    val clinicName: String = "Layanan PKPR Puskesmas Tempuran Karawang",
    val address: String = "Jl. Raya Tempuran No. 12, Tempuran, Karawang, Jawa Barat",
    val counselorHotline: String = "0812-XXXX-PKPR",
    val operationalHours: String = "Senin - Sabtu (08.00 - 14.00 WIB)",
    val counselorSpecialist: String = "Ns. Konselor PKPR Tempuran (Terlatih & Bersertifikat Kemenkes)",
    val confidentialityAssurance: String = "Dukungan konseling ramah remaja, 100% anonim, tanpa diskriminasi, dan tanpa pemberitahuan ke pihak sekolah atau keluarga tanpa persetujuan Anda."
)

object ResearchData {

    val contactInfo = ContactPKPR()

    // 5 Item Kovariat Karakteristik Awal (A4 - A8)
    val covariateItems = listOf(
        CovariateOption(
            code = "A4",
            label = "Usia Peserta",
            description = "Kategori umur remaja sesuai kriteria inklusi (15 - 19 tahun).",
            options = listOf("15 tahun", "16 tahun", "17 tahun", "18 tahun", "19 tahun")
        ),
        CovariateOption(
            code = "A5",
            label = "Jenis Kelamin",
            description = "Identifikasi jenis kelamin biologis untuk penyesuaian analisis demografis.",
            options = listOf("Laki-laki", "Perempuan")
        ),
        CovariateOption(
            code = "A6",
            label = "Tingkat Kelas di SMAN 1 Tempuran",
            description = "Tingkat rombongan belajar aktif di sekolah.",
            options = listOf("Kelas X (Fase E)", "Kelas XI (Fase F)", "Kelas XII (Fase F)")
        ),
        CovariateOption(
            code = "A7",
            label = "Riwayat Paparan Edukasi HIV Sebelumnya",
            description = "Apakah pernah menerima edukasi formal mengenai pencegahan HIV/AIDS.",
            options = listOf("Pernah Mengikuti", "Belum Pernah Sama Sekali")
        ),
        CovariateOption(
            code = "A8",
            label = "Status Kepemilikan Perangkat Smartphone",
            description = "Modalitas privasi penggunaan gawai harian.",
            options = listOf(
                "Perangkat Pribadi Penuh (Milik Sendiri)",
                "Perangkat Bersama / Dipakai Bersama Anggota Keluarga"
            )
        )
    )

    // Bagian A: Privacy Trust (8 Item: PT1 - PT8)
    val privacyTrustItems = listOf(
        SurveyItem("PT1", "Saya percaya sistem digital ini memiliki perlindungan keamanan teknis yang kuat untuk menjaga data pribadi saya.", "Privacy Trust"),
        SurveyItem("PT2", "Saya yakin riwayat interaksi dan jawaban saya di dalam sistem ini tersimpan secara aman dan terenkripsi.", "Privacy Trust"),
        SurveyItem("PT3", "Saya percaya fitur keamanan pada sistem ini mampu mencegah pihak yang tidak berwenang membuka informasi saya.", "Privacy Trust"),
        SurveyItem("PT4", "Saya yakin informasi sensitif yang saya sampaikan di dalam sistem ini tidak akan bocor kepada pihak sekolah atau guru tanpa izin saya.", "Privacy Trust"),
        SurveyItem("PT5", "Saya percaya identitas dan privasi saya tetap terjaga kerahasiaannya dari anggota keluarga maupun teman sebaya saat menggunakan sistem ini.", "Privacy Trust"),
        SurveyItem("PT6", "Saya merasa aman menyampaikan pertanyaan jujur mengenai skrining HIV karena sistem ini menjamin kerahasiaan percakapan.", "Privacy Trust"),
        SurveyItem("PT7", "Saya percaya sistem ini hanya menggunakan data saya murni untuk tujuan dukungan edukasi dan kesehatan yang telah dijelaskan.", "Privacy Trust"),
        SurveyItem("PT8", "Saya yakin pengelola sistem ini dapat dipertanggungjawabkan dan tidak akan menyalahgunakan informasi kesehatan saya untuk tujuan lain.", "Privacy Trust")
    )

    // Bagian B: HIVST Self-Efficacy (8 Item: SE1 - SE8)
    val selfEfficacyItems = listOf(
        SurveyItem("SE1", "Saya mampu mempelajari petunjuk penggunaan kit HIVST dan menyiapkan komponen alat tes secara mandiri di tempat yang privat.", "HIVST Self-Efficacy"),
        SurveyItem("SE2", "Saya mampu membuka kemasan tabung cairan penyangga (buffer) dan menyiapkan spatula usap gusi tanpa menumpahkan cairannya.", "HIVST Self-Efficacy"),
        SurveyItem("SE3", "Saya mampu melakukan usapan pada gusi atas dan gusi bawah (oral swab) secara benar sesuai langkah pada panduan kit HIVST.", "HIVST Self-Efficacy"),
        SurveyItem("SE4", "Saya mampu memasukkan alat tes ke dalam tabung penyangga dan menunggu waktu pembacaan hasil (20–40 menit) sesuai ketentuan prosedur.", "HIVST Self-Efficacy"),
        SurveyItem("SE5", "Saya mampu membaca dan membedakan tampilan garis hasil nonreaktif, reaktif, maupun invalid pada jendela baca kit HIVST secara tepat.", "HIVST Self-Efficacy"),
        SurveyItem("SE6", "Saya mampu memahami arti hasil tes dikaitkan dengan periode jendela (window period 3 bulan) sehingga tidak salah menyimpulkan status risiko.", "HIVST Self-Efficacy"),
        SurveyItem("SE7", "Saya mampu mengelola rasa tegang atau cemas saat menunggu dan melihat hasil skrining mandiri HIVST.", "HIVST Self-Efficacy"),
        SurveyItem("SE8", "Saya mampu menghubungi petugas konselor kesehatan untuk pemeriksaan konfirmasi atau tes ulang apabila hasil skrining menunjukkan reaktif atau invalid.", "HIVST Self-Efficacy")
    )

    // Bagian C: Intensi HIVST Berbasis TACT (6 Item: INT1 - INT6)
    val intentionTactItems = listOf(
        SurveyItem("INT1", "Saya bersedia menggunakan kit HIVST berbasis cairan oral yang saya terima untuk mengetahui status skrining HIV saya.", "Intensi HIVST (TACT)"),
        SurveyItem("INT2", "Saya berniat menjalankan sendiri pengambilan sampel usapan gusi dan membaca hasil kit HIVST tersebut di ruang pribadi saya.", "Intensi HIVST (TACT)"),
        SurveyItem("INT3", "Saya berencana menggunakan kit HIVST yang telah saya terima dalam rentang waktu 14 hari ke depan setelah program ini selesai.", "Intensi HIVST (TACT)"),
        SurveyItem("INT4", "Saya sudah memikirkan waktu dan tempat pribadi yang aman untuk melaksanakan tes mandiri dengan kit HIVST tersebut dalam 2 minggu ini.", "Intensi HIVST (TACT)"),
        SurveyItem("INT5", "Saya akan tetap meluangkan waktu untuk menyelesaikan prosedur skrining dengan kit HIVST ini meskipun jadwal kegiatan sekolah saya padat.", "Intensi HIVST (TACT)"),
        SurveyItem("INT6", "Secara keseluruhan, saya memiliki niat yang kuat untuk menuntaskan penggunaan kit HIVST ini secara mandiri sesuai petunjuk dalam 14 hari ke depan.", "Intensi HIVST (TACT)")
    )

    // Bagian D: Help-Seeking Intention/Readiness (8 Item: HS1 - HS8)
    val helpSeekingItems = listOf(
        SurveyItem("HS1", "Mencari informasi lanjutan mengenai periode jendela (window period) dan pencegahan HIV melalui panduan resmi di dalam sistem.", "Help-Seeking Readiness"),
        SurveyItem("HS2", "Mengajukan pertanyaan tentang keraguan hasil skrining HIVST kepada layanan informasi kesehatan resmi yang terjamin kerahasiaannya.", "Help-Seeking Readiness"),
        SurveyItem("HS3", "Menghubungi petugas konselor Pelayanan Kesehatan Peduli Remaja (PKPR) Puskesmas untuk berkonsultasi mengenai hasil HIVST saya.", "Help-Seeking Readiness"),
        SurveyItem("HS4", "Berkonsultasi dengan dokter, perawat, atau bidan di fasilitas kesehatan mitra apabila saya mengalami kecemasan terkait risiko HIV.", "Help-Seeking Readiness"),
        SurveyItem("HS5", "Memanfaatkan layanan konseling jarak jauh (telecounseling atau pendamping kesehatan terlatih) yang direkomendasikan sistem.", "Help-Seeking Readiness"),
        SurveyItem("HS6", "Meminta dukungan emosional atau saran dari orang dewasa yang paling saya percaya (seperti orang tua/wali atau guru BK/UKS) jika saya membutuhkan pendampingan ke layanan kesehatan.", "Help-Seeking Readiness"),
        SurveyItem("HS7", "Berdiskusi secara bijak dengan teman sebaya atau pendamping sebaya (peer educator) yang terlatih mengenai cara mengakses layanan ramah remaja.", "Help-Seeking Readiness"),
        SurveyItem("HS8", "Menggunakan fitur rujukan aktif (active linkage) pada sistem untuk menjadwalkan tes konfirmasi atau layanan pencegahan di Puskesmas apabila diperlukan.", "Help-Seeking Readiness")
    )

    // Seluruh 30 butir instrumen Tabel 3.5 lengkap
    val all30SurveyItems = privacyTrustItems + selfEfficacyItems + intentionTactItems + helpSeekingItems

    // 4 Video Edukasi Lengkap Bersubtitle dan Kuis Kilat
    val videoLessons = listOf(
        VideoLesson(
            id = "vid_1",
            title = "Video 1: Tutorial Praktis Kit HIVST Cairan Oral (Mastery Experience)",
            category = "Keterampilan Prosedural OraQuick",
            durationSeconds = 60,
            summary = "Panduan visual langkah demi langkah menggunakan kit OraQuick: persiapan puasa 15 menit, pembukaan tabung buffer, usapan gusi atas & bawah, dan interpretasi C & T.",
            slides = listOf(
                VideoSlide(0, "Langkah 1: Persiapan & Privasi", "Pastikan Anda berada di ruang pribadi yang tenang. Puasa makan, minum air dingin, dan permen karet minimal 15 menit sebelum tes.", "persiapan_puasa"),
                VideoSlide(15, "Langkah 2: Menyiapkan Tabung Buffer", "Buka tutup tabung cairan penyangga (buffer) secara tegak lurus dan letakkan kokoh pada dudukan (stand) agar cairan tidak tumpah.", "buka_tabung_buffer"),
                VideoSlide(30, "Langkah 3: Usapan Oral Mucosal Transudate", "Keluarkan spatula pipih. Usapkan bantalan pipih 1 kali sepanjang gusi luar atas, lalu balik dan usapkan 1 kali sepanjang gusi luar bawah. Jangan usap lidah atau pipi dalam.", "usap_gusi_atas_bawah"),
                VideoSlide(45, "Langkah 4: Waktu Tunggu 20-40 Menit", "Masukkan spatula ke dalam tabung buffer. Biarkan berdiri tegak. Baca hasil antara menit ke-20 hingga ke-40. Pembacaan lewat 40 menit tidak lagi akurat.", "timer_reaksi_hasil")
            ),
            quizQuestions = listOf(
                VideoQuizQuestion(
                    id = "q1_1",
                    question = "Berapa lama minimal kita harus berpuasa dari makan/minum sebelum melakukan usap gusi HIVST?",
                    options = listOf("5 menit", "15 menit", "1 jam", "Tidak perlu puasa"),
                    correctIndex = 1,
                    explanation = "Puasa makan dan minum minimal 15 menit mencegah residu makanan mengganggu viskositas cairan mukosa gusi."
                ),
                VideoQuizQuestion(
                    id = "q1_2",
                    question = "Bagian mulut manakah yang harus diusap oleh bantalan spatula OraQuick?",
                    options = listOf("Lidah dan langit-langit mulut", "Permukaan gigi depan", "Gusi luar atas 1x dan gusi luar bawah 1x", "Tenggorokan bagian dalam"),
                    correctIndex = 2,
                    explanation = "Spesimen yang diambil adalah Oral Mucosal Transudate dari sepanjang gusi luar atas dan bawah."
                ),
                VideoQuizQuestion(
                    id = "q1_3",
                    question = "Kapan rentang waktu yang benar untuk membaca garis hasil pada kit HIVST?",
                    options = listOf("Detik ke-30", "5 hingga 10 menit", "20 hingga 40 menit", "Keesokan harinya"),
                    correctIndex = 2,
                    explanation = "Hasil valid dibaca pada rentang 20 hingga 40 menit setelah spatula dimasukkan ke buffer."
                )
            )
        ),
        VideoLesson(
            id = "vid_2",
            title = "Video 2: Cerita Teman Sebaya (Peer Story: Vicarious Modeling)",
            category = "Ketahanan Emosional & Privasi",
            durationSeconds = 60,
            isPriorityRule2A = true,
            summary = "Kisah siswa SMAN 1 Tempuran mengatasi rasa takut stigma dan kecemasan ponsel dipinjam keluarga menggunakan fitur proteksi privasi mandiri.",
            slides = listOf(
                VideoSlide(0, "Kisah Rian di SMAN 1 Tempuran", "Rian (17 th) sempat takut melakukan tes karena khawatir dicap negatif oleh teman sebaya dan takut HP diperiksa keluarga di rumah.", "rian_cemas_stigma"),
                VideoSlide(18, "Memanfaatkan Fitur Privasi Aplikasi", "Dengan kode token anonim dan tombol darurat 'Mode Samaran Biologi', Rian merasa aman belajar tanpa jejak sensitif di layar.", "fitur_privasi_aman"),
                VideoSlide(36, "Mengatasi Rasa Grogi Saat Menunggu", "Rian menarik napas dalam saat menunggu 20 menit: 'Mengetahui status kesehatan sedini mungkin adalah bentuk perlindungan terbaik untuk masa depan.'", "mengelola_kecemasan"),
                VideoSlide(50, "Kelegaan & Kesiapan Konseling", "Rian memahami bahwa jika butuh teman cerita, konselor PKPR Puskesmas Tempuran selalu siap mendampingi tanpa menghakimi.", "linkage_pkpr_ramah")
            ),
            quizQuestions = listOf(
                VideoQuizQuestion(
                    id = "q2_1",
                    question = "Apa fungsi utama tombol merah 'Mode Samaran' jika seseorang tiba-tiba mendekat?",
                    options = listOf("Mematikan daya ponsel otomatis", "Mengubah layar seketika menjadi rangkuman materi Biologi SMA", "Mengirim pesan SOS ke guru", "Menghapus aplikasi"),
                    correctIndex = 1,
                    explanation = "Mode samaran langsung mengubah layar menjadi materi pelajaran Biologi Imunitas untuk menjaga privasi di ruang bersama."
                ),
                VideoQuizQuestion(
                    id = "q2_2",
                    question = "Mengapa mengetahui status HIV sejak usia remaja sangat penting?",
                    options = listOf("Agar bisa dikeluarkan dari sekolah", "Agar mendapat penanganan dan perlindungan kesehatan sedini mungkin", "Agar diumumkan kepada teman sebaya", "Tidak penting"),
                    correctIndex = 1,
                    explanation = "Deteksi dini memungkinkan intervensi dini sehingga remaja dapat tetap hidup sehat, produktif, dan terlindungi."
                ),
                VideoQuizQuestion(
                    id = "q2_3",
                    question = "Bagaimana sikap petugas konselor PKPR Puskesmas Tempuran terhadap remaja yang berkonsultasi?",
                    options = listOf("Menghakimi dan memarahi", "Menelepon orang tua tanpa izin", "Ramah, menjaga kerahasiaan penuh, dan tanpa diskriminasi", "Menolak melayani"),
                    correctIndex = 2,
                    explanation = "Layanan PKPR berpedoman pada standar ramah remaja: menjamin kerahasiaan, empati, dan bebas stigma."
                )
            )
        ),
        VideoLesson(
            id = "vid_3",
            title = "Video 3: Memahami Periode Jendela (Window Period 12 Minggu)",
            category = "Pengetahuan Klinis Imunologi",
            durationSeconds = 60,
            summary = "Penjelasan proses pembentukan antibodi tubuh selama 3 bulan pasca-pajanan untuk mencegah salah tafsir (false reassurance).",
            slides = listOf(
                VideoSlide(0, "Apa itu Periode Jendela?", "Periode jendela (window period) adalah rentang waktu antara masuknya virus HIV ke tubuh hingga terbentuknya antibodi yang dapat dideteksi alat tes.", "grafik_periode_jendela"),
                VideoSlide(20, "Durasi Periode Jendela Kit Oral", "Kit HIVST cairan oral mendeteksi antibodi. Diperlukan waktu hingga 12 minggu (3 bulan) agar antibodi mencapai kadar optimal.", "durasi_12_minggu"),
                VideoSlide(40, "Pencegahan False Reassurance", "Jika tes dilakukan baru 2 minggu setelah risiko pajanan dan hasilnya 1 garis (nonreaktif), tes WAJIB diulang setelah genap 12 minggu.", "retasting_reminder")
            ),
            quizQuestions = listOf(
                VideoQuizQuestion(
                    id = "q3_1",
                    question = "Berapa lama rata-rata periode jendela untuk pembentukan antibodi yang terdeteksi kit HIVST cairan oral?",
                    options = listOf("1 hari", "1 minggu", "12 minggu (3 bulan)", "5 tahun"),
                    correctIndex = 2,
                    explanation = "Standar periode jendela tes antibodi cairan oral adalah 12 minggu (sekitar 84 hari)."
                ),
                VideoQuizQuestion(
                    id = "q3_2",
                    question = "Apa arti jika hasil tes nonreaktif namun pajanan risiko baru terjadi 1 minggu yang lalu?",
                    options = listOf("Pasti 100% bebas HIV selamanya", "Belum tentu akurat karena masih dalam periode jendela, perlu tes ulang", "Alat tes rusak", "Harus langsung minum antibiotik"),
                    correctIndex = 1,
                    explanation = "Antibodi belum terbentuk pada minggu ke-1, sehingga wajib tes ulang setelah 12 minggu pasca pajanan."
                ),
                VideoQuizQuestion(
                    id = "q3_3",
                    question = "Apakah kit HIVST mendeteksi virus secara langsung atau antibodi tubuh?",
                    options = listOf("Mendeteksi antibodi yang diproduksi tubuh terhadap virus", "Mendeteksi sel darah merah", "Mendeteksi air liur", "Mendeteksi suhu tubuh"),
                    correctIndex = 0,
                    explanation = "Kit generasi ke-3 OraQuick mendeteksi antibodi spesifik HIV-1 dan HIV-2 di dalam transudat mukosa oral."
                )
            )
        ),
        VideoLesson(
            id = "vid_4",
            title = "Video 4: Alur Rujukan Rahasia ke PKPR Puskesmas Tempuran",
            category = "Active Linkage & Konfirmasi",
            durationSeconds = 60,
            summary = "Penjelasan perbedaan skrining mandiri vs diagnosis definitif laboratorium serta alur pendampingan tes konfirmasi 3 reagen gratis.",
            slides = listOf(
                VideoSlide(0, "Skrining Mandiri vs Diagnosis Pasti", "Kit HIVST adalah alat skrining awal mandiri. Hasil 2 garis (reaktif) BUKAN vonis akhir dan harus dikonfirmasi di fasilitas kesehatan.", "skrining_vs_konfirmasi"),
                VideoSlide(20, "Tes Konfirmasi 3 Reagen Gratis", "Puskesmas Tempuran menyediakan tes darah konfirmasi serial 3 reagen secara gratis, cepat, dan terjamin kerahasiaannya.", "tiga_reagen_gratis"),
                VideoSlide(40, "Pendampingan Empatik PKPR", "Konselor PKPR akan mendampingi Anda di ruang konseling khusus remaja tanpa biaya dan tanpa stigma sosial.", "konseling_khusus_remaja")
            ),
            quizQuestions = listOf(
                VideoQuizQuestion(
                    id = "q4_1",
                    question = "Jika hasil kit HIVST cairan oral menunjukkan 2 garis (reaktif), apa tindakan yang benar?",
                    options = listOf("Panik dan mengisolasi diri", "Melakukan tes konfirmasi darah di Puskesmas Tempuran", "Membeli obat di warung", "Mengabaikan hasil"),
                    correctIndex = 1,
                    explanation = "Hasil reaktif pada skrining mandiri wajib dilanjutkan dengan tes konfirmasi standar 3 reagen di Puskesmas."
                ),
                VideoQuizQuestion(
                    id = "q4_2",
                    question = "Apakah pemeriksaan konfirmasi di layanan PKPR Puskesmas dipungut biaya?",
                    options = listOf("Berbayar mahal", "Gratis dan terjamin kerahasiaannya", "Hanya untuk siswa berprestasi", "Harus bayar tunai"),
                    correctIndex = 1,
                    explanation = "Layanan tes konfirmasi dan konseling PKPR Puskesmas difasilitasi gratis oleh program kesehatan Kemenkes RI."
                ),
                VideoQuizQuestion(
                    id = "q4_3",
                    question = "Berapa lama komitmen respon (SLA) pendampingan saat menghubungi konselor PKPR melalui sistem?",
                    options = listOf("1 bulan", "Maksimal 1x24 jam (< 2 jam untuk kondisi cemas berat)", "1 minggu", "Tidak ada respon"),
                    correctIndex = 1,
                    explanation = "Sistem active linkage menetapkan SLA respon cepat <= 1x24 jam guna mendampingi remaja yang membutuhkan arahan segera."
                )
            )
        )
    )

    // 5 E-Modul Interaktif Kemenkes & WHO
    val eModules = listOf(
        EModule(
            id = "mod_1",
            code = "MODUL A",
            title = "Memahami HIV vs. AIDS & Tantangan Deteksi Dini Remaja",
            estimatedReadMinutes = 4,
            keyPoints = listOf(
                "HIV adalah virus (Human Immunodeficiency Virus), sedangkan AIDS adalah stadium lanjut infeksi.",
                "Seseorang yang terinfeksi HIV dapat tampak sehat bugar selama bertahun-tahun tanpa gejala fisik.",
                "Target global kaskade 95-95-95: 95% orang tahu statusnya, 95% yang tahu mendapat pengobatan ARV, dan 95% tersupresi virusnya."
            ),
            fullContent = """
                Human Immunodeficiency Virus (HIV) adalah virus yang menyerang dan melemahkan sistem pertahanan tubuh manusia, khususnya sel darah putih jenis CD4. Tanpa pengobatan, infeksi ini secara bertahap merusak kekebalan tubuh hingga mencapai tahap AIDS (Acquired Immunodeficiency Syndrome).
                
                Salah satu mitos terbesar di kalangan remaja adalah anggapan bahwa orang dengan HIV pasti terlihat kurus, pucat, atau sakit-sakitan. Faktanya, virus HIV dapat berada di dalam tubuh selama 5 hingga 10 tahun tanpa menimbulkan gejala fisik apa pun. Seseorang dapat merasa sangat bugar dan beraktivitas normal di sekolah, namun virus tetap aktif berkembang biak.
                
                Oleh karena itu, satu-satunya cara mengetahui status kekebalan tubuh secara objektif adalah melalui skrining atau tes laboratorium. Deteksi dini pada remaja memungkinkan akses cepat ke terapi Antiretroviral (ARV), sehingga jumlah virus dapat ditekan hingga tidak terdeteksi (undetectable = untransmittable).
            """.trimIndent(),
            clinicalCitation = "Pedoman Pencegahan & Pengendalian HIV AIDS Kemenkes RI; WHO Consolidated Guidelines on HIV Prevention (2024)"
        ),
        EModule(
            id = "mod_2",
            code = "MODUL B",
            title = "Mengenal HIV Self-Testing (HIVST) Cairan Oral sebagai Instrumen Skrining",
            estimatedReadMinutes = 5,
            keyPoints = listOf(
                "HIVST cairan oral (OraQuick) tidak menusuk jarum dan tidak mengambil darah.",
                "Spesimen yang diambil adalah Oral Mucosal Transudate (bukan air liur/saliva biasa).",
                "Sensitivitas >= 99% dan spesifisitas >= 99.8%. Berfungsi sebagai skrining awal, bukan diagnosis definitif."
            ),
            fullContent = """
                HIV Self-Testing (HIVST) cairan oral adalah terobosan inovatif yang direkomendasikan WHO untuk mengatasi hambatan stigma dan rasa takut jarum suntik pada remaja. Alat ini memungkinkan seseorang mengambil sampel sendiri dan melihat hasilnya dalam waktu 20 menit di ruang yang privat.
                
                Penting untuk dipahami bahwa alat tes ini TIDAK mendeteksi virus pada air liur (karena cairan saliva tidak menularkan HIV). Yang diambil oleh spatula adalah 'Oral Mucosal Transudate', yaitu cairan kaya antibodi yang merembes dari kapiler darah di antara gusi dan bibir.
                
                Hasil tes mandiri ini diklasifikasikan menjadi tiga:
                1. Nonreaktif: Muncul 1 garis merah pada area Kontrol (C) dan tidak ada garis pada area Tes (T).
                2. Reaktif: Muncul 2 garis (pada garis C dan garis T, meskipun garis T tampak samar).
                3. Invalid: Garis C tidak muncul sama sekali. Alat harus diulang dengan kit baru.
                
                Ingat: Hasil 2 garis pada HIVST adalah indikasi skrining awal dan HARUS dikonfirmasi dengan pemeriksaan darah laboratorium di Puskesmas Tempuran.
            """.trimIndent(),
            clinicalCitation = "Kepmenkes No. HK.01.07/MENKES/90/2019; WHO Guidelines on HIV Self-Testing"
        ),
        EModule(
            id = "mod_3",
            code = "MODUL C",
            title = "Periode Jendela (Window Period 12 Minggu) & Pencegahan False Reassurance",
            estimatedReadMinutes = 4,
            keyPoints = listOf(
                "Periode jendela tes cairan oral adalah 12 minggu (3 bulan) pasca-paparan risiko.",
                "Hasil nonreaktif yang dites sebelum 12 minggu tidak dapat dijadikan kepastian mutlak.",
                "Jadwalkan tes ulang (retesting) setelah melewati batas 84 hari untuk hasil konklusif."
            ),
            fullContent = """
                Ketika virus HIV masuk ke tubuh, sistem imun memerlukan waktu untuk memproduksi antibodi dalam jumlah yang cukup agar dapat dideteksi oleh alat tes. Rentang waktu sejak virus masuk hingga antibodi terdeteksi disebut sebagai 'Periode Jendela' (Window Period).
                
                Untuk kit HIVST generasi ke-3 berbasis cairan oral, periode jendela standar adalah 12 minggu atau sekitar 84 hari. 
                
                Apa yang terjadi jika seseorang melakukan tes pada minggu ke-2 setelah terpapar perilaku berisiko?
                Hasil tes sangat mungkin menunjukkan 1 garis (Nonreaktif), bukan karena orang tersebut pasti bebas virus, melainkan karena antibodi tubuhnya belum cukup banyak untuk ditangkap oleh strip tes. Fenomena ini disebut 'False Reassurance' (rasa aman semu yang keliru).
                
                Oleh sebab itu, edukasi adaptif ini selalu meminta Anda menghitung tanggal risiko terakhir dan menjadwalkan tes konklusif setelah genap 12 minggu.
            """.trimIndent(),
            clinicalCitation = "WHO (2024) Window Periods for HIV Diagnosis and Testing Strategies"
        ),
        EModule(
            id = "mod_4",
            code = "MODUL D",
            title = "Perlindungan Data Pribadi Spesifik & Tips Aman Menggunakan Perangkat Bersama",
            estimatedReadMinutes = 4,
            keyPoints = listOf(
                "Prinsip Privacy-by-Design: data tidak mengaitkan identitas riil Anda.",
                "Gunakan Tombol Darurat Mode Samaran Biologi jika ada orang mendekat.",
                "Pahami hak Anda sebagai subjek data: hak hapus riwayat dan hak menarik persetujuan kapan saja."
            ),
            fullContent = """
                Banyak remaja di pedesaan dan pinggiran kota menggunakan ponsel pintar yang dipakai bergantian dengan orang tua, saudara kandung, atau teman sebaya (Shared Device). Kondisi ini menimbulkan kekhawatiran besar bahwa aktivitas belajar kesehatan reproduksi dapat diketahui orang lain secara tidak sengaja.
                
                Aplikasi ini menerapkan standar Privacy-by-Design yang ketat:
                1. Token Pseudonim: Anda hanya dikenal dengan kode acak seperti KRW-SMAN1T-XXX tanpa nama asli, NISN, atau alamat.
                2. Quick Decoy Camouflage: Menekan tombol merah seketika menyamarkan layar menjadi materi 'Biologi SMA: Sistem Imunitas Tubuh'.
                3. Discreet Lock Screen: Notifikasi ponsel disamarkan menjadi 'Pembaruan materi belajar mandiri'.
                4. Auto-Logout 5 Menit: Sesi otomatis terkunci jika perangkat ditinggalkan tanpa sentuhan.
                
                Hak Anda sebagai peserta dilindungi penuh: Anda berhak menghapus seluruh riwayat lokal atau mengundurkan diri dari penelitian kapan pun tanpa sanksi akademik apa pun.
            """.trimIndent(),
            clinicalCitation = "Pedoman Etik Penelitian Kesehatan Remaja; Prinsip Privacy-by-Design"
        ),
        EModule(
            id = "mod_5",
            code = "MODUL E",
            title = "Mengatasi Stigma & Mengenal Layanan Ramah Remaja (PKPR) Puskesmas Tempuran",
            estimatedReadMinutes = 5,
            keyPoints = listOf(
                "Stigma sosial sering kali lebih berbahaya daripada virus itu sendiri karena menghalangi pertolongan.",
                "Layanan PKPR Puskesmas Tempuran dirancang khusus dengan konselor yang ramah dan suportif.",
                "Tersedia 3 jalur rujukan aktif (Active Linkage) sesuai kebutuhan hasil skrining Anda."
            ),
            fullContent = """
                Stigma dan diskriminasi adalah penghalang nomor satu bagi remaja untuk mencari pertolongan kesehatan. Rasa malu, takut dikucilkan oleh teman, atau takut dilaporkan kepada guru sering kali membuat remaja memendam kecemasan sendirian.
                
                Pelayanan Kesehatan Peduli Remaja (PKPR) di Puskesmas Tempuran Karawang hadir untuk memutus rantai stigma tersebut. Layanan ini menjamin:
                - Ruang Konsultasi Terpisah: Menjamin privasi Anda tidak terlihat oleh antrean pasien umum.
                - Tenaga Kesehatan Bersertifikat: Petugas kesehatan terlatih yang tidak menghakimi pilihan hidup atau masa lalu Anda.
                - Kerahasiaan Medis Terikat Sumpah: Hasil konsultasi dan pemeriksaan Anda tidak akan pernah dipublikasikan.
                - Fasilitas Tes Konfirmasi Gratis: Melayani pemeriksaan lanjutan dengan reagen laboratorium terstandar.
                
                Jangan biarkan rasa cemas menghentikan langkah Anda. Mencari bantuan dan melindungi diri adalah tanda kedewasaan dan keberanian yang patut dihargai.
            """.trimIndent(),
            clinicalCitation = "Petunjuk Teknis PKPR Kementerian Kesehatan Republik Indonesia"
        )
    )
}
