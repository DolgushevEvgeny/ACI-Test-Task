package numericsystems;

import java.io.IOException;
import java.io.FileNotFoundException;

import Reader.Reader;

public class NumericSystems {
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        Reader reader = new Reader();
        reader.read();
    }
}
