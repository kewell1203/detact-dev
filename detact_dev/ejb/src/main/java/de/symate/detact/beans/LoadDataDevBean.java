/*
 * Projekt: Modellgestützte Steuerung von Werkzeugmaschinen.
 *
 * TU Dresden, Institut für Werkzeugmaschinen und Steuerungstechnik
 */
package de.symate.detact.beans;

import de.symate.detact.analysis.util.ProgressStatus;
import de.symate.detact.analysis.util.ProgressStatusImpl;
import de.symate.detact.beans.analysis.DataHolder;
import de.symate.detact.beans.analysis.LoadDataProcess;
import de.symate.detact.persistence.elements.types.ProcessType;
import org.apache.log4j.Logger;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.*;

import java.io.Serializable;

/**
 * Bean für die Datenanalyse zum Laden der Daten
 *
 * @author Gunnar Dietz
 * @version $Revision: $
 */
@Name(value = "loadDataDevBean")
@Scope(value = ScopeType.SESSION)
public class LoadDataDevBean extends DetactPageBean implements Serializable {

    private static final long serialVersionUID = -2221416628133966624L;

    /** Der Prozesstyp, für den Daten geladen werden sollen */
    @In(value = "#{processType}", required=false)
    private ProcessType processType;

    @In
    private LoadDataProcess loadDataProcess;

    private final ProgressStatus status;

    @In(value = "#{dataHolderDev}", required = false)
    @Out(value = "#{dataHolderDev}", required = false)
    private DataHolder dataHolder;

    @SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(LoadDataDevBean.class);

    public LoadDataDevBean() {
        status = new ProgressStatusImpl();
    }

    protected DataHolder getDataHolder() {
        if (dataHolder == null)
            dataHolder = new DataHolderDevImpl();
        return dataHolder;
    }

    public void actionLoadData() {
        if (processType == null) {
            log.warn("No processType. Returning.");
            return;
        }
        log.debug("Loading data for processType " + processType.getId());
        status.start();
        getDataHolder().removeContent();
        loadDataProcess.loadDataAsync(processType, null, status, getDataHolder());
    }

    public boolean isRunning() {
        return status.isRunning();
    }

    public boolean isProcessing() {
        return status.isProcessing();
    }

    public int getPercentLoaded() {
        return status.getPercent();
    }

    public String actionAnalysis() {
        status.reset();
        return go("success");
    }

}

/*
 * $Log: $
 */
