package Factory;

import Data.Arabic;
import Data.Hebrem;
import Data.Roman;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NumberFactory {
    static ArrayList<ArrayList<VocabularyToken>> vocabularyList = null;
    
    public NumberFactory() throws FileNotFoundException {
        NumberFactory.vocabularyList = new ArrayList<>();
        loadVocabulary();
    }
    
    private void loadVocabulary() throws FileNotFoundException {
        FileReader fr = new FileReader("C:\\Users\\EugeneDolgushev\\Documents\\GitHub\\ACI-Test-Task\\vocabulary.txt");
        BufferedReader textReader = new BufferedReader(fr);
        
        String alphabet;
        try {
            while ((alphabet = textReader.readLine()) != null) {
                String[] parse = alphabet.split(";");
                ArrayList<VocabularyToken> vocabulary = new ArrayList<>();
                
                for (int i = 0; i < parse.length; ++i) {
                    //String[] element = parse[i].split(":");
                    VocabularyToken token = new VocabularyToken(parse[i]);
                    vocabulary.add(token);
                }
                
                vocabularyList.add(vocabulary);
            }
        } catch (IOException ex) {
            Logger.getLogger(NumberFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    static public double parse(String number) {
        if (number.length() == 0) {
            throw new StringIndexOutOfBoundsException("Empty argument ");
        }
        for (int i = 0; i < vocabularyList.size(); ++i) {
            if (parseString(vocabularyList.get(i), number)) {
                switch (i) {
                    case 0: 
                        try {
                            return getRomanValue(number, vocabularyList.get(i));
                        } catch (NumberFormatException ex) {
                            throw new NumberFormatException(ex.toString());
                        }
                        
                    case 1: 
                        Object[] answer = Arabic.convert(number);
                        if (!(boolean)answer[0]) {
                            throw new NumberFormatException("Can't convert Arabic number " + number);
                        }
                        return (double)answer[1];
                    case 2:
                        try {
                            return Hebrem.convert(number);
                        } catch (NumberFormatException ex) {
                            throw new NumberFormatException(ex.toString());
                        }
                }
            }
        }
        
        throw new NumberFormatException("Can't parse number: ");
    }
    
    static private boolean parseString(ArrayList<VocabularyToken> vocabulary, String input) {
        for (int i = 0; i < input.length(); ++i) {
            if (!findInVocabulary(vocabulary, input.substring(i, i+1))) {
                return false;
            }
        }
        return true;
    }
    
    static private boolean findInVocabulary(ArrayList<VocabularyToken> vocabulary, String input) {
        for (int i = 0; i < vocabulary.size(); ++i) {
            if (input.equals(vocabulary.get(i).getKey())) {
                return true;
            }
        }
        return false;
    }
    
    static private double getRomanValue(String value, ArrayList<VocabularyToken> vocabulary) {
        if (Roman.parse(value, vocabulary)) {
            Object[] answer = Roman.convert(value);
            if (!(boolean)answer[0]) {
                throw new NumberFormatException("Can't convert Roman number ");
            }
            return (double) answer[1];
        }
        
        throw new NumberFormatException("Can't parse Roman number ");
    }
}