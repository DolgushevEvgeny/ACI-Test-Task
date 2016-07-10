package Data;

import Factory.VocabularyToken;
import java.util.ArrayList;

public class Roman {

    static public Object[] convert(String value) {
        double answer = 0;
        boolean isPositive = true;
        char[] array = value.toCharArray();
        for (int i = 0; i < array.length; ++i) {
            switch (array[i]) {
                case 'M': 
                    answer += 1000;
                    break;
                case 'D':
                    answer += 500;
                    break;
                case 'C':
                    if (i + 1 < array.length && (array[i+1] == 'D' || array[i+1] == 'M')) {
                        answer -= 100;
                    } else answer += 100;
                    break;
                case 'L': 
                    answer += 50;
                    break;
                case 'X':
                    if (i + 1 < array.length && (array[i+1] == 'L' || array[i+1] == 'C' || array[i+1] == 'D' || array[i+1] == 'M')) {
                        answer -= 10;
                    } else answer += 10;
                    break;
                case 'V':
                    answer += 5;
                    break;
                case 'I':
                    if (i + 1 < array.length && (array[i+1] == 'V' || array[i+1] == 'X' || array[i+1] == 'L' || array[i+1] == 'C' || array[i+1] == 'D' || array[i+1] == 'M')) {
                        answer -= 1;
                    } else answer += 1;
                    break;
                case '-':
                    isPositive = false;
                    break;
            }
        }
        
        Object[] obj = new Object[2];
        obj[0] = true;
        obj[1] = isPositive ? answer : answer * -1;
        return obj; 
    }
    
    static public boolean parse(String value, ArrayList<VocabularyToken> vocabulary) {
        String currentSymbol = value.substring(0, 1);
        int sameSymbol = 0;
        boolean isError = true;
        for (int i = 0; i < value.length(); ++i) {
            for (int j = 0; j < vocabulary.size(); ++j) {
                if (vocabulary.get(j).getKey().equals(value.substring(i, i+1))) {
                    isError = false;
                    break;
                }
            }
            
            if (!isError) {
                if (currentSymbol.equals(value.substring(i, i+1))) {
                    ++sameSymbol;
                    if (sameSymbol > 3) {
                        return false;
                    }
                } else {
                    currentSymbol = value.substring(i, i+1);
                    sameSymbol = 1;
                }
            }
        }
        
        return true;
    }
}
