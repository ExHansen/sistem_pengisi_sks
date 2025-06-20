# Sistem Pengisian SKS

Aplikasi berbasis Java Swing dan MySQL yang digunakan untuk manajemen reservasi pengisian SKS oleh mahasiswa dan admin kampus.

Screenshoot Aplikasi:
![image](https://github.com/user-attachments/assets/f20986d1-4f37-477f-9a8d-78c681bc6295)
![image](https://github.com/user-attachments/assets/c8708e2e-5791-4cc1-a780-ab784ee45b86)
![image](https://github.com/user-attachments/assets/af5ca699-746d-4eb0-a42d-3e985ed75e10)
![image](https://github.com/user-attachments/assets/f728b965-4d94-44b2-974c-67c91d4b6813)
![image](https://github.com/user-attachments/assets/76e29da0-b576-44a3-8358-c16f83557236)
![image](https://github.com/user-attachments/assets/26d09a8f-d5ef-499e-a5da-dac2b2a95379)


## 📂 Struktur Proyek
```bash
sistem_pengisi_sks/
├── .idea/ # Konfigurasi IntelliJ IDEA
│ └── libraries/
├── out/ # Hasil kompilasi Java
│ └── production/
│ └── sistem_pengisi_sks/
│ ├── dao/
│ ├── db_connection/
│ └── frame/
├── src/ # Source code utama
│ ├── dao/ # Berisi MahasiswaDAO, MatakuliahDAO, dsb
│ ├── db_connection/ # Model dan koneksi database
│ └── frame/ # Tampilan GUI (LoginFrame, MainFrame, AdminFrame)
└── README.md # Dokumentasi proyek
```

---

## ⚙️ Fitur Aplikasi

- 🔐 Login sebagai Mahasiswa atau Admin
- 🧾 Registrasi Mahasiswa Baru
- 📚 Lihat & pilih Mata Kuliah
- 🗂️ Kelola Mata Kuliah (Admin)
- ✅ Reservasi SKS berdasarkan kuota & IPK
- 🕑 Riwayat Reservasi
- 🚪 Logout

---

## 🚀 Cara Menjalankan Proyek

### 1. Clone Repositori

```bash
git clone https://github.com/ExHansen/sistem_pengisi_sks.git

cd sistem_pengisi_sks

Download Module: https://downloads.mysql.com/archives/c-j/
versi: 9.2.0

Masukkan module dalam Open Module Settings > Library > Add Project Library
```

### 2. Buat Database
Dalam MySQL, buatlah sebuah database dengan tabel dan masukkan data
```bash
CREATE TABLE mahasiswa (
    nim VARCHAR(20) PRIMARY KEY,
    nama VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    semester INT NOT NULL,
    ipk DECIMAL(3,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE mata_kuliah (
    kode_mk VARCHAR(10) PRIMARY KEY,
    nama_mk VARCHAR(100) NOT NULL,
    sks INT NOT NULL,
    semester INT NOT NULL,
    dosen VARCHAR(100) NOT NULL,
    hari VARCHAR(20) NOT NULL,
    waktu VARCHAR(20) NOT NULL,
    kuota INT NOT NULL,
    terisi INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE reservasi (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nim VARCHAR(20) NOT NULL,
    kode_mk VARCHAR(10) NOT NULL,
    tanggal_reservasi TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('AKTIF', 'DIBATALKAN') DEFAULT 'AKTIF',
    FOREIGN KEY (nim) REFERENCES mahasiswa(nim),
    FOREIGN KEY (kode_mk) REFERENCES mata_kuliah(kode_mk),
    UNIQUE KEY unique_reservation (nim, kode_mk, status)
);
  
INSERT INTO mahasiswa (nim, nama, email, password, semester, ipk)
VALUES ('2310501095', 'Hansen Vernandez', '2310501095@mahasiswa.upnvj.ac.id', 'hansen', 4, 4.00),
(1, 'admin', 'admin@upnvj.ac.id', 'admin', 1, 4.00);

INSERT INTO mata_kuliah (kode_mk, nama_mk, sks, semester, dosen, hari, waktu, kuota) VALUES
('SI101', 'Algoritma dan Pemrograman', 3, 1, 'Dr. Andi Wijaya', 'Senin', '08:00-10:30', 40),
('SI102', 'Matematika Diskrit', 3, 1, 'Prof. Sari Indah', 'Selasa', '10:30-13:00', 40),
('SI201', 'Struktur Data', 3, 3, 'Dr. Budi Rahmat', 'Rabu', '08:00-10:30', 35),
('SI202', 'Basis Data', 3, 3, 'Dr. Citra Dewi', 'Kamis', '13:00-15:30', 35),
('SI301', 'Proyek Perangkat Lunak', 3, 5, 'Prof. Dedi Kurnia', 'Jumat', '08:00-10:30', 30),
('SI302', 'Jaringan Komputer', 3, 5, 'Dr. Eka Prasetya', 'Senin', '13:00-15:30', 30),
('SI103', 'Logika Matematika', 2, 1, 'Dr. Fitri Handayani', 'Selasa', '15:30-17:00', 40),
('SI203', 'Pemrograman Web', 3, 3, 'Dr. Gani Sutrisno', 'Rabu', '13:00-15:30', 35);
```

### 3. Run
Setelah itu, run file SKSReservationSystem

