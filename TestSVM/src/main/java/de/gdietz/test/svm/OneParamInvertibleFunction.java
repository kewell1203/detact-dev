/*
 * Projekt: Modellgestützte Steuerung von Werkzeugmaschinen.
 *
 * TU Dresden, Institut für Werkzeugmaschinen und Steuerungstechnik
 */
package de.gdietz.test.svm;

/**
 * Ein-parametrige umkehrbare Funktion für den "Bau" von <code>OneDimDPartKernelFunction</code>s
 *
 * @author Gunnar Dietz
 * @version $Revision:$
 */
public interface OneParamInvertibleFunction {

    /**
     * Berechnet die Funktion.
     *
     * @param sigma der Parameter
     * @param d der d-Wert für den Kernel
     * @return der Wert der Funktion
     */
    public double phi(double sigma, double d);

    /**
     * Berechnet die Umkehrfunktion.
     *
     * @param sigma der Parameter
     * @param phi der Wert von phi
     * @return der Wert von d
     */
    public double phiInverse(double sigma, double phi);

}

/*
 * $Log:$
 */
