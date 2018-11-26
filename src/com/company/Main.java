package com.company;

public class Main {

    public static void main(String[] args) {
	    // write your code here
        Graphe graphe = new Graphe(2.5, 1);
        graphe.cleanFiles();
        System.out.println("Size before" + graphe.getListEdge().size());
        graphe.filterRelevant();
        System.out.println("Size middle" + graphe.getListEdge().size());
        //graphe.printEdgeList();
        //graphe.printNodeList();
        graphe.filterZone();
        System.out.println("Size after" + graphe.getListEdge().size());
        graphe.filterNodeEdge();


        graphe.writeEdge();
        graphe.writeNodes();

        //graphe.testDist();


        System.out.println("IT WORKS");

    }
}
