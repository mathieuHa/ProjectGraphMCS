package com.company;

import java.util.LinkedList;
import java.util.Map;

public class Main {

	private Graphe graphe;

	public Main(){
		graphe = new Graphe(2.5, 1, Graphe.LAVAL_CENTER, Graphe.LAVAL_DATA);
	}

	public void start(){
		System.out.println("Size before" + graphe.getListEdge().size());
		graphe.filterRelevant();
        System.out.println("Size middle" + graphe.getListEdge().size());
        //graphe.printEdgeList();
        //graphe.printNodeList();
        graphe.filterZone();
        System.out.println("Size after" + graphe.getListEdge().size());
        graphe.filterNodeEdge();
		graphe.updateNodes();
		
        graphe.writeEdges("web/edges.js");
        graphe.writeNodes("web/nodes.js");

        System.out.println("Start BFS");
        /*BFS bfs = new BFS(graphe);
        System.out.println("Path trouvé : " + bfs.execute());
        System.out.println(bfs.getPathBFS());*/

        //Test test = new Test();
        FordF f = new FordF(graphe);
        f.execute();

		//graphe.getNodeSrc().setId(1);
		//graphe.getNodeDst().setId(2);
		//System.out.println(graphe.getNodeSrc() + "\n" + graphe.getNodeDst());
		//System.out.println("MaxFlow : " + fordF(graphe.getNodeSrc(), graphe.getNodeDst()));

        //BFS bfs = new BFS(graphe);

        //graphe.testDist();

        System.out.println("IT WORKS");
	}

	// Remet la marque de tous les noeuds à false.
	private void resetNodesMark() {
		for (Map.Entry me : graphe.getListNode().entrySet()) {
			Node node = (Node) me.getValue();
			node.setMark(false);
		}
	}
	
	/*private boolean BFS(Node src, Node dst) {
		resetNodesMark();
		LinkedList<Node> queue = new LinkedList<>();
		queue.offer(src);
		src.setMark(true);

		Node node, nghbg;
		while (!queue.isEmpty()) {
            node = queue.poll();

			// On regarde les voisins
			for (Edge edge : node.getEdges()) {
				nghbg = edge.getNode(node);
				if (!nghbg.getMark() && (edge.getCapacity() - edge.getFlow(node) > 0)) {
					queue.offer(nghbg);
					nghbg.setMark(true);
					nghbg.setPred(node);
				}
            }
        }
		return dst.getMark();
	}*/
	
	/*private int fordF(Node src, Node dst) {
		int max_flow = 0, path_flow;
		Edge edge;
		Node node;

		int compteur = 0;
		// Tant qu'un chemin existe, on augmente le flot
		while (BFS(src, dst)) {

			if (compteur%1000000 == 0)
				System.out.println("Tested paths : " + compteur);
			
			path_flow = Integer.MAX_VALUE;
			
			// On détermine le flot max du chemin : path_flow
			node = dst;
			while (node.getId() != src.getId()) {
				edge = node.getEdge(node.getPred());
				path_flow = Math.min(path_flow, edge.getFlow(node.getPred()));
				node = node.getPred();
			}

			// On ajoute le flot du chemin au flot global
			max_flow += path_flow;
			
			// Pour chaque edge, on update les capacités résiduelles
			node = dst;
			while (node.getId() != src.getId()) {
				edge = node.getEdge(node.getPred());
				edge.incFlow(node.getPred(), path_flow);
				edge.incFlow(node, -path_flow);
				node = node.getPred();
			}

			compteur++;
		}
		
		return max_flow;
	}*/

	public static void main(String[] args) {
		Main main = new Main();
		main.start();
    }

}
