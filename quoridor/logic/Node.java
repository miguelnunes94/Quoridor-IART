package quoridor.logic;

import java.util.ArrayList;

public class Node {
	public int g,f;
	public int x,y;
	public Boolean wall;
	public Node parent;
	public ArrayList<Node>sucessores;
	public String [][] board;
	public Node(int g,int x,int y, Node p,int f,Boolean wall,String[][] board){
		this.g=g;
		this.x=x;
		this.y=y;
		this.parent=p;
		this.f=f;
		this.sucessores=new ArrayList<Node>();
		this.wall=wall;
		this.board=board;
	}
	@Override
	public boolean equals(Object other){
	    if (other == null) 
	    	return false;
	    if (other == this) 
	    	return true;
	    if (!(other instanceof Node))
	    	return false;
	    
	    return ((Node)other).x==x && ((Node)other).y==y;
	}
}
