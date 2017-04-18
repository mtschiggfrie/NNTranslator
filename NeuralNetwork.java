import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import static javax.script.ScriptEngine.FILENAME;

/**
 * Created by MatthewT on 4/3/2017.
 */
public class NeuralNetwork {

    private ArrayList<Layer> layers;
    private ArrayList<ArrayList<Integer>> inputs;
    private ArrayList<ArrayList<Integer>> outputs;

    public NeuralNetwork() {
        layers = new ArrayList<>();
        InputReader inputReader = new InputReader();
        inputs = new ArrayList<>(inputReader.getInputVectors());
        outputs = new ArrayList<>(inputReader.getOutputVectors());

        int numberOfInputs = inputs.get(0).size();
        int numberOfOutputs = outputs.get(0).size();
        int numberOfLayers = 3;
        double learningConstant = 0.01;
        int reps = 0;

        layers.add(new Layer(numberOfInputs, 100));
        layers.add(new Layer(100, 50));
        layers.add(new Layer(50, numberOfOutputs));

        loadNetwork();
        saveNetwork();

        int index = 0;
        while (index < reps) {

            for (int i = 0; i < outputs.size(); ++i) {
                ArrayList<Double> currentInput = new ArrayList<>();
                for (int j = 0; j < inputs.get(i).size(); ++j) {
                    currentInput.add((double) inputs.get(i).get(j));
                }

                ArrayList<Double> outputOfLayer = new ArrayList<>();
                ArrayList<ArrayList<Double>> outputOfLayers = new ArrayList<>();
                for (int j = 0; j < numberOfLayers; ++j) {
                    outputOfLayer = layers.get(j).computeOutputs(currentInput);
                    currentInput = outputOfLayer;
                    outputOfLayers.add(outputOfLayer);
                }

                Double error = 0.0;
                for (int j = 0; j < outputOfLayer.size(); ++j) {
                    error = error + outputs.get(i).get(j) - outputOfLayer.get(j);
                }

                System.out.println(inputReader.convertOutputInt(outputs.get(i)));
                System.out.println(inputReader.convertOutputDouble(outputOfLayer));

                ArrayList<Double> previousLayerWeightChange = new ArrayList<>();

                for (int j = outputOfLayers.size() - 1; j > -1; --j) {
                    ArrayList<Double> layerWeightChange = new ArrayList<>();
                    for (int k = 0; k < outputOfLayers.get(j).size(); ++k) {
                        double currentOutput = outputOfLayers.get(j).get(k);
                        double deltaWeight = 0;
                        if (j == outputOfLayers.size() - 1) {
                            deltaWeight = learningConstant * (outputs.get(i).get(k) - currentOutput) * (1 - currentOutput) * currentOutput;
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
        }
    }

    private void saveNetwork() {
        BufferedWriter bw = null;
        FileWriter fw = null;

        try {
            fw = new FileWriter("savedNN2.txt");
            bw = new BufferedWriter(fw);

            for (int i = 0; i < layers.size(); ++i) {
                ArrayList<ArrayList<Double>> layerWeights = layers.get(i).getNodeWeights();
                bw.write("LAYER " + i + "\n");
                for (int j = 0; j < layerWeights.size(); ++j) {
                    for (int p = 0; p < layerWeights.get(j).size(); ++p) {
                        bw.write(layerWeights.get(j).get(p) + ",");
                    }
                    bw.write("\n");
                }
            }

            System.out.println("Done");

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            try {
                if (bw != null)
                    bw.close();

                if (fw != null)
                    fw.close();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void loadNetwork() {
        layers.clear();
        try {
            Scanner scanner = new Scanner(new File("C:/Users/MatthewT/Desktop/nn/savedNN.txt"));
            Layer layer;
            scanner.nextLine();
            ArrayList<ArrayList<Double>> weights = new ArrayList<ArrayList<Double>>();

            while(scanner.hasNextLine()) {
                String currentLine = scanner.nextLine().toLowerCase();
                if (!currentLine.contains("layer")) {
                    String[] array = currentLine.split(",");
                    ArrayList<Double> currentWeights = new ArrayList<>();
                    for (int i = 0; i < array.length; ++i) {
                        currentWeights.add(Double.parseDouble(array[i]));
                    }
                    weights.add(currentWeights);
                }
                else {
                    layers.add(new Layer(weights));
                    weights.clear();
                }
            }
            layers.add(new Layer(weights));
            weights.clear();
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }

}
