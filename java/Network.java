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
    static double maxDistance; //max distance between nodes, used for scaling

    public static Map<Double, Node[]> getStates() {
        return states;
    }

    public static void addState(double time, Node[] state) {
        states.put(time, state);
    }

    public static void loop() {
        Node[] currentState = states.get(time);
        Node[] newState = new Node[currentState.length];
        for (int i = 0; i < currentState.length; i++) {
            double[] deltas = calculateDeltas(currentState, i);
            newState[i] = currentState[i].updateNode(deltas[0], deltas[1],
                deltas[2]);
        }
        time += deltaT;
        addState(time, newState);
    }

    public static double[] calculateDeltas(Node[] state, int index) {
        Node currentNode = state[index];
        double deltaS = 0;
        double deltaI = -deltaT * nu * currentNode.I();
        double deltaR = deltaT * nu * currentNode.I();
        for (int j = 0; j < state.length; j++) {
            if (index == j) {
                deltaS -= deltaT * beta * currentNode.S() * currentNode.I() /
                    state[j].population();
                deltaI += deltaT * beta * currentNode.S() * currentNode.I()/
                    state[j].population();
            } else {
                deltaS -= deltaT * currentNode.getGammas()[j] * beta *
                    currentNode.S() * state[j].I() / state[j].population();
                deltaI += deltaT * currentNode.getGammas()[j] * beta *
                    currentNode.S() * state[j].I() / state[j].population();
            }
        }
        double[] deltas = {deltaS, deltaI, deltaR};
        return deltas;
    }

    public static void setGammasByDistance(Node[] state) {
        for (int i = 0; i < state.length; i++) {
            state[i].initializeGammas(state.length);
            for (int j = 0; j < state.length; j++) {
                if (i != j) {
                    double dist = Node.distance(state[i], state[j]);
                    double adjustedGamma = gamma*(1-dist/maxDistance);
                    state[i].setGamma(j, adjustedGamma);
                }
            }
        }
    }

    public static void main(String args[]) {
        time = 0.;
        deltaT = 0.01;
        beta = 0.9;
        nu = 0.33;
        gamma = 0.05;
        maxDistance = 0.01533282863663108; //Folk to Smith
        Node[] initialState = getDorms();
        states = new HashMap<Double, Node[]>();
        states.put(time, initialState);
        while(time < 30) {
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
        List<String> thisLine = new ArrayList<String>();
        thisLine.add("time");
        for (Node node : states.get(times.get(0))) {
            thisLine.add("");
            thisLine.add(node.name());
            thisLine.add("");
            thisLine.add("");
        }
        output.add(thisLine.toArray(new String[thisLine.size()]));
        while (iter.hasNext()) {
            Double currentT = iter.next();
            if (cursor%100 == 0) {
                thisLine = new ArrayList<String>();
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

    public static Node[] getDorms() {
        Node[] output = new Node[19];
        output[0] = new Node(226., 0., 0., 33.779159, -84.403749);
        output[0].setName("Armstrong/Hefner");
        output[1] = new Node(91., 0., 0., 33.771645, -84.391830);
        output[1].setName("Brown");
        output[2] = new Node(158., 0., 0., 33.778911, -84.404411);
        output[2].setName("Caldwell");
        output[3] = new Node(119., 0., 0., 33.772735, -84.391699);
        output[3].setName("Cloudman");
        output[4] = new Node(126., 0., 0., 33.774203, -84.391651);
        output[4].setName("Field");
        output[5] = new Node(128., 0., 0., 33.778200, -84.403713);
        output[5].setName("Fitten");
        output[6] = new Node(156., 0., 0., 33.778920, -84.404846);
        output[6].setName("Folk");
        output[7] = new Node(112., 0., 0., 33.777507, -84.403921);
        output[7].setName("Freeman");
        output[8] = new Node(351., 0., 0., 33.773459, -84.391551);
        output[8].setName("Glenn");
        output[9] = new Node(118., 0., 0., 33.774196, -84.390908);
        output[9].setName("Hanson");
        output[10] = new Node(94., 0., 0., 33.772111, -84.391768);
        output[10].setName("Harris");
        output[11] = new Node(155., 0., 0., 33.772732, -84.390881);
        output[11].setName("Harrison");
        output[12] = new Node(134., 0., 0., 33.774083, -84.391292);
        output[12].setName("Hopkins");
        output[13] = new Node(123., 0., 0., 33.772125, -84.390900);
        output[13].setName("Howell");
        output[14] = new Node(152., 0., 0., 33.774528, -84.391540);
        output[14].setName("Matheson");
        output[15] = new Node(114., 0., 0., 33.777856, -84.403914);
        output[15].setName("Montag");
        output[16] = new Node(122., 0., 0., 33.774528, -84.391084);
        output[16].setName("Perry");
        output[17] = new Node(285., 10., 0., 33.771423, -84.391471);
        output[17].setName("Smith");
        output[18] = new Node(266., 0., 0., 33.773450, -84.391060);
        output[18].setName("Towers");
        setGammasByDistance(output);
        return output;
    }
}
