package de.gdietz.test.svm;

import de.symate.detact.analysis.svm.KernelParameter;

public class RatterKernelParameter implements KernelParameter {

    private double gamma1;
    private double gamma2;
    private double gamma;

    public RatterKernelParameter(double gamma1, double gamma2, double gamma) {
        this.gamma1 = gamma1;
        this.gamma2 = gamma2;
        this.gamma = gamma;
    }

    public double getGamma1() {
        return gamma1;
    }

    public double getGamma2() {
        return gamma2;
    }

    public double getGamma() {
        return gamma;
    }

    @Override
    public String toString() {
        return "gamma1=" + gamma1 + "; gamma2=" + gamma2 + "; gamma=" + gamma;
    }

}
