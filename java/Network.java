import java.util.Map;
import java.util.HashMap;
public class Network {
    static Map<Double, Node[]> states; //Maps times to network states
    static double time;
    static double deltaT;
    static double beta; //infectivity constant
    static double nu; //recovery constant
    static double gamma; //interactivity constant

    public static Map<Double, Node[]> getStates() {
        return states;
    }

    public static void addState(double time, Node[] state) {
        states.put(time, state);
    }

    public static Node updateNode(Node oldNode, double deltaS, double deltaI, double deltaR) {
        double newS = oldNode.S() + deltaS;
        double newI = oldNode.I() + deltaI;
        double newR = oldNode.R() + deltaR;
        Node output = new Node(newS, newI, newR);
        return output;
    }

    public static void loop() {
        Node[] currentState = states.get(time);
        Node[] newState = new Node[currentState.length];
        for (int i = 0; i < currentState.length; i++) {
            Node currentNode = currentState[i];
            double deltaS = 0;
            double deltaI = -nu * currentNode.I();
            double deltaR = nu * currentNode.I();
            for (int j = 0; j < currentState.length; j++) {
                if (i == j) {
                    deltaS -= deltaT * beta * currentNode.S() * currentNode.I();
                    deltaI += deltaT * beta * currentNode.S() * currentNode.I();
                } else {
                    deltaS -= deltaT * gamma * beta * currentNode.S() * currentState[j].I();
                    deltaI += deltaT * gamma * beta * currentNode.S() * currentState[j].I();
                }
            }
            newState[i] = updateNode(currentNode, deltaS, deltaI, deltaR);
        }
        time += deltaT;
        addState(time, newState);
    }

    public static void main(String args[]) {
        time = 0.;
        deltaT = 1.;
        beta = 0.1;
        nu = 0.1;
        gamma = 0.1;
        states = new HashMap<Double, Node[]>();
        Node[] initialState = new Node[3];
        initialState[0] = new Node(100.);
        initialState[1] = new Node(45., 5.);
        initialState[2] = new Node(50.);
        states.put(time, initialState);
        while(time < 100) {
            loop();
        }
        System.out.println(states);
    }
}
