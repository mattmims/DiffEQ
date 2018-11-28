public class Node {
    private double S;
    private double I;
    private double R;
    private final double size; //potentially implement later?

    public Node(double S, double I, double R) {
        S = S;
        I = I;
        R = R;
        size = S + I + R;
    }

    public Node(double S, double I) {
        this(S, I, 0.);
    }

    public Node(double S) {
        this(S, 0., 0.);
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
