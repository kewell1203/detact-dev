package de.gdietz.test.svm;

import de.symate.detact.analysis.data.DataValue;

/**
 * User: Eduard
 */
public class RatterKernelLinPoly {
    public double kernel(RatterKernelParameter p, DataValue[] values1, DataValue[] values2){
        int n = values1.length;
        double gamma1 = p.getGamma1();
        double gamma2 = p.getGamma2();
        double gamma = p.getGamma();
        double d1 = values1[0].getDouble();
        double d2 = values2[0].getDouble();
        double[] omega1 = new double[n-1];
        double[] omega2 = new double[n-1];
        for(int i = 0; i < n-1; i++) {
            omega1[i] = values1[i+1].getDouble();
            omega2[i] = values2[i+1].getDouble();
        }
        return calculateKernel(gamma1, gamma2, gamma, omega1, d1, omega2, d2);
    }

    public double calculateKernel(double gamma1, double gamma2, double gamma, double[] omega1, double d1,
                                  double[] omega2,double d2) {
        double omegaDotProd = 0d;
        for(int i = 0; i < omega1.length; i++)
            omegaDotProd += omega1[i] * omega2[i];
        return gamma1 * d1 * d2 + gamma * Math.pow(omegaDotProd, gamma2);
    }
}
