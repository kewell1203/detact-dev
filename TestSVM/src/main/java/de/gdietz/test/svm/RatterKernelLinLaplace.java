package de.gdietz.test.svm;

/**
 * Created with IntelliJ IDEA.
 * User: Eduard
 * Date: 30.05.13
 * Time: 16:23
 * To change this template use File | Settings | File Templates.
 */
public class RatterKernelLinLaplace  {
    public double kernel(RatterKernelParameter p, Value[] values1, Value[] values2){
        int n = values1.length;
        double gamma1 = p.getGamma1();
        double gamma2 = p.getGamma2();
        double gamma = p.getGamma();
        double d1 = values1[0].getValue();
        double d2 = values2[0].getValue();
//        double[] omega1 = new double[n-1];
//        double[] omega2 = new double[n-1];
        double omega1 = values1[1].getValue();
        double omega2 = values2[1].getValue();
//        for(int i = 0; i < n-1; i++) {
//            omega1[i] = values1[i+1].getValue();
//            omega2[i] = values2[i+1].getValue();
//        }
        return calculateKernel(gamma1, gamma2, gamma, omega1, d1, omega2, d2);
    }

    public double calculateKernel(double gamma1, double gamma2, double gamma, double omega1, double d1,
                                  double omega2,double d2) {
        double omegaSquareSum = 0d;
//        for(int i = 0; i < omega1.length; i++)
//            omegaSquareSum += (omega1[i] - omega2[i]) * (omega1[i] - omega2[i]);
        return gamma1 * d1 * d2 + Math.exp(- gamma * Math.abs(omega1-omega2));
    }
}
