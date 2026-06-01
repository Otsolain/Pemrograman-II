<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Hitung Nilai - URL Mapping web.xml</title>
    <style>
        body { font-family: Arial; margin: 40px; }
        input { padding: 6px; margin: 4px 0 12px 0; width: 100%; box-sizing: border-box; }
        button { padding: 8px 20px; background: #28a745; color: white; border: none; border-radius: 4px; cursor: pointer; }
        .form-box { max-width: 350px; border: 1px solid #ccc; padding: 20px; border-radius: 8px; }
    </style>
</head>
<body>
    <div class="form-box">
        <h2>Form Hitung Nilai</h2>
        <p style="font-size:85%;color:#555;">Servlet dipetakan lewat web.xml ke URL <code>/MenghitungNilai</code></p>
        <form method="POST" action="MenghitungNilai">
            <label>Nama:</label>
            <input type="text" name="nama" required />

            <label>Nilai 1:</label>
            <input type="number" name="nilai1" min="0" max="100" required />

            <label>Nilai 2:</label>
            <input type="number" name="nilai2" min="0" max="100" required />

            <label>Nilai 3:</label>
            <input type="number" name="nilai3" min="0" max="100" required />

            <button type="submit">Hitung</button>
        </form>
    </div>
</body>
</html>
