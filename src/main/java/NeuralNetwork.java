// SONO BATEU
// PARA NAO ME PERDER
// EU PRECISO INICIAR OS NEURONIOS NO CONSTRUTOR
// E NO GET INPUTS MUDAR OS NEURONIOS DE ENTRADA APENAS
// ESTUDAR POSSIBILDIADE DE CONSTANTE DE QUANTAS ENTRADAS VAMOS TER

import java.awt.*;
import java.util.*;
import java.util.List;

public class NeuralNetwork {
    private final Bird bird;
    private final Map<Integer, List<Pipe>> pipesMap;


    // O --- O \
    //    X     O
    // O --- O /

    private final List<Neuron> inputsLayer;
    private final List<Neuron> hiddenLayer;
    private final Neuron outputsLayer;
    private final Random random = new Random();

    // Onde comeca, ( Onde vai, Onde termina )
    Map<Neuron, Map<Neuron, Double>> weights;

    public NeuralNetwork(Bird bird, Map<Integer, List<Pipe>> pipesMap) {
        this.bird = bird;
        this.pipesMap = pipesMap;
        weights = new HashMap<>();
        hiddenLayer = new ArrayList<>();
        inputsLayer = new ArrayList<>();
        outputsLayer = new Neuron();


        // MUDAR
        for (Neuron neuron : inputsLayer) {
            weights.put(neuron, new HashMap<>());

            for (Neuron neuronHidden : hiddenLayer) {
                weights.get(neuronHidden).put(neuronHidden, random.nextDouble() * 2 - 1);
            }
        }
    }

    public void getInputs() {
        List<Pipe> nextPipe = getNextPipe();

        if (nextPipe != null) {
            for (Pipe pipe : nextPipe) {
                double inputX =  pipe.getX() + pipe.getWidth() / 2;
                double inputY = pipe.getY();
                double birdX = bird.getX() + bird.getWidth();
                double birdY = bird.getY() + bird.getHeight() / 2;

                if (inputY < 0) {
                    inputY += (int) pipe.getHeight();
                }

                double dist = Math.sqrt(Math.pow((inputX - birdX), 2) + Math.pow((inputY - birdY), 2));


                // TIRAR
                inputsLayer.add(new Neuron(
                        dist
                ));
            }
        }
    }

    public void getNextAction(Graphics g) {
        List<Neuron> inputs = getInputs();
        drawNeuralInputs(g, inputs);
    }

    public void drawNeuralInputs(Graphics g, List<Neuron> inputs) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", 0, 20));

        int x = 10;
        int y = 30;

        for (int i = 0; i < inputs.size(); i++) {
            Neuron neuron = inputs.get(i);
            g.drawString("Distância até cano " + i + ": " + String.format("%.2f", neuron.getInitValue()), x, y);
            y += 20;
        }
    }

    private List<Pipe> getNextPipe() {
        if (pipesMap.isEmpty()) return null;

        Pipe next = null;
        int nextKey = -1;

        for (Map.Entry<Integer, java.util.List<Pipe>> entry : pipesMap.entrySet()) {
            Pipe pipe = entry.getValue().get(0);
            if (pipe.getX() + pipe.getWidth() > bird.getX()) {
                if (next == null || pipe.getX() < next.getX()) {
                    next = pipe;
                    nextKey = entry.getKey();
                }
            }
        }

        return nextKey == -1 ? null : pipesMap.get(nextKey);
    }

    public List<Neuron> getOutputsLayer() {
        return outputsLayer;
    }

    public List<Neuron> getHiddenLayer() {
        return hiddenLayer;
    }

    public List<Neuron> getInputsLayer() {
        return inputsLayer;
    }
}