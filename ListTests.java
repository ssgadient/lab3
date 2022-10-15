import static org.junit.Assert.*;
import org.junit.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class containsLetterX implements StringChecker {

    public boolean checkString(String s) {
      return s.contains("x") || s.contains("X");
    }
  
}

public class ListTests {
    
    @Test
    public void testFilter() {
        List<String> input1 = new ArrayList<String>(Arrays.asList("xylophone", "apples", "", "XxXx", "    x "));
        assertEquals(new ArrayList<String>(Arrays.asList("xylophone", "XxXx", "    x ")), ListExamples.filter(input1, new containsLetterX()));
    }

    @Test
    public void testMerge() {
        List<String> input1 = new ArrayList<String>(Arrays.asList("a", "c", "e"));
        List<String> input2 = new ArrayList<String>(Arrays.asList("b", "d", "f"));
    }

}