Code for the Simplest Search Engine from week 2:
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

