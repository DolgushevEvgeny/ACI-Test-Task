package Reader;

public class Token {
    private final double value;
    private final int priority;
    
    public Token(double input, int key) {
        this.value = input;
        this.priority = key;
    }
    
    public double getValue() {
        return this.value;
    }
    
    public int getPriority() {
        return this.priority;
    }
}
