import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Six_Degrees {

    public HashMap<Integer, String> IDtoActor = new HashMap<>();
    private HashMap<String, Integer> ActortoID = new HashMap<>();
    private Graph myGraph = new Graph(10000000);

    public void readData(Path inputPath) {
        BufferedReader reader = null;
        String line;
        int numTimes = 0;
        String name;
        String cast;
        long ID;
        boolean finishedReading = false; // because not every movie is in JSON format
        while (!finishedReading) {
            try {
                // reading the CSV information
                reader = Files.newBufferedReader(inputPath);

                line = reader.readLine(); // this line surprisingly is the key to reading next line

                // start Apache CSV
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                        .withHeader("movie_id", "title", "cast", "crew")
                        .withIgnoreHeaderCase()
                        .withTrim());
                JSONParser parser = new JSONParser();
                //Reader myReader = new FileReader("C:\\Users\\jghuynh\\Downloads\\" +
                //      "tmdb-5000-movie-dataset\\tmdb_5000_credits.csv");

                for (CSVRecord movie : csvParser) {
                    // access the "cast" column in csv
                    cast = movie.get("cast");
                    String movieTitle = movie.get("title");
                    // cast = a huuuge JSON Array that has info for all actors in 1 movie

                    // put cast into JSON Array so we can parse it
                    JSONArray jsonArray = (JSONArray) parser.parse(cast);
                    ArrayList<Integer> actorsPerMovie = new ArrayList<>();

                    // getting the name portion from cast JSON array for every movie
                    int previousID = 0;
                    Iterator itr1 = jsonArray.iterator();
                    while (itr1.hasNext()) {

                        JSONObject actorInfo = (JSONObject) itr1.next();

                        // Accessing the "name" portion and cast ID
                        ID = (long) actorInfo.get("id");
                        name = (String) actorInfo.get("name");

                        IDtoActor.put((int) ID, name);
                        ActortoID.put(name, (int) ID);
                        actorsPerMovie.add((int) ID);
                        if (previousID != 0) {
                            myGraph.addEdge(previousID, ID);
                        }
                        previousID = (int) ID;
                    }

                    for (int index1 = 0; index1 < actorsPerMovie.size(); index1++) {
                        for (int index2 = index1 + 1; index2 < actorsPerMovie.size(); index2++) {
                            myGraph.addEdge(actorsPerMovie.get(index1), actorsPerMovie.get(index2));
                        }
                    }
                }
                finishedReading = true;

            } catch (IOException e) {
                System.out.println("Oops! File does not exist!");
            } catch (ParseException e) {
                System.out.println("Oops! Possibly wrong format.");
            }

        }
    }

    /**
     * Transforms a path full of IDs into a path full of actor names, then prints it
     * @param IDPath the ArrayList path filled with IDs
     */
    public void convertToActor(ArrayList<Integer> IDPath) {
        System.out.println("Inside Convert To Actor function");
        for (int index = IDPath.size() - 1; index >= 0; index--) {
            if (index >= 1) {
                System.out.print(IDtoActor.get(index) + " --> ");
            }
            else {
                System.out.println(IDtoActor.get(index));
            }
        }
    }

    /**
     * Finds the shortes path between 2 actors
     * @param actor1 the first actor
     * @param actor2 the second actor
     */
    private void findPath(String actor1, String actor2) {
        // fix spelling
        actor1 = actor1.toLowerCase();
        actor2 = actor2.toLowerCase();
        // and check if in hashMap
        char actor1Split[] = actor1.toCharArray();
        for (int index = 0; index < actor1.length(); index ++)
        {
            // if index now points to a first character of a word
            if (index == 0 && actor1Split[index] != ' ' ||
                    actor1Split[index] != ' ' && actor1Split[index - 1] == ' ') {
                actor1Split[index] = (char) (actor1Split[index] - 'a' + 'A');
            }
        }

        // check spelling of actor 2
        char actor2Split[] = actor2.toCharArray();
        for (int index = 0; index < actor2.length(); index ++)
        {
            // if index now points to a first character of a word
            if (index == 0 && actor2Split[index] != ' ' ||
                    actor2Split[index] != ' ' && actor2Split[index - 1] == ' ') {
                actor2Split[index] = (char) (actor2Split[index] - 'a' + 'A');
            }
        }
        actor1 = new String(actor1Split);
        actor2 = new String(actor2Split);

        int ID1;
        int ID2;
        if (ActortoID.get(actor1) != null && ActortoID.get(actor2) != null && !ActortoID.get(actor1).equals(ActortoID.get(actor2))) {
            ID1 = ActortoID.get(actor1);
            ID2 = ActortoID.get(actor2);
            LinkedList<Integer> path = myGraph.printShortestDistance(ID1, ID2);

            for (int index = 0; index < path.size(); index ++) {
                int ID = path.get(index);
                if (index < path.size() - 1) {

                    System.out.print(IDtoActor.get(ID) + " --> ");
                }
                else {
                    System.out.print(IDtoActor.get(ID));
                }
            }
            System.out.println();
            System.out.println("非常非常好！");
        }
        else {
            System.out.println("No such actor exists!");
            return;
        }
    }

    public static void main(String[] args) {
        Six_Degrees myCSV = new Six_Degrees();
//        myCSV.readData(Paths.get("C:\\Users\\jghuynh\\Downloads\\tmdb-5000-movie-dataset" +
//                "\\tmdb_5000_credits.csv"));
        myCSV.readData(Paths.get(args[0]));

        Scanner myScanner = new Scanner(System.in);  // Create a Scanner object
        System.out.print("Actor 1: ");
        String actor1 = myScanner.nextLine();  // Read user input
        System.out.print("Actor 2: ");
        String actor2 = myScanner.nextLine();
        myCSV.findPath(actor1, actor2);

    }
}
