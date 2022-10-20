# Week 2  
Code for the Simplest Search Engine:
```
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

class WordHandler implements URLHandler {
    // The one bit of state on the server: a number that will be manipulated by
    // various requests.
    ArrayList<String> database = new ArrayList<String>();

    public String handleRequest(URI url) {
        System.out.println(url);
        if (url.getPath().equals("/")) {
            return String.format("Number of database entries: " + Integer.toString(this.database.size()));
        } 
        else if (url.getPath().contains("/add")) {
            System.out.println("Path: " + url.getPath());
            String[] parameters = url.getQuery().split("=");
            if (parameters[0].equals("s")) {
                if (this.database.contains(parameters[1])) {
                    return String.format("\"%s\" already exists in database!", parameters[1]);
                }
                else {
                    this.database.add(parameters[1]);
                    return String.format("New string \"%s\" added!", parameters[1]);
                }
            }
            else {
                return "Bad format!";
            }
        } 
        else {
            System.out.println("Path: " + url.getPath());
            if (url.getPath().contains("/search")) {
                String[] parameters = url.getQuery().split("=");
                if (parameters[0].equals("s")) {
                    String os = "";
                    for (String s: this.database) {
                        if (s.contains(parameters[1])) {os += s + "\n";}
                    }
                    return os;
                }
            }
            return "404 Not Found!";
        }
    }
}

class SearchEngine {
    public static void main(String[] args) throws IOException {
        if(args.length == 0){
            System.out.println("Missing port number! Try any number between 1024 to 49151");
            return;
        }

        if(args[0].equals("22")){
            System.out.println("Did you know that port 22 is the one used by ssh? We can't use it for a web server.");
            return;
        }

        int port = Integer.parseInt(args[0]);

        Server.start(port, new WordHandler());
    }
}
```
---
Simplest Search Engine Add Method Screenshots and Explanations:  

![Image](https://ssgadient.github.io/CSE15L/lab-2/Add_Part1.PNG)  
![Image](https://ssgadient.github.io/CSE15L/lab-2/Add_Part2.PNG)  
![Image](https://ssgadient.github.io/CSE15L/lab-2/Add_Part3.PNG)  

When the program is first executed, it runs the main method, checking that the port number is valid (within the bounds 1024 to 49151 and not port 22, which is reserved for ssh). We then start the server on the inputed port and call the WordHandler class. After the database is instantiated and the url is gotten, we check the given path for the add query.  

`else if (url.getPath().contains("/add"))` 

We split the string around the equal sign to separate the "s" string from the string to be added. 

`String[] parameters = url.getQuery().split("=");`

The program then gets the string after the question mark and before the equals sign and checks if it is in the right format. It must have an "s" character right after the "?" in order to be considered a proper string query.  

`if (parameters[0].equals("s"))`  

We check if that string is already in the database. If it is, we inform the user. If it is not, then we add it to the database and send an output message. 
```
if (this.database.contains(parameters[1])) {
    return String.format("\"%s\" already exists in database!", parameters[1]);
}
else {
    this.database.add(parameters[1]);
    return String.format("New string \"%s\" added!", parameters[1]);
}
```

The relevant arguments to the add method are the "/add" phrase, followed by the "?s=" string to indicate the query, followed by the string to be added to the database. The string to be added does not change throughout the method.

---
Simplest Search Engine Search Method Screenshots and Explanations:

![Image](https://ssgadient.github.io/CSE15L/lab-2/Search_Part1.PNG)  
![Image](https://ssgadient.github.io/CSE15L/lab-2/Search_Part2.PNG)  

Instead of going into the else if clause for /add, we go into the else clause, which contains /search and an error message for any incorrect queries.  

```
else {
    System.out.println("Path: " + url.getPath());
    if (url.getPath().contains("/search"))
```
Once again, we split the string around the equal sign to separate the "s" string from the string to be searched. 

`String[] parameters = url.getQuery().split("=");`

Once we verify that the character after the "?" is an "s", we initialize our output string as empty. Then for each string in our database, we check if our search string is included inside that string. If it is, we add it to our output string alone with a newline character. Finally, we return our output string to the web page. 

```
String os = "";
for (String s: this.database) {
    if (s.contains(parameters[1])) {os += s + "\n";}
}
return os;
```  

The relevant arguments to the search method are the "/search" phrase, followed by the "?s=" string to indicate the query, followed by the string to be searched in the database. However, when iterating through the for loop, the string s changed to each entry in the database before being compared against the search term. Additionally, the os string was updated each time a database entry contained the search term. 

# Week 3
## Bug 1: Arrays  
Failure inducing input for reverseInPlace()
```
@Test 
public void testReverseInPlace() {
    int[] input1 = { 3 };
    int[] input2 = { 1, 2, 3, 4, 5 };
    ArrayExamples.reverseInPlace(input1);
    ArrayExamples.reverseInPlace(input2);
    assertArrayEquals(new int[]{ 3 }, input1);
    assertArrayEquals(new int[]{5, 4, 3, 2, 1}, input2);
}
```
Symptoms:  
```
2) testReverseInPlace(ArrayTests)
arrays first differed at element [3]; expected:<2> but was:<4>
        at org.junit.internal.ComparisonCriteria.arrayEquals(ComparisonCriteria.java:78)
        at org.junit.internal.ComparisonCriteria.arrayEquals(ComparisonCriteria.java:28)
        at org.junit.Assert.internalArrayEquals(Assert.java:534)
        at org.junit.Assert.assertArrayEquals(Assert.java:418)
        at org.junit.Assert.assertArrayEquals(Assert.java:429)
        at ArrayTests.testReverseInPlace(ArrayTests.java:12)
        ... 30 trimmed
Caused by: java.lang.AssertionError: expected:<2> but was:<4>
        at org.junit.Assert.fail(Assert.java:89)
        at org.junit.Assert.failNotEquals(Assert.java:835)
        at org.junit.Assert.assertEquals(Assert.java:120)
        at org.junit.Assert.assertEquals(Assert.java:146)
        at org.junit.internal.ExactComparisonCriteria.assertElementsEqual(ExactComparisonCriteria.java:8)
        at org.junit.internal.ComparisonCriteria.arrayEquals(ComparisonCriteria.java:76)
```
Explanation:  
reverseInPlace uses a for loop to set the elements at the beginning of the list to the values found at the end of the list, and increment by index. However, the for loop will eventually meet itself in the middle of the list. I tested this with the array {1, 2, 3, 4, 5}.  The error message says that the arrays differ at element <3> , which is true because we should be getting {5, 4, 3, 2, 1}, but instead, we get {5, 4, 3, 4, 5}. We can fix this by swapping the first and last element by using a temporary variable to hold information. Then, the elements earlier in the array would not be overwritten.

## Bug 2: Lists
Failure inducing input for filter()
```
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
    
}
```
Symptoms:
```
1) testFilter(ListTests)
java.lang.AssertionError: expected:<[xylophone, XxXx,     x ]> but was:<[    x , XxXx, xylophone]>
        at org.junit.Assert.fail(Assert.java:89)
        at org.junit.Assert.failNotEquals(Assert.java:835)
        at org.junit.Assert.assertEquals(Assert.java:120)
        at org.junit.Assert.assertEquals(Assert.java:146)
        at ListTests.testFilter(ListTests.java:20)
```
Explanation:  
For testing the filter method, I created a new class called containsLetterX, which checks if a string contains the character x. I used an input list {“xylophone”, “apple”, “”, “XxXx”, “    x “} to check for failures. The symptoms are {“    x “, “XxXx”, “xylophone”} instead of the expected {“xylophone”, “XxXx”, “    x “}. Since the comment above the method clearly says that the new list should retain the order found in the original list, our bug is that the method creates a return list and copies elements to the 0th index, as shown in the code `result.add(0, s);`. We can fix this by adding to the end of the list using `result.add(s)` instead of to the beginning. 
