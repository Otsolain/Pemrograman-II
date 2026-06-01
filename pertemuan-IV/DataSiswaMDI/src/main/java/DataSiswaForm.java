import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class DataSiswaForm extends JInternalFrame {

    private JTable tabelSiswa;
    private DefaultTableModel modelTabel;
    private JScrollPane scrollPane;
    private JTextField nimTF, namaTF, nilaiTF;
    private JButton btnTambah;

    public DataSiswaForm() {
        setTitle("Data Siswa");
        setSize(600, 400);
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);
        setResizable(true);
        setLocation(50, 50);

        initComponents();
        initTabel();
        isiDataContoh();
    }

    private void initComponents() {
        JPanel panelInput = new JPanel();

        nimTF   = new JTextField(10);
        namaTF  = new JTextField(15);
        nilaiTF = new JTextField(5);
        btnTambah = new JButton("Tambah");

        panelInput.add(new JLabel("NIM:"));
        panelInput.add(nimTF);
        panelInput.add(new JLabel("Nama:"));
        panelInput.add(namaTF);
        panelInput.add(new JLabel("Nilai:"));
        panelInput.add(nilaiTF);
        panelInput.add(btnTambah);

        btnTambah.addActionListener(e -> tambahData());

        setLayout(new java.awt.BorderLayout());
        add(panelInput, java.awt.BorderLayout.NORTH);
    }

    private void initTabel() {
        modelTabel = new DefaultTableModel();
        tabelSiswa = new JTable();
        tabelSiswa.setModel(modelTabel);

        modelTabel.addColumn("NIM");
        modelTabel.addColumn("Nama");
        modelTabel.addColumn("Nilai");

        scrollPane = new JScrollPane(tabelSiswa);
        add(scrollPane, java.awt.BorderLayout.CENTER);
    }

    private void isiDataContoh() {
        Object[] data1 = new Object[3];
        data1[0] = "2023001";
        data1[1] = "Andrea Pirlo";
        data1[2] = 90;
        modelTabel.addRow(data1);

        Object[] data2 = {"2023002", "Cristiano Ronaldo", 85};
        modelTabel.addRow(data2);

        Object[] data3 = {"2023003", "Lionel Messi", 92};
        modelTabel.addRow(data3);
    }

    private void tambahData() {
        Object[] dataBaru = {
            nimTF.getText(),
            namaTF.getText(),
            nilaiTF.getText()
        };
        modelTabel.addRow(dataBaru);

        nimTF.setText("");
        namaTF.setText("");
        nilaiTF.setText("");
    }
}
