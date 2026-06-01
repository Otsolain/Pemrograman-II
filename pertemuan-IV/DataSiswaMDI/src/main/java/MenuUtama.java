import javax.swing.*;

public class MenuUtama extends JFrame {

    private JDesktopPane desktopPane;
    private JMenuBar menuBar;
    private JMenu menuData;
    private JMenuItem menuItemSiswa;

    public MenuUtama() {
        setTitle("Aplikasi Data Siswa");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        desktopPane = new JDesktopPane();
        setContentPane(desktopPane);

        menuBar = new JMenuBar();
        menuData = new JMenu("Data");
        menuItemSiswa = new JMenuItem("Data Siswa");

        menuItemSiswa.addActionListener(e -> {
            DataSiswaForm childForm = new DataSiswaForm();
            desktopPane.add(childForm);
            childForm.setVisible(true);
        });

        menuData.add(menuItemSiswa);
        menuBar.add(menuData);
        setJMenuBar(menuBar);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuUtama().setVisible(true));
    }
}
