package com.company;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

public class Main {

	private Graphe graphe;

	public Main(){
		graphe = new Graphe(2, 0.5, Graphe.LAVAL_CENTER, Graphe.LAVAL_DATA);
	}

	public void start(){
		graphe.cleanFiles();
		System.out.println("Size before" + graphe.getListEdge().size());
		graphe.filterRelevant();
        System.out.println("Size middle" + graphe.getListEdge().size());
        //graphe.printEdgeList();
        //graphe.printNodeList();
        graphe.filterZone();
        System.out.println("Size after" + graphe.getListEdge().size());
		graphe.filterNodeEdge();
		graphe.updateNodes();
		
		//Check la connexité du graphe
		for (Map.Entry node: graphe.getListNode().entrySet()){
			Node nodeTmp = (Node) node.getValue();
			if(nodeTmp.getEdges().isEmpty())
				System.err.println("Le graphe n'est pas connexe !");
		}
		
		//Check la source et le puits
		for (Edge edge : graphe.getListEdge())
			if(edge.getSrc().getId() == 0 || edge.getDest().getId() == 10000)
				System.err.println("Source ou puits mal initialisé");

        /*System.out.println("Start BFS");
        BFS bfs = new BFS(graphe);
        System.out.println("Path trouvé : " + bfs.execute());
        System.out.println(bfs.getPathBFS());*/

        //Test test = new Test();
        //FordF f = new FordF(graphe);
        //f.execute();
		System.out.println("MaxFlow : " + fordF(graphe.getNodeSrc(), graphe.getNodeDst()));
		graphe.writeEdgesV3("web/edges.js");
		graphe.writeNodes("web/nodes.js");
		graphe.writeBlock("web/block.js", extractCut(graphe.getNodeSrc())); // to fill HERE

		//graphe.getNodeSrc().setId(1);
		//graphe.getNodeDst().setId(2);
		//System.out.println(graphe.getNodeSrc() + "\n" + graphe.getNodeDst());


        //BFS bfs = new BFS(graphe);

        //graphe.testDist();

        System.out.println("IT WORKS");
	}
	
	private ArrayList<Coord> extractCut(Node src) {
		resetNodesMark();
		ArrayList<Coord> coords = new ArrayList<>();
		LinkedList<Node> queue = new LinkedList<>();
		queue.offer(src);
		src.setMark(true);

		Node node, nghbg = null;
		while (!queue.isEmpty()) {
            node = queue.poll();
			// On regarde les voisins
			for (Edge edge : node.getEdges()) {
				nghbg = edge.getNode(node);
				if (!nghbg.getMark()) {
					nghbg.setMark(true);
					if(edge.getResCap(node) > 0)
						queue.offer(nghbg);
					else
						coords.add(node.getPos());
				}
            }
        }
		
		System.out.println("Point de cut trouvé : " +coords.size());
		return coords;
	}

	// Remet la marque de tous les noeuds à false.
	private void resetNodesMark() {
		for (Map.Entry me : graphe.getListNode().entrySet()) {
			Node node = (Node) me.getValue();
			node.setMark(false);
		}
	}
	
	private boolean BFS(Node src, Node dst) {
		resetNodesMark();
		LinkedList<Node> queue = new LinkedList<>();
		queue.offer(src);
		src.setMark(true);

		Node node, nghbg = null;
		while (!queue.isEmpty()) {
            node = queue.poll();
			// On regarde les voisins
			for (Edge edge : node.getEdges()) {
				nghbg = edge.getNode(node);
				if (!nghbg.getMark() && (edge.getResCap(node) > 0)) {
					queue.offer(nghbg);
					nghbg.setMark(true);
					nghbg.setPred(node);
				}
            }
        }
		return dst.getMark();
	}
	
	private int fordF(Node src, Node dst) {
		int max_flow = 0, path_flow;
		Edge edge;
		Node node;

		// Tant qu'un chemin existe, on augmente le flot
		while (BFS(src, dst)) {
			
			path_flow = Integer.MAX_VALUE;
			
			// On détermine le flot max du chemin : path_flow
			node = dst;
			while (node.getId() != src.getId()) {
				edge = node.getEdge(node.getPred());
				path_flow = Math.min(path_flow, edge.getResCap(node.getPred()));
				node = node.getPred();
			}
			System.out.println("PathFlow : " +path_flow);

			// On ajoute le flot du chemin au flot global
			max_flow += path_flow;
			
			// Pour chaque edge, on update les capacités résiduelles
			node = dst;
			while (node.getId() != src.getId()) {
				edge = node.getEdge(node.getPred());
				edge.incFlow(path_flow);
				node = node.getPred();
			}
		}
		
		return max_flow;
	}

	public static void main(String[] args) {
		Main main = new Main();
		main.start();
    }

}
