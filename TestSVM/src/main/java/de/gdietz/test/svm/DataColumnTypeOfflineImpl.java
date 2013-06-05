package de.gdietz.test.svm;

import de.symate.detact.analysis.data.DataColumnType;
import de.symate.detact.analysis.data.DataColumnTypeImpl;

public class DataColumnTypeOfflineImpl extends DataColumnTypeImpl {

    private static final String OBJECT_NAME = "offline";

    public DataColumnTypeOfflineImpl(DataColumnType.DataType type, String name, String unitName) {
        super(type, RelationType.UNKNOWN, OBJECT_NAME, name, name, unitName);
    }

    public DataColumnTypeOfflineImpl(DataColumnType.DataType type, String name) {
        this(type, name, null);
    }

    public DataColumnTypeOfflineImpl(String name) {
        this(DataType.DOUBLE, name);
    }

}
