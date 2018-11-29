package com.company;

import java.util.ArrayList;
import java.util.LinkedList;

public class BFS {

    public BFS(Graphe gr){
        LinkedList<Node> file = new LinkedList<Node>();
        LinkedList<Node> temp;
        file.addLast(gr.getNodeSrc());
        gr.getNodeSrc().setMark(true);
        while (!file.isEmpty()){
            Node s = file.removeLast();
            System.out.println(s);
            temp = getNeigbourg(gr.getListEdge(), s);
            for (Node node : temp){
                if (!node.getMark()){
                    file.addLast(node);
                    node.setMark(true);
                }
            }
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
}
