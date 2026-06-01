# Pertemuan 15 - Aplikasi Web MVC + Transaksi Nilai

## Topik
Lanjutan MVC: menambah fitur transaksi penilaian. Class model `Nilai`, controller `NilaiController`, dan library JasperReports ditambahkan ke project.

## Yang Dibuat
Project Pertemuan 14 + fitur input nilai mahasiswa. Admin bisa input nilai (tugas 30%, UTS 30%, UAS 40%), nilai akhir dan grade dihitung otomatis. Mahasiswa bisa lihat nilai sendiri setelah login.

## Lokasi File

```
pertemuan-XV/
├── README.md
├── Input.png
└── AplikasiPenilaianMahasiswa/     ← buka project ini di NetBeans
    ├── pom.xml                     ← sudah include library JasperReports
    ├── database/
    │   └── script_db.sql           ← jalankan di SSMS (ada tabel tbnilai)
    └── src/main/java/com/unpam/
        ├── model/
        │   ├── Koneksi.java
        │   ├── Enkripsi.java
        │   ├── Mahasiswa.java
        │   ├── MataKuliah.java
        │   └── Nilai.java          ← class baru di pertemuan ini
        ├── view/
        │   ├── MainForm.java
        │   └── PesanDialog.java
        ├── util/
        │   └── Auth.java
        └── controller/
            ├── LoginController.java
            ├── LogoutController.java
            ├── MahasiswaController.java
            ├── MataKuliahController.java
            └── NilaiController.java    ← controller baru di pertemuan ini
```

## Setup Database
Jalankan `database/script_db.sql` di SSMS. Script ini sudah include tabel `tbnilai`.

## Cara Menjalankan
Buka project di NetBeans → Run → buka `http://localhost:8080/PenilaianMahasiswa15`

## Screenshot

![Input Nilai](Input.png)
