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
        return RatterKernelTanhGauss.atanh(phi) / sigma;
    }

}

/*
 * $Log:$
 */
