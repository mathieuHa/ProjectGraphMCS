package com.company;
import org.json.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Graphe {
    private ArrayList<Edge> listEdge;
    private ArrayList<Node> listNode;


    public Graphe(){
        listEdge = new ArrayList<>();
        listNode = new ArrayList<>();
        this.initGraph();
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
                            coord.setX(coArray.getDouble(0));
                            coord.setY(coArray.getDouble(1));
                            edgeTemp.getPointList().add(coord);
                        }
                        listEdge.add(edgeTemp);
                    } else if (jTempGeo.getString("type").equals("Point")){
                        Node nodeTemp = new Node();
                        Coord coord = new Coord();
                        nodeTemp.setId(jTempProp.getInt("id"));
                        coord.setX(arrayCoord.getDouble(0));
                        coord.setY(arrayCoord.getDouble(1));
                        nodeTemp.setPos(coord);
                        listNode.add(nodeTemp);
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void filterPrimary () {
        ArrayList<Edge> listTemp = new ArrayList<Edge>();
        for (Edge edge : listEdge) {
            if (edge.getType().equals("primary")) {
                listTemp.add(edge);
            }
        }
        listEdge = listTemp;

        ArrayList<Node> listTempNode = new ArrayList<Node>();

        HashMap<Integer, Node> listNodeHM = new HashMap<>();
        for (Edge edge : listEdge) {
            for (Node node : listNode) {
                if (edge.getSrc().getId() == node.getId() || edge.getDest().getId() == node.getId()) {
                    listNodeHM.put(node.getId(), node);
                }
            }
        }

        for (int i = 0; i < listNodeHM.size(); i++) {
            listTempNode.add(listNode.get(i));
        }
        listNode = listTempNode;
    }

    public void filterRelevant () {
        ArrayList<Edge> listTemp = new ArrayList<Edge>();
        for (Edge edge: listEdge){
            if (edge.getType().equals("primary") || edge.getType().equals("secondary") || edge.getType().equals("tertiary")){
                listTemp.add(edge);
            }
        }
        listEdge = listTemp;
    }


    public void printEdgeList() {
        System.out.println(listEdge);
    }

    public void printNodeList() {
        System.out.println(listNode);
    }

    public void writeNodes() {
        int i = 0;
        try {
            FileWriter fileWriter = new FileWriter("web/result.js");
            fileWriter.write("var points = [");
            for (Node node: listNode){
                fileWriter.write("" +
                        "{\n" +
                        "    \"type\": \"Feature\",\n" +
                        "    \"geometry\": {\n" +
                        "        \"type\": \"Point\",\n" +
                        "        \"coordinates\": ["+ node.getPos().getX()+ ", "+ node.getPos().getY() +"]\n" +
                        "    }\n" +
                        "}");
                i++;
                if (i!=listNode.size()){
                    fileWriter.write(",");
                }
            }
            fileWriter.write("]; \n" +
                    "L.geoJSON(points).addTo(mymap);");

            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeEdge() {
        int i = 0;
        try {
            FileWriter fileWriter = new FileWriter("web/result.js");
            fileWriter.write("var edges = [");
            for (Edge edge: listEdge){
                fileWriter.write("" +
                        "{\n" +
                        "    \"type\": \"Feature\",\n" +
                        "    \"geometry\": {\n" +
                        "        \"type\": \"Polygon\",\n" +
                        "        \"coordinates\": [[");
                int e = 0;
                for (Coord coord: edge.getPointList()){
                    fileWriter.write("["+coord.getX()+","+coord.getY()+"]\n");
                    e++;
                    if (e!=edge.getPointList().size()){
                        fileWriter.write(",");
                    }
                }
                fileWriter.write("]]\n");
                fileWriter.write("}\n }\n");
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
}
