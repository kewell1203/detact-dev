package de.gdietz.test.svm;

public class GammaParameter implements KernelParameter {

    private double gamma;

    public GammaParameter(double gamma) {
        this.gamma = gamma;
    }

    public double getGamma() {
        return gamma;
    }

    @Override
    public String toString() {
        return "gamma=" + gamma;
    }

}
