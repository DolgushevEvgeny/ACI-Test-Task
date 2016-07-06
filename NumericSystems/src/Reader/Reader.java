package Reader;

import Factory.NumberFactory;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Reader {
    public static int maxPriority = 1;
    public static PrevToken prevToken = null;
    private static ArrayList<Token> tokens = null;
    
    enum PrevToken {
        Token,
        Sign,
        None
    }
    
    public Reader() {}
    
    public void read() throws FileNotFoundException, IOException {
        try {
            NumberFactory factory = new NumberFactory();
        } catch (FileNotFoundException ex) {
            System.out.println("Can't load vocabulary file.");
            return;
        }
        
        FileReader fr;
        try {
            fr = new FileReader("C:\\Users\\EugeneDolgushev\\Documents\\GitHub\\ACI-Test-Task\\test.txt");
        } catch (FileNotFoundException ex) {
            System.out.println("Can't load test file.");
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
            
            char[] array = parsedExpression[0].toCharArray();
            String current = "";
            prevToken = PrevToken.None;
            
            tokens = new ArrayList<>();
            
            for (int j = 0; j < array.length; ++j) {
                if (array[j] == ' ') {
                    continue;
                } else {
                    if (array[j] != '+' && array[j] != '*' && array[j] != '/') {
                        if (array[j] != '-') {
                            current += array[j];
                            prevToken = PrevToken.Token;
                        } else {
                            if (array[j] == '-' && (prevToken == PrevToken.None || prevToken == PrevToken.Sign)) {
                                tokens = addSignToken(array[j]);
                                prevToken = PrevToken.Token;
                            } else {
                                try {
                                    tokens.add(new Token(NumberFactory.parse(current), 1));
                                    current = "";
                                    tokens = addSignToken(array[j]);
                                    prevToken = PrevToken.Sign;
                                } catch (NumberFormatException ex) {
                                    System.out.println(ex + current + " Unknown number format in " + parsedExpression[0]);
                                    isError = true;
                                    break;
                                } catch (StringIndexOutOfBoundsException ex) {
                                    System.out.println(ex + "in " + parsedExpression[0]);
                                    isError = true;
                                    break;
                                }
                            }
                        }
                    } else {
                        if (!current.equals("")) {
                                try {
                                    tokens.add(new Token(NumberFactory.parse(current), 1));
                                    current = "";
                                    tokens = addSignToken(array[j]);
                                    prevToken = PrevToken.Sign;
                                } catch (NumberFormatException ex) {
                                    System.out.println(ex + current + " Unknown number format in " + parsedExpression[0]);
                                    isError = true;
                                    break;
                                } catch (StringIndexOutOfBoundsException ex) {
                                    System.out.println(ex + "in " + parsedExpression[0]);
                                    isError = true;
                                    break;
                                }
                        } else tokens = addSignToken(array[j]);
                    }
                }
            }
            
            if (!isError) {
                try {
                    tokens.add(new Token(NumberFactory.parse(current), 1));
                    try {
                        double equationResult = calc();
                        System.out.println(value + " - " + (answer == equationResult) + " " + equationResult);
                    } catch (NumberFormatException ex) {
                        System.out.println(ex.toString());
                    }
                } catch (NumberFormatException ex) {
                    System.out.println(ex.toString());
                }
            }
            tokens.clear();
        }
        
        textReader.close();
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
                        case 2: PlusMinusOperation(i); break;
                        case 3: MultDivisionOperation(i); break;
                        case 4: UnaryMinus(i); break;
                    }
                    --i;
                }
            }
            maxPriority -= 1;
        }
        
        maxPriority = 1;
        return tokens.get(0).getValue();
    }
    
    static private void PlusMinusOperation(int index) {
        double first = tokens.get(index-1).getValue();
        double second = tokens.remove(index+1).getValue();
        double sign = tokens.remove(index).getValue();
        switch((int)sign) {
            case 1:
                tokens.set(index-1, new Token(first + second, 1));
                break;
            case 2:
                tokens.set(index-1, new Token(first - second, 1));
                break;
        }
    }
    
    static private void MultDivisionOperation(int index) {
        double first = tokens.get(index-1).getValue();
        double second = tokens.remove(index+1).getValue();
        double sign = tokens.remove(index).getValue();
        switch((int)sign) {
            case 3:
                tokens.set(index-1, new Token(first * second, 1));
                break;
            case 4:
                if (second == 0) {
                    throw new NumberFormatException("Division By Zero");
                }
                tokens.set(index-1, new Token(first / second, 1));
                break;
        }
    }
    
    static private void UnaryMinus(int index) {
        double first = tokens.remove(index+1).getValue();
        tokens.set(index, new Token(first*-1, 1));
    }
}
