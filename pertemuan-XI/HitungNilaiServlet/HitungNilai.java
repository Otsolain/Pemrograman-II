package proses;
        
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "HitungNilai", urlPatterns = {"/HitungNilai"})
public class HitungNilai extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String nama = request.getParameter("nama");
        String nilaiStr = request.getParameter("nilai");

        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Hasil Hitung Nilai</title>");
            out.println("</head>");
            out.println("<body>");

            if (nilaiStr == null || nilaiStr.isEmpty()) {
                out.println("<h2>Error</h2>");
                out.println("<p>Nilai belum diisi.</p>");
                out.println("<a href='index.html'>Kembali</a>");
            } else {
                int nilai = Integer.parseInt(nilaiStr);
                String grade;

                if (nilai >= 80) {
                    grade = "A";
                } else if (nilai >= 70) {
                    grade = "B";
                } else if (nilai >= 60) {
                    grade = "C";
                } else if (nilai >= 50) {
                    grade = "D";
                } else {
                    grade = "E";
                }

                out.println("<h2>Hasil Perhitungan Nilai</h2>");
                out.println("<p>Nama : " + nama + "</p>");
                out.println("<p>Nilai : " + nilai + "</p>");
                out.println("<p>Grade : " + grade + "</p>");
                out.println("<a href='index.html'>Kembali</a>");
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
        return "Servlet untuk menghitung nilai";
    }
}
