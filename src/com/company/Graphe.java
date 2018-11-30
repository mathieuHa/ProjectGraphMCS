package com.company;
import org.json.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Graphe {
	// Coordonnées du centre de Laval et fichier de donnée de Laval
	public static final Coord LAVAL_CENTER = new Coord( 48.075514, -0.763056);
	public static final String LAVAL_DATA = "in/data.json";
	
	// Champs
    private ArrayList<Edge> listEdge;

    private Integer maxFlow;

    private HashMap<Integer, Node> listNode;
    private Coord centerPoint;
    private double distOuter;
    private double distInner;

    private Node nodeDst;
    private Node nodeSrc;

    // Constructeur, prends les rayons des deux cercles en paramètre, ainsi que les coordonnées du centre d'une ville
    public Graphe(double distOuter, double distInner, Coord cityCenter, String jsonFileName){
        listEdge = new ArrayList<>();
        listNode = new HashMap<>();
        this.distInner = distInner;
        this.distOuter = distOuter;
        centerPoint = cityCenter;
        this.initGraph(jsonFileName);
        //this.fillNode();
        this.nodeDst = new Node(0);
        this.nodeSrc = new Node(10000);

    }

    public Graphe(ArrayList<Edge> listEdge) {
        this.listEdge = listEdge;
    }

    public Graphe(Graphe graphe) {
        this.listEdge = new ArrayList<>(graphe.getListEdge());
        this.listNode = new HashMap<>(graphe.getListNode());
        this.nodeSrc = new Node(graphe.getNodeSrc());
        this.nodeDst = new Node(graphe.getNodeDst());

    }

    public Graphe() {
        this.listEdge = new ArrayList<>();
        this.listNode = new HashMap<>();
        this.nodeDst = new Node();
        this.nodeSrc = new Node();
    }

    public ArrayList<Edge> getListEdge() {
        return listEdge;
    }

    public void setListEdge(ArrayList<Edge> listEdge) {
        this.listEdge = listEdge;
    }

    // Parse le fichier JSON en entrée, rempli les structures listEdge et listNode
    private void initGraph(String jsonFileName){
        JSONObject base;
        JSONArray features;
        StringBuilder buf = new StringBuilder();
        File file = new File(jsonFileName);

		// Try-with-resource : ferme automatiquement le Scanner à la fin dans le cas où il a été initialisé
        try (Scanner sc = new Scanner(file)){
            while (sc.hasNextLine()){
                buf.append(sc.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.err.println("File " + jsonFileName + " does not exist : " + e);
        }

        try {
            base = new JSONObject(buf.toString());
            if (base.length() == 0){
                System.err.println("ERROR in base");
            }
            features = base.getJSONArray("features");
			JSONObject jTemp, jTempProp, jTempGeo;
			JSONArray arrayCoord, coArray;
			Node nodeTemp;
			Edge edgeTemp;
			Coord coord;
			
			// Initialisation des nodes
            for (int i=0; i< features.length(); i++){
                jTemp = features.getJSONObject(i);
                if (jTemp != null) {
                    jTempProp = jTemp.getJSONObject("properties");
                    jTempGeo  = jTemp.getJSONObject("geometry");
                    arrayCoord = jTempGeo.getJSONArray("coordinates");
                    if (jTempGeo.getString("type").equals("Point")){
                        nodeTemp = new Node();
						nodeTemp.setId(jTempProp.getInt("id"));
                        coord = new Coord();
                        coord.setLatitude(arrayCoord.getDouble(1));
                        coord.setLongitude(arrayCoord.getDouble(0));
						nodeTemp.setPos(coord);
                        listNode.put(nodeTemp.getId(), nodeTemp);
                    }
                }
            }
			
			// Initialisation des edges
			for (int i=0; i< features.length(); i++){
                jTemp = features.getJSONObject(i);
                if (jTemp != null) {
                    jTempProp = jTemp.getJSONObject("properties");
                    jTempGeo  = jTemp.getJSONObject("geometry");
                    arrayCoord = jTempGeo.getJSONArray("coordinates");
                    if (jTempGeo.getString("type").equals("LineString")){
                        edgeTemp = new Edge();
                        edgeTemp.setSrc(listNode.get(jTemp.getInt("from")));
                        edgeTemp.setDest(listNode.get(jTemp.getInt("to")));
                        edgeTemp.setType(jTempProp.getString("highway"));
                        edgeTemp.setId(jTempProp.getInt("id"));
                        edgeTemp.setName(jTempProp.optString("name"));
                        for (int a=0; a<arrayCoord.length(); a++) {
                            coArray = arrayCoord.getJSONArray(a);
                            coord = new Coord();
                            coord.setLatitude(coArray.getDouble(1));
                            coord.setLongitude(coArray.getDouble(0));
                            edgeTemp.getPointList().add(coord);
                        }
                        Edge inv = new Edge(edgeTemp);
                        inv.InvEdge();
                        listEdge.add(edgeTemp);
                        listEdge.add(inv);
                    }
                }
            }

        } catch (JSONException e) {
            System.err.println("Failed to parse JSON file " + jsonFileName + " : " + e);
        }
    }

    // Supprimes toutes les arrêtes qui ne sont pas des grosses routes
    public void filterRelevant () {
        ArrayList<Edge> listTemp = new ArrayList<>();
        for (Edge edge : listEdge){
            if (edge.getType().equals("primary") || edge.getType().equals("primary_link") ||
                    edge.getType().equals("secondary") || edge.getType().equals("secondary_link") ||
                    edge.getType().equals("tertiary") || edge.getType().equals("tertiary_link") ||
                    edge.getType().equals("trunk") ||  edge.getType().equals("trunk_link") ||
                    edge.getType().equals("motorway") || edge.getType().equals("motorway_link")
            )
                    {
                listTemp.add(edge);
            }
        }
        listEdge = listTemp;
    }

    // Supprimes dans la liste des noeuds, ceux qui ne sont pas utilisés par les arrêtes
    public void filterNodeEdge () {

        HashMap<Integer, Node> listNodeHM = new HashMap<>();

        for (Iterator<Edge> it = listEdge.iterator(); it.hasNext(); ) {
			Edge edge = it.next();
            if (!edge.getBorder().equals("link")){
                listNodeHM.put(edge.getSrc().getId(), edge.getSrc());
                listNodeHM.put(edge.getDest().getId(), edge.getDest());
            }
			else if(edge.getSrc().getId() == 0 || edge.getDest().getId() == 10000) {
				it.remove();
			}
        }
        listNodeHM.put(0, nodeSrc);
        listNodeHM.put(10000, nodeDst);
        listNode = listNodeHM;
        System.out.println("Size after " +  listNode.size());
    }


    // Remplis les positions des noeuds dans les arrêtes
    private void fillNode() {
        for (Edge edge : listEdge) {
            edge.getDest().setPos(listNode.get(edge.getDest().getId()).getPos());
            edge.getSrc().setPos(listNode.get(edge.getSrc().getId()).getPos());
        }
    }


    // Filtre sur les nodes qui sont entre les deux cercles
    public void filterZone() {
        ArrayList<Edge> listTemp = new ArrayList<>();
        ArrayList<Edge> listEdgeSide = new ArrayList<>();
        for (Edge edge: listEdge){
            double distSrc = distanceCoord(new Coord(edge.getSrc().getPos().getLatitude(), edge.getSrc().getPos().getLongitude()),  centerPoint);
            double distDst = distanceCoord(new Coord(edge.getDest().getPos().getLatitude(), edge.getDest().getPos().getLongitude()),  centerPoint);
            if (    (distSrc > distInner && distSrc < distOuter) || (distDst > distInner && distDst < distOuter) ) {
                listTemp.add(edge);
                if ((distSrc > distInner && distSrc < distOuter) && (distDst > distInner && distDst < distOuter)) {
                    edge.setBorder("in");
                } else if ((distSrc > distInner && distSrc < distOuter)) {
                    edge.setBorder("dst");
                    if (distDst < distInner) {
                        addBorderEdge(listEdgeSide, edge.getDest(), nodeSrc);
                    } else if (distDst > distOuter) {
                        addBorderEdge(listEdgeSide, edge.getDest(), nodeDst);
                    }
                }

                 else if ((distDst > distInner && distDst < distOuter)) {
                    edge.setBorder("src");
                    if (distSrc < distInner) {
                        addBorderEdge(listEdgeSide, edge.getSrc(), nodeSrc);
                    } else if (distSrc > distOuter) {
                        addBorderEdge(listEdgeSide, edge.getSrc(), nodeDst);
                    }
                }
            }
        }
        listTemp.addAll(listEdgeSide);
        listEdge = listTemp;
    }

    private void addBorderEdge(ArrayList<Edge> listEdgeSide, Node src, Node dst){
        Edge sens1 = new Edge(src, dst);
        Edge sens2 = new Edge(dst, src);
        sens1.setCapacity(Integer.MAX_VALUE);
        sens2.setCapacity(Integer.MAX_VALUE);
        listEdgeSide.add(sens1);
        listEdgeSide.add(sens2);
    }

	public void updateNodes() {
	
		for (Map.Entry node: listNode.entrySet()){
			Node nodeTmp = (Node) node.getValue();
			
			for (Edge edge : listEdge) {
				if (edge.getSrc().getId() == nodeTmp.getId() || edge.getDest().getId() == nodeTmp.getId())
					nodeTmp.addEdge(edge);
			}
		}
	}


    // affiche la liste des arrêtes dans la console
    public void printEdgeList() {
        System.out.println(listEdge);
    }

    // Affiche la liste des noeuds dans la  console
    public void printNodeList() {
        System.out.println(listNode);
    }

    // écrit dans le fichier nodes.js la liste des points à afficher
    public void writeNodes(String fileName) {
        int i = 0;
        try (FileWriter fileWriter = new FileWriter(fileName, false)) {
			
            Iterator it = listNode.keySet().iterator();

            for (Map.Entry node: listNode.entrySet()){
                Node nodeTmp = (Node) node.getValue();
                if (nodeTmp.getPos() != null) {
                    fileWriter.write("L.marker([" + nodeTmp.getPos().getLatitude() + ", " + nodeTmp.getPos().getLongitude() + "]).addTo(mymap).bindPopup('"+nodeTmp.getId()+"').openPopup();\n");
                    i++;
                    if (i!=listNode.size()){
                        //fileWriter.write(",\n");
                    }
                }


            }
            //fileWriter.write("]\n \t}\n}]; \n" +
              //      "L.geoJSON(points).addTo(mymap);");

        } catch (IOException e) {
            System.err.println("Cannot write file " + fileName + " : " + e);
        }
    }

    // écrit dans le fichier edges la liste des arrêtes à afficher en utilisant toutes les coordonnées entres les src et dst pour plus de précision
    public void writeEdges(String fileName) {
        int i = 0;
        try (FileWriter fileWriter = new FileWriter(fileName, false)) {

            fileWriter.write("var edges = [");
            for (Edge edge: listEdge){
                if (!edge.getBorder().equals("link")){ // on écrit pas les arrêtes vers la source et le puit
                    fileWriter.write("" +
                            "{\n" +
                            "    \"type\": \"LineString\",\n" +
                            "    \"coordinates\": [\n");
                    int e = 0;
                    for (Coord coord: edge.getPointList()){
                        fileWriter.write("\t\t["+coord.getLongitude()+","+coord.getLatitude()+"]");
                        e++;
                        if (e!=edge.getPointList().size()){
                            fileWriter.write(",\n");
                        }
                    }
                    fileWriter.write("]\n");
                    fileWriter.write(" }\n");
                    i++;
                    if (i!=listEdge.size()){
                        fileWriter.write(",");
                    }
                }
            }
            fileWriter.write("]; \n" +
                    "L.geoJSON(edges).addTo(mymap);");
        } catch (IOException e) {
            System.err.println("Cannot write file " + fileName + " : " + e);
        }
    }

    // écrit dans le fichier edges la liste des arrêtes à afficher en utilisant toutes les coordonnées entres les src et dst pour plus de précision
    public void writeEdgesV3(String fileName) {
        int i = 0;
        try (FileWriter fileWriter = new FileWriter(fileName, false)) {

            for (Edge edge: listEdge){
                if (!edge.getBorder().equals("link")){ // on écrit pas les arrêtes vers la source et le puit
                    fileWriter.write("var linees = L.polyline(" +
                            "[");
                    int e = 0;
                    for (Coord coord: edge.getPointList()){
                        fileWriter.write("["+coord.getLatitude()+","+coord.getLongitude()+"]");
                        e++;
                        if (e!=edge.getPointList().size()){
                            fileWriter.write(",");
                        }
                    }
                    fileWriter.write("],");
                    if (edge.getFlow()!=1)
                        fileWriter.write("{color:'green'}");
                    else
                        fileWriter.write("{color:'red'}");
                    fileWriter.write(").addTo(mymap).bindPopup('"+"Src : "+edge.getSrc().getId()+" Dst : "+ edge.getDest().getId()+"');\n");
                    i++;
                    /*if (i!=listEdge.size()){
                        fileWriter.write(",");
                    }*/
                }
            }

        } catch (IOException e) {
            System.err.println("Cannot write file " + fileName + " : " + e);
        }
    }

    // Ecris dans le fichier edges la liste des arrêtes à afficher en utilisant seulement les arrêtes src et dst
    public void writeEdgeEndToEnd(String fileName) { //fileName : "web/edges.js"
        System.out.println("V2");
        int i = 0;
        try (FileWriter fileWriter = new FileWriter(fileName, false)) {

            fileWriter.write("var edges = [");
            for (Edge edge: listEdge){
                fileWriter.write("" +
                        "{\n" +
                        "    \"type\": \"LineString\",\n" +
                        "    \"coordinates\": [");
                fileWriter.write("["+edge.getSrc().getPos().getLongitude()+","+edge.getSrc().getPos().getLatitude()+"], ");
                fileWriter.write("["+edge.getDest().getPos().getLongitude()+","+edge.getDest().getPos().getLatitude()+"]");
                fileWriter.write("   ]\n");
                fileWriter.write("}\n");
                i++;
                if (i!=listEdge.size()){
                    fileWriter.write(",");
                }
            }
            fileWriter.write("]; \n" +
                    "L.geoJSON(edges).addTo(mymap);");

        } catch (IOException e) {
            System.err.println("Cannot write file " + fileName + " : " + e);
        }
    }

    // Supprimes le contenu des fichiers edges et nodes
    public void cleanFiles (){
        try (FileWriter fileWriter  = new FileWriter("web/edges.js"); 
			 FileWriter fileWriter2 = new FileWriter("web/nodes.js")) {
			
        } catch (IOException e) {
			System.err.println("Cannot clean file(s) : " + e);
        }

    }

    // Calcul la distance entre deux coordonnées
    public double distanceCoord (Coord A, Coord B) {
        return Math.acos( (Math.sin(Math.toRadians(A.getLatitude())) * Math.sin(Math.toRadians(B.getLatitude())))
                + (Math.cos(Math.toRadians(A.getLatitude())) * Math.cos(Math.toRadians(B.getLatitude()))
                * Math.cos(Math.toRadians(B.getLongitude() - A.getLongitude()) ) ) ) * 6371.0;
    }

    public HashMap<Integer, Node> getListNode() {
        return listNode;
    }

    public void setListNode(HashMap<Integer, Node> listNode) {
        this.listNode = listNode;
    }
    public Node getNodeDst() {
        return nodeDst;
    }

    public void setNodeDst(Node nodeDst) {
        this.nodeDst = nodeDst;
    }

    public Node getNodeSrc() {
        return nodeSrc;
    }

    public void setNodeSrc(Node nodeSrc) {
        this.nodeSrc = nodeSrc;
    }

    @Override
    public String toString() {
        return "Graphe{" +
                "listEdge=" + listEdge +
                ", maxFlow=" + maxFlow +
                ", listNode=" + listNode +
                '}';
    }
}
