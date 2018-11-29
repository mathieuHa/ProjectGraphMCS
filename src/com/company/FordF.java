package com.company;

import java.util.LinkedList;

public class FordF {
    private Graphe gr;
    private BFS bfs;
    private LinkedList<Node> path;
    private Integer maxFlow = 0;

    public FordF (Graphe gr){
        this.gr = gr;
    }

    public void execute() {
        int minFlow;
        bfs = new BFS(gr);
        while (bfs.execute()){
            System.out.println("PATH FOUND WITH BFS");
            path = bfs.getPathBFS();
            System.out.println("PATH SIZE = " + path.size());
            minFlow = getMinFlow();
            System.out.println("MINFLOW " + minFlow);
            maxFlow+=minFlow;
            updateFlow(minFlow);
            //System.out.println(gr);
            System.out.println("MAXFLOW " + maxFlow);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Integer getMinFlow() {
        int maxFlow = Integer.MAX_VALUE;
        for (int i=0; i< path.size()-1; i++){
            Integer tempFlow = getValueFlowEdgeFromNode(path.get(i), path.get(i+1));
            if (tempFlow!=null)
                maxFlow = Math.min(maxFlow, tempFlow);
        }
        return maxFlow;
    }

    private void updateFlow(int minFlow) {
        Node node1;
        Node node2;
        for (int i=0; i<path.size()-1; i++){
            node1 = path.get(i);
            node2 = path.get(i+1);
            for (Edge edge: gr.getListEdge()){
                int flowCurrent = edge.getFlow();
                if(edge.getSrc().getId() == node1.getId() && edge.getDest().getId() == node2.getId()){
                    edge.setFlow(flowCurrent+minFlow);
                } else if (edge.getSrc().getId() == node2.getId() && edge.getDest().getId() == node1.getId()) {
                    edge.setFlow(flowCurrent-minFlow);
                }
            }
        }
    }

    private Integer getValueFlowEdgeFromNode(Node node1, Node node2) {
        for (Edge edge: gr.getListEdge()) {
            //System.out.println("getValueFlowEdgeFromNode");
            if(edge.getSrc().getId() == node1.getId() && edge.getDest().getId() == node2.getId()){
                //System.out.println("FLOW FOUND");
                return edge.getResidualCapacity();
            } else if (edge.getSrc().getId() == node2.getId() && edge.getDest().getId() == node1.getId()) {
                //System.out.println("FLOW FOUND");
                return edge.getFlow();
            }
        }

        return null;
    }
}
