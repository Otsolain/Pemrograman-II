import java.util.Scanner;

public class MainMahasiswa {

    static class Mahasiswa {
        String nim, nama;
        double uts, uas;

        double hitungRata() {
            return (uts + uas) / 2;
        }

        String tentukanGrade() {
            double rata = hitungRata();
            if (rata >= 80) return "A";
            else if (rata >= 70) return "B";
            else if (rata >= 60) return "C";
            else if (rata >= 50) return "D";
            else return "E";
        }

        void tampilData() {
            System.out.println("\n=== Hasil Data Mahasiswa ===");
            System.out.println("NIM  : " + nim);
            System.out.println("Nama : " + nama);
            System.out.println("UTS  : " + uts);
            System.out.println("UAS  : " + uas);
            System.out.println("Rata : " + hitungRata());
            System.out.println("Grade: " + tentukanGrade());
        }
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Mahasiswa mhs = new Mahasiswa();

        System.out.println("=== Input Data Mahasiswa ===");

        System.out.print("NIM : ");
        mhs.nim = input.nextLine();

        System.out.print("Nama : ");
        mhs.nama = input.nextLine();

        System.out.print("Nilai UTS : ");
        mhs.uts = input.nextDouble();

        System.out.print("Nilai UAS : ");
        mhs.uas = input.nextDouble();

        mhs.tampilData();
    }
}
