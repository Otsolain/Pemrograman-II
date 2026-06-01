package gui;

import model.Product;
import structure.ProductStack;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class MainApp extends JFrame {
    private ProductStack productStack = new ProductStack();
    private DefaultListModel<String> listModel = new DefaultListModel<>();
    private JList<String> productList = new JList<>(listModel);
    private JTextField tfId, tfName, tfPrice, tfStock, tfSearch;
    private JTextArea logArea;

    public MainApp() {
        setTitle("Aplikasi Manajemen Produk - UTS Pemrograman II");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Input Produk"));

        tfId    = new JTextField();
        tfName  = new JTextField();
        tfPrice = new JTextField();
        tfStock = new JTextField();

        inputPanel.add(new JLabel("ID Produk:"));   inputPanel.add(tfId);
        inputPanel.add(new JLabel("Nama Produk:")); inputPanel.add(tfName);
        inputPanel.add(new JLabel("Harga (Rp):")); inputPanel.add(tfPrice);
        inputPanel.add(new JLabel("Stok:"));        inputPanel.add(tfStock);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnAdd       = new JButton("Tambah");
        JButton btnRemove    = new JButton("Hapus");
        JButton btnSortPrice = new JButton("Sort Harga");
        JButton btnSortName  = new JButton("Sort Nama");

        tfSearch = new JTextField(10);
        JButton btnSearch = new JButton("Cari");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnRemove);
        buttonPanel.add(btnSortPrice);
        buttonPanel.add(btnSortName);
        buttonPanel.add(new JLabel("Cari:"));
        buttonPanel.add(tfSearch);
        buttonPanel.add(btnSearch);

        JScrollPane listScroll = new JScrollPane(productList);
        listScroll.setBorder(BorderFactory.createTitledBorder("Daftar Produk (Stack)"));

        logArea = new JTextArea(5, 50);
        logArea.setEditable(false);
        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setBorder(BorderFactory.createTitledBorder("Log Aktivitas"));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(listScroll, BorderLayout.CENTER);
        add(logScroll, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> {
            try {
                String id    = tfId.getText().trim();
                String name  = tfName.getText().trim();
                double price = Double.parseDouble(tfPrice.getText().trim());
                int stock    = Integer.parseInt(tfStock.getText().trim());

                if (id.isEmpty() || name.isEmpty())
                    throw new IllegalArgumentException("ID dan Nama tidak boleh kosong!");

                Product p = new Product(id, name, price, stock);
                productStack.push(p);
                log("Produk ditambahkan: " + name);
                refreshList(productStack.getAll());
                clearInputs();
            } catch (NumberFormatException ex) {
                showError("Harga dan Stok harus berupa angka!");
            } catch (IllegalArgumentException ex) {
                showError(ex.getMessage());
            }
        });

        btnRemove.addActionListener(e -> {
            try {
                Product removed = productStack.pop();
                log("Produk dihapus: " + removed.getName());
                refreshList(productStack.getAll());
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });

        btnSortPrice.addActionListener(e -> {
            refreshList(productStack.sortByPrice());
            log("Diurutkan berdasarkan Harga");
        });

        btnSortName.addActionListener(e -> {
            refreshList(productStack.sortByName());
            log("Diurutkan berdasarkan Nama");
        });

        btnSearch.addActionListener(e -> {
            String keyword = tfSearch.getText().trim();
            Product found = productStack.search(keyword);
            if (found != null) {
                log("Ditemukan: " + found);
                JOptionPane.showMessageDialog(this, "Produk ditemukan:\n" + found);
            } else {
                log("Produk '" + keyword + "' tidak ditemukan.");
                showError("Produk tidak ditemukan!");
            }
        });
    }

    private void refreshList(LinkedList<Product> list) {
        listModel.clear();
        for (Product p : list) listModel.addElement(p.toString());
    }

    private void clearInputs() {
        tfId.setText("");
        tfName.setText("");
        tfPrice.setText("");
        tfStock.setText("");
    }

    private void log(String msg) {
        logArea.append(msg + "\n");
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
        log("Error: " + msg);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainApp().setVisible(true));
    }
}
