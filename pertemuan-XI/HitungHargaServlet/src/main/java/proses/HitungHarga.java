package proses;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "HitungHarga", urlPatterns = {"/proses/HitungHarga"})
public class HitungHarga extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String namaBarang = request.getParameter("namaBarang");
        String hargaStr = request.getParameter("harga");
        String jumlahStr = request.getParameter("jumlah");

        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head><title>Hasil Hitung Harga</title></head>");
            out.println("<body style='font-family:Arial; margin:40px;'>");

            if (hargaStr == null || hargaStr.isEmpty() || jumlahStr == null || jumlahStr.isEmpty()) {
                out.println("<h2>Error</h2>");
                out.println("<p>Harga atau jumlah belum diisi.</p>");
                out.println("<a href='" + request.getContextPath() + "/index.jsp'>Kembali</a>");
            } else {
                int harga = Integer.parseInt(hargaStr);
                int jumlah = Integer.parseInt(jumlahStr);
                int total = harga * jumlah;

                out.println("<h2>Hasil Perhitungan Harga</h2>");
                out.println("<p>Nama Barang : " + namaBarang + "</p>");
                out.println("<p>Harga : " + harga + "</p>");
                out.println("<p>Jumlah : " + jumlah + "</p>");
                out.println("<p>Total Harga : " + total + "</p>");
                out.println("<a href='" + request.getContextPath() + "/index.jsp'>Kembali</a>");
            }

            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet untuk menghitung total harga";
    }
}
