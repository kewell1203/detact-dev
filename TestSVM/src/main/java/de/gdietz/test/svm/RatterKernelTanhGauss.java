package de.gdietz.test.svm;

import de.symate.detact.analysis.data.DataHandler;
import de.symate.detact.analysis.data.DataValue;
import de.symate.detact.analysis.svm.ratter.RatterKernelParameter;
import libsvm.svm_model;

/**
 * User: Eduard
 */
public class RatterKernelTanhGauss implements RatterKernel {
    public double kernel(RatterKernelParameter p, DataValue[] values1, DataValue[] values2){
        int n = values1.length;
        double gamma1 = p.getkD().getGamma();
        double gamma2 = p.getkD().getSigma();
        double gamma = p.getkOmega().getGamma();
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
        double omegaSquareSum = 0d;
        for(int i = 0; i < omega1.length; i++)
            omegaSquareSum += (omega1[i] - omega2[i]) * (omega1[i] - omega2[i]);
        return gamma1 * Math.tanh(gamma2 * d1) * Math.tanh(gamma2 * d2) + Math.exp(-gamma * omegaSquareSum);
    }

    public double getDecision(RatterKernelParameter p, svm_model model, DataHandler data, double[] x){
        double gamma1 = p.getkD().getGamma();
        double gamma2 = p.getkD().getSigma();
        double gamma = p.getkOmega().getGamma();
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
