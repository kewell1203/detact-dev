/*
 * Projekt: Modellgestützte Steuerung von Werkzeugmaschinen.
 *
 * TU Dresden, Institut für Werkzeugmaschinen und Steuerungstechnik
 */
package de.gdietz.test.svm;

/**
 * phi(sigma, d) = d.
 *
 * @author Gunnar Dietz
 * @version $Revision:$
 */
public class OneDimDPartLinearKernelFunction implements OneParamInvertibleFunction {

    @Override
    public double phi(double sigma, double d) {
        return d;
    }

    @Override
    public double phiInverse(double sigma, double phi) {
        return phi;
    }

}

/*
 * $Log:$
 */
