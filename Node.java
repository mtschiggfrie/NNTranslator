import java.util.ArrayList;
import java.util.Random;

/**
 * Created by MatthewT on 4/3/2017.
 */
public class Node {

    private ArrayList<Double> weights;
    private ArrayList<Double> previousWeights;
    private double bias;

    public Node(int numberOfWeights) {
        weights = new ArrayList<>();
        previousWeights = new ArrayList<>();
        for (int i = 0; i < numberOfWeights; ++i) {
            Random random = new Random();
            weights.add(random.nextDouble());
        }
        bias = (new Random()).nextDouble();
    }

    public Node(ArrayList<Double> list) {
        weights = new ArrayList<>(list);
        previousWeights = new ArrayList<>();
        bias = (new Random()).nextDouble();
    }

    private double sigmoidFunction(double sum) {
        return 1 / (1 + Math.pow(Math.E,(-1 * sum)));
    }

    public double computeOutput(ArrayList<Double> inputs) {
        double sum = bias;
        for (int i = 0; i < inputs.size(); ++i) {
            sum = sum + (inputs.get(i) * weights.get(i));
        }
        return sigmoidFunction(sum);
    }

    public void updateWeights(ArrayList<Double> changeOfWeights) {
        previousWeights = new ArrayList<>(weights);
        for (int i = 0; i < weights.size(); ++i) {
            weights.set(i, weights.get(i) + changeOfWeights.get(i));
        }
    }

    public void revertWeights() {
        weights = new ArrayList<>(previousWeights);
    }

    public int weightsSize() {
        return weights.size();
    }

    public ArrayList<Double> getWeights() {
        return weights;
    }
}
