import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

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
    private int textY;

    private int lastId;

    List<NeuronWeights> weights;

    public NeuralNetwork(Bird bird) {
        this.bird = bird;
        weights = new ArrayList<>();
        hiddenLayer = new ArrayList<>();
        inputsLayer = new ArrayList<>();
        outputsLayer = new Neuron(getNextId());

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
                    inputsLayer.add(i, new Neuron(dist, getNextId()));
                } else {
                    inputsLayer.get(i).setInitValue(dist);
                }
            }
        }
    }

    private int getNextId() {
        return ++lastId;
    }

    public void getNextAction(Graphics g, List<Pipe> nextPipe) {
        getInputs(nextPipe);
        startHidden();
        startWeights();
        getHidden();
        drawNeuralInputs(g);
        drawNeuralHidden(g);
    }

    public void getHidden() {
        for (Neuron neuroHidden : hiddenLayer) {
            List<NeuronWeights> weightsList = weights.stream().filter(n -> n.getNeuronOutput() == neuroHidden).toList();
            for (NeuronWeights weights : weightsList) {
                neuroHidden.setInitValue(weights.getOutputValue(true));
            }
        }
    }

    public void startHidden() {
        if (hiddenLayer.isEmpty()) {
            for (Neuron neuronInput : inputsLayer) {
                hiddenLayer.add(new Neuron(getNextId()));
            }
        }
    }

    public void startWeights() {
        if (weights.isEmpty()) {
            for (Neuron neuronInput : inputsLayer) {
                for (Neuron neuronOutput : hiddenLayer) {
                    weights.add(new NeuronWeights(neuronInput, neuronOutput, Math.random()));
                }
            }
        }
    }

    public void drawNeuralInputs(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("BOLD", 0, 15));

        int x = 10;
        int y = 30;

        g.drawString("Geração: " + generation, x, y);

        y += 20;

        g.setColor(Color.GREEN);

        for (int i = 0; i < inputsLayer.size(); i++) {
            Neuron neuron = inputsLayer.get(i);
            g.drawString("Camada de entrada " + i + ": " + String.format("%.2f", neuron.getInitValue()), x, y);

            y += 20;
        }
    }


    public void drawNeuralHidden(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("BOLD", 0, 15));

        int x = 10;
        int y = 90;

        for (int i = 0; i < hiddenLayer.size(); i++) {
            Neuron neuron = hiddenLayer.get(i);
            g.drawString("Camada oculta " + i + ": " + String.format("%.2f", neuron.getInitValue()), x, y);
            y += 20;
        }
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