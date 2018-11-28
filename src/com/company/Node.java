package com.company;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Node {
    private int id;
    private Coord pos;
    private Integer dist = Integer.MAX_VALUE;
    private Boolean mark = false;
    private Node pred;


    public Node(){
        this.pos = new Coord();
    }
    public Node(int id) {
        this.id = id;
    }

    public Node(int id, Coord pos) {
        this.id = id;
        this.pos = pos;
    }
    public Node(Node node) {
        this.id = node.getId();
        this.pos = new Coord(node.getPos());
        this.dist = node.getDist();
        this.mark = node.getMark();
        this.pred = node.getPred();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Coord getPos() {
        return pos;
    }

    public void setPos(Coord pos) {
        this.pos = pos;
    }

    public int getDist() {
        return dist;
    }

    public void setDist(int dist) {
        this.dist = dist;
    }

    public void setDist(Integer dist) {
        this.dist = dist;
    }

    public Boolean getMark() {
        return mark;
    }

    public void setMark(Boolean mark) {
        this.mark = mark;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", pos=" + pos +
                "} \n";
    }

    public Node getPred() {
        return pred;
    }

    public void setPred(Node pred) {
        this.pred = pred;
    }
}
