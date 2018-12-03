public class Node {
    private double S;
    private double I;
    private double R;
    private final double pop;
    private double latitude;
    private double longitude;
    private double[] gammas;
    private String name;

    public Node(double s, double i, double r, double lat, double lon) {
        S = s;
        I = i;
        R = r;
        pop = S + I + R;
        latitude = lat;
        longitude = lon;
    }

    public Node(double s, double i, double r, double lat, double lon, double[] connections) {
        S = s;
        I = i;
        R = r;
        pop = S + I + R;
        latitude = lat;
        longitude = lon;
        gammas = connections;
    }

    public Node(double s, double i, double r) {
        S = s;
        I = i;
        R = r;
        pop = S + I + R;
    }

    public Node(double s, double i) {
        this(s, i, 0.);
    }

    public Node(double s) {
        this(s, 0., 0.);
    }

    public double S() {
        return S;
    }

    public double I() {
        return I;
    }

    public double R() {
        return R;
    }

    public double population() {
        return pop;
    }

    public double longitude() {
        return longitude;
    }

    public double latitude() {
        return latitude;
    }

    public void initializeGammas(int size) {
        gammas = new double[size];
    }

    public double[] getGammas() {
        return gammas;
    }

    public void setGamma(int index, double value) {
        gammas[index] = value;
    }

    public static double distance(Node n1, Node n2) {
        return Math.sqrt(Math.pow(n1.longitude() - n2.longitude(),2) +
            Math.pow(n1.latitude() - n2.latitude(), 2));
    }

    public String name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Node updateNode(double deltaS, double deltaI,
        double deltaR) {
        double newS = this.S() + deltaS;
        double newI = this.I() + deltaI;
        double newR = this.R() + deltaR;
        Node output = new Node(newS, newI, newR, this.longitude(), this.latitude(), this.getGammas());
        output.setName(this.name());
        return output;
    }
}
