import com.jogamp.common.util.SHASum;

import java.awt.*;
import java.util.*;
import java.util.List;

public class NeuralNetwork {
    private final Bird bird;

    // O --- O \
    //    X     O
    // O --- O /

    private final List<Neuron> inputsLayer;
    private final List<Neuron> hiddenLayer;
    private final Neuron outputsLayer;
    private final Random random = new Random();
    private int generation;

    // Onde comeca, ( Onde vai, Onde termina )
    Map<Neuron, Map<Neuron, Double>> weights;

    public NeuralNetwork(Bird bird) {
        this.bird = bird;
        weights = new HashMap<>();
        hiddenLayer = new ArrayList<>();
        inputsLayer = new ArrayList<>();
        outputsLayer = new Neuron();

        generation = 0;
    }

    public void getInputs(List<Pipe> nextPipe) {
        if (nextPipe != null) {
            for (int i = 0; i < nextPipe.size(); i++) {
                Pipe pipe = nextPipe.get(i);
                double inputX =  pipe.getX() + pipe.getWidth() / 2;
                double inputY = pipe.getY();
                double birdX = bird.getX() + bird.getWidth();
                double birdY = bird.getY() + bird.getHeight() / 2;

                if (inputY < 0) {
                    inputY += (int) pipe.getHeight();
                }

                double dist = Math.sqrt(Math.pow((inputX - birdX), 2) + Math.pow((inputY - birdY), 2));

                if (inputsLayer.size() < nextPipe.size()) {
                    inputsLayer.add(i, new Neuron(dist));
                } else {
                    inputsLayer.get(i).setInitValue(dist);
                }
            }
        }
    }

    public void getNextAction(Graphics g, List<Pipe> nextPipe) {
        getInputs(nextPipe);
        getHidden();
        drawNeuralInputs(g);
    }

    public void getHidden() {
        if (!inputsLayer.isEmpty()) {
            for (Neuron neuronInput : inputsLayer) {
                if (!weights.containsKey(neuronInput)) {
                    weights.put(neuronInput, new HashMap<>());
                }

                for (Neuron neuroHidden : hiddenLayer) {
                    Map<Neuron, Double> innerMap = weights.get(neuronInput);

                    if (!innerMap.containsKey(neuroHidden)) {
                        innerMap.put(neuroHidden, Math.random());
                    }

                    Double weightValue = innerMap.get(neuroHidden);

                    if (weightValue == null) {
                        weightValue = Math.random() * 2 - 1;
                    }

                    double newValue = neuronInput.getInitValue() * weightValue;
                    neuroHidden.setInitValue(newValue);
                }
            }
        }
    }

    public void drawNeuralInputs(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", 0, 20));

        int x = 10;
        int y = 30;

        for (int i = 0; i < inputsLayer.size(); i++) {
            Neuron neuron = inputsLayer.get(i);
            g.drawString("Distância até cano " + i + ": " + String.format("%.2f", neuron.getInitValue()), x, y);
            y += 20;
        }

        g.drawString("Geração: " + generation, x, y);
    }

    @Override
    public String toString() {
        return "NeuralNetwork{\n" +
                "    inputsLayerSize=" + inputsLayer.size() + "\n" +
                "    hiddenLayerSize=" + hiddenLayer.size() + "\n" +
                "    weightsSize=" + weights.size()  + "\n" +
                "    inputsLayer=" + inputsLayer + "\n" +
                "    hiddenLayer=" + hiddenLayer + "\n" +
                "    outputsLayer=" + outputsLayer + "\n" +
                "    weights=" + weights + "\n" +
                '}';
    }
}