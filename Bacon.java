import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Bacon {

    public static String centerActor;
    static HashMap<String, ArrayList<String>> actors = new HashMap<String, ArrayList<String>>();// Stores the movies
												// that
    // each actor took part in
    static HashMap<String, ArrayList<String>> movies = new HashMap<String, ArrayList<String>>();// Stores the actors who
    // participated in each
    // movie

    public static String help() {
	return "The implemented commands are:  quit, find <name>, recenter <name>, avgdist, circles, topcenter <n>";
    }

    public static void quit() {
	System.exit(0);
    }

    private static HashMap<String, Vertex> getMapWithValues(HashMap<String, Vertex> map,
	    HashMap<String, ArrayList<String>> actors, HashMap<String, ArrayList<String>> movies) {
	for (String key : map.keySet()) {
	    ArrayList<Vertex> values = new ArrayList<Vertex>();
	    Vertex v = map.get(key);
	    if (actors.containsKey(key)) {
		ArrayList<String> films = actors.get(key);
		for (String x : films) {
		    Vertex movie = map.get(x);
		    values.add(movie);
		}
	    } else if (movies.containsKey(key)) {
		ArrayList<String> actorss = movies.get(key);
		for (String x : actorss) {
		    Vertex actor = map.get(x);
		    values.add(actor);
		}
	    }
	    v.setValues(values);
	    map.put(key, v);
	}
	return map;
    }

    public static HashMap<String, Vertex> graphGenerator(HashMap<String, ArrayList<String>> actors,
	    HashMap<String, ArrayList<String>> movies) {
	// CREATING MY GRAPH MAP
	HashMap<String, Vertex> map = new HashMap<String, Vertex>();
	for (String key : actors.keySet()) {
	    Vertex vertex = new Vertex(key);
	    map.put(key, vertex);
	}
	for (String key : movies.keySet()) {
	    Vertex vertex = new Vertex(key);
	    map.put(key, vertex);
	}

	map = getMapWithValues(map, actors, movies);
	/*
	 * /TESTING PRINTING THE MAP for(String key : map.keySet()) {
	 * System.out.print(key + ": "); for(Vertex v: map.get(key).getValues()) {
	 * System.out.print(v.getContent() + " "); //for(Vertex v2 : v.getValues()) {
	 * //System.out.print(v2.getContent() + " "); //} //System.out.println(); }
	 * System.out.println(); }//
	 */// Done Testing! IT IS WORKING!!!
	return map;
    }

    public static void circles(HashMap<String, ArrayList<String>> actors, HashMap<String, Vertex> map) {
	// Takes a very long time with large databases but it works perfectly with small
	// databases
	HashMap<Integer, ArrayList<String>> circles = new HashMap<Integer, ArrayList<String>>();
	int baconNo = 0;
	int i = 0;
	for (String key : actors.keySet()) {

	    if (!map.get(key).isVisted()) {
		continue;
	    }
	    ArrayList<Vertex> temPath = findHelper(key, map);
	    baconNo = temPath.size() / 2;
	    if (!circles.containsKey(baconNo)) {
		ArrayList<String> list = new ArrayList<String>();
		list.add(key);
		circles.put(baconNo, list);
		continue;
	    }
	    if (circles.get(baconNo).contains(key)) {
		continue;// IS THERE AN ERROR HERE? i WANTED TO AVOID DUPLICATION OF ENTRIES
	    }
	    ArrayList<String> list = circles.get(baconNo);
	    list.add(key);
	    circles.put(baconNo, list);
	}
	// System.out.println("TESTTTT");//TEST
	for (Integer key : circles.keySet()) {
	    System.out.print(key + " " + circles.get(key).size() + " [");
	    i = 0;
	    for (String x : circles.get(key)) {
		if (i == 2 || circles.get(key).size() == i + 1) {
		    System.out.print(x);
		    break;
		}
		if (i > 2) {
		    break;
		}
		System.out.print(x + ", ");
		i++;
	    }
	    System.out.print("]");
	    System.out.println();

	}
    }

    public static String avgDistance(HashMap<String, Vertex> map, HashMap<String, ArrayList<String>> actors) {
	int totalBaconNumber = 0;
	int reachable = 0;
	int nonreachable = 0;
	double avgDistance = 0;
	for (String key : map.keySet()) {
	    if (!actors.containsKey(key)) {
		continue;
	    }
	    if (map.get(key).isVisted()) {
		reachable++;
	    }
	    if (!map.get(key).isVisted()) {
		nonreachable++;
		continue;
	    }
	    ArrayList<Vertex> temPath = findHelper(key, map);
	    totalBaconNumber += temPath.size() / 2;
	}
	avgDistance = (double) totalBaconNumber / reachable;
	System.out.println(avgDistance + " " + centerActor + " " + "(" + reachable + "," + nonreachable + ")"); // GET
														// IT
														// BACK
														// TODO
	String output = avgDistance + " " + centerActor + " " + "(" + reachable + "," + nonreachable + ")";
	return output;
	// System.out.println(avgDistance);//TEST TODO
    }

    public static double avgDistanceHelper(HashMap<String, Vertex> map, HashMap<String, ArrayList<String>> actors) {
	int totalBaconNumber = 0;
	int reachable = 0;
	int nonreachable = 0;
	double avgDistance = 0;
	for (String key : map.keySet()) {
	    if (!actors.containsKey(key)) {
		continue;
	    }
	    if (map.get(key).isVisted()) {
		reachable++;
	    }
	    if (!map.get(key).isVisted()) {
		nonreachable++;
		continue;
	    }
	    ArrayList<Vertex> temPath = findHelper(key, map);
	    totalBaconNumber += temPath.size() / 2;
	}
	avgDistance = (double) totalBaconNumber / reachable;
	return avgDistance;
    }

    public static ArrayList<Vertex> find(String search, HashMap<String, Vertex> map) {
	if ((!map.containsKey(search))) {
	    System.out.println(search + " doesn't appear in the database");
	    return new ArrayList<Vertex>();// BREAK WHEN IN METHOD TODO
	}
	ArrayList<Vertex> path = new ArrayList<Vertex>();
	Vertex v = map.get(search);
	// Get out if the name is not connected to the center
	if (v.getPredecessor() == null && !v.isCenterActor()) {
	    System.out.println(search + " is not reachable");// GET IT BACK TODO
	    return new ArrayList<Vertex>();
	}
	while (v.getPredecessor() != null) {
	    path.add(v);
	    v = v.getPredecessor();
	}
	path.add(map.get(centerActor));
	// TEST Printing Path
	ArrayList<Vertex> pathFinal = new ArrayList<Vertex>();// the final path in correct order
	for (int i = path.size() - 1; i >= 0; i--) {
	    pathFinal.add(path.get(i));
	    if (i == 0) {
		int baconNumber = pathFinal.size() / 2;
		System.out.print(path.get(i).getContent() + "  (" + baconNumber + ")"); // GET IT BACK TODO
		break;
	    }
	    System.out.print(path.get(i).getContent() + " --> "); // GET IT BACK TODO
	}
	return pathFinal;
    }

    public static ArrayList<Vertex> findHelper(String search, HashMap<String, Vertex> map) {
	if (!map.containsKey(search)) {
	    // System.out.println(search + " doesn't appear in the database");
	    return new ArrayList<Vertex>();// BREAK WHEN IN METHOD TODO
	}
	ArrayList<Vertex> path = new ArrayList<Vertex>();
	Vertex v = map.get(search);
	// Get out if the name is not connected to the center
	if (v.getPredecessor() == null) {
	    // System.out.println(search + " is not reachable");//GET IT BACK TODO
	    return new ArrayList<Vertex>();
	}
	while (v.getPredecessor() != null) {
	    path.add(v);
	    v = v.getPredecessor();
	}
	path.add(map.get(centerActor));
	// TEST Printing Path
	ArrayList<Vertex> pathFinal = new ArrayList<Vertex>();// the final path in correct order
	for (int i = path.size() - 1; i >= 0; i--) {
	    pathFinal.add(path.get(i));
	    if (i == 0) {
		int baconNumber = pathFinal.size() / 2;
		// System.out.print(path.get(i).getContent() + " (" + baconNumber + ")"); //GET
		// IT BACK TODO
		break;
	    }
	    // System.out.print(path.get(i).getContent() + " --> "); //GET IT BACK TODO
	}
	return pathFinal;
    }

    public static HashMap<String, Vertex> recenter(String name, HashMap<String, Vertex> map) {

	map = graphGenerator(actors, movies);
	centerActor = name;

	if (!map.containsKey(centerActor)) {
	    System.out.println("The name you entered is not found in the database");
	    return map;
	}

	Queue<Vertex> queue = new LinkedList<Vertex>();
	map.get(centerActor).setAsCenterActor();
	queue.add(map.get(centerActor));
	// String search = "Eve";
	// Setting the map around the center
	while (!queue.isEmpty()) {
	    Vertex v = queue.remove();
	    v.setAsVisited();
	    for (Vertex x : v.getValues()) {
		if (x.getPredecessor() != null) {
		    continue;// Is there an error here? I though that bec it was added before there is a
			     // shorter path already, you know?
		}
		x.setPredecessor(v);
		queue.add(x);
	    }
	    if (v.isCenterActor) {
		v.setPredecessor(null);
		continue;
	    }
	}
	return map;
    }

    public static HashMap<String, Vertex> recenterHelper(String name, HashMap<String, Vertex> map) {
	map = graphGenerator(actors, movies);
	centerActor = name;

	if (!map.containsKey(centerActor)) {
	    // System.out.println("The name you entered is not found in the database");
	    return map;
	}
	map.get(centerActor).isCenterActor = true;

	Queue<Vertex> queue = new LinkedList<Vertex>();
	map.get(centerActor).setAsCenterActor();
	queue.add(map.get(centerActor));
	// String search = "Eve";
	// Setting the map around the center
	while (!queue.isEmpty()) {
	    Vertex v = queue.remove();
	    v.setAsVisited();
	    for (Vertex x : v.getValues()) {
		if (x.getPredecessor() != null) {
		    continue;// Is there an error here? I though that bec it was added before there is a
			     // shorter path already, you know?
		}
		x.setPredecessor(v);
		queue.add(x);
	    }
	    if (v.isCenterActor) {
		v.setPredecessor(null);
		continue;
	    }
	}
	return map;
    }

    public static void topCenter(HashMap<String, Vertex> map, int n) {
	// Working good with small databases but has a few errors in the numbers with
	// large databases TODO
	HashMap<String, Double> list = new HashMap<String, Double>();
	for (String key : map.keySet()) {
	    if (!actors.containsKey(key)) {
		continue;
	    }
	    map = recenterHelper(key, map);
	    // Calling AvgDistance Method
	    double avgDistance = 0;
	    avgDistance = avgDistanceHelper(map, actors);
	    // DONE!
	    list.put(key, avgDistance);
	}
	int i = 1;
	String name = "";
	int size = list.size();
	while (i <= n && i <= size) {
	    double min = 1000000;
	    for (String s : list.keySet()) {
		if (list.get(s) < min) {
		    min = list.get(s);
		    name = s;
		}
	    }
	    list.remove(name);
	    System.out.println(i + ". " + name + " " + min);
	    i++;
	}
    }

    public static void main(String[] args) throws MalformedURLException, IOException {
	String fname;
	 String centerActor = "Kevin Bacon (I)";
	// centerActor = args[1];
	boolean isURL = false;
	// Checking whether the input file is URL or not
	if (args.length > 1) {
	    fname = args[0];
	    centerActor = "";
	    for (int i = 1; i < args.length; i++) {
		centerActor += args[i];
	    }
	} else if (args.length == 1) {
	    fname = args[0];
	} else {
	    System.out.println("No file name or URL entered");
	    return;
	}
	if (fname.length() > 5) {
	    String temp = fname.substring(0, 5);
	    if (temp.equals("http:")) {
		isURL = true;
	    }
	}
	Scanner input;
	// Done with checking the input in the argument line
	// Now time to create a scanner
	if (isURL == false) {
	    input = new Scanner(new File("fname"));
	} else {
	    input = new Scanner(new URL(fname).openStream());
	}

	// Creating the actors and films maps

	while (input.hasNext()) {
	    String line = input.nextLine();
	    String arr[] = line.split("\\|");
	    // System.out.println(arr[0] + ' ' + arr[1]);//TEST
	    String actor = arr[0];
	    String film = arr[1];
	    if (actors.get(actor) == null) {
		ArrayList<String> films = new ArrayList<String>();
		films.add(film);
		actors.put(actor, films);
	    } else {
		ArrayList<String> temp = actors.get(actor);
		temp.add(film);
		actors.put(actor, temp);
	    }
	    // NOW TIME FOR THE FILMS MAP
	    if (movies.get(film) == null) {
		ArrayList<String> temp = new ArrayList<String>();
		temp.add(actor);
		movies.put(film, temp);
	    } else {
		ArrayList<String> temp = movies.get(film);
		temp.add(actor);
		movies.put(film, temp);
	    }
	}
	input.close();

	// Breadth First Search Code
	HashMap<String, Vertex> map = graphGenerator(actors, movies);
	map = recenterHelper(centerActor, map);
	// */Finding the path
	// String search = "Eve";
	// ArrayList<Vertex> path = find(search, centerActor, map);//GET IT BACK! TODO
	// Finding the average distance
	// avgDistance(map, centerActor, actors);//GET IT BACK! TODO
	// Circles
	// circles(actors, map, centerActor);//GET IT BACK! TODO
	// *///TODO
	// TOP center
	// topCenter(map, actors, centerActor);//GET IT BACK! TODO

	boolean done = false;
	while (!done) {
	    System.out.println();// Aesthetic space
	    Scanner sc = new Scanner(System.in);
	    System.out.print("Please enter a command or just write 'help' : ");
	    String s = sc.nextLine();
	    String arr[] = s.split(" ");
	    if (arr.length <= 0) {
	    	System.out.println("Wrong entry. Please try again or write 'help' ");
	    	continue;
	    }
	    if (arr.length >= 2) {
		    String command = arr[0];
		    String variable = arr[1];
			if (command.equals("find")) {
			    find(variable, map);
			    continue;
			} else if (command.equals("recenter")) {
			    map = recenter(variable, map);// PAY ATTENTION TO THIS! TODO
			    // centerActor = variable;
			    continue;
			} else if (command.equals("topcenter")) {
			    int n = Integer.parseInt(variable);
			    topCenter(map, n);
			    continue;
			} else {
			    System.out.println("Wrong entry. Please try again");
			}
	    } else if (arr.length == 1) {
		String command = arr[0];
		if (command.equals("help")) {
		    System.out.println(help());
		    continue;
		}
		if (command.equals("quit")) {
		    done = true;
		    quit();
		    break;// IS THERE AN ERROR HERE?
		}
		if (command.equals("avgdist")) {
		    avgDistance(map, actors);
		    continue;
		}
		if (command.equals("circles")) {
		    circles(actors, map);
		    continue;
		} else {
		    System.out.println("Wrong entry. Please try again");
		}
	    }
	}

    }
}