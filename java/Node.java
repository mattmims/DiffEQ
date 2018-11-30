public class Node {
    private double S;
    private double I;
    private double R;
    private final double size; //potentially implement later?

    public Node(double s, double i, double r) {
        S = s;
        I = i;
        R = r;
        size = S + I + R;
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
}
