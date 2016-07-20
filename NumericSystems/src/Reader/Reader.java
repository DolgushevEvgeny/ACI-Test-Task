package Reader;

import Factory.NumberFactory;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Reader {
    private static int maxPriority = 1;
    private static PrevToken prevToken = null;
    private static ArrayList<Token> tokens = null;
    private static final String TESTFILE_PATH = "C:\\Users\\EugeneDolgushev\\Documents\\GitHub\\ACI-Test-Task\\test.txt";
    private static final String ERROR_DIVISION_BY_ZERO = "Division By Zero";
    private static final String ERROR_UNKNOWN_NUMBER_FORMAT = " Unknown number format in ";
    private static final String VOCABULARY_FILE_ERROR = "Can't load vocabulary file";
    private static final String TEST_FILE_ERROR = "Can't load test file";
    
    private static char[] string = null;
    private static String currentToken;
    private static int index = 0;
    private static boolean isDivider;
    private static int countToError;
    private static final String[] symbols = {"+", "-", "*", "/"};
    private static final ArrayList<String> dividers = new ArrayList<>(Arrays.asList(symbols));
    
    enum PrevToken {
        Token,
        Sign,
        UnaryMinus,
        None
    }
    
    public Reader() {}
    
    public void read() throws FileNotFoundException, IOException {
        try {
            NumberFactory factory = new NumberFactory();
        } catch (FileNotFoundException ex) {
            System.out.println(VOCABULARY_FILE_ERROR);
            return;
        }
        
        FileReader fr;
        try {
            fr = new FileReader(TESTFILE_PATH);
        } catch (FileNotFoundException ex) {
            System.out.println(TEST_FILE_ERROR);
            return;
        }
        
        BufferedReader textReader = new BufferedReader(fr);
        
        String value;
        boolean isError;
        while ((value = textReader.readLine()) != null) {
            isError = false;
            String[] parsedExpression = value.split("=");
            double answer;
            try {
                answer = getAnswer(parsedExpression[1]);
            } catch (NumberFormatException ex) {
                System.out.println(ex + " Can't parse answer " + parsedExpression[1] + " in " + value);
                continue;
            }           
            
            string = parsedExpression[0].toCharArray();
            prevToken = PrevToken.None;
            
            tokens = new ArrayList<>();
            try {
                isDivider = true;
                index = 0;
                countToError = 0;
                getToken();
                try {
                    double equationResult = calc();
                    System.out.println(value + " - " + (answer == equationResult) + " " + equationResult);
                } catch (NumberFormatException ex) {
                    System.out.println(ex.toString());
                }
            } catch (NumberFormatException | StringIndexOutOfBoundsException ex) {
                System.out.println(ex + parsedExpression[0]);
            }
            
            tokens.clear();
        }
        
        textReader.close();
    }
    
    static private void getToken() {
        currentToken = "";
        for (int i = index; index < string.length; ++index) {
            if (!dividers.contains(String.valueOf(string[index]))) {
                currentToken += String.valueOf(string[index]);
                isDivider = false;
            } else {
                if (isDivider) {
                    currentToken += String.valueOf(string[index]);
                    ++countToError;
                }
                if (countToError > 2) {
                    throw new NumberFormatException("To many operators ");
                }
                isDivider = true;
                
                currentToken = currentToken.trim();
                if (!currentToken.isEmpty()) {
                    try {
                        isUnaryMinusSign();
                    } catch (NumberFormatException ex) {
                        throw new NumberFormatException(ex.toString());
                    } catch (StringIndexOutOfBoundsException ex) {
                        throw new StringIndexOutOfBoundsException(ex.toString());
                    }
                }
            }
        }
        
        currentToken = currentToken.trim();
        if (!currentToken.isEmpty()) {
            try {
                isUnaryMinusSign();
            } catch (NumberFormatException ex) {
                throw new NumberFormatException(ex.toString());
            } catch (StringIndexOutOfBoundsException ex) {
                throw new StringIndexOutOfBoundsException(ex.toString());
            }
        }
    }
    
    static private void isUnaryMinusSign() {
        if (currentToken.equals("-") && (prevToken == PrevToken.None || prevToken == PrevToken.Sign)) {
            tokens = addSignToken('-');
            prevToken = PrevToken.UnaryMinus;
            getToken();
        } else {
            try {
                isDivisionSign(); 
            } catch (NumberFormatException ex) {
                throw new NumberFormatException(ex.toString());
            } catch (StringIndexOutOfBoundsException ex) {
                throw new StringIndexOutOfBoundsException(ex.toString());
            }
        }
    }
    
    static private void isDivisionSign() {
        if (currentToken.equals("/") && prevToken == PrevToken.Token) {
            tokens = addSignToken('/');
            prevToken = PrevToken.Sign;
            getToken();
        } else {
            try {
                isMultipleSign();
            } catch (NumberFormatException ex) {
                throw new NumberFormatException(ex.toString());
            } catch (StringIndexOutOfBoundsException ex) {
                throw new StringIndexOutOfBoundsException(ex.toString());
            }
        }
    }
    
    static private void isMultipleSign() {
        if (currentToken.equals("*") && prevToken == PrevToken.Token) {
            tokens = addSignToken('*');
            prevToken = PrevToken.Sign;
            getToken();
        } else {
            try {
                isSubtractingSign();
            } catch (NumberFormatException ex) {
                throw new NumberFormatException(ex.toString());
            } catch (StringIndexOutOfBoundsException ex) {
                throw new StringIndexOutOfBoundsException(ex.toString());
            }
        }
    }
    
    static private void isSubtractingSign() {
        if (currentToken.equals("-") && prevToken == PrevToken.Token) {
            tokens = addSignToken('-');
            prevToken = PrevToken.Sign;
            getToken();
        } else {
            try {
                isAdditingSign();
            } catch (NumberFormatException ex) {
                throw new NumberFormatException(ex.toString());
            } catch (StringIndexOutOfBoundsException ex) {
                throw new StringIndexOutOfBoundsException(ex.toString());
            }
        }
    }
    
    static private void isAdditingSign() {
        if (currentToken.equals("+") && prevToken == PrevToken.Token) {
            tokens = addSignToken('+');
            prevToken = PrevToken.Sign;
            getToken();
        } else {
            try {
                isNumber();
            } catch (NumberFormatException ex) {
                throw new NumberFormatException(ex.toString());
            } catch (StringIndexOutOfBoundsException ex) {
                throw new StringIndexOutOfBoundsException(ex.toString());
            }
        }
    }
    
    static private void isNumber() {
        try {
            tokens.add(new Token(NumberFactory.parse(currentToken), 1));
            prevToken = PrevToken.Token;
            countToError = 0;
            getToken();
        } catch (NumberFormatException ex) {
            throw new NumberFormatException(ex.toString() + currentToken + ERROR_UNKNOWN_NUMBER_FORMAT);
        } catch (StringIndexOutOfBoundsException ex) {
            throw new StringIndexOutOfBoundsException(ex.toString() + "in ");
        }
    }
    
    static ArrayList<Token> addSignToken(char sign) {
        switch (sign) {
            case '+' :
                maxPriority = maxPriority > 2 ? maxPriority : 2;
                tokens.add(new Token(1, 2));
                break;
            case '-' :
                if (prevToken == PrevToken.None || prevToken == PrevToken.Sign) {
                    maxPriority = 4;
                    tokens.add(new Token(5, 4));
                    break;
                }
                maxPriority = maxPriority > 2 ? maxPriority : 2;
                tokens.add(new Token(2, 2));
                break;
            case '*' :
                maxPriority = maxPriority > 3 ? maxPriority : 3;
                tokens.add(new Token(3, 3));
                break;
            case '/' :
                maxPriority = maxPriority > 3 ? maxPriority : 3;
                tokens.add(new Token(4, 3));
                break;
        }
        ++index;
        return tokens;
    }
    
    static double getAnswer(String value) {
        value = value.trim();
        try {
            return NumberFactory.parse(value);
        } catch (NumberFormatException ex) {
            throw new NumberFormatException(ex.toString());
        }
    }
    
    static private double calc() {
        while (tokens.size() != 1) {
            for (int i = 0; i < tokens.size(); ++i) {
                if (tokens.get(i).getPriority() == maxPriority) {
                    switch(maxPriority) {
                        case 2: PlusMinusOperation(i, tokens.get(i-1).getValue(),
                                tokens.remove(i+1).getValue(), tokens.remove(i).getValue()); break;
                        case 3: MultDivisionOperation(i, tokens.get(i-1).getValue(),
                                tokens.remove(i+1).getValue(), tokens.remove(i).getValue()); break;
                        case 4: UnaryMinus(i, tokens.remove(i+1).getValue()); break;
                    }
                    --i;
                }
            }
            maxPriority -= 1;
        }
        
        maxPriority = 1;
        return tokens.get(0).getValue();
    }
    
    static private void PlusMinusOperation(int index, double first, double second, double sign) {
        switch((int)sign) {
            case 1:
                tokens.set(index-1, new Token(first + second, 1));
                break;
            case 2:
                tokens.set(index-1, new Token(first - second, 1));
                break;
        }
    }
    
    static private void MultDivisionOperation(int index, double first, double second, double sign) {
        switch((int)sign) {
            case 3:
                tokens.set(index-1, new Token(first * second, 1));
                break;
            case 4:
                if (second == 0) {
                    throw new NumberFormatException(ERROR_DIVISION_BY_ZERO);
                }
                tokens.set(index-1, new Token(first / second, 1));
                break;
        }
    }
    
    static private void UnaryMinus(int index, double first) {
        tokens.set(index, new Token(first*-1, 1));
    }
}
