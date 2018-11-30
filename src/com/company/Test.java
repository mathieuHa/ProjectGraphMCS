package com.company;

import java.util.Map;

public class Test {
    private Graphe graphe;

    public Test() {
        graphe = new Graphe();
        init();
		FordF f = new FordF(graphe);
        f.execute();
    }

    private void init() {

        addEdge(1, 2, 10);
        addEdge(2, 3, 2);
        addEdge(1, 3, 10);
        addEdge(3, 5, 9);
        addEdge(2, 5, 8);
        addEdge(5, 4, 6);
        addEdge(4, 6, 10);
        addEdge(5, 6 ,10);
        addEdge(2, 4, 4);
        setSourceAndDest(1,6);

    }

    private void addEdge(int src, int dst, int cap) {
        Node nodeSrc = new Node(src);
        Node nodeDst = new Node(dst);
        Edge edge = new Edge(nodeSrc, nodeDst);
        edge.setCapacity(cap);
        this.graphe.getListEdge().add(edge);
        addNode(nodeSrc);
        addNode(nodeDst);
    }

    private void addNode(Node node) {
        this.graphe.getListNode().put(node.getId(), node);
    }

    private void setSourceAndDest (int src, int dst){
        this.graphe.setNodeSrc(graphe.getListNode().get(src));
        this.graphe.setNodeDst(graphe.getListNode().get(dst));
    }

}
