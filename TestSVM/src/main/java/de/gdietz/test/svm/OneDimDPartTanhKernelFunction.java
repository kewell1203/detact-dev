/*
 * Projekt: Modellgestützte Steuerung von Werkzeugmaschinen.
 *
 * TU Dresden, Institut für Werkzeugmaschinen und Steuerungstechnik
 */
package de.gdietz.test.svm;

/**
 * phi(sigma, d) = tanh(sigma * d).
 *
 * @author Gunnar Dietz
 * @version $Revision:$
 */
public class OneDimDPartTanhKernelFunction implements OneParamInvertibleFunction {

    @Override
    public double phi(double sigma, double d) {
        return Math.tanh(sigma * d);
    }

    @Override
    public double phiInverse(double sigma, double phi) {
        return atanh(phi) / sigma;
    }

    public static double atanh(double x){
        return 0.5 * Math.log ((1 + x)/ (1 - x));
    }

}

/*
 * $Log:$
 */
