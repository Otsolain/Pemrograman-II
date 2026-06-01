# UTS - Aplikasi Manajemen Produk (Stack Data Structure)

## Topik
Implementasi struktur data Stack menggunakan LinkedList pada aplikasi GUI. Operasi push, pop, search, dan sort.

## Yang Dibuat
Aplikasi desktop manajemen produk berbasis Stack. Fitur: tambah produk (push), hapus produk terakhir (pop), cari produk by nama, sort by harga, sort by nama, dan log aktivitas.

## Lokasi File

```
UTS/
├── README.md
└── ManajemenProdukStack/       ← buka project ini di NetBeans
    ├── pom.xml
    └── src/main/java/
        ├── model/
        │   └── Product.java
        ├── structure/
        │   └── ProductStack.java
        └── gui/
            └── MainApp.java    ← main class
```

## Cara Menjalankan
Buka project di NetBeans → Run (F6)

## Cara Pakai
1. Isi ID, Nama, Harga, Stok → klik **Tambah** (push ke stack)
2. Klik **Hapus** untuk pop produk terakhir
3. Ketik nama di field Cari → klik **Cari**
4. Klik **Sort Harga** atau **Sort Nama** untuk mengurutkan tampilan
