import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Ask the user whether to compress (c) or decompress (d) the file
        System.out.println("Vai vēlaties kompresēt (c) vai dekompresēt (d) failu?");
        String choice = scanner.nextLine();

        // Ask for the file name
        System.out.println("Lūdzu, ievadiet faila nosaukumu:");
        String fileName = scanner.nextLine();

        // Check if the file name ends with '.html'
        if (!fileName.endsWith(".html")) {
            System.out.println("Nepareizs faila nosaukums. Failam jābūt ar paplašinājumu '.html'.");
            return;
        }

        // Check if the file exists
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("Norādītais fails neeksistē.");
            return;
        }

        // Read the entire content of the file and store it as a string
        String fileContent = readFileContent(file);

        // Choose whether to compress or decompress based on the user's choice
        if (choice.equalsIgnoreCase("c")) {
            compress(fileContent);
        } else if (choice.equalsIgnoreCase("d")) {
            decompress(fileContent);
        } else {
            System.out.println("Nepareiza izvēle. Lūdzu, izvēlieties 'c' vai 'd'.");
        }
    }

    // Method to read the content of a file and return it as a string
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

    // Compression method
    private static void compress(String fileContent) {
        byte[] input = fileContent.getBytes();
        ArrayList<LZ77Token> lz77out = compress(input);
        // byte[] hufmanout = Huffman.compress(lzout);
    }

    // Decompression method
    private static void decompress(String fileContent) {
        byte[] input = fileContent.getBytes();
        // byte[] hufmanout = Huffman.decompress(lzout);
        // byte[] lzout = LZ77.decompress(input);
    }

    public static ArrayList<LZ77Token> compress(byte[] input) {
        ArrayList<LZ77Token> compressedVal = new ArrayList<>();
        int slidingWindowSize = 15;
        int bufferAheadSize = 3;
        int currentIndex = 0;

        while (currentIndex < input.length) {
            int matchLength = 0;
            int matchStartIndex = -10;
            int windowStartIndex = Math.max(0, currentIndex - slidingWindowSize);
            for (int i = windowStartIndex; i < currentIndex; i++) {
                int j = 0;

                while (j < bufferAheadSize && currentIndex + j < input.length && input[i + j] == input[currentIndex + j]) {
                    j++;
                }
                if (j > matchLength) {
                    matchLength = j;
                    matchStartIndex = i;
                }
            }

            if (matchLength > 0) {
                byte nextChar;
                if (currentIndex + matchLength < input.length) {
                    nextChar = input[currentIndex + matchLength];
                } else {
                    nextChar = 0;
                }
                LZ77Token tokenToAdd = new LZ77Token(currentIndex - matchStartIndex, matchLength, nextChar);

                compressedVal.add(tokenToAdd);
                currentIndex = matchLength + currentIndex + 1;

            } else {
                LZ77Token tokenToAdd = new LZ77Token(0, 0, input[currentIndex]);
                compressedVal.add(tokenToAdd);
                currentIndex = currentIndex + 1;
            }
        }
        return compressedVal;
    }

    public static byte[] decompress(ArrayList<LZ77Token> compressedVal) {
        ArrayList<Byte> decompressedVal = new ArrayList<>();
        for (LZ77Token token : compressedVal) {
            if (token.getLenght() != 0) {
                int startIndex = decompressedVal.size() - token.getOffset();
                for (int i = 0; i < token.getLenght(); i++) {
                    decompressedVal.add(decompressedVal.get(startIndex + i));
                }
                if (token.getChar() != 0) {
                    decompressedVal.add(token.getChar());
                }
            } else {
                decompressedVal.add((byte) token.getChar());
            }
        }

        byte[] decompressedResult = new byte[decompressedVal.size()];
        for (int i = 0; i < decompressedVal.size(); i++) {
            decompressedResult[i] = decompressedVal.get(i);
        }
        return decompressedResult;
    }
}

class LZ77Token {
    private int offset;
    private int length;
    private byte character;

    public LZ77Token(int offset, int length, byte character) {
        this.offset = offset;
        this.length = length;
        this.character = character;
    }

    public int getOffset() {
        return this.offset;
    }

    public int getLenght() {
        return this.length;
    }

    public byte getChar() {
        return this.character;
    }

    // Convert LZ77 token to a binary array for further processing
    public byte[] toBinary() {
        byte[] convert = new byte[3];
        convert[0] = (byte) offset;
        convert[1] = (byte) length;
        convert[2] = character;
        return convert;
    }
}
