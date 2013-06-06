/*
 * Projekt: Modellgestützte Steuerung von Werkzeugmaschinen.
 *
 * TU Dresden, Institut für Werkzeugmaschinen und Steuerungstechnik
 */
package de.gdietz.test.svm;

import de.symate.detact.analysis.data.DataColumnType;
import de.symate.detact.analysis.data.DataHandler;
import de.symate.detact.analysis.data.DataValue;
import de.symate.detact.analysis.svm.GammaParameter;
import de.symate.detact.analysis.svm.KernelFunction;
import de.symate.detact.analysis.svm.kernel.GammaSigmaParameter;
import de.symate.detact.analysis.svm.kernel.OneDimKernelFunction;
import de.symate.detact.analysis.svm.ratter.RatterKernelParameter;
import libsvm.svm_model;

/**
 * Kern-Funktion für Ratter-Karten.
 * Stellt eine Kern-Funktion dar, die aus zwei getrennten eindimensionalen Kernels besteht,
 * wobei der d-Anteil eine streng monoton steigende Funktion sein muss.
 *
 * @author Gunnar Dietz
 * @version $Revision: 1.1 $
 */
public class RatterKernelFunction implements KernelFunction<RatterKernelParameter>, RatterKernel {

    private DataColumnType dType;
    private DataColumnType omegaType;

    private OneDimDPartKernelFunction dPart;
    private OneDimKernelFunction<GammaParameter> omegaPart;

    public RatterKernelFunction(DataColumnType dType, DataColumnType omegaType,
                                OneDimDPartKernelFunction dPart,
                                OneDimKernelFunction<GammaParameter> omegaPart) {
        this.dType = dType;
        this.omegaType = omegaType;
        this.dPart = dPart;
        this.omegaPart = omegaPart;
    }

    public double kernel(RatterKernelParameter k, DataValue[] values1, DataValue[] values2) {
        double d1 = DataHandler.get(dType, values1).getDouble();
        double d2 = DataHandler.get(dType, values2).getDouble();
        double omega1 = DataHandler.get(omegaType, values1).getDouble();
        double omega2 = DataHandler.get(omegaType, values2).getDouble();

        return dPart.kernel(k.getkD(), d1, d2) + omegaPart.kernel(k.getkOmega(), omega1, omega2);
    }

    @Override
    public double getDecision(RatterKernelParameter p, svm_model model, DataHandler data, double[] omegas) {
        // TODO
        return 0;
    }

}

/*
 * $Log: RatterKernelFunction.java,v $
 * Revision 1.1  2012/01/12 18:02:22  dietz
 * Refactoring. Anwendung laeuft wieder, allerdings wird RConnector-Bean noch nicht geladen
 *
 * Revision 1.3  2011-11-03 10:56:23  dietz
 * Ueberarbeitung SVM-Klassen. Diese koennen nun sowohl innerhalb Catenas als auch in einer offline-Applikation benutzt werden.
 *
 * Revision 1.2  2011-10-10 14:06:04  dietz
 * Ueberarbeitung der Daten-Klassen fuer die Analyse
 *
 */
