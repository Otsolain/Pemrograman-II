package structure;

import model.Product;
import java.util.LinkedList;
import java.util.logging.Logger;

public class ProductStack {
    private LinkedList<Product> stack = new LinkedList<>();
    private static final Logger logger = Logger.getLogger(ProductStack.class.getName());

    public void push(Product p) {
        stack.push(p);
        logger.info("Produk ditambahkan: " + p.getName());
    }

    public Product pop() throws Exception {
        if (stack.isEmpty()) {
            logger.warning("Gagal hapus: Stack kosong!");
            throw new Exception("Stack kosong! Tidak ada produk untuk dihapus.");
        }
        Product p = stack.pop();
        logger.info("Produk dihapus: " + p.getName());
        return p;
    }

    public LinkedList<Product> getAll() {
        return stack;
    }

    public Product search(String name) {
        for (Product p : stack) {
            if (p.getName().equalsIgnoreCase(name)) return p;
        }
        return null;
    }

    public LinkedList<Product> sortByPrice() {
        LinkedList<Product> sorted = new LinkedList<>(stack);
        sorted.sort((a, b) -> Double.compare(a.getPrice(), b.getPrice()));
        return sorted;
    }

    public LinkedList<Product> sortByName() {
        LinkedList<Product> sorted = new LinkedList<>(stack);
        sorted.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
        return sorted;
    }

    public boolean isEmpty() { return stack.isEmpty(); }
}
