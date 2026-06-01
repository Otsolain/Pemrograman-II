import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class HitungNilai extends HttpServlet {

    protected void processRequest(HttpServletRequest request,
                                  HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        String nama   = request.getParameter("nama");
        double nilai1 = Double.parseDouble(request.getParameter("nilai1"));
        double nilai2 = Double.parseDouble(request.getParameter("nilai2"));
        double nilai3 = Double.parseDouble(request.getParameter("nilai3"));
        double rata   = (nilai1 + nilai2 + nilai3) / 3;

        String grade;
        if      (rata >= 85) grade = "A";
        else if (rata >= 70) grade = "B";
        else if (rata >= 60) grade = "C";
        else if (rata >= 50) grade = "D";
        else                 grade = "E";

        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html><html><body style='font-family:Arial;margin:40px;'>");
            out.println("<h2>Hasil Nilai: " + nama + "</h2>");
            out.println("<p>Rata-rata: <b>" + String.format("%.2f", rata) + "</b></p>");
            out.println("<p>Grade: <b>" + grade + "</b></p>");
            out.println("<a href='index.jsp'>Kembali</a>");
            out.println("</body></html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException { processRequest(req, res); }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException { processRequest(req, res); }
}
