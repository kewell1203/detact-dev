package de.gdietz.test.svm;

import de.symate.detact.analysis.data.Data;
import de.symate.detact.analysis.data.DataColumn;
import de.symate.detact.analysis.data.DataColumnImpl;
import de.symate.detact.analysis.data.DataColumnType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FakeData implements Data {

    private DataColumnType dependentType;
    private List<DataColumnType> dependentTypes;
    private List<DataColumnType> independentTypes;

    private DataColumn dependent;
    private List<DataColumn> independents;

    private List<DataColumn> data;

    private void create(DataColumnType dependentType, List<DataColumnType> independentTypes) {
        data = new ArrayList<DataColumn>();
        this.dependentType = dependentType;
        dependentTypes = new ArrayList<DataColumnType>(1);
        dependentTypes.add(dependentType);
        dependent = new DataColumnImpl(dependentType);
        data.add(dependent);

        this.independentTypes = independentTypes;
        independents = new ArrayList<DataColumn>();
        for (DataColumnType independentType : independentTypes) {
            DataColumn independent = new DataColumnImpl(independentType);
            independents.add(independent);
            data.add(independent);
        }
    }

    public FakeData(DataColumnType dependentType, List<DataColumnType> independentTypes) {
        create(dependentType, independentTypes);
    }

    public FakeData(DataColumnType dependentType, DataColumnType... independentTypes) {
        List<DataColumnType> independentTypeList  = new ArrayList<DataColumnType>();
        Collections.addAll(independentTypeList, independentTypes);
        create(dependentType, independentTypeList);
    }

    public FakeData(String dependentName, String... independentNames) {
        DataColumnType dependentType = new DataColumnTypeOfflineImpl(dependentName);
        List<DataColumnType> independentTypes = new ArrayList<DataColumnType>();
        for (String independentName : independentNames) {
            DataColumnType independentType = new DataColumnTypeOfflineImpl(independentName);
            independentTypes.add(independentType);
        }
        create(dependentType, independentTypes);
    }

    public void add(double dependentValue, double... independentValues) {
        if (independentValues.length != independentTypes.size())
            throw new IllegalArgumentException("Wrong number of values");

        dependent.add(dependentValue);
        for (int j = 0; j < independentValues.length; j++)
            independents.get(j).add(independentValues[j]);
    }

    @Override
    public List<DataColumn> getData() {
        return data;
    }

    @Override
    public List<DataColumnType> getDependentTypes() {
        return dependentTypes;
    }

    @Override
    public List<DataColumnType> getIndependentTypes() {
        return independentTypes;
    }

    @Override
    public List<DataColumnType> getControllableTypes() {
        return independentTypes;
    }

    private final List<DataColumnType> EMPTY_TYPE_LIST = new ArrayList<DataColumnType>();

    @Override
    public List<DataColumnType> getConditionTypes() {
        return EMPTY_TYPE_LIST;
    }

    @Override
    public List<DataColumnType> getGroupingTypes() {
        return EMPTY_TYPE_LIST;
    }

}
