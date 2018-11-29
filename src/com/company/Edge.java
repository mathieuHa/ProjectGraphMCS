package com.company;

import java.util.ArrayList;

public class Edge {
    private int id;
    private Node src;
    private Node dest;
    private String type;
    private String name;
    private String border;
    private int dist = 1;
    private ArrayList<Coord> pointList;
	private int capacity = 1;
	// Flow
	private int flow = 0;
	// Flow de dst vers src
	private int flowDest = 0;

    public Edge(){
        this.src = new Node();
        this.dest = new Node();
        this.pointList = new ArrayList<>();
    }

    public Edge (Node src, Node dst) {
        this.src = src;
        this.dest = dst;
        this.border = "link";
    }

    public Edge(int id, Node src, Node dest, String type, String name) {
        this.id = id;
        this.src = src;
        this.dest = dest;
        this.type = type;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Node getSrc() {
        return src;
    }

    public void setSrc(Node src) {
        this.src = src;
    }

    public Node getDest() {
        return dest;
    }

    public void setDest(Node dest) {
        this.dest = dest;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Coord> getPointList() {
        return pointList;
    }

    public void setPointList(ArrayList<Coord> pointList) {
        this.pointList = pointList;
    }
    public String getBorder() {
        return border;
    }

    public void setBorder(String border) {
        this.border = border;
    }

    public int getDist() {
        return dist;
    }

    public void setDist(int dist) {
        this.dist = dist;
    }
	
	public int getCapacity() {
		return capacity;
	}

	/*public void incFlow(Node n, int path_flow) {
		if(n.getId() == src.getId())
			flowSrc += path_flow;
		else if(n.getId() == dest.getId())
			flowDest += path_flow;
		else
			System.err.println("incFlow : Error");
	}*/

	public int getResidualCapacity () {
	    return capacity - flow;
    }
	/*public int getFlow(Node n){
		if(n.getId() == src.getId())
			return flowSrc;
		else if(n.getId() == dest.getId())
			return flowDest;
		else {
			System.err.println("getFlow : Error");
			return Integer.MAX_VALUE;
		}
	}

	// Renvoie le 2nd noeud (celui qui n'est pas passé en param)
	public Node getNode(Node n) {
		if (n.getId() == src.getId())
			return dest;
		else if (n.getId() == dest.getId())
			return src;
		else {
			System.err.println("getNode : Impossible de récupérer le noeud.\n" + this);
			return null;
		}
	}
	}*/

    @Override
    public String toString() {
        return "Edge{" +
                "id=" + id +
                ", src=" + src +
                ", dest=" + dest +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", capa=" + capacity + " " +
                ", flow=" + flow + " " +
                ", border='" + border + '\'' +
                ", pointList=" + pointList +
                "}\n";
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getFlow() {
        return flow;
    }

    public void setFlow(int flow) {
        this.flow = flow;
    }
}
