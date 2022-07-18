package metro;

public class Connection {
    private final Station toStation;
    private final int weight;
    private final boolean transfer;

    public Connection(Station toStation, int weight) {
        this.toStation = toStation;
        this.weight = weight;
        this.transfer = false;
    }

    public Connection(Station toStation, boolean transfer) {
        this.toStation = toStation;
        this.weight = 5;
        this.transfer = transfer;
    }

    private Connection(Station toStation, int weight, boolean transfer) {
        this.toStation = toStation;
        this.weight = weight;
        this.transfer = transfer;
    }

    @Override
    public String toString() {
        return String.valueOf(toStation);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Connection)) {
            return false;
        }

        Connection c = (Connection) o;

        return c.toStation.equals(this.toStation) && c.weight == this.weight && c.transfer == this.transfer;
    }

    public Connection getUnweighted() {
        int newWeight = 1;
        if (this.transfer) {
            newWeight = 0;
        }
        return new Connection(this.toStation, newWeight, this.transfer);
    }

    public Station getConnectedStation() {
        return toStation;
    }

    public int getWeight() {
        return weight;
    }

    public boolean isTransferTo(Station station) {
        return transfer && toStation.equals(station);
    }
}
