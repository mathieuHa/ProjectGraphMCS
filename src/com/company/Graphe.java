package com.company;
import org.json.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Graphe {
    private ArrayList<Edge> listEdge;
    private HashMap<Integer, Node> listNode;
    private ArrayList<Node> listNodeTest;
    private Coord centerPoint;
    private double distOuter;
    private double distInner;

    // Constructeur, prends les rayons des deux cercles en paramètre
    public Graphe(double distOuter, double distInner){
        listEdge = new ArrayList<>();
        listNode = new HashMap<>();
        this.distInner = distInner;
        this.distOuter = distOuter;
        centerPoint = new Coord( 48.075514, -0.763056);
        this.initGraph();
        this.fillNode();
        System.out.println(this.distanceCoord(new Coord(48.758926, 1.877370), new Coord(48.759067, 1.878261)));
    }

    public Graphe(ArrayList<Edge> listEdge) {
        this.listEdge = listEdge;
    }

    public ArrayList<Edge> getListEdge() {
        return listEdge;
    }

    public void setListEdge(ArrayList<Edge> listEdge) {
        this.listEdge = listEdge;
    }

    // Parse le fichier JSON en entrée, rempli les structures listEdge et listNode
    private void initGraph(){
        JSONObject base;
        JSONArray features;
        StringBuilder buf = new StringBuilder();
        File file = new File("in/data.json");

        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()){
                buf.append(sc.nextLine());
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            base = new JSONObject(buf.toString());
            if (base.length() == 0){
                System.out.println("ERROR in base");
            }
            features = base.getJSONArray("features");

            for (int i=0; i< features.length(); i++){
                JSONObject jTemp = features.getJSONObject(i);
                if (jTemp != null) {
                    JSONObject jTempProp = jTemp.getJSONObject("properties");
                    JSONObject jTempGeo  = jTemp.getJSONObject("geometry");
                    JSONArray arrayCoord = jTempGeo.getJSONArray("coordinates");
                    if (jTempGeo.getString("type").equals("LineString")){
                        Edge edgeTemp = new Edge();
                        Node nodeSrc = new Node(jTemp.getInt("from"));
                        edgeTemp.setSrc(nodeSrc);
                        Node nodeDst = new Node(jTemp.getInt("to"));
                        edgeTemp.setDest(nodeDst);
                        edgeTemp.setType(jTempProp.getString("highway"));
                        edgeTemp.setId(jTempProp.getInt("id"));
                        edgeTemp.setName(jTempProp.optString("name"));
                        for (int a=0; a<arrayCoord.length(); a++) {
                            JSONArray coArray = arrayCoord.getJSONArray(a);
                            Coord coord = new Coord();
                            coord.setLatitude(coArray.getDouble(1));
                            coord.setLongitude(coArray.getDouble(0));
                            edgeTemp.getPointList().add(coord);
                        }
                        listEdge.add(edgeTemp);
                    } else if (jTempGeo.getString("type").equals("Point")){
                        Node nodeTemp = new Node();
                        Coord coord = new Coord();
                        nodeTemp.setId(jTempProp.getInt("id"));
                        coord.setLatitude(arrayCoord.getDouble(1));
                        coord.setLongitude(arrayCoord.getDouble(0));
                        nodeTemp.setPos(coord);
                        listNode.put(nodeTemp.getId(), nodeTemp);
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Supprimes toutes les arrêtes qui ne sont pas des grosses routes
    public void filterRelevant () {
        ArrayList<Edge> listTemp = new ArrayList<Edge>();
        for (Edge edge: listEdge){
            if (edge.getType().equals("primary") || edge.getType().equals("tertiary") || edge.getType().equals("secondary") || edge.getType().equals("trunk") || edge.getType().equals("motorway")){
                listTemp.add(edge);
            }
        }
        listEdge = listTemp;
    }

    // Supprimes dans la liste des noeuds, ceux qui ne sont pas utilisés par les arrêtes
    public void filterNodeEdge () {

        HashMap<Integer, Node> listNodeHM = new HashMap<>();

        for (Edge edge : listEdge) {
            listNodeHM.put(edge.getSrc().getId(), edge.getSrc());
            listNodeHM.put(edge.getDest().getId(), edge.getDest());
        }
        listNode = listNodeHM;
        System.out.println("Size after " +  listNode.size());
    }


    // Remplis les positions des noeuds dans les arrêtes
    private void fillNode() {
        for (Edge edge: listEdge) {
            edge.getDest().setPos(listNode.get(edge.getDest().getId()).getPos());
            edge.getSrc().setPos(listNode.get(edge.getSrc().getId()).getPos());
        }
    }


    // filter sur les nodes qui sont entre les deux cercles
    public void filterZone() {
        ArrayList<Edge> listTemp = new ArrayList<Edge>();
        for (Edge edge: listEdge){
            double distSrc = distanceCoord(new Coord(edge.getSrc().getPos().getLatitude(), edge.getSrc().getPos().getLongitude()),  centerPoint);
            double distDst = distanceCoord(new Coord(edge.getDest().getPos().getLatitude(), edge.getDest().getPos().getLongitude()),  centerPoint);
            if (    (distSrc > distInner && distSrc < distOuter) || (distDst > distInner && distDst < distOuter) ) {
                listTemp.add(edge);
            }
        }
        listEdge = listTemp;
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
    public void writeNodes() {
        int i = 0;
        try {
            FileWriter fileWriter = new FileWriter("web/nodes.js");
            fileWriter.write("var points = [");
            Iterator it = listNode.keySet().iterator();

            fileWriter.write("" +
                    "{\n" +
                    "    \"type\": \"Feature\",\n" +
                    "    \"geometry\": {\n" +
                    "        \"type\": \"MultiPoint\",\n" +
                    "        \"coordinates\": [\n");
            for (Map.Entry node: listNode.entrySet()){
                Node nodeTmp = (Node) node.getValue();
                fileWriter.write(
                    "\t \t ["+ nodeTmp.getPos().getLongitude()+ ", "+ nodeTmp.getPos().getLatitude() +"]");
                i++;
                if (i!=listNode.size()){
                    fileWriter.write(",\n");
                }

            }
            fileWriter.write("]\n \t}\n}]; \n" +
                    "L.geoJSON(points).addTo(mymap);");

            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // écrit dans le fichier edges la liste des arrêtes à afficher en utilisant toutes les coordonnées entres les src et dst pour plus de précision
    public void writeEdge() {
        int i = 0;
        try {
            FileWriter fileWriter = new FileWriter("web/edges.js");
            fileWriter.write("var edges = [");
            for (Edge edge: listEdge){
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
            fileWriter.write("]; \n" +
                    "L.geoJSON(edges).addTo(mymap);");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Ecris dans le fichier edges la liste des arrêtes à afficher en utilisant seulement les arrêtes src et dst
    public void writeEdgeEndToEnd() {
        System.out.println("V2");
        int i = 0;
        try {
            FileWriter fileWriter = new FileWriter("web/edges.js");
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
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Supprimes le contenu des fichiers edges et nodes
    public void cleanFiles (){
        try {
            FileWriter fileWriter = new FileWriter("web/edges.js");
            fileWriter.close();
            fileWriter = new FileWriter("web/nodes.js");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // Calcul la distance entre deux coordonnées
    private double distanceCoord (Coord A, Coord B) {
        return Math.acos( (Math.sin(Math.toRadians(A.getLatitude())) * Math.sin(Math.toRadians(B.getLatitude())))
                + (Math.cos(Math.toRadians(A.getLatitude())) * Math.cos(Math.toRadians(B.getLatitude()))
                * Math.cos(Math.toRadians(B.getLongitude() - A.getLongitude()) ) ) ) * 6371.0;
    }
}
