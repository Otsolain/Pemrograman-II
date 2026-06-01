import javax.swing.*;
import java.awt.event.*;

public class KalkulatorGUI extends JFrame {

    JLabel lbl1, lbl2, lblHasil;
    JTextField txt1, txt2, txtHasil;
    JButton btnTambah, btnHapus, btnExit;

    public KalkulatorGUI() {
        setTitle("Program GUI Sederhana");
        setSize(350, 250);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        lbl1 = new JLabel("Angka Pertama");
        lbl1.setBounds(20, 20, 120, 25);
        add(lbl1);

        lbl2 = new JLabel("Angka Kedua");
        lbl2.setBounds(20, 60, 120, 25);
        add(lbl2);

        lblHasil = new JLabel("Hasil");
        lblHasil.setBounds(20, 100, 120, 25);
        add(lblHasil);

        txt1 = new JTextField();
        txt1.setBounds(150, 20, 150, 25);
        add(txt1);

        txt2 = new JTextField();
        txt2.setBounds(150, 60, 150, 25);
        add(txt2);

        txtHasil = new JTextField();
        txtHasil.setBounds(150, 100, 150, 25);
        txtHasil.setEditable(false);
        add(txtHasil);

        btnTambah = new JButton("Tambah");
        btnTambah.setBounds(20, 150, 90, 25);
        add(btnTambah);

        btnHapus = new JButton("Hapus");
        btnHapus.setBounds(120, 150, 90, 25);
        add(btnHapus);

        btnExit = new JButton("Exit");
        btnExit.setBounds(220, 150, 80, 25);
        add(btnExit);

        btnTambah.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    double a = Double.parseDouble(txt1.getText());
                    double b = Double.parseDouble(txt2.getText());
                    double hasil = a + b;
                    txtHasil.setText(String.valueOf(hasil));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Input harus angka!");
                }
            }
        });

        btnHapus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                txt1.setText("");
                txt2.setText("");
                txtHasil.setText("");
            }
        });

        btnExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new KalkulatorGUI().setVisible(true));
    }
}
