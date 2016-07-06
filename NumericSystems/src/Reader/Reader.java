package Reader;

import Factory.NumberFactory;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Reader {
    public static int maxPriority = 1; 
    
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
            fr = new FileReader("C:\\test.txt");
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
            PrevToken prevToken = PrevToken.None;
            
            ArrayList<Token> tokens = new ArrayList<>();
            
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
                                current += array[j];
                            } else {
                                try {
                                    tokens.add(new Token(NumberFactory.parse(current), 1));
                                    current = "";
                                    tokens = addSignToken(array[j], tokens);
                                    prevToken = PrevToken.Sign;
                                } catch (NumberFormatException ex) {
                                    System.out.println(ex + current + " Unknown number format in " + parsedExpression[0]);
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
                                    tokens = addSignToken(array[j], tokens);
                                    prevToken = PrevToken.Sign;
                                } catch (NumberFormatException ex) {
                                    System.out.println(ex + current + " Unknown number format in " + parsedExpression[0]);
                                    isError = true;
                                    break;
                                }
                        } else tokens = addSignToken(array[j], tokens);
                    }
                }
            }
            
            if (!isError) {
                try {
                    tokens.add(new Token(NumberFactory.parse(current), 1));
                    try {
                        double equationResult = calc(tokens);
                        System.out.print(value);
                        System.out.print(" - " + (answer == equationResult));
                        System.out.println(" " + equationResult);
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
    
    static ArrayList<Token> addSignToken(char sign, ArrayList<Token> tokens) {
        switch (sign) {
            case '+' :
                maxPriority = maxPriority > 2 ? maxPriority : 2;
                tokens.add(new Token(1, 2));
                break;
            case '-' :
                maxPriority = maxPriority > 2 ? maxPriority : 2;
                tokens.add(new Token(2, 2));
                break;
            case '*' :
                maxPriority = 3;
                tokens.add(new Token(3, 3));
                break;
            case '/' :
                maxPriority = 3;
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
    
    static private double calc(ArrayList<Token> tokens) {
        double total = 0;
        while (tokens.size() != 1) {
            for (int i = 0; i < tokens.size(); ++i) {
                if (tokens.get(i).getPriority() == maxPriority) {
                    double first = tokens.get(i-1).getValue();
                    double second = tokens.remove(i+1).getValue();
                    double sign = tokens.remove(i).getValue();
                    try {
                        total = doOperation(sign, first, second);
                    } catch (NumberFormatException ex) {
                        throw new NumberFormatException("Division By Zero");
                    }
                    
                    tokens.set(i-1, new Token(total, 1));
                    --i;
                }
            }
            maxPriority -= 1;
        }
        
        maxPriority = 1;
        return total;
    }
    
    static private double doOperation(double sign, double first, double second) {
        double total = 0;
        switch ((int)sign) {
            case 1:
                total = first + second;
                break;
            case 2:
                total = first - second;
                break;
            case 3:
                total = first * second;
                break;
            case 4:
                if (second == 0) {
                    throw new NumberFormatException("Division By Zero");
                }
                total = first / second;
                break;
        }
        
        return total;
    }
}
