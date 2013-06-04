package de.gdietz.test.svm;

public class CostParameter implements SvmParameter {

    private double cost;

    public CostParameter(double cost) {
        this.cost = cost;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return "cost=" + cost;
    }

}
