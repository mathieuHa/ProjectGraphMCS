package com.company;

public class Main {

    public static void main(String[] args) {
	    
        Graphe graphe = new Graphe(2.5, 1, Graphe.LAVAL_CENTER, Graphe.LAVAL_DATA);
        //graphe.cleanFiles();  //Pas utile : il suffit de dire qu'on overwrite quand on veut écrire en passant un false -> new FileWriter(fileName, false)
        System.out.println("Size before" + graphe.getListEdge().size());
        graphe.filterRelevant();
        System.out.println("Size middle" + graphe.getListEdge().size());
        //graphe.printEdgeList();
        //graphe.printNodeList();
        graphe.filterZone();
        System.out.println("Size after" + graphe.getListEdge().size());
        graphe.filterNodeEdge();

        graphe.writeEdges("web/edges.js");
        graphe.writeNodes("web/nodes.js");


        BFS bfs = new BFS(graphe);

        //graphe.testDist();


        System.out.println("IT WORKS");

    }
}
