package metro;

import java.util.ArrayList;

public class Station implements Comparable {
    // Values to create a proper hash code for the object
    private static final int HASH_CONSTANT = 5;
    private static final int INITIAL_HASH = 0;
    private static final int HASH_DIVISOR = Integer.MAX_VALUE / 2;

    private final String name;
    private final String line;

    private final ArrayList<Station> prev;
    private final ArrayList<Station> next;
    private final ArrayList<Station> transfer;

    private int timeToNext;

    private int distance = Integer.MAX_VALUE;

    public Station(String name, String line) {
        this.name = name;
        this.line = line;

        prev = new ArrayList<>();
        next = new ArrayList<>();
        transfer = new ArrayList<>();
    }

    public void addPrev(Station s) {
        prev.add(s);
    }

    public void addNext(Station s) {
        next.add(s);
    }

    public void addTransfer(Station s) {
        transfer.add(s);
    }

    public void addTime(int time) {
        this.timeToNext = time;
    }

    public void setDistance(int value) {
        distance = value;
    }

    public String getName() {
        return name;
    }

    public String getLine() {
        return line;
    }

    public Station[] getNext() {
        return next.toArray(new Station[0]);
    }

    public Station[] getTransfer() {
        return transfer.toArray(new Station[0]);
    }

    public boolean hasTransfer() {
        return !transfer.isEmpty();
    }

    public int getTimeToNext() {
        return timeToNext;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Station)) {
            return false;
        }

        Station s = (Station) o;

        return (s.name + s.line).equals((this.name + this.line));
    }

    @Override
    public int hashCode() {
        int hashValue = INITIAL_HASH;
        String description = name + line;
        for (int i = 0; i < description.length(); i++) {
            char ch = description.charAt(i);
            hashValue = (hashValue * HASH_CONSTANT + ch) % HASH_DIVISOR;
        }

        return hashValue;
    }

    @Override
    public String toString() {
        return name + " (" + line + ")";
    }

    public String convertToJson() {
        StringBuilder output = new StringBuilder();
        output.append("\t\t{\n");
        output.append("\t\t\t").append("\"name\": \"").append(name).append("\",\n");

        // Add prev info
        output.append("\t\t\t\"prev\": ");
        addArrayInfoToJson(output, prev);

        // Add next info
        output.append("\t\t\t\"next\": ");
        addArrayInfoToJson(output, next);

        // Add prev info
        output.append("\t\t\t\"transfer\": ");
        if (transfer.isEmpty()) {
            output.append("[]\n");
        } else {
            output.append("[\n");
            for (Station s : transfer) {
                output.append("\t\t\t\t{\n");
                output.append("\t\t\t\t\t\"line\": \"").append(s.line).append("\",\n");
                output.append("\t\t\t\t\t\"station\": \"").append(s.name).append("\"\n");
                output.append("\t\t\t\t},\n");
            }
            output.replace(output.length() - 2, output.length(), "\n");
            output.append("\t\t\t],\n");
        }

        // Add time info
        if (timeToNext == 0) {
            output.replace(output.length()-2, output.length(), "\n");
        } else {
            output.append("\t\t\t\t\"time\": ").append(timeToNext).append("\n");
        }
        output.append("\t\t}");

        return output.toString();
    }

    private void addArrayInfoToJson(StringBuilder output, ArrayList<Station> array) {
        if (array.isEmpty()) {
            output.append("[]\n");
        } else {
            output.append("[\n");
            for (Station s : array) {
                output.append("\t\t\t\t\"").append(s.name).append("\",\n");
            }
            output.replace(output.length() - 2, output.length(), "\n");
            output.append("\t\t\t],\n");
        }
    }

    @Override
    public int compareTo(Object o) {
        Station s = (Station) o;
        return Integer.compare(this.distance, s.distance);
    }
}
