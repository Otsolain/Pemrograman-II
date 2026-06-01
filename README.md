# Pemrograman II

Kumpulan project praktikum mata kuliah Pemrograman II — Universitas Pamulang.

## Tech Stack

| Kategori | Tools |
|----------|-------|
| Bahasa | Java 8 |
| Build | Maven |
| IDE | Apache NetBeans |
| Database | Microsoft SQL Server (SQL Authentication) |
| Web Server | Apache Tomcat |
| Laporan | JasperReports 6.20 |
| Packaging | Launch4j + NSIS |

## Daftar Pertemuan

| Pertemuan | Topik | Tipe | Project |
|-----------|-------|------|---------|
| [I](pertemuan-I/) | Input Nilai Mahasiswa | Console | `InputNilaiMahasiswa` |
| [II](pertemuan-II/) | OOP Mahasiswa | Console | `OOPMahasiswa` |
| [III](pertemuan-III/) | Kalkulator GUI | Desktop Swing | `KalkulatorGUI` |
| [IV](pertemuan-IV/) | Aplikasi MDI Data Siswa | Desktop Swing MDI | `DataSiswaMDI` |
| [V](pertemuan-V/) | CRUD Data Mahasiswa (JDBC) | Desktop + SQL Server | `DataMahasiswaJDBC` |
| [VI](pertemuan-VI/) | Cari Data Mahasiswa | Desktop + SQL Server | `CariDataMahasiswa` |
| [VII](pertemuan-VII/) | Laporan Mahasiswa (JasperReports) | Desktop + SQL Server | `LaporanMahasiswaJasper` |
| [VIII](pertemuan-VIII/) | Packaging Aplikasi (Launch4j + NSIS) | Desktop → EXE | `AplikasiAdministrasiNilai` |
| [IX](pertemuan-IX/) | Aplikasi Penjualan Barang | Desktop + SQL Server | `AplikasiPenjualanBarang` |
| [X](pertemuan-X/) | Session dan Cookies (JSP) | Web JSP | `SessionCookieLogin` |
| [XI](pertemuan-XI/) | Java Servlet | Web Servlet | `HitungNilaiServlet`, `HitungHargaServlet` |
| [XII](pertemuan-XII/) | Servlet URL Mapping (web.xml) | Web Servlet | `ServletURLMapping` |
| [XIII](pertemuan-XIII/) | Layout JSP + CSS | Web JSP | `LayoutAdministrasiNilai` |
| [XIV](pertemuan-XIV/) | Aplikasi Web MVC Base | Web MVC + SQL Server | `AplikasiPenilaianMahasiswa` |
| [XV](pertemuan-XV/) | Web MVC + Transaksi Nilai | Web MVC + SQL Server | `AplikasiPenilaianMahasiswa` |
| [XVI](pertemuan-XVI/) | Web MVC + Laporan PDF | Web MVC + SQL Server | `AplikasiPenilaianMahasiswa` |
| [XVII](pertemuan-XVII/) | Aplikasi Rent Car | Web MVC + SQL Server | `AplikasiRentCar` |
| [UTS](UTS/) | Manajemen Produk (Stack) | Desktop Swing | `ManajemenProdukStack` |

> Pertemuan XIV → XV → XVI adalah satu project yang berkembang bertahap (by progress).

## Setup

### 1. Database (SQL Server)
Aktifkan **Mixed Mode Authentication** di SQL Server:
SSMS → klik kanan server → Properties → Security → pilih **SQL Server and Windows Authentication mode** → restart service SQL Server.

Jalankan file `script_db.sql` / `script_db_sqlserver.sql` di masing-masing folder project menggunakan SSMS.

Semua project menggunakan login yang sama:
```
user     : app_user
password : AppUser123!
```

### 2. Buka Project di NetBeans
File → Open Project → pilih folder project yang ada `pom.xml`-nya → NetBeans otomatis detect sebagai Maven project → dependency (SQL Server driver, JasperReports, dll) di-download otomatis.

### 3. Jalankan
- **Desktop**: klik kanan project → Run (F6)
- **Web**: klik kanan project → Run → otomatis deploy ke Tomcat → buka browser

### 4. Tomcat (untuk project web P10-P17)
Pastikan Tomcat sudah terdaftar di NetBeans:
Services → Servers → Add Server → pilih Apache Tomcat → arahkan ke folder instalasi Tomcat.
