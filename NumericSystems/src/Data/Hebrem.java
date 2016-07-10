package Data;

import java.util.ArrayList;

public class Hebrem {
    private static ArrayList<String> tokens = null;
    private static int index = 0;
    
    private static final char LETTER_H = 'h';
    private static final char LETTER_APOSTROPHE = '`';
    private static final char LETTER_QUOTE = '\'';
    private static final char LETTER_T = 't';
    private static final char LETTER_K = 'k';
    private static final char LETTER_S = 's';
    private static final char LETTER_L = 'l';
    private static final char LETTER_B = 'b';
    private static final char LETTER_G = 'g';
    private static final char LETTER_D = 'd';
    private static final char LETTER_W = 'w';
    private static final char LETTER_Z = 'z';
    private static final char LETTER_X = 'x';
    
    
    static public double convert(String input) {
        input = input.toLowerCase();
        try {
            split(input);
        } catch (NumberFormatException ex) {
            throw new NumberFormatException(ex.toString());
        }
        try {
            parse();
        } catch (NumberFormatException ex) {
            throw new NumberFormatException(ex.toString());
        }
        
        tokens.clear();
        return 0;
    }
    
    static private boolean parse() {
        for (int i = tokens.size() - 1; i > 0; --i) {
            
        }
        return true;
    }
    
    static private void split(String input) {
        tokens = new ArrayList<>();
        char[] inputArray = input.toCharArray();
        
        for (index = 0; index < inputArray.length; ++index) {
            try {
                switch (inputArray[index]){
                    case LETTER_T: checkLetterT(inputArray); break;
                    case LETTER_K: checkLetterKSL(inputArray, LETTER_H); break;
                    case LETTER_S: checkLetterKSL(inputArray, LETTER_APOSTROPHE); break;
                    case LETTER_L: checkLetterKSL(inputArray, LETTER_H); break;
                    case LETTER_QUOTE: checkLetterQuote(inputArray); break;
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
            addToken(input[index], input[index + 1]);
            return;
        }
        
        throw new NumberFormatException("Expected \"'\" or 'h'.");
    }
    
    static private void checkLetterKSL(char[] input, char letter) {
        if (input[index + 1] == letter) {
            addToken(input[index], letter);
        } else {
            addToken(input[index]);
        }
    }
    
    static private void checkLetterQuote(char[] input) {
        if (input[index + 1] == LETTER_QUOTE) {
            addToken(input[index], LETTER_QUOTE);
            return;
        }
        if (input[index + 1] == LETTER_B) {
            addToken(input[index], LETTER_B);
            return;
        }
        if (input[index + 1] == LETTER_G) {
            addToken(input[index], LETTER_G);
            return;
        }
        if (input[index + 1] == LETTER_D) {
            addToken(input[index], LETTER_D);
            return;
        }
        if (input[index + 1] == LETTER_H) {
            addToken(input[index], LETTER_H);
            return;
        }
        if (input[index + 1] == LETTER_W) {
            addToken(input[index], LETTER_W);
            return;
        }
        if (input[index + 1] == LETTER_Z) {
            addToken(input[index], LETTER_Z);
            return;
        }
        if (input[index + 1] == LETTER_X) {
            addToken(input[index], LETTER_X);
            return;
        }
        if ((input[index + 1] == LETTER_T) && (input[index + 2] == LETTER_QUOTE)) {
            addToken(input[index], LETTER_T, LETTER_QUOTE);
            return;
        }
        
        addToken(input[index]);
    }
    
    static private void addToken(char letter) {
        tokens.add(String.valueOf(letter));
    }
    
    static private void addToken(char letter1, char letter2) {
        tokens.add(String.valueOf(letter1) + String.valueOf(letter2));
        ++index;
    }
    
    static private void addToken(char letter1, char letter2, char letter3) {
        tokens.add(String.valueOf(letter1) + String.valueOf(letter2) + String.valueOf(letter3));
        ++index;
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
