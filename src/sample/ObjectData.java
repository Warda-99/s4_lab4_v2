package sample;

import javafx.scene.Node;


public class ObjectData {

    // Klasa przechowuje informacje o zmiennych z klas poddanych refleksji

    private Node fieldType;         // Typ pola (textField itp)
    private String name;            // Nazwa zmiennej
    private Class<?> valueClass;    // Klasa typu zmiennej

    public ObjectData(Node fieldType, String name, Class<?> valueClass) {
        this.fieldType = fieldType;
        this.name = name;
        this.valueClass = valueClass;
    }

    public Node getFieldType() {
        return fieldType;
    }

    public String getName() {
        return name;
    }

    public Class<?> getValueClass() {
        return valueClass;
    }
}
