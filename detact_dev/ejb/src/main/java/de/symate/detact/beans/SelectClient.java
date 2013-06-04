package de.symate.detact.beans;

import java.io.Serializable;

import de.symate.detact.persistence.elements.auxiliaries.User;
import de.symate.detact.persistence.elements.types.Client;
import de.symate.detact.persistence.proxies.ProxyManager;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;

@Name(value = "selectClientBean")
@Scope(value = ScopeType.SESSION)
public class SelectClient implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1;

    @In(value = "#{currentUser}")
    private User currentUser;

    @Out(value = "currentClient", scope = ScopeType.SESSION, required = false)
    private Client client;

    /**
     * @return the project
     */
    public Client getClient() {
        return client;
    }

    /**
     * @param currentClient
     */
    public void setClient(Client currentClient) {
        this.client = currentClient;
    }

    /**
     * @return the currentUser
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * @param currentUser the currentUser to set
     */
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    @Begin(join = true)
    public String selectClient(Client newClient) {
        this.client = newClient;
        ProxyManager proxyManager = (ProxyManager) Component
                .getInstance("proxyManager");
        proxyManager.setProxyFilterEnabled(false);
        return "success";
    }

}
