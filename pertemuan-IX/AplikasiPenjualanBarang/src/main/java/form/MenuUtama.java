package form;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MenuUtama extends JFrame {

    private JMenuBar menuBar;

    private JMenu menuMaster;
    private JMenu menuTransaksi;
    private JMenu menuLaporan;
    private JMenu menuKeluar;

    private JMenuItem itemBarang;
    private JMenuItem itemCustomer;
    private JMenuItem itemSupplier;

    private JMenuItem itemPenjualan;
    private JMenuItem itemInventory;

    private JMenuItem itemLapTransaksi;
    private JMenuItem itemLapInventory;

    private JMenuItem itemExit;

    public MenuUtama() {
        setTitle("Menu Utama - Aplikasi Penjualan Barang");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initKomponen();
        setLayout(new BorderLayout());

        JLabel lblJudul = new JLabel("APLIKASI PENJUALAN BARANG", SwingConstants.CENTER);
        lblJudul.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel lblSub = new JLabel("Menu Utama Sistem Penjualan", SwingConstants.CENTER);
        lblSub.setFont(new Font("Arial", Font.PLAIN, 16));

        JPanel panelTengah = new JPanel(new GridLayout(2, 1));
        panelTengah.add(lblJudul);
        panelTengah.add(lblSub);

        add(panelTengah, BorderLayout.CENTER);
    }

    private void initKomponen() {
        menuBar = new JMenuBar();

        menuMaster = new JMenu("Master");
        menuTransaksi = new JMenu("Transaksi");
        menuLaporan = new JMenu("Laporan");
        menuKeluar = new JMenu("Keluar");

        itemBarang = new JMenuItem("Data Barang");
        itemCustomer = new JMenuItem("Data Customer");
        itemSupplier = new JMenuItem("Data Supplier");

        itemPenjualan = new JMenuItem("Transaksi Penjualan");
        itemInventory = new JMenuItem("Inventory Barang");

        itemLapTransaksi = new JMenuItem("Laporan Transaksi");
        itemLapInventory = new JMenuItem("Laporan Inventory");

        itemExit = new JMenuItem("Exit");

        menuMaster.add(itemBarang);
        menuMaster.add(itemCustomer);
        menuMaster.add(itemSupplier);

        menuTransaksi.add(itemPenjualan);
        menuTransaksi.add(itemInventory);

        menuLaporan.add(itemLapTransaksi);
        menuLaporan.add(itemLapInventory);

        menuKeluar.add(itemExit);

        menuBar.add(menuMaster);
        menuBar.add(menuTransaksi);
        menuBar.add(menuLaporan);
        menuBar.add(menuKeluar);

        setJMenuBar(menuBar);

        itemBarang.addActionListener((ActionEvent e) -> new FormBarang().setVisible(true));
        itemCustomer.addActionListener((ActionEvent e) -> new FormCustomer().setVisible(true));
        itemSupplier.addActionListener((ActionEvent e) -> new FormSupplier().setVisible(true));
        itemPenjualan.addActionListener((ActionEvent e) -> new FormTransaksi().setVisible(true));
        itemInventory.addActionListener((ActionEvent e) -> new FormInventory().setVisible(true));
        itemLapTransaksi.addActionListener((ActionEvent e) -> new LaporanTransaksi().setVisible(true));
        itemLapInventory.addActionListener((ActionEvent e) -> new LaporanInventory().setVisible(true));

        itemExit.addActionListener((ActionEvent e) -> {
            int jawab = JOptionPane.showConfirmDialog(
                    this,
                    "Yakin ingin keluar?",
                    "Konfirmasi",
                    JOptionPane.YES_NO_OPTION
            );
            if (jawab == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuUtama().setVisible(true));
    }
}
