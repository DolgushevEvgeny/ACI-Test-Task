package Data;

public class Arabic {

    static public Object[] convert(String value) {
        double answer;
        
        try {
            answer = Double.parseDouble(value);
        } 
        catch (NumberFormatException ex) {
            Object[] obj = new Object[2];
            obj[0] = false;
            obj[1] = 0;
            return obj;
        }
        
        Object[] obj = new Object[2];
        obj[0] = true;
        obj[1] = answer;
        return obj;
    }

    static public boolean parse(String value) {
        try {
            Double.parseDouble(value);
        }
        catch (NumberFormatException ex) {
            return false;
        }
        
        return true;
    }
}
