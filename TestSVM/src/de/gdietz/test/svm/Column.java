package de.gdietz.test.svm;

import java.util.ArrayList;
import java.util.List;

class Column {

    private ColumnType type;

    private List<Double> values;

    public Column(ColumnType type) {
        this.type = type;
        values = new ArrayList<Double>();
    }

    public Column(String name) {
        this(new ColumnType(name));
    }

    public ColumnType getType() {
        return type;
    }

    public List<Double> getValues() {
        return values;
    }

    public void add(double... values) {
        for (double value : values)
            this.values.add(value);
    }

    public int size() {
        return values.size();
    }

    public double get(int index) {
        return values.get(index);
    }

    @Override
    public String toString() {
        return type + "=" + values;
    }

}

