package com.company;

public class Node {
    private int id;
    private Coord pos;



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

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", pos=" + pos +
                "} \n";
    }
}
