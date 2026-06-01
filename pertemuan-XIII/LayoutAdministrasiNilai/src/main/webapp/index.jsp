<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Aplikasi Administrasi Nilai</title>
    <link rel="stylesheet" type="text/css" href="style.css">
</head>
<body>

    <div id="header">
        <h2>Aplikasi Administrasi Nilai Mahasiswa</h2>
    </div>

    <nav>
        <ul>
            <li><a href="#">Master Data</a>
                <ul>
                    <li><a href="DataMhs.jsp">Data Mahasiswa</a></li>
                    <li><a href="DataMatkul.jsp">Data Mata Kuliah</a></li>
                </ul>
            </li>

            <li><a href="#">Transaksi</a>
                <ul>
                    <li><a href="InputNilai.jsp">Input Nilai</a></li>
                </ul>
            </li>

            <li><a href="#">Laporan</a>
                <ul>
                    <li><a href="LaporanNilai.jsp">Laporan Nilai</a></li>
                </ul>
            </li>

            <li><a href="Logout.jsp">Logout</a></li>
        </ul>
    </nav>

    <div id="konten">
        <h3>Selamat Datang di Aplikasi Administrasi Nilai</h3>
        <p>Silakan pilih menu di atas untuk memulai.</p>
    </div>

    <div id="footer">
        <p>&copy; 2026 - Aplikasi Administrasi Nilai</p>
    </div>

</body>
</html>
