package com.unpam.controller;

import com.unpam.model.Mahasiswa;
import com.unpam.model.MataKuliah;
import com.unpam.model.Nilai;
import com.unpam.util.Auth;
import com.unpam.view.MainForm;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet(name = "NilaiController", urlPatterns = {"/NilaiController"})
public class NilaiController extends HttpServlet {

    protected void processRequest(HttpServletRequest request,
                                  HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        String aksi   = request.getParameter("aksi");
        String konten = "";
        MainForm mainForm = new MainForm();

        if (!Auth.isLogin(request)) {
            mainForm.tampilkan(request, response,
                "<p style='color:red;'>Anda harus login terlebih dahulu.</p>"
                + "<a href='LoginController'>Login</a>");
            return;
        }

        if (Auth.isMahasiswa(request)) {
            konten = nilaiSaya(Auth.getNim(request));
            mainForm.tampilkan(request, response, konten);
            return;
        }

        if (aksi == null) {
            konten = formInput("", "", "", "", "", "", "", "", "", "");

        } else if (aksi.equals("cariMhs")) {
            String nim   = teks(request.getParameter("nim"));
            String nama = "", smt = "", kls = "";
            String info = "";
            Mahasiswa mhs = new Mahasiswa();
            mhs.setNim(nim);
            if (mhs.cari()) {
                nama = teks(mhs.getNama());
                smt  = String.valueOf(mhs.getSemester());
                kls  = teks(mhs.getKelas());
            } else {
                info = "<p style='color:red;'>" + mhs.getPesan() + "</p>";
            }
            konten = info + formInput(nim, nama, smt, kls,
                    teks(request.getParameter("kodeMataKuliah")),
                    teks(request.getParameter("namaMataKuliah")),
                    teks(request.getParameter("jumlahSks")),
                    "", "", "");

        } else if (aksi.equals("cariMk")) {
            String kode  = teks(request.getParameter("kodeMataKuliah"));
            String namaMk = "", sks = "";
            String info = "";
            MataKuliah mk = new MataKuliah();
            mk.setKodeMataKuliah(kode);
            if (mk.cari()) {
                namaMk = teks(mk.getNamaMataKuliah());
                sks    = String.valueOf(mk.getJumlahSks());
            } else {
                info = "<p style='color:red;'>" + mk.getPesan() + "</p>";
            }
            konten = info + formInput(
                    teks(request.getParameter("nim")),
                    teks(request.getParameter("nama")),
                    teks(request.getParameter("semester")),
                    teks(request.getParameter("kelas")),
                    kode, namaMk, sks, "", "", "");

        } else if (aksi.equals("lihatMhs")) {
            konten = daftarMahasiswa();

        } else if (aksi.equals("lihatMk")) {
            konten = daftarMataKuliah();

        } else if (aksi.equals("lihatNilai")) {
            konten = daftarNilai();

        } else if (aksi.equals("simpan")) {
            try {
                Nilai nilai = new Nilai();
                nilai.setNim(request.getParameter("nim"));
                nilai.setKodeMataKuliah(request.getParameter("kodeMataKuliah"));
                nilai.setNilaiTugas(angka(request.getParameter("nilaiTugas")));
                nilai.setNilaiUTS(angka(request.getParameter("nilaiUTS")));
                nilai.setNilaiUAS(angka(request.getParameter("nilaiUAS")));

                if (nilai.simpan()) {
                    double na    = nilai.hitungNilaiAkhir();
                    String grade = nilai.hitungGrade();
                    konten = "<h3>Input Nilai Mahasiswa</h3>"
                           + "<p style='color:green;'><b>Nilai berhasil disimpan!</b></p>"
                           + "<table border='1' cellpadding='6' style='border-collapse:collapse;'>"
                           + "<tr><td>NIM</td><td>" + nilai.getNim() + "</td></tr>"
                           + "<tr><td>Kode MK</td><td>" + nilai.getKodeMataKuliah() + "</td></tr>"
                           + "<tr><td>Nilai Akhir</td><td><b>" + String.format("%.2f", na) + "</b></td></tr>"
                           + "<tr><td>Grade</td><td><b>" + grade + "</b></td></tr>"
                           + "</table><br>"
                           + "<a href='NilaiController'>Input Lagi</a> | "
                           + "<a href='NilaiController?aksi=lihatNilai'>Lihat Daftar Nilai</a>";
                } else {
                    konten = "<p style='color:red;'>Gagal: " + nilai.getPesan() + "</p>"
                           + "<a href='NilaiController'>Kembali</a>";
                }
            } catch (Exception ex) {
                konten = "<p style='color:red;'>Error: " + ex.getMessage() + "</p>"
                       + "<a href='NilaiController'>Kembali</a>";
            }

        } else if (aksi.equals("hapus")) {
            Nilai nilai = new Nilai();
            nilai.setNim(request.getParameter("nim"));
            nilai.setKodeMataKuliah(request.getParameter("kodeMataKuliah"));
            if (nilai.hapus()) {
                konten = "<p style='color:green;'>Data nilai berhasil dihapus.</p>"
                       + "<a href='NilaiController'>Kembali</a> | "
                       + "<a href='NilaiController?aksi=lihatNilai'>Lihat Daftar Nilai</a>";
            } else {
                konten = "<p style='color:red;'>" + nilai.getPesan() + "</p>"
                       + "<a href='NilaiController'>Kembali</a>";
            }
        }

        mainForm.tampilkan(request, response, konten);
    }

    private String formInput(String nim, String nama, String semester, String kelas,
                             String kodeMk, String namaMk, String sks,
                             String nTugas, String nUts, String nUas) {
        StringBuilder sb = new StringBuilder();
        sb.append("<h2 align='center'>Input Nilai Mahasiswa</h2>");
        sb.append("<form method='POST' action='NilaiController'>");
        sb.append("<table align='center' cellpadding='4'>");

        sb.append("<tr><td align='right'>NIM</td><td>")
          .append("<input type='text' name='nim' value='").append(nim).append("'>")
          .append("<button type='submit' name='aksi' value='cariMhs'>Cari</button>")
          .append("<button type='submit' name='aksi' value='lihatMhs'>Lihat</button>")
          .append("</td></tr>");

        sb.append(baris("Nama", "nama", nama, 30));
        sb.append(baris("Semester", "semester", semester, 4));
        sb.append(baris("Kelas", "kelas", kelas, 4));

        sb.append("<tr><td align='right'>Kode Mata Kuliah</td><td>")
          .append("<input type='text' name='kodeMataKuliah' value='").append(kodeMk).append("'>")
          .append("<button type='submit' name='aksi' value='cariMk'>Cari</button>")
          .append("<button type='submit' name='aksi' value='lihatMk'>Lihat</button>")
          .append("</td></tr>");

        sb.append(baris("Nama Mata Kuliah", "namaMataKuliah", namaMk, 30));
        sb.append(baris("Jumlah SKS", "jumlahSks", sks, 4));
        sb.append(barisAngka("Nilai Tugas", "nilaiTugas", nTugas));
        sb.append(barisAngka("Nilai UTS", "nilaiUTS", nUts));
        sb.append(barisAngka("Nilai UAS", "nilaiUAS", nUas));

        sb.append("<tr><td colspan='2' align='center'>")
          .append("<button type='submit' name='aksi' value='simpan'>Simpan</button> ")
          .append("<button type='submit' name='aksi' value='hapus'>Hapus</button>")
          .append("</td></tr>");

        sb.append("</table></form>");
        sb.append("<p align='center'><a href='NilaiController?aksi=lihatNilai'>Lihat Daftar Nilai</a></p>");
        return sb.toString();
    }

    private String baris(String label, String name, String value, int size) {
        return "<tr><td align='right'>" + label + "</td><td>"
             + "<input type='text' name='" + name + "' value='" + value + "' size='" + size + "'>"
             + "</td></tr>";
    }

    private String barisAngka(String label, String name, String value) {
        return "<tr><td align='right'>" + label + "</td><td>"
             + "<input type='number' step='0.01' min='0' max='100' name='" + name + "' value='" + value + "'>"
             + "</td></tr>";
    }

    // ─── Daftar Mahasiswa ───────────────────────────────────────
    private String daftarMahasiswa() {
        Mahasiswa mhs = new Mahasiswa();
        StringBuilder sb = new StringBuilder("<h3>Daftar Mahasiswa</h3>");
        if (mhs.tampilSemua()) {
            sb.append("<table border='1' cellpadding='6' style='border-collapse:collapse;'>");
            sb.append("<tr><th>NIM</th><th>Nama</th><th>Semester</th><th>Kelas</th></tr>");
            Object[][] list = mhs.getList();
            if (list != null) {
                for (Object[] r : list) {
                    sb.append("<tr><td>").append(r[0]).append("</td><td>").append(r[1])
                      .append("</td><td>").append(r[2]).append("</td><td>").append(r[3])
                      .append("</td></tr>");
                }
            }
            sb.append("</table>");
        } else {
            sb.append("<p style='color:red;'>").append(mhs.getPesan()).append("</p>");
        }
        sb.append("<br><a href='NilaiController'>Kembali ke Form</a>");
        return sb.toString();
    }

    // ─── Daftar Mata Kuliah ─────────────────────────────────────
    private String daftarMataKuliah() {
        MataKuliah mk = new MataKuliah();
        StringBuilder sb = new StringBuilder("<h3>Daftar Mata Kuliah</h3>");
        if (mk.tampilSemua()) {
            sb.append("<table border='1' cellpadding='6' style='border-collapse:collapse;'>");
            sb.append("<tr><th>Kode MK</th><th>Nama Mata Kuliah</th><th>SKS</th></tr>");
            Object[][] list = mk.getList();
            if (list != null) {
                for (Object[] r : list) {
                    sb.append("<tr><td>").append(r[0]).append("</td><td>").append(r[1])
                      .append("</td><td>").append(r[2]).append("</td></tr>");
                }
            }
            sb.append("</table>");
        } else {
            sb.append("<p style='color:red;'>").append(mk.getPesan()).append("</p>");
        }
        sb.append("<br><a href='NilaiController'>Kembali ke Form</a>");
        return sb.toString();
    }

    // ─── Daftar Nilai ───────────────────────────────────────────
    private String daftarNilai() {
        Nilai nilai = new Nilai();
        StringBuilder sb = new StringBuilder("<h3>Data Nilai Mahasiswa</h3>");
        if (nilai.tampilSemua()) {
            sb.append("<table border='1' cellpadding='6' style='border-collapse:collapse;'>");
            sb.append("<tr><th>NIM</th><th>Nama</th><th>Mata Kuliah</th>"
                    + "<th>Tugas</th><th>UTS</th><th>UAS</th><th>Nilai Akhir</th><th>Grade</th></tr>");
            Object[][] list = nilai.getList();
            if (list != null) {
                for (Object[] r : list) {
                    sb.append("<tr>");
                    for (Object c : r) sb.append("<td>").append(c).append("</td>");
                    sb.append("</tr>");
                }
            }
            sb.append("</table>");
        } else {
            sb.append("<p style='color:red;'>").append(nilai.getPesan()).append("</p>");
        }
        sb.append("<br><a href='NilaiController'>Kembali ke Form</a>");
        return sb.toString();
    }

    // ─── Helper ─────────────────────────────────────────────────
    private String teks(String s)  { return (s == null) ? "" : s; }
    private double angka(String s) {
        try { return Double.parseDouble(s); } catch (Exception e) { return 0; }
    }

    // ─── Nilai milik mahasiswa yang sedang login ────────────────
    private String nilaiSaya(String nim) {
        Nilai nilai = new Nilai();
        StringBuilder sb = new StringBuilder("<h3>Nilai Saya</h3>");
        sb.append("<p>NIM: <b>").append(teks(nim)).append("</b></p>");
        if (nilai.tampilByNim(nim)) {
            Object[][] list = nilai.getList();
            if (list != null && list.length > 0) {
                sb.append("<table border='1' cellpadding='6' style='border-collapse:collapse;'>");
                sb.append("<tr style='background:#eef;'>"
                        + "<th>Kode MK</th><th>Mata Kuliah</th><th>SKS</th>"
                        + "<th>Tugas</th><th>UTS</th><th>UAS</th>"
                        + "<th>Nilai Akhir</th><th>Grade</th><th>Status</th></tr>");
                for (Object[] r : list) {
                    String grade = String.valueOf(r[7]);
                    String status = (grade.equals("D") || grade.equals("E")) ? "Tidak Lulus" : "Lulus";
                    sb.append("<tr>");
                    for (Object c : r) sb.append("<td>").append(c).append("</td>");
                    sb.append("<td>").append(status).append("</td>");
                    sb.append("</tr>");
                }
                sb.append("</table>");
            } else {
                sb.append("<p>Belum ada nilai yang tersedia untuk Anda.</p>");
            }
        } else {
            sb.append("<p style='color:red;'>").append(nilai.getPesan()).append("</p>");
        }
        return sb.toString();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException { processRequest(req, res); }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException { processRequest(req, res); }
}
