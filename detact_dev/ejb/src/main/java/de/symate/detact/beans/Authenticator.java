/*
 * Projekt: Modellgestützte Steuerung von Werkzeugmaschinen. 
 * 
 * TU Dresden, Institut für Werkzeugmaschinen und Steuerungstechnik
 */
package de.symate.detact.beans;

import de.symate.detact.persistence.elements.auxiliaries.User;
import de.symate.detact.persistence.queries.auxiliaries.UsersQuery;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;

/**
 * Authentifiziert den Benutzer während der Anmeldung an der WebApp.
 * 
 * @author Jens Weller
 */
@Name("authenticator")
public class Authenticator {

    @Logger
    private Log log;

    @In
    @Out(scope = ScopeType.SESSION)
    private Identity identity;

    @In(required = false)
    @Out(required = false, scope = ScopeType.SESSION)
    private User currentUser;

    @In(value = "#{usersQuery}")
    private UsersQuery usersQuery;

    /**
     * Überprüft die Benutzerdaten. Wird vom Seam-Framework aufgerufen.
     * 
     * @return IO oder nicht?
     */
    public boolean authenticate() {
        try {
            // Den ersten Benutzer suchen
            currentUser = usersQuery.getSingleResult();
            log.info(currentUser.getLoginName());
            identity.addRole("admin");
            identity.addRole("processTypeAdmin");
            return true;
        } catch (Exception e) {
            log.fatal("Login failed", e);
        }

        // Zugang untersagen, wenn kein Nutzer existiert
        return false;
    }
}
