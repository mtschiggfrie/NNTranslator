import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by MatthewT on 4/12/2017.
 */
public class InputReader {

    ArrayList<String> inputSentences;
    ArrayList<String> outputSentences;

    ArrayList<ArrayList<Integer>> inputBitVectors;
    ArrayList<ArrayList<Integer>> outputBitVectors;

    public InputReader(){
        inputSentences = new ArrayList<>();
        outputSentences = new ArrayList<>();
        inputSentences = readFile("english.txt");
        outputSentences = readFile("spanish.txt");

        inputBitVectors = new ArrayList<>();
        outputBitVectors = new ArrayList<>();

        inputBitVectors = sentencesToBits(inputSentences);
        outputBitVectors = sentencesToBits(outputSentences);

    }

    private ArrayList<ArrayList<Integer>> sentencesToBits(ArrayList<String> sentenceVectors) {
        ArrayList<ArrayList<Integer>> listOfBitsPerSentence = new ArrayList<>();
        for (int i = 0; i < sentenceVectors.size(); ++i) {
            ArrayList<Integer> bitVectors = new ArrayList<>();
            String currentSentence = sentenceVectors.get(i);
            int index = 0;
            for (int j = 0; j < currentSentence.length(); ++j) {
                String inBits = characterToBits(currentSentence.charAt(j));
                for (int k = 0; k < inBits.length(); ++k) {
                    bitVectors.add(Integer.parseInt("" + inBits.charAt(k)));
                }
                index = index + 5;
            }
            while (index < 500) {
                bitVectors.add(0);
                index++;
            }
            listOfBitsPerSentence.add(bitVectors);
        }
        return listOfBitsPerSentence;
    }

    private String characterToBits(char character) {
        String binary = "";
        if (character == ' ') {
            binary = "00000";
        }
        else {
            int charValue = ((int) character) - 96;
            while (charValue > 0) {
                if (charValue % 2 == 0) {
                    binary = "0" + binary;
                }
                else {
                    binary = "1" + binary;
                }
                charValue = charValue / 2;
            }
            while (binary.length() < 5) {
                binary = "0" + binary;
            }
        }
        return binary;
    }

    private ArrayList<String> readFile(String fileName) {
        ArrayList<String> listOfSentences = new ArrayList<>();
        try {
            Scanner sentence = new Scanner(new File("C:/Users/MatthewT/Desktop/nn/out/production/" + fileName));
            String currentSentence = "";
            while(sentence.hasNextLine()) {
                String currentLine = sentence.nextLine().toLowerCase();
                if (currentLine.length() > 0) {
                    listOfSentences.add(currentLine);
                }
            }
            sentence.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        return listOfSentences;
    }

    private void saveInput(ArrayList<String> list) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter( new FileWriter( "englishinput.txt"));
            for (int i = 0; i < list.size(); ++i) {
                String currentline = list.get(i);
                currentline = currentline.replaceAll("[-+.^:,\";']","");
                currentline = currentline.replaceAll("\\P{Print}", "");
                if (currentline.length() <= 100) {
                    writer.write(currentline + "\n");
                }
            }
        }
        catch ( IOException e){

        }
        finally {
            try  {
                if ( writer != null)
                    writer.close( );
            }
            catch ( IOException e) {

            }
        }
    }

    public ArrayList<ArrayList<Integer>> getInputVectors() {
        return inputBitVectors;
    }

    public ArrayList<ArrayList<Integer>> getOutputVectors() {
        return outputBitVectors;
    }

    public String convertOutputInt(ArrayList<Integer> bitList) {
        String newOutput = "";
        for (int i = 0; i < bitList.size(); i = i + 5) {
            int ascii = 0;
            for (int k = 0; k < 5; ++k) {
                int bit = bitList.get(i + k);
                for (int p = 0; p < (4 - k); ++p) {
                    bit = bit * 2;
                }
                ascii = ascii + bit;
            }
            ascii = ascii + 96;
            char newLetter = (char) ascii;
            newOutput = newOutput + newLetter;
        }
        return newOutput;
    }

    public String convertOutputDouble(ArrayList<Double> bitList) {
        String newOutput = "";
        ArrayList<Integer> newBitList = new ArrayList<>();
        for (int i = 0; i < bitList.size(); ++i) {
            if (bitList.get(i) < 0.5) {
                newBitList.add(0);
            }
            else {
                newBitList.add(1);
            }
        }

        for (int i = 0; i < newBitList.size(); i = i + 5) {
            int ascii = 0;
            for (int k = 0; k < 5; ++k) {
                int bit = newBitList.get(i + k);
                for (int p = 0; p < (4 - k); ++p) {
                    bit = bit * 2;
                }
                ascii = ascii + bit;
            }
            ascii = ascii + 96;
            char newLetter = (char) ascii;
            newOutput = newOutput + newLetter;
        }
        return newOutput;
    }

}
