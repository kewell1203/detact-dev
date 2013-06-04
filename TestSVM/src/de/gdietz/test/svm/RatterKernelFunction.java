package de.gdietz.test.svm;

import java.lang.reflect.MalformedParameterizedTypeException;

public class RatterKernelFunction implements KernelFunction<RatterKernelParameter> {

    public static double kernelTanhGauss(double gamma1, double gamma2, double gamma,
            double omega1, double d1, double omega2, double d2) {
        double omegadiff = omega1 - omega2;
        return gamma1 * Math.tanh(gamma2 * d1) * Math.tanh(gamma2 * d2) +
                Math.exp(-gamma * omegadiff * omegadiff);
    }

    public static double kernelLinGauss(double gamma1, double gamma2, double gamma,
            double omega1, double d1, double omega2, double d2) {
        double omegadiff = omega1 - omega2;
        return gamma1 * d1 * d2 + Math.exp(-gamma * omegadiff * omegadiff);
    }

    public static double kernelLinPoly(double gamma1, double gamma2, double gamma,
            double omega1, double d1, double omega2, double d2) {
        return gamma1 * d1 * d2 + gamma*Math.pow(omega1 * omega2, gamma2);
    }

    public static double kernelTanhLaplace(double gamma1, double gamma2, double gamma,
            double omega1, double d1, double omega2, double d2){
        return gamma1 * Math.tanh(gamma2*d1)*Math.tanh(gamma2*d2) + Math.exp(-gamma * Math.abs(omega1-omega2));
    }

    public static double kernelLinLaplace(double gamma1, double gamma2, double gamma,
            double omega1, double d1, double omega2, double d2){
        return gamma1 * d1 * d2 + Math.exp(-gamma * Math.abs(omega1-omega2));
    }

    public static double kernelTanhRQ (double gamma1, double gamma2, double gamma,
            double omega1, double d1, double omega2, double d2){
        double omegadiffsquare = Math.pow(omega1 -omega2,2);
        return  gamma1 * Math.tanh(gamma2*d1)*Math.tanh(gamma2*d2) +
                1 - omegadiffsquare / (omegadiffsquare + gamma);
    }

     public static double kernelTanhWave (double gamma1, double gamma2, double gamma,
            double omega1, double d1, double omega2, double d2){
         double absdiffomega = Math.abs(omega1-omega2);
         return gamma1 * Math.tanh(gamma2*d1)*Math.tanh(gamma2*d2)
                 + gamma/absdiffomega*Math.sin(absdiffomega/gamma);

     }

    public static double kernelTanhCauchy (double gamma1, double gamma2, double gamma,
            double omega1, double d1, double omega2, double d2){
        return gamma1 * Math.tanh(gamma2*d1)*Math.tanh(gamma2*d2)
                + 1/ (1 + Math.pow((omega1-omega2),2)/gamma);
    }

    public static double kernelTanhSpline  (double gamma1, double gamma2, double gamma,
            double omega1, double d1, double omega2, double d2){
        double prodome = omega1 * omega2;
        double minome = Math.min(omega1,omega2);
        double midome = 0.5 * (omega1 + omega2);
        return    gamma1 * Math.tanh(gamma2*d1)*Math.tanh(gamma2*d2)
                + gamma + prodome + prodome* minome - midome * Math.pow(minome,2)
                + Math.pow(minome,3)/3  ;

    }

    public double kernel(RatterKernelParameter k, Value[] values1, Value[] values2) {
//        double omega1 = values1[0].getValue();
//        double omega2 = values2[0].getValue();
//        double d1 = values1[1].getValue();
//        double d2 = values2[1].getValue();

        double omega1 = values1[1].getValue();
        double omega2 = values2[1].getValue();
        double d1 = values1[0].getValue();
        double d2 = values2[0].getValue();        double gamma1 = k.getGamma1();
        double gamma2 = k.getGamma2();
        double gamma = k.getGamma();

        //return kernelTanhSpline(gamma1, gamma2, gamma, omega1, d1, omega2, d2);
        return kernelLinGauss(gamma1, gamma2, gamma, omega1, d1, omega2, d2);
    }

}
