public class NeuronWeights {
    private Neuron neuronInput;
    private Neuron neuronOutput;
    private double value;

    public NeuronWeights() {
    }

    public NeuronWeights(Neuron neuronInput, Neuron neuronOutput, double value) {
        this.neuronInput = neuronInput;
        this.neuronOutput = neuronOutput;
        this.value = value;
    }

    public NeuronWeights(Neuron neuronInput) {
        this.neuronInput = neuronInput;
    }

    public Neuron getNeuronInput() {
        return neuronInput;
    }

    public void setNeuronInput(Neuron neuronInput) {
        this.neuronInput = neuronInput;
    }

    public Neuron getNeuronOutput() {
        return neuronOutput;
    }

    public void setNeuronOutput(Neuron neuronOutput) {
        this.neuronOutput = neuronOutput;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getOutputValue(boolean includeSigmoid) {
        return neuronInput.getOutputValue(includeSigmoid) * value;
    }

    @Override
    public String toString() {
        return "NeuronWeights{" +
                "neuronInput=" + neuronInput +
                ", neuronOutput=" + neuronOutput +
                ", value=" + value +
                '}';
    }
}
