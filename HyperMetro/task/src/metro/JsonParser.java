package metro;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonParser {

    private static String pathToJson;

    public static MetroGraph readJson(String filePath) throws IOException {
        String jsonContents = new String(Files.readAllBytes(Paths.get(filePath)));
        pathToJson = filePath;
        MetroGraph graph = new MetroGraph();
        ArrayList<Station> stations = new ArrayList<>();

        Matcher metroLineMatcher = Pattern.compile(RegexUtils.metroLineRegex).matcher(jsonContents);
        while (metroLineMatcher.find()) {
            String metroLineString = metroLineMatcher.group("line");
            String metroLineName = metroLineMatcher.group("lineName");
            Matcher metroStationMatcher = Pattern.compile(RegexUtils.metroStationRegex).matcher(metroLineString);
            while (metroStationMatcher.find()) {
                // Create stations and add them to the graph
                String stationName = metroStationMatcher.group("stationName");
                Station station = new Station(stationName, metroLineName);
                //System.out.println(station);

                // Add connection info to the station
                String[] nextStationNames = parseStationsNameArray(metroStationMatcher.group("nextField").replaceFirst("next", ""));
                for (String nextName : nextStationNames) {
                    Station st = new Station(nextName, metroLineName);
                    station.addNext(st);
                }
                String[] prevStationNames = parseStationsNameArray(metroStationMatcher.group("prevField").replaceFirst("prev", ""));
                for (String nextName : prevStationNames) {
                    Station st = new Station(nextName, metroLineName);
                    station.addPrev(st);
                }
                Station[] transferStations = parseTransferArray(metroStationMatcher.group("transferField").replaceFirst("transfer", ""));
                for (Station st : transferStations) {
                    station.addTransfer(st);
                }

                // Add time info to the station
                if (metroStationMatcher.group("timeInfo") != null) {
                    station.addTime(Integer.parseInt(metroStationMatcher.group("timeInfo")));
                }

                //station.showDetailedInfo();

                graph.addNode(station);
                stations.add(station);
            }
        }

        // Add connections for each station
        for (Station station : stations) {
            Station[] nextStations = station.getNext();
            for (Station s : nextStations) {
                graph.addConnection(station, s, station.getTimeToNext());
            }
            Station[] transferStations = station.getTransfer();
            for (Station s : transferStations) {
                graph.addTransfer(station, s);
            }
        }

        return graph;
    }

    private static String[] parseStationsNameArray(String text) {
        Matcher arrayMatcher = Pattern.compile("\"[\\w-'.&\\s]+\"").matcher(text);
        ArrayList<String> list = new ArrayList<>();
        while (arrayMatcher.find()) {
            list.add(arrayMatcher.group().replaceAll("\"", ""));
        }

        return list.toArray(new String[0]);
    }

    private static Station[] parseTransferArray(String text) {
        ArrayList<Station> transfers = new ArrayList<>();
        Matcher transferInfoMatcher = Pattern.compile(RegexUtils.transferStationRegex).matcher(text);
        while (transferInfoMatcher.find()) {
            String transferStationName = transferInfoMatcher.group("transferStationName");
            String transferLineName = transferInfoMatcher.group("transferLineName");
            Station station = new Station(transferStationName, transferLineName);
            transfers.add(station);
        }

        return transfers.toArray(new Station[0]);
    }

    // Todo: implement this method
    public static void updateJson(MetroGraph graph) {
        StringBuilder json = new StringBuilder();
        json.append("{\n");

        HashMap<String, ArrayList<Station>> lines = graph.getMetroLines();

        json.append("}");
        //System.out.println(json);

        try (FileWriter fileWriter = new FileWriter(pathToJson)) {
            fileWriter.append(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
