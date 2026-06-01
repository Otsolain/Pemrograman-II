import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet(name = "HitungNilai", urlPatterns = {"/HitungNilai"})
public class HitungNilai extends HttpServlet {

    protected void processRequest(HttpServletRequest request,
                                  HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        String nama   = request.getParameter("nama");
        double nilai1 = Double.parseDouble(request.getParameter("nilai1"));
        double nilai2 = Double.parseDouble(request.getParameter("nilai2"));
        double nilai3 = Double.parseDouble(request.getParameter("nilai3"));

        double rata = (nilai1 + nilai2 + nilai3) / 3;

        String grade;
        if      (rata >= 85) grade = "A";
        else if (rata >= 70) grade = "B";
        else if (rata >= 60) grade = "C";
        else if (rata >= 50) grade = "D";
        else                 grade = "E";

        String keterangan = (rata >= 60) ? "LULUS" : "TIDAK LULUS";

        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html><head><title>Hasil Nilai</title></head>");
            out.println("<body style='font-family:Arial; margin:40px;'>");
            out.println("<div style='max-width:400px; border:1px solid #ccc; padding:20px; border-radius:8px;'>");
            out.println("<h2>Hasil Hitung Nilai</h2>");
            out.println("<table border='1' cellpadding='8' style='border-collapse:collapse; width:100%;'>");
            out.println("<tr><td>Nama</td><td><b>" + nama + "</b></td></tr>");
            out.println("<tr><td>Nilai 1</td><td>" + nilai1 + "</td></tr>");
            out.println("<tr><td>Nilai 2</td><td>" + nilai2 + "</td></tr>");
            out.println("<tr><td>Nilai 3</td><td>" + nilai3 + "</td></tr>");
            out.println("<tr><td>Rata-rata</td><td><b>" + String.format("%.2f", rata) + "</b></td></tr>");
            out.println("<tr><td>Grade</td><td><b>" + grade + "</b></td></tr>");
            out.println("<tr><td>Keterangan</td><td><b style='color:" +
                        (keterangan.equals("LULUS") ? "green" : "red") + ";'>" +
                        keterangan + "</b></td></tr>");
            out.println("</table>");
            out.println("<br><a href='index.html'>Kembali</a>");
            out.println("</div></body></html>");
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
}
