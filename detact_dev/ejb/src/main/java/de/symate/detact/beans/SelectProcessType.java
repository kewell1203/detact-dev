package de.symate.detact.beans;

import java.io.Serializable;
import java.util.List;

import de.symate.detact.persistence.elements.types.Client;
import de.symate.detact.persistence.elements.types.ProcessType;
import de.symate.detact.persistence.queries.types.ProcessTypesQuery;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;

@Name(value = "selectProcessTypeBean")
@Scope(value = ScopeType.SESSION)
public class SelectProcessType implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1;

    @In(value = "#{currentClient}", scope = ScopeType.SESSION)
    private Client client;

    @In(value = "#{processTypesQuery}")
    private ProcessTypesQuery processTypesQuery;

    @Out(value = "processType", scope = ScopeType.SESSION, required = false)
    private ProcessType processType;

    /**
     * @return the project
     */
    public Client getClient() {
        return client;
    }

    private String screenSize;

    /**
     * @param client the client to set
     */
    public void setClient(Client client) {
        this.client = client;
    }

    public void setProcessType(ProcessType processType) {
        this.processType = processType;
    }

    /**
     * @return the processType
     */
    public ProcessType getProcessType() {
        return processType;
    }

    public List<?> getProcessTypes() {
        return processTypesQuery.getResultList();
    }

    public String getScreenSize() {
        return screenSize;
    }

    public void setScreenSize(String screenSize) {
        this.screenSize = screenSize;
    }

    public String selectProcessType(ProcessType pT) {
        this.processType = pT;
        return "success";
    }
}