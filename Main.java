import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Jautā lietotājam, vai vēlas kompresēt vai dekompresēt failu
        System.out.println("Vai vēlaties kompresēt (c) vai dekompresēt (d) failu?");
        String choice = scanner.nextLine();

        // Jautā faila nosaukumu
        System.out.println("Lūdzu, ievadiet faila nosaukumu:");
        String fileName = scanner.nextLine();

        // Pārbauda, vai faila nosaukums satur paplašinājumu '.html'
        if (!fileName.endsWith(".html")) {
            System.out.println("Nepareizs faila nosaukums. Failam jābūt ar paplašinājumu '.html'.");
            return;
        }

        // Pārbauda, vai fails eksistē
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("Norādītais fails neeksistē.");
            return;
        }

        // Nolasa visu faila saturu un saglabā to string mainīgajā
        String fileContent = readFileContent(file);

        // Izvēlas kompresēt vai dekompresēt atkarībā no lietotāja izvēles
        if (choice.equalsIgnoreCase("c")) {
            compress(fileContent);
        } else if (choice.equalsIgnoreCase("d")) {
            decompress(fileContent);
        } else {
            System.out.println("Nepareiza izvēle. Lūdzu, izvēlieties 'c' vai 'd'.");
        }
    }

    // Metode, kas nolasa faila visu saturu un atgriež to kā string
    private static String readFileContent(File file) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            System.out.println("Kļūda, lasot failu: " + e.getMessage());
        }
        return content.toString();
    }

    // Kompresijas metode
    private static void compress(String fileContent) {
        System.out.println(fileContent);
    }

    // Dekompresijas metode
    private static void decompress(String fileContent) {
        System.out.println(fileContent);
    }
}