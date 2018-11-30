package com.company;

import java.util.*;

public class BFS {

    private Graphe gr;
    private LinkedList<Node> path;

    public BFS(Graphe gr){
        this.gr = gr;
    }

    public boolean execute () {
        init();
        LinkedList<Node> file = new LinkedList<Node>();
        LinkedList<Node> temp;
        file.addLast(gr.getNodeSrc());
        gr.getNodeSrc().setMark(true);
        System.out.println("SOURCE " + gr.getNodeSrc());
        System.out.println("SINK   " + gr.getNodeDst());

        boolean found = false;
        while (!file.isEmpty() && !found){
            Node s = file.removeLast();
            /*if (s.getId()!=0 && s.getId()!=10000 && (gr.distanceCoord(gr.LAVAL_CENTER, s.getPos()) < 1.2) ){
                System.err.println("Node Close " + s);
            }*/
            if (s.getId() == gr.getNodeDst().getId()){
                System.out.println("FOUND "+s);
                found = true;
            } else {
                //System.out.println("Selected " + s);
                temp = getNeigbourg(gr.getListEdge(), s);
                //System.out.println("Found " + temp.size() + " Neigbourg\n" + temp);
                for (Node node : temp) {
                    file.addLast(node);
                }
            }
        }

        System.out.println(found);

        return found;
    }

    private void init() {
        path = new LinkedList<>();
        for (Map.Entry node: gr.getListNode().entrySet()) {
            Node nodeTmp = (Node) node.getValue();
            nodeTmp.setPred(null);
            nodeTmp.setMark(false);
        }
    }

    private LinkedList<Node> getNeigbourg(ArrayList<Edge> listEdge, Node node) {
        LinkedList<Node> listNode = new LinkedList<>();
        //System.out.println("FINDING NEIGBOUR FOR "+ node);
        Node nodeDst;
        Node nodeSrc;
        for (Edge edge: listEdge) {
            //System.out.println("getNeigbourg ");
            nodeSrc = getNode( edge.getSrc().getId());
            nodeDst = getNode(edge.getDest().getId());
            /*System.out.println("Src " + nodeSrc);

            System.out.println("Dst " + nodeDst);
            System.out.println("Node " + node);
            System.out.println("------------------------------------------");*/
            if (nodeDst.getId() == node.getId() && !nodeSrc.getMark()) {
                //System.out.println("src && !marked Flow " + edge.getFlow());
                if (edge.getFlow() > 0) {
                    nodeSrc.setPred(node);
                    nodeSrc.setMark(true);
                    listNode.addLast(nodeSrc);
                }
            } else if (nodeSrc.getId() == node.getId() && !nodeDst.getMark()){
                //System.out.println("dst && !marked Res " + edge.getResidualCapacity());
                if (edge.getResidualCapacity() > 0) {
                    nodeDst.setPred(node);
                    nodeDst.setMark(true);
                    listNode.addLast(nodeDst);
                }
            }
        }

        //System.out.println("FOUND NEIGHBOOR " + listNode.size());

        return listNode;
    }

    public LinkedList<Node> getPathBFS() {
        Node node = gr.getNodeDst();
        boolean keepGoing = (node.getPred() != null);
        path.add(node);
        System.out.println(gr.getNodeDst());
        while (keepGoing) {
            Node nodeTmp = node.getPred();
            path.addFirst(nodeTmp);
            keepGoing = (nodeTmp.getPred() != null && nodeTmp.getId()!=gr.getNodeSrc().getId());
            node = nodeTmp;
        }

        return path;
    }

    private Node getNode(int id) {
        return this.gr.getListNode().get(id);
    }

    public LinkedList<Node> getPath() {
        return path;
    }

    public void setPath(LinkedList<Node> path) {
        this.path = path;
    }
}
