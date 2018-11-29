package com.company;

import java.util.ArrayList;

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
		graphe.updateNode();
		
        graphe.writeEdges("web/edges.js");
        graphe.writeNodes("web/nodes.js");

        System.out.println("Start BFS");
        /*BFS bfs = new BFS(graphe);
        System.out.println("Path trouvé : " + bfs.execute());
        System.out.println(bfs.getPathBFS());*/

        //Test test = new Test();
        FordF f = new FordF(graphe);
        f.execute();
        //graphe.testDist();


        System.out.println("IT WORKS");

    }
	
	private ArrayList<Node> findPath(Node src, Node dst) {
		return null;
	}
	
	/*private float fordF(Node src, Node dst) {
		int max_flow = 0, path_flow;
		ArrayList<Node> path = null;
		Edge edge;
		
		// Tant qu'un chemin existe, on augmente le flot
		while (!(path = findPath(src, dst)).isEmpty()) {
			
			path_flow = Integer.MAX_VALUE;
			
			// On détermine le flot max du chemin : path_flow
			for (int i = 0; i < path.size()-1; i++) {
				edge = path.get(i).getEdge(path.get(i+1));
				path_flow = Math.min(path_flow, edge.getFlow(path.get(i)));
			}
			
			// On ajoute le flot du chemin au flot global
			max_flow += path_flow;
			
			// Pour chaque edge, on update les capacités résiduelles
			for (int i = 0; i < path.size()-1; i++) {
				edge = path.get(i).getEdge(path.get(i+1));
				edge.incFlow(path.get(i), path_flow);
				edge.incFlow(path.get(i+1), -path_flow);
			}
		}
		
		return max_flow;
	}*/
}
