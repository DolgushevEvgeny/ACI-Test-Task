package Data;

//import Factory.VocabularyToken;
import java.util.ArrayList;

public class Hebrem {
    private static ArrayList<String> tokens = null;
    private static int index = 0;
    private static final char LETTER_H = 'h';
    private static final char LETTER_APOSTROPHE = '`';
    private static final char LETTER_T = 't';
    private static final char LETTER_K = 'k';
    private static final char LETTER_S = 's';
    private static final char LETTER_L = 'l';
    
    
    static public double convert(String input) {
        return 0;
    }
    
    static private boolean parse(String input) {
        for (int i = 0; i < input.length(); ++i) {
            
        }
        return true;
    }
    
    static private void split(String input) {
        tokens = new ArrayList<>();
        char[] inputArray = input.toCharArray();
        
        for (index = 0; index < inputArray.length; ++index) {
            try {
                switch (inputArray[index]){
                    case LETTER_T: checkLetterT(inputArray); ++index; break;
                    case LETTER_K: checkLetterKSL(inputArray, LETTER_H); break;
                    case LETTER_S: checkLetterKSL(inputArray, LETTER_APOSTROPHE); break;
                    case LETTER_L: checkLetterKSL(inputArray, LETTER_H); break;
                    default:
                        tokens.add(String.valueOf(inputArray[index]));
                        break;
                }
            } catch (NumberFormatException ex) {
                throw new NumberFormatException(ex.toString());
            }
            
        }
        
        index = 0;
    }
    
    static private void checkLetterT(char[] input) {
        if ((input[index + 1] == LETTER_APOSTROPHE) || (input[index + 1] == LETTER_H)) {
            tokens.add(String.valueOf(input[index] + input[index + 1]));
            return;
        }
        
        throw new NumberFormatException("Expected \"'\" or 'h'.");
    }
    
    static private void checkLetterKSL(char[] input, char letter) {
        if (input[index + 1] == letter) {
            tokens.add(String.valueOf(input[index] + input[index + 1]));
            ++index;
        } else {
            tokens.add(String.valueOf(input[index]));
        }
    }
    
    /*static private void checkLetterK(char[] input) {
        if (input[index + 1] == 'h') {
            tokens.add(String.valueOf(input[index] + input[index + 1]));
            ++index;
        } else {
            tokens.add(String.valueOf(input[index]));
        }
    }
    
    static private void checkLetterS(char[] input) {
        if (input[index + 1] == '`') {
            tokens.add(String.valueOf(input[index] + input[index + 1]));
            ++index;
        } else {
            tokens.add(String.valueOf(input[index]));
        }
    }
    
    static private void checkLetterL(char[] input) {
        if (input[index + 1] == 'h') {
            tokens.add(String.valueOf(input[index] + input[index + 1]));
            ++index;
        } else {
            tokens.add(String.valueOf(input[index]));
        }
    }*/
}
