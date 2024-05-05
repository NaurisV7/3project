import java.util.LinkedList;

// Huffman kompresija un dekompresija
public class Huffman {
    CreateHuffmanTree HT = new CreateHuffmanTree();
    LinkedList<byte[]> result = new LinkedList<byte[]>();
    

    public byte[] compress(byte[] lzout){
        // izveido simbolu biežuma koku saskaitot katru bitu kombināciju biežumu failā
        HT.addData(lzout, "comp");
        // izvadīt bitu kombinācijas kas piešķirtas simboliem pēc bitu skaita
        for(int i=0; i<=8; i++){
            //rekursijas metode: iet cauri kokam pa līmeņiem un atgriež bitus, ja tas ir simbols
            LinkedList<Node> symbols = findSymbols(HT.root, i); // pirmais HT elements 
            int count = symbols.size();
            if(count>0){
                // pievienot "count" un "i", katru 4 bitos un 1 baitā
                byte countb = (byte) count;
                byte ib = (byte) i;
                byte[] marker = {(byte)((countb << 4) | ib)};
                result.add(marker);
                // ieraksta simbolus un to bitu vērtības
                while(symbols.peek()!=null){
                    Node symbol = symbols.poll();
                    //ierkasta "symbol.HTsymbol" un "symbol.symbol", katru savā baitā
                    byte[] HTsymb = {(byte) symbol.HTsymbol};
                    byte[] symb = {(byte) symbol.symbol};
                    result.add(HTsymb);
                    result.add(symb);
                }
            }  
        }
        // ieraksta baitu "0000 0000", lai patektu, ka nav citu simbolu
        byte[] zeros = {(byte) 0};
        result.add(zeros);
        // vajadzētu pārveidot koku lai varētu ātrāk atrast specifisku simbolu
        Object[] savedSymb = result.toArray();
        // pārraksta failu aizstājot simbolus ar tiem piešķirtiem bitu kombinācijām
        //      while(!EOF){//sameklē simbolu HT un atgriež tā jauno bitu kombināciju}
        byte[] resultb = new byte[1];
        int freeb = 8;
        for(byte s : lzout){
            // atrast "s" huffman kokā
            int parity = 0, count = 0, length;
            byte sb, HTsb;
            for(Object ob : savedSymb){
                if(count<1){
                    int lc = (int) ob;
                    length = (byte) (lc & 0b00001111);
                    count = lc >> 4;
                }else{
                    if(parity==0){ // HTsymbol
                        HTsb = (byte) ob;
                    }else{ // symbol
                        byte b = (byte) ob;
                        if(s==b){
                            sb = (byte) HTsb;
                        }
                        count--;
                    }
                }
            }
            // pievienot sarakstā "result"
            if ((freeb-length)>=0){
                resultb[0] = (byte) (sb << (freeb-length));
                freeb -= length;
                if(freeb==0){
                    result.add(resultb);
                    freeb=8;
                }
            }else{
                resultb[0] = (byte) (sb >> (length-freeb));
                result.add(resultb);
                resultb[0] = (byte) (sb << (8-(length-freeb)));
                freeb = 8-(length-freeb);
            }


        }
        // result.add(EOFb);
        return result;
        
    }

    public byte[] decompress(byte[] input){
        // izveido simbolu biežuma koku nolasot informāciju no faila sākuma
        HT.createDecompTree(input);
        
    }
    // rekursijas metode, lai iegūtu dilstošā secībā sakārtotus sibolus un to jaunās bitu kombinācijas
    private LinkedList<Node> findSymbols(Node n, int length){
        
        LinkedList<Node> foundSymbols = new LinkedList<Node>();
        // ja elementam Node ir symbols un tā jaunā bitu kombinācija to pievieno sarakstam
        if(n.HTsymbolLength==length){
            foundSymbols.add(n);
            return foundSymbols;
        // ja nav, tad pārbauda elementa Node apakšelementus
        }else if(n.HTsymbolLength==0){
            LinkedList<Node> nRight = findSymbols(n.right, length);
            LinkedList<Node> nLeft = findSymbols(n.left, length);
            while(nRight.peek()!=null){
                foundSymbols.add(nRight.poll());
            }
            while(nLeft.peek()!=null){
                foundSymbols.add(nLeft.poll());
            }
            return foundSymbols;
        }else{
            return null;
        }
    }
}
