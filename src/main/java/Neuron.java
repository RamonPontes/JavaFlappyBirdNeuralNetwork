import java.util.Objects;

public class Neuron {
    private double id;
    private double initValue;
    private double biasValue;
    private double outputValue;

    public Neuron(double initValue, int id) {
        this.initValue = initValue;
        this.id = id;
    }

    public Neuron(int id) {
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

    public double getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Neuron neuron = (Neuron) o;
        return Double.compare(id, neuron.id) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
