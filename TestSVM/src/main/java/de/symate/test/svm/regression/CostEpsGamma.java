package de.symate.test.svm.regression;

/**
 * Created with IntelliJ IDEA.
 * User: Eduard
 * Date: 18.06.13
 * Time: 17:39
 * To change this template use File | Settings | File Templates.
 */
public class CostEpsGamma {
    private double cost;
    private double eps;
    private double gamma;

    public CostEpsGamma(double cost, double eps, double gamma) {
        this.cost = cost;
        this.eps = eps;
        this.gamma = gamma;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public  double getCost() {
        return cost;
    }

    public void setEps(double eps) {
        this.eps = eps;
    }

    public double getEps() {
        return eps;
    }

    public void setGamma(double gamma) {
        this.gamma = gamma;
    }

    public double getGamma() {
        return gamma;
    }
}
