import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

class SearchEngineHandler implements URLHandler {
    // The one bit of state on the server: a number that will be manipulated by
    // various requests.
    ArrayList<String> words = new ArrayList<>();

    public String handleRequest(URI url) {
        if (url.getPath().equals("/")) {
            if (words.size() == 0) {
                return "No words added yet!";
            }
            else {
                return String.format("Words: %s", words);
            }
        } else if (url.getPath().equals("/add")) {
            String[] parameters = url.getQuery().split("=");
            if (parameters[0].equals("s")) {
                words.add(parameters[1]);
                return String.format("Added %s to the list of words",
                        parameters[1]);
            }
        } else if (url.getPath().equals("/search")) {
            String[] parameters = url.getQuery().split("=");
            if (parameters[0].equals("s")) {
                ArrayList<String> foundWords = new ArrayList<>();
                for (String word : words) {
                    if (word.contains(parameters[1])) {
                        foundWords.add(word);
                    }
                }

                if (foundWords.size() == 0) {
                    return "No words found";
                } else {
                    return String.format("Found words: %s", foundWords);
                }
            }
        }

        return "404 Not Found!";
    }
}

class SearchEngine {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println(
                    "Missing port number! Try any number between 1024 to " +
                            "49151");
            return;
        }

        int port = Integer.parseInt(args[0]);

        Server.start(port, new SearchEngineHandler());
    }
}