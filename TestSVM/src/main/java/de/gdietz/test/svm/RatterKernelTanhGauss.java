package de.gdietz.test.svm;

import org.encog.mathutil.libsvm.svm_model;

/**
 * Created with IntelliJ IDEA.
 * User: Eduard
 * Date: 30.05.13
 * Time: 12:24
 * To change this template use File | Settings | File Templates.
 */
public class RatterKernelTanhGauss implements RatterKernel {
    public double kernel(RatterKernelParameter p, Value[] values1, Value[] values2){
        int n = values1.length;
        double gamma1 = p.getGamma1();
        double gamma2 = p.getGamma2();
        double gamma = p.getGamma();
        double d1 = values1[0].getValue();
        double d2 = values2[0].getValue();
        double[] omega1 = new double[n-1];
        double[] omega2 = new double[n-1];
        for(int i = 0; i < n-1; i++) {
            omega1[i] = values1[i+1].getValue();
            omega2[i] = values2[i+1].getValue();
        }
        return calculateKernel(gamma1, gamma2, gamma, omega1, d1, omega2, d2);
    }

    public double calculateKernel(double gamma1, double gamma2, double gamma, double[] omega1, double d1,
                                  double[] omega2,double d2) {
        double omegaSquareSum = 0d;
        for(int i = 0; i < omega1.length; i++)
            omegaSquareSum += (omega1[i] - omega2[i]) * (omega1[i] - omega2[i]);
        return gamma1 * Math.tanh(gamma2 * d1) * Math.tanh(gamma2 * d2) + Math.exp(-gamma * omegaSquareSum);
    }

    public double getDecision(RatterKernelParameter p, svm_model model, DataHandler data, double[] x){
        double gamma1 = p.getGamma1();
        double gamma2 = p.getGamma2();
        double gamma = p.getGamma();
        double squareOmega = 0d;
        double el = 0d;
        double de = 0d;
        SvmModelResult smr = new SvmModelResult(model, data);
        double bias = smr.getBias();
        double[] cs = smr.getCs();
        double[] ds = smr.getDs();
        double[][] omegas = smr.getOmegas();
        for(int i = 0; i < omegas.length; i++) {
            squareOmega = 0d;
            for(int j = 0; j < omegas[0].length; j++)
                squareOmega += (omegas[i][j] - x[j]) * (omegas[i][j] - x[j]);
            el += cs[i] * Math.exp(-gamma * squareOmega) ;
            de += cs[i] * gamma1 * Math.tanh(gamma2 * ds[i]);
        }
        return atanh((bias-el)/ de) / gamma2 ;
    }

    public static double atanh(double x){
        return 0.5 * Math.log ((1 + x)/ (1 - x));
    }
}
