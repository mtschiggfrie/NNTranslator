import java.util.ArrayList;

/**
 * Created by MatthewT on 4/3/2017.
 */
public class NeuralNetwork {

    private ArrayList<Layer> layers;

    public NeuralNetwork() {
        layers = new ArrayList<>();
        int numberOfInputs = 3;
        int numberOfOutputs = 1;
        int numberOfLayers = 3;
        double learningConstant = 1;
        int reps = 100000;

        layers.add(new Layer(numberOfInputs, 5));
        layers.add(new Layer(5, 2));
        layers.add(new Layer(2, numberOfOutputs));

        String[][] inputs = {{"0","1","0"},{"1","1","1"}, {"1","0","1"},{"1","1","0"},{"1","0","0"},{"0","0","1"},{"0","1","1"}};
        String[] outputs = {"1","1","0","0","1","1","0"};

        ArrayList<Double> outputList = new ArrayList<>();
        for (int i = 0; i < outputs.length; ++i) {
            outputList.add(Double.parseDouble(outputs[i]));
        }

        int index = 0;
        while (index < reps) {
            int correct = 0;
            for (int i = 0; i < outputList.size(); ++i) {
                ArrayList<Double> currentInput = new ArrayList<>();
                for (int j = 0; j < inputs[0].length; ++j) {
                    currentInput.add(Double.parseDouble(inputs[i][j]));
                }

                ArrayList<Double> outputOfLayer = new ArrayList<>();
                ArrayList<ArrayList<Double>> outputOfLayers = new ArrayList<>();
                for (int j = 0; j < numberOfLayers; ++j) {
                    outputOfLayer = layers.get(j).computeOutputs(currentInput);
                    currentInput = outputOfLayer;
                    outputOfLayers.add(outputOfLayer);
                }

                Double error = outputList.get(i) - outputOfLayer.get(0);
                if (outputList.get(i) == 0 && outputOfLayer.get(0) < 0.5) {
                    correct++;
                }
                else if (outputList.get(i) == 1 && outputOfLayer.get(0) > 0.5) {
                    correct++;
                }

                ArrayList<Double> previousLayerWeightChange = new ArrayList<>();

                for (int j = outputOfLayers.size() - 1; j > -1; --j) {
                    ArrayList<Double> layerWeightChange = new ArrayList<>();
                    for (int k = 0; k < outputOfLayers.get(j).size(); ++k) {
                        double currentOutput = outputOfLayers.get(j).get(k);
                        double deltaWeight = 0;
                        if (j == outputOfLayers.size() - 1) {
                            deltaWeight = learningConstant * (outputList.get(i) - currentOutput) * (1 - currentOutput) * currentOutput;
                            layerWeightChange.add(deltaWeight);
                        } else {
                            for (int p = 0; p < previousLayerWeightChange.size(); ++p) {
                                deltaWeight = previousLayerWeightChange.get(p) * (1 - currentOutput) * currentOutput;
                                layerWeightChange.add(deltaWeight);
                            }
                        }
                    }
                    previousLayerWeightChange = new ArrayList<>(layerWeightChange);
                    layers.get(j).updateWeights(layerWeightChange);
                    layerWeightChange.clear();
                }

            }
            index++;
            System.out.println(correct);
        }
    }

    private double dotProduct(ArrayList<Double> l1, ArrayList<Double> l2) {
        double dotProduct = 0;
        for (int i = 0; i < l1.size(); ++i) {
            dotProduct += (l1.get(i) * l2.get(i));
        }
        return dotProduct;
    }

    private double deriv(double d) {
        return d * (1 - d);
    }
}
