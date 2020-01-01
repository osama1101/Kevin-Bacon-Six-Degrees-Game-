import java.util.*;
public class Vertex {
	String content;//The string value/key stored in the vertex
	ArrayList<Vertex> values;//A list containing the values stored in this vertex. aka it contains the adjacency list for that vertex
	boolean visited;// Whether this vertex got visited before or not
	Vertex predecessor;// The path back to the center
	boolean isCenterActor;// Is this vertex the center actor or not?
	
	Vertex(String content){
		this.content = content;
		this.values = null;
		this.visited = false;
		this.predecessor = null;
		this.isCenterActor = false;
	}
	
	Vertex(String content, ArrayList<Vertex> values){
		this.content = content;
		this.values = values;
		this.visited = false;
		this.predecessor = null;
		this.isCenterActor = false;
	}
	
	public String getContent() {
		return content;
	}
	
	public boolean equals(String s) {
		if(content.equals(s)) {
			return true;
		}
		return false;
	}
	
	public ArrayList<Vertex> getValues(){
		return values;
	}
	
	public void setValues(ArrayList<Vertex> values) {
		this.values = values;
	}
	
	public boolean isVisted() {
		if(visited == true)
			return true;
		return false;
	}
	
	public void setAsVisited() {
		visited = true;
	}
	
	public void setPredecessor(Vertex v) {
		this.predecessor = v;
	}
	
	public Vertex getPredecessor() {
		return predecessor;
	}
	
	public boolean isCenterActor() {
		return isCenterActor;
	}
	
	public void setAsCenterActor() {
		this.isCenterActor = true;
	}
}
