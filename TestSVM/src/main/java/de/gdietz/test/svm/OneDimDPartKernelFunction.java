/*
 * Projekt: Modellgestützte Steuerung von Werkzeugmaschinen.
 *
 * TU Dresden, Institut für Werkzeugmaschinen und Steuerungstechnik
 */
package de.gdietz.test.svm;

import de.symate.detact.analysis.svm.kernel.GammaSigmaParameter;
import de.symate.detact.analysis.svm.kernel.OneDimKernelFunction;

/**
 * Interface für den d-Anteil der Ratter-Kernel-Funktionen
 *
 * @author Gunnar Dietz
 * @version $Revision:$
 */
public class OneDimDPartKernelFunction implements OneDimKernelFunction<GammaSigmaParameter> {

    private OneParamInvertibleFunction phi;

    public OneDimDPartKernelFunction(OneParamInvertibleFunction phi) {
        this.phi = phi;
    }

    @Override
    public double kernel(GammaSigmaParameter k, double x1, double x2) {
        return k.getGamma() * phi.phi(k.getSigma(), x1) * phi.phi(k.getSigma(), x2);
    }

    public OneParamInvertibleFunction getPhi() {
        return phi;
    }

}

/*
 * $Log:$
 */
