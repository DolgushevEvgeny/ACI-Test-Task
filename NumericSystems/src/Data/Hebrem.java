package Data;

import java.util.ArrayList;
import java.util.Objects;

public class Hebrem {
    private static ArrayList<String> tokens = null;
    private static ArrayList<Integer> numbers = null;
    private static int index = 0;
    private static int total = 0;
    private static int temp = 0;
    private static boolean isQuote = false;
    
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
    private static final char LETTER_Y = 'y';
    private static final char LETTER_M = 'm';
    private static final char LETTER_N = 'n';
    private static final char LETTER_P = 'p';
    private static final char LETTER_R = 'r';
    
    
    static public double convert(String input) {
        input = input.toLowerCase();
        try {
            split(input);
        } catch (NumberFormatException ex) {
            throw new NumberFormatException(ex.toString());
        }
        try {
            convert();
            tokens.clear();
        } catch (NumberFormatException ex) {
            throw new NumberFormatException(ex.toString());
        }
        if (!parse()) {
            numbers.clear();
            throw new NumberFormatException("Wrong number ");
        }
        
        numbers.clear();
        return total;
    }
    
    static private boolean parse() {
        if (numbers.size() > 1) {
            for (int i = numbers.size() - 1; i > 1; --i) {
                if (numbers.get(i) < numbers.get(i-1)) {
                    return false;
                }
                if (Objects.equals(numbers.get(i), numbers.get(i-1)) && numbers.get(i) != 400000) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    static private void convert() {
        numbers = new ArrayList<>();
        for (int i = 0; i < tokens.size(); ++i) {
            char[] splitToken = tokens.get(i).toCharArray();
            try {
                for (index = 0; index < splitToken.length; ++index) {
                    toNumber(splitToken);
                }
                isQuote = false;
                numbers.add(temp);
                index = 0;
                total += temp;
            } catch (NumberFormatException ex) {
                throw new NumberFormatException(ex.toString());
            }
        }
    }
    
    static private void toNumber(char[] input) {
        switch(input[index]) {
            case LETTER_QUOTE:
                temp = isQuote ? 1000 : 1;
                isQuote = true; 
                break;
            case LETTER_B: 
                temp = isQuote ? 2000 : 2;
                break;
            case LETTER_G: 
                temp = isQuote ? 3000 : 3;
                break;
            case LETTER_D:
                temp = isQuote ? 4000 : 4;
                break;
            case LETTER_H: 
                temp = isQuote ? 5000 : 5;
                break;
            case LETTER_W: 
                temp = isQuote ? 6000 : 6;
                break;
            case LETTER_Z: 
                temp = isQuote ? 7000 : 7;
                break;
            case LETTER_X: 
                temp = isQuote ? 8000 : 8;
                break;
            case LETTER_T: 
                if (isLetterQuote(input[index + 1])) {
                    temp = isQuote ? 9000 : 9;
                    ++index;
                    break;
                }
                if (isLetterH(input[index + 1])) {
                    temp = isQuote ? 400000 : 400;
                    ++index;
                    break;   
                }
                throw new NumberFormatException("Wrong letter " + input[index]);
            case LETTER_Y: 
                temp = isQuote ? 10000 : 10;
                break;
            case LETTER_K: 
                if (isLetterH(input[index + 1])) {
                    temp = isQuote ? 20000 : 20;
                    ++index;
                    break; 
                }
                temp = isQuote ? 100000 : 100;
                break;
            case LETTER_L: 
                if (isLetterH(input[index + 1])) {
                    temp = isQuote ? 300000 : 300;
                    ++index;
                    break;   
                }
                temp = isQuote ? 30000 : 30;
                break;
            case LETTER_M: 
                temp = isQuote ? 40000 : 40;
                break;
            case LETTER_N: 
                temp = isQuote ? 50000 : 50;
                break;
            case LETTER_S: 
                if (isLetterApostrophe(input[index + 1])) {
                    temp = isQuote ? 90000 : 90;
                    ++index;
                    break;   
                }
                temp = isQuote ? 60000 : 60;
                break; 
            case LETTER_APOSTROPHE: 
                temp = isQuote ? 70000 : 70;
                break;
            case LETTER_P: 
                temp = isQuote ? 80000 : 80;
                break;
            case LETTER_R: 
                temp = isQuote ? 200000 : 200;
                break;
            default:
                throw new NumberFormatException("Wrong letter " + input[index]);
        }
    }
    
    static private boolean isLetterH(char letter) {
        try {
            return letter == LETTER_H;
        } catch (IndexOutOfBoundsException ex) {
            return false;
        }
    }
    
    static private boolean isLetterQuote(char letter) {
        return letter == LETTER_QUOTE;
    }
    
    static private boolean isLetterApostrophe(char letter) {
        try {
            return letter == LETTER_APOSTROPHE;
        } catch (IndexOutOfBoundsException ex) {
            return false;
        }
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
                        addToken(inputArray[index]);
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
}