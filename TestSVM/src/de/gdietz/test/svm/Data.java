package de.gdietz.test.svm;

import java.util.List;

public interface Data {

    public List<Column> getData();

    public ColumnType getResultType();
    public List<ColumnType> getIndependentTypes();

}
