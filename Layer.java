import java.util.ArrayList;

/**
 * Created by MatthewT on 4/3/2017.
 */
public class Layer {

    private ArrayList<Node> listOfNodes;
    private ArrayList<Double> previousOutputs;
    private ArrayList<Double> previousInputs;

    public Layer(int numberOfInputs, int numberOfOutputs) {
        listOfNodes = new ArrayList<>();
        previousInputs = new ArrayList<>();
        previousOutputs = new ArrayList<>();
        for (int i = 0; i < numberOfOutputs; ++i) {
            listOfNodes.add(new Node(numberOfInputs));
        }
    }

    public ArrayList<Double> computeOutputs(ArrayList<Double> inputs) {
        previousInputs = inputs;
        ArrayList<Double> outputs = new ArrayList<>();
        for (int i = 0; i < listOfNodes.size(); ++i) {
            outputs.add(listOfNodes.get(i).computeOutput(inputs));
        }
        previousOutputs = outputs;
        return outputs;
    }

    public void updateWeights(ArrayList<Double> layerWeightChange) {
        ArrayList<Double> changeOfWeights = new ArrayList<>();
        int numberOfSets = layerWeightChange.size() / listOfNodes.size();

        double error = 0;
        double minError = 10;
        int minErrorIndex = 0;

        for (int k = 0; k < numberOfSets; ++k) {
            error = 0;
            for (int i = 0; i < listOfNodes.size(); ++i) {
                for (int j = 0; j < previousInputs.size(); ++j) {
                    changeOfWeights.add(previousInputs.get(j) * layerWeightChange.get(i + (k * listOfNodes.size())));
                }
                listOfNodes.get(i).updateWeights(changeOfWeights);
                double newOutput = listOfNodes.get(i).computeOutput(previousInputs);
                error += (previousOutputs.get(i) - newOutput);
                changeOfWeights.clear();
                listOfNodes.get(i).revertWeights();
            }
            if (Math.abs(error) < minError) {
                minError = error;
                minErrorIndex = k;
            }
        }

        for (int i = 0; i < listOfNodes.size(); ++i) {
            for (int j = 0; j < previousInputs.size(); ++j) {
                changeOfWeights.add(previousInputs.get(j) * layerWeightChange.get(i + (minErrorIndex * listOfNodes.size())));
            }
            listOfNodes.get(i).updateWeights(changeOfWeights);
            changeOfWeights.clear();
        }
    }
}
