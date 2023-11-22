

import java.io.*;
import java.util.*;

public class NilaiSiswaApp {

    private static final String CSV_FILE_PATH = "D://temp/direktori/data_sekolah.csv";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("Menu:");
            System.out.println("1. Generate file TXT untuk menampilkan modus");
            System.out.println("2. Generate file TXT untuk menampilkan nilai rata-rata dan median");
            System.out.println("3. Generate kedua file");
            System.out.println("4. Exit");
            System.out.print("Pilihan (1/2/3/4): ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Baca karakter newline setelah nextInt()

            switch (choice) {
                case 1:
                    generateModusFile();
                    break;
                case 2:
                    generateMeanMedianFile();
                    break;
                case 3:
                    generateModusFile();
                    generateMeanMedianFile();
                    break;
                case 4:
                    System.out.println("Terima kasih!");
                    break;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        } while (choice != 4);

        scanner.close();
    }

    private static void generateModusFile() {
        Map<String, List<Integer>> studentGrades = readStudentGradesFromCSV();
        String filePath = "D://temp/direktori/modus.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            Map<String, Integer> modusMap = new HashMap<>();

            for (List<Integer> grades : studentGrades.values()) {
                for (Integer grade : grades) {
                    modusMap.put(String.valueOf(grade), modusMap.getOrDefault(String.valueOf(grade), 0) + 1);
                }
            }

            int maxFrequency = Collections.max(modusMap.values());
            List<String> modusList = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : modusMap.entrySet()) {
                if (entry.getValue() == maxFrequency) {
                    modusList.add(entry.getKey());
                }
            }

            writer.write("Modus: " + modusList.toString() + "\n");
            System.out.println("File modus.txt berhasil dibuat.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generateMeanMedianFile() {
        Map<String, List<Integer>> studentGrades = readStudentGradesFromCSV();
        String filePath = "D://temp/direktori/mean_median.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            List<Integer> allGrades = new ArrayList<>();
            for (List<Integer> grades : studentGrades.values()) {
                allGrades.addAll(grades);
            }

            double mean = calculateMean(allGrades);
            int median = calculateMedian(allGrades);

            writer.write("Mean: " + mean + "\n");
            writer.write("Median: " + median + "\n");

            System.out.println("File mean_median.txt berhasil dibuat.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, List<Integer>> readStudentGradesFromCSV() {
        Map<String, List<Integer>> studentGrades = new HashMap<>();
        try (Scanner scanner = new Scanner(new File(CSV_FILE_PATH))) {
            // Melewati baris header
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                String name = parts[0];
                List<Integer> grades = new ArrayList<>();
                for (int i = 1; i < parts.length; i++) {
                    if (!parts[i].isEmpty()) {
                        grades.add(Integer.parseInt(parts[i]));
                    }
                }
                studentGrades.put(name, grades);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return studentGrades;
    }

    private static double calculateMean(List<Integer> grades) {
        int sum = 0;
        for (int grade : grades) {
            sum += grade;
        }
        return (double) sum / grades.size();
    }

    private static int calculateMedian(List<Integer> grades) {
        Collections.sort(grades);
        int size = grades.size();
        if (size % 2 == 0) {
            return (grades.get(size / 2 - 1) + grades.get(size / 2)) / 2;
        } else {
            return grades.get(size / 2);
        }
    }
}
