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
        byte[] input = fileContent.getBytes();
        //byte[] lzout = LZ77.compress(input);
        //byte[] hufmanout = Huffman.compress(lzout);
    }

    // Dekompresijas metode
    private static void decompress(String fileContent) {
        byte[] input = fileContent.getBytes();
        //byte[] hufmanout = Huffman.decompress(lzout);
        //byte[] lzout = LZ77.decompress(input);
    }
    
  public static ArrayList<Token> compress(String input){
    ArrayList<Token> compressedVal = new ArrayList<Token>();
    int slidingWindowSize = 15;
    int bufferAheadSize = 3;
    int currentIndex = 0;


    while(currentIndex < input.length()){
      int matchLength = 0;
      int matchStartIndex = -10;
      int windowStartIndex = Math.max(0, currentIndex - slidingWindowSize);
      for(int i = windowStartIndex; i < currentIndex; i++){
        int j = 0; 

        while(j<bufferAheadSize && currentIndex +j <input.length() && input.charAt(i+j) == input.charAt(currentIndex+j)){
          j++;
        }
        if(j>matchLength){
          matchLength = j;
          matchStartIndex = i;
        }
        } 

      if(matchLength >0){
        char nextChar;
        if(currentIndex+matchLength < input.length()){
          nextChar = input.charAt(currentIndex+matchLength);
        }else{
          nextChar = '\0';
        }
        Token tokenToAdd = new Token(currentIndex - matchStartIndex,matchLength,nextChar); 

        compressedVal.add(tokenToAdd);
        currentIndex= matchLength+currentIndex+1;
        
      }else{
        Token tokenToAdd = new Token(0,0,input.charAt(currentIndex));
        compressedVal.add(tokenToAdd);
        currentIndex = currentIndex+1;
      }
      
      }
      return compressedVal; 

    
    }


    public static String decompress(ArrayList<Token> compressedVal){
      StringBuilder  decompressedVal = new StringBuilder();
      for(Token token : compressedVal){
        if(token.length != 0){
          int startIndex = decompressedVal.length()- token.distance;
          for(int i= 0; i< token.length; i++){
            decompressedVal.append(decompressedVal.charAt(startIndex+i));
          }
          if(token.nextChar != '\0'){
            decompressedVal.append(token.nextChar);
          }
        }else{
          decompressedVal.append(token.nextChar);
        }
    }
    return decompressedVal.toString();
}


  
  
}


// LZ77 token klase kas paredzēta LZ77 tokena izveidei objekta formā, lai varētu vieglāk piekļūt pie tokena informācijas pirms tā pārveidošanas
class LZ77Token{
    private int offset;
    private int lenght;
    private byte character;
    public LZ77Token(int offset, int lenght, byte character){
        this.offset = offset;
        this.lenght = lenght;
        this.character = character;
    }

    public int getOffset(){
        return this.offset;
    }

    public int getLenght(){
        return this.lenght;
    }

    public int getChar(){
        return this.character;
    }
    // pārveido LZ77 tokenu par bināro masīvu, lai to varētu tālāk sūtit Huffman
    public byte[] toBinnary(){
        byte[] convert = new byte[3];
        convert[0] = (byte)offset;
        convert[1] = (byte)lenght;
        convert[2] = character;
        return convert;
    }
}
