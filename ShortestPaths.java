package skeleton;
/*Assignment 6 (ShortestPaths): Lilli Lewis
 * 12/4/23
 * Sources:
 * For positive infinity:
 * https://stackoverflow.com/questions/12952024/how-to-implement-infinity-in-java
 * For iteration through hashmap:
 * https://sentry.io/answers/iterate-hashmap-java/#:~:text=Perhaps%20the%20most%20straightforward%20approach,or%20entries%20in%20the%20HashMap.
 * I confirm that the above list of sources is complete (none) AND that I/we have 
 *  not talked to anyone else about the solution to this problem.*/
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
import skeleton.ShortestPaths.PathData;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.io.File;
import java.io.FileNotFoundException;

/** Provides an implementation of Dijkstra's single-source shortest paths
 * algorithm.
 * Sample usage:
 *   Graph g = // create your graph
 *   ShortestPaths sp = new ShortestPaths();
 *   Node a = g.getNode("A");
 *   sp.compute(a);
 *   Node b = g.getNode("B");
 *   LinkedList<Node> abPath = sp.getShortestPath(b);
 *   double abPathLength = sp.getShortestPathLength(b);
 *   */
public class ShortestPaths {
    // stores auxiliary data associated with each node for the shortest
    // paths computation:
    private HashMap<Node,PathData> paths;
    
    /** Compute the shortest path to all nodes from origin using Dijkstra's
     * algorithm. Fill in the paths field, which associates each Node with its
     * PathData record, storing total distance from the source, and the
     * back pointer to the previous node on the shortest path.
     * Precondition: origin is a node in the Graph.
     * @param origin, a Node*/ 
    public void compute(Node origin) {
//    	shortest_paths(v);
    	paths = new HashMap<Node,PathData>();
//    	S = {};
    	HashSet<Node> S = new HashSet<Node>();
//    	F = (v);
    	PriorityQueue<Node> F = new PriorityQueue<Node>(Comparator.comparingDouble(node->paths.get(node).distance));
    	F.add(origin);
//    	v.d = 0;
//    	v.bp = null;
    	paths.put(origin, new PathData(0, null));
//    	while (F != {}) {
    	while(!F.isEmpty()) {
//     	   f = node in F with min d value;
//     	   Remove f from F, add it so S;
    		Node f = F.remove();
    		S.add(f);
//     	   for each neighbor w of f {
    		for(Map.Entry<Node, Double> neighbor: f.getNeighbors().entrySet()) {
    			Node w = neighbor.getKey();
//     	       if (w not in S or F) {
    			if((!F.contains(w)) && (!S.contains(w))) {
//     	           w.d = f.d + weight(f, w);
//     	           w.bp = f;
    				paths.put(w, new PathData((neighbor.getValue()+paths.get(f).distance), f));
//     	           add w to F;
    				F.add(w);
    			} else if ((paths.containsKey(w))&&((paths.get(f).distance + neighbor.getValue())<paths.get(w).distance)) {
//     	       } else if (f.d + weight(f, w) < w.d) {
//     	           w.d = f.d + weight(f, w);
//     	           w.bp = f;	
    				paths.get(w).distance = (paths.get(f).distance + neighbor.getValue());
    				paths.get(w).previous = f;
    			}//else if
    		}//for
    	}//while
    }//compute
    
    
    /** Returns the length of the shortest path from the origin to destination.
     * If no path exists, return Double.POSITIVE_INFINITY.
     * Precondition: destination is a node in the graph, and compute(origin)
     * has been called. 
     * @param destination, a Node*/
    public double shortestPathLength(Node destination) {
        // TODO 2 - implement this method to fetch the shortest path length
        // from the paths data computed by Dijkstra's algorithm.
    	try {
	        if(paths.get(destination).previous==null)//no path exists
	        	return Double.POSITIVE_INFINITY;
	        else {
	        	//distance will already hold the shortest path length after compute has been called
	        	return paths.get(destination).distance;
	        }//else
    	}catch(UnsupportedOperationException e) {
    		throw new UnsupportedOperationException(e.getMessage());
    	}//catch
    }//shortestPathLength

    /** Returns a LinkedList of the nodes along the shortest path from origin
     * to destination. This path includes the origin and destination. If origin
     * and destination are the same node, it is included only once.
     * If no path to it exists, return null.
     * Precondition: destination is a node in the graph, and compute(origin)
     * has been called. 
     * @param destination, the destination Node*/
    public LinkedList<Node> shortestPath(Node destination) {
        // TODO 3 - implement this method to reconstruct sequence of Nodes
        // along the shortest path from the origin to destination using the
        // paths data computed by Dijkstra's algorithm.
    	try {
    		if(paths.get(destination).previous==null)//no path exists
        		return null;
    	LinkedList<Node> ll = new LinkedList<Node>();
    	ll.add(destination);
    	Node temp = destination;
    	while(paths.get(temp).previous!=null) {//loop through previous nodes
    		ll.add(paths.get(temp).previous);//add previous nodes to linked list
    		temp = paths.get(temp).previous;
    	}//while
    	return ll;
    	}catch(UnsupportedOperationException e) {
    		throw new UnsupportedOperationException(e.getMessage());
    	}//catch
    }//shortestPath


    /** Inner class representing data used by Dijkstra's algorithm in the
     * process of computing shortest paths from a given source node. */
    class PathData {
        double distance; // distance of the shortest path from source
        Node previous; // previous node in the path from the source

        /** constructor: initialize distance and previous node */
        public PathData(double dist, Node prev) {
            distance = dist;
            previous = prev;
        }
    }


    /** Static helper method to open and parse a file containing graph
     * information. Can parse either a basic file or a CSV file with
     * sidewalk data. See GraphParser, BasicParser, and DBParser for more.*/
    protected static Graph parseGraph(String fileType, String fileName) throws
        FileNotFoundException {
        // create an appropriate parser for the given file type
        GraphParser parser;
        if (fileType.equals("basic")) {
            parser = new BasicParser();
        } else if (fileType.equals("db")) {
            parser = new DBParser();
        } else {
            throw new IllegalArgumentException(
                    "Unsupported file type: " + fileType);
        }

        // open the given file
        parser.open(new File(fileName));

        // parse the file and return the graph
        return parser.parse();
    }
    
    /**
     * paths accessor method
     * @return paths
     */
    public HashMap<Node,PathData> getPaths(){
    	return this.paths;
    }//getPaths
    
    
    public static void main(String[] args) {
    	if (args.length<3||args.length>4) {
    		System.out.println("Error: incorrect arguments. Try again:");
    		System.out.println("<fileType> <fileName> <Origin> <Destination(optional)>");
    		System.exit(0);
    	}//if
    	
      // read command line args
      String fileType = args[0];
      String fileName = args[1];
      String SidewalkOrigCode = args[2];

      String SidewalkDestCode = null;
      if (args.length == 4) {
        SidewalkDestCode = args[3];
      }

      // parse a graph with the given type and filename
      Graph graph;
      try {
          graph = parseGraph(fileType, fileName);
      } catch (FileNotFoundException e) {
          System.out.println("Could not open file " + fileName);
          return;
      }
      graph.report();
      
      // TODO 4: create a ShortestPaths object, use it to compute shortest
      // paths data from the origin node given by origCode.
      ShortestPaths sp = new ShortestPaths();
      sp.compute(graph.getNode(SidewalkOrigCode));

      // TODO 5:
      // If destCode was not given, print each reachable node followed by the
      // length of the shortest path to it from the origin.
      if(args.length == 3) {
    	  System.out.println("Shortest paths from "+SidewalkOrigCode+":");
    	  HashMap<Node,PathData> pathsCopy = sp.getPaths();
    	  for(Map.Entry<Node, PathData> entry: pathsCopy.entrySet()) {//loop through paths
    		  if(entry!=null && entry.getValue().distance!=Double.POSITIVE_INFINITY) {//if there's a path
    			  System.out.print(entry.getKey().getId() + ": ");//print node name
    			  System.out.println(entry.getValue().distance);//print distance
    		  }//if
    	  }//for
      }//if

      // TODO 6:
      // If destCode was given, print the nodes in the path from
      // origCode to destCode, followed by the total path length
      // If no path exists, print a message saying so.
      if(args.length == 4) {
    	  HashMap<Node,PathData> pathsCopy = sp.getPaths();
    	  Node key = null;
    	  //PathData pd = null;
    	  for(Map.Entry<Node, PathData> entry: pathsCopy.entrySet()) {//loop through paths
    		  if(entry.getKey().getId().equals(SidewalkDestCode)) {//get the correct node and its pathData
    			  key= entry.getKey();
    			  //pd = entry.getValue();
    		  }//if
    	  }//for
    	  if(key==null) {//key will only be in paths if it's reachable from the origin, so if it isn't in paths, it isn't reachable
    		  System.out.println("Error: node "+SidewalkDestCode+" is unreachable from origin "+SidewalkOrigCode);
    		  System.exit(0);
    	  }//if
    	  Stack<Node> prevNodesStack = new Stack<Node>();//make a stack because the linked list is in backwards order
    	  LinkedList<Node> ll = sp.shortestPath(key);
    	  if(ll==null) {
    		  System.out.print("Warning: Destination is the same as origin: "+SidewalkOrigCode + " "+SidewalkDestCode);
    		  System.exit(0);
    	  }//if
    	  int size = ll.size();//so that it doesn't change during the loop
    	  for(int i = 0; i<size; i++) {
    		  prevNodesStack.push(ll.remove());//push values from list into stack
    	  }//for
    	  for(int i = 0; i<size; i++) {
    		  System.out.print(prevNodesStack.pop().getId() + " ");//print values in correct order
    	  }//for
    	  System.out.println(sp.shortestPathLength(key));
      }//if
    }//main
}//ShortestPaths
