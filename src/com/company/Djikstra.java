package com.company;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

public class Djikstra {
    private Graphe graphe;


    public Djikstra (Graphe graphe) {
        this.graphe = new Graphe(graphe);
    }

    private void init(){
        for (Map.Entry node: graphe.getListNode().entrySet()) {
            Node nodeTmp = (Node) node.getValue();
            nodeTmp.setDist(Integer.MAX_VALUE);
        }
        graphe.getListNode().get(0).setDist(0);
    }

    private Node findMin(Node node){
        LinkedList<Node> voisins;
        int min = Integer.MAX_VALUE;
        Node s = null;

        voisins = getNeigbourg(graphe.getListEdge(), node);
        for (Node n: voisins){
            if (n.getDist() < min){
                min = n.getDist();
                s = n;
            }
        }

        return s;
    }

    private void updateDist(Node s1, Node s2) {
        if (s2.getDist() > s1.getDist() + 1) {
            s2.setDist(s1.getDist() + 1);
            s2.setPred(s1);
        }
    }

    private LinkedList<Node> getNeigbourg(ArrayList<Edge> listEdge, Node node) {
        LinkedList<Node> listNode = new LinkedList<>();
        for (Edge edge: listEdge) {
            if (edge.getSrc().getId() == node.getId()) {
                listNode.add(edge.getDest());
            }
        }

        return listNode;
    }

    public void run () {
        init();
        while (!graphe.getListNode().isEmpty()){
            // TODO l
        }
    }
}
