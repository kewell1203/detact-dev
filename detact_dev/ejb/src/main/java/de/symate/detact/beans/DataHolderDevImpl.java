/*
 * Projekt: Modellgestützte Steuerung von Werkzeugmaschinen.
 *
 * TU Dresden, Institut für Werkzeugmaschinen und Steuerungstechnik
 */
package de.symate.detact.beans;

import de.symate.detact.analysis.data.Data;
import de.symate.detact.analysis.data.DataColumn;
import de.symate.detact.analysis.data.DataColumnType;
import de.symate.detact.analysis.data.DataHandler;
import de.symate.detact.beans.analysis.DataHolder;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasse, die Daten im Sinne eines Seam-Beams hält.
 * Hier im Gegensatz zur richtigen Anwendung im Session-Kontext (und nicht der Conversation).
 *
 * @author Gunnar Dietz
 * @version $Revision: $
 */
@Name("dataHolderDev")
@Scope(value = ScopeType.SESSION)
public class DataHolderDevImpl implements DataHolder {

    private Data content;

    public DataHolderDevImpl() {
        content = null;
    }

    public DataHolderDevImpl(Data content) {
        this.content = content;
    }

    @Override
    public Data getContent() {
        return content;
    }

    @Override
    public void setContent(Data content) {
        this.content = content;
    }

    @Override
    public void removeContent() {
        content = null;
    }

    @Override
    public List<DataColumn> getData() {
        if (content == null)
            throw new IllegalStateException("DataHolder contains no data.");
        return content.getData();
    }

    @Override
    public List<DataColumnType> getDependentTypes() {
        if (content == null)
            throw new IllegalStateException("DataHolder contains no data.");
        return content.getDependentTypes();
    }

    @Override
    public List<DataColumnType> getIndependentTypes() {
        if (content == null)
            throw new IllegalStateException("DataHolder contains no data.");
        return content.getIndependentTypes();
    }

    @Override
    public List<DataColumnType> getControllableTypes() {
        if (content == null)
            throw new IllegalStateException("DataHolder contains no data.");
        return content.getControllableTypes();
    }

    @Override
    public List<DataColumnType> getConditionTypes() {
        if (content == null)
            throw new IllegalStateException("DataHolder contains no data.");
        return content.getConditionTypes();
    }

    @Override
    public List<DataColumnType> getGroupingTypes() {
        if (content == null)
            throw new IllegalStateException("DataHolder contains no data.");
        return content.getGroupingTypes();
    }

    @Override
    public String getDescription() {
        if (content == null)
            return null;
        List<DataColumn> data = content.getData();
        if (data == null)
            return null;
        List<String> columnNames = new ArrayList<String>(data.size());
        for (DataColumn column : data)
            columnNames.add(column.getType().getUniqueName());
        int rows = new DataHandler(content).size();
        return String.format("%d rows, columns: %s", rows, columnNames.toString());
    }

    @Override
    public String toString() {
        String id = getClass().getSimpleName() + '@' + Integer.toHexString(hashCode());
        return String.format("%s (content: %s)", id, content);
    }

}

/*
 * $Log: $
 */
