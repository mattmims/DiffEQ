import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import com.opencsv.*;
import java.io.Writer;
import java.io.FileWriter;
import java.io.IOException;

public class Network {
    static Map<Double, Node[]> states; //Maps times to network states
    static double time; //1 unit time = one day
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
            double deltaI = -deltaT * nu * currentNode.I();
            double deltaR = deltaT * nu * currentNode.I();
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
        deltaT = 0.001;
        beta = 0.9;
        nu = 0.33;
        gamma = 0.05;
        states = new HashMap<Double, Node[]>();
        Node[] initialState = new Node[4];
        initialState[0] = new Node(100.);
        initialState[1] = new Node(50.);
        initialState[2] = new Node(100.);
        initialState[3] = new Node(95., 5.);
        states.put(time, initialState);
        //System.out.println(initialState[0].S() + ", " + initialState[0].I() + ", " + initialState[0].R());
        //System.out.println(initialState[3].S() + ", " + initialState[3].I() + ", " + initialState[3].R());
        while(time < 3) {
            loop();
        }
        writeOutput(states);
    }

    public static void writeOutput(Map<Double, Node[]> mp) {
        List<Double> times = new ArrayList<Double>(states.keySet());
        Collections.sort(times);
        Iterator<Double> iter = times.iterator();
        int cursor = 0;
        List<String[]> output = new ArrayList<String[]>();
        while (iter.hasNext()) {
            Double currentT = iter.next();
            if (cursor%20 == 0) {
                List<String> thisLine = new ArrayList<String>();
                thisLine.add(Double.toString(currentT));
                Node[] state = states.get(currentT);
                for (Node node : state) {
                    thisLine.add("");
                    thisLine.add(Double.toString(node.S()));
                    thisLine.add(Double.toString(node.I()));
                    thisLine.add(Double.toString(node.R()));
                }
                output.add(thisLine.toArray(new String[thisLine.size()]));
            }
            cursor++;
        }
        try {
            Writer writer = new FileWriter("output.csv");
            CSVWriter csvWriter = new CSVWriter(writer);
            csvWriter.writeAll(output);
            csvWriter.close();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
