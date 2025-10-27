public class Neuron {
    private double initValue;
    private double biasValue;
    private double outputValue;

    public Neuron(double initValue) {
        this.initValue = initValue;
    }

    public Neuron() {
    }

    public double getInitValue() {
        return initValue;
    }

    public void setInitValue(double initValue) {
        this.initValue = initValue;
    }

    public double getOutputValue(boolean includeSigmoid) {
        if (!includeSigmoid) return initValue;
        return sigmoid(initValue + biasValue);
    }

    public double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    @Override
    public String toString() {
        return " {\n" +
                "         hashCode=" + this.hashCode() + "\n" +
                "         initValue=" + initValue + "\n" +
                "         biasValue=" + biasValue + "\n" +
                "         outputValue=" + outputValue + "\n" +
                '}';
    }
}
