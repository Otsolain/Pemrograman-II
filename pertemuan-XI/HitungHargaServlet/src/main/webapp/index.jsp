<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Hitung Harga</title>
    <style>
        body { font-family: Arial; margin: 40px; }
        input { padding: 6px; margin: 4px 0 12px 0; width: 100%; box-sizing: border-box; }
        button { padding: 8px 20px; background: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer; }
        .form-box { max-width: 350px; border: 1px solid #ccc; padding: 20px; border-radius: 8px; }
    </style>
</head>
<body>
    <div class="form-box">
        <h2>Form Hitung Harga</h2>
        <form method="POST" action="proses/HitungHarga">
            <label>Nama Barang:</label>
            <input type="text" name="namaBarang" required />

            <label>Harga Satuan (Rp):</label>
            <input type="number" name="harga" required />

            <label>Jumlah:</label>
            <input type="number" name="jumlah" required />

            <button type="submit">Hitung</button>
        </form>
    </div>
</body>
</html>
