package metro;

import java.util.*;

public class MetroGraph {
    private final HashMap<Station, ArrayList<Connection>> adjacencyList;
    private final ArrayList<Station> stations;
    private HashMap<Station, Integer> stationDistances;
    private HashMap<Station, Station> previousStations;

    public MetroGraph() {
        adjacencyList = new HashMap<>();
        stations = new ArrayList<>();
    }

    public void addNode(Station node) {
        adjacencyList.put(node, new ArrayList<>());
        stations.add(node);
    }

    public void addConnection(Station from, Station temp, int weight) {
        // Find temp station among all nodes. Temp has contains only name and line information.
        // But the station that we get contains also all connection data (e.g. next, prev, transfer)
        Station to = null;
        for (Station s : stations) {
            if (s.equals(temp)) {
                to = s;
                break;
            }
        }
        Connection cForwards = new Connection(to, weight);
        if (!adjacencyList.get(from).contains(cForwards)) {
            adjacencyList.get(from).add(cForwards);
        }
        Connection cBackward = new Connection(from, weight);
        if (!adjacencyList.get(to).contains(cBackward)) {
            adjacencyList.get(to).add(cBackward);
        }
    }

    public void addTransfer(Station from, Station temp) {
        // Find temp station among all nodes. Temp has contains only name and line information.
        // But the station that we get contains also all connection data (e.g. next, prev, transfer)
        Station to = null;
        for (Station s : stations) {
            if (s.equals(temp)) {
                to = s;
                break;
            }
        }
        Connection cForwards = new Connection(to, true);
        if (!adjacencyList.get(from).contains(cForwards)) {
            adjacencyList.get(from).add(cForwards);
        }
        Connection cBackwards = new Connection(from, true);
        if (!adjacencyList.get(to).contains(cBackwards)) {
            adjacencyList.get(to).add(cBackwards);
        }
    }

    // Todo: get lines info to later recreate .json file
    public HashMap<String, ArrayList<Station>> getMetroLines() {
        HashMap<String, ArrayList<Station>> metroLines = new HashMap<>();


        return null;
    }

    public String route(Station from, Station to, boolean showTime) {
        if (showTime) {
            dijkstra(from, to, adjacencyList);
        } else {
            HashMap<Station, ArrayList<Connection>> unweightedAdjacencyList = new HashMap<>();
            for (Station key : adjacencyList.keySet()) {
                ArrayList<Connection> unweightedConnections = new ArrayList<>();
                for (Connection c : adjacencyList.get(key)) {
                    Connection unweighted = c.getUnweighted();
                    unweightedConnections.add(unweighted);
                }
                unweightedAdjacencyList.put(key, unweightedConnections);
            }

            dijkstra(from, to, unweightedAdjacencyList);
        }
        return getReadablePath(to, showTime);
    }

    private void dijkstra(Station from, Station to, HashMap<Station, ArrayList<Connection>> list) {
        stationDistances = new HashMap<>();
        previousStations = new HashMap<>();
        PriorityQueue<Station> priorityQueue = new PriorityQueue<>();
        for (Station key : list.keySet()) {
            if (key.equals(from)) {
                key.setDistance(0);
                stationDistances.put(key, 0);
            } else {
                stationDistances.put(key, Integer.MAX_VALUE);
                key.setDistance(Integer.MAX_VALUE);
            }
            previousStations.put(key, null);
            priorityQueue.add(key);
        }
        ArrayList<Station> visitedStations = new ArrayList<>();
        while (!priorityQueue.isEmpty()) {
            Station station = priorityQueue.remove();
            if (!visitedStations.contains(station)) {
                visitedStations.add(station);
                Connection[] neighbors = list.get(station).toArray(new Connection[0]);

                for (Connection c : neighbors) {
                    Station connectedStation = c.getConnectedStation();
                    int weight = c.getWeight();
                    int alternativeDistance = stationDistances.get(station) + weight;

                    if (alternativeDistance < stationDistances.get(connectedStation)) {
                        stationDistances.put(connectedStation, alternativeDistance);
                        connectedStation.setDistance(alternativeDistance);
                        previousStations.put(connectedStation, station);
                    }

                    priorityQueue.remove(connectedStation);
                    priorityQueue.add(connectedStation);
                }
            }
        }
    }

    private String getReadablePath(Station to, boolean showTime) {
        ArrayDeque<Station> route = new ArrayDeque<>();
        Station s = to;
        while(s != null) {
            route.addFirst(s);
            s = previousStations.get(s);
        }

        StringBuilder path = new StringBuilder();
        Station prev = null;
        for (Station station : route) {
            if (prev != null && isTransferBetween(prev, station)) {
                path.append("Transition to ").append(station.getLine()).append("\n");
            }
            path.append(station.getName()).append("\n");

            prev = station;
        }

        if (showTime) {
            path.append("Total: ").append(stationDistances.get(to)).append(" minutes in the way");
        } else {
            path.replace(path.length() - 1, path.length(), "");
        }
        return String.valueOf(path);
    }

    private boolean isTransferBetween(Station s1, Station s2) {
        ArrayList<Connection> connections = adjacencyList.get(s1);
        for (Connection c : connections) {
            if (c.isTransferTo(s2)) {
                return true;
            }
        }
        return false;
    }
}

//          /fastest-route Metro-Railway Bishops-road Hammersmith-and-City Hammersmith