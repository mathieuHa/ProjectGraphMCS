package com.company;

import java.util.ArrayList;

public class Edge {
    private int id;
    private Node src;
    private Node dest;
    private String type;
    private String name;
    private ArrayList<Coord> pointList;

    @Override
    public String toString() {
        return "Edge{" +
                "id=" + id +
                ", src=" + src +
                ", dest=" + dest +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", pointList=" + pointList +
                "} \n";
    }

    public Edge(){
        this.src = new Node();
        this.dest = new Node();
        this.pointList = new ArrayList<>();
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
}
