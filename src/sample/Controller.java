package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;


import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    @FXML private TextField tf_className;
    @FXML private TextArea ta_output;
    @FXML private GridPane classField;

    private Class<?> c = null;
    private Object instance = null;
    private Constructor<?> constructor = null;
    private Method[] methods;
    private List<ObjectData> fieldsList;


    @FXML
    public void initialize() {
        ta_output.setEditable(false);
    }

    @FXML
    public void createObject(ActionEvent event) {
        try {
            clear();
            c = Class.forName(tf_className.getText());      // Pobranie obiektu podanej klasy
            constructor = c.getConstructor();               // Wyciagniecie konstruktora
            instance = constructor.newInstance();           // Tworzenie instancji klasy
            methods = c.getDeclaredMethods();               // Wczytanie metod klasy
            createFields();
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            printInfo("Nie udalo sie wczytac klasy. Sprobuj test.Character");
        }
    }

    @FXML
    public void saveChanges(ActionEvent event) {
        setValue();
        printInfo(instance.toString()); // Wypisanie zmienionych zmiennych
    }

    // Budowanie pola
    private void createFields() {

        fieldsList = new ArrayList<>();

        int currentRow = 0;     // Zmienna pomocnicza do odlicznia rzedow
        String value;           // Zmienna pomocniczna do odczytwania wartosci z getterow

        for (Field field : c.getDeclaredFields()) {

            currentRow++;

            // Dla pola z nazwa text, tworzy TextArea
            if(field.getName().toLowerCase().contains("text")) {
                value = invokeGetter(instance, field.getName());
                addField(new TextArea(value), currentRow, field.getName(), field.getType());
            }

            // Dla pol z typem boolena, torzy CheckBox
            else if (isFieldType(field, boolean.class, Boolean.class)) {
                addField(new CheckBox(), currentRow, field.getName(), field.getType());
            }

            // Dla pol z typem String, int itp. tworzy TextField
            else if (isFieldType(field, Integer.class, int.class, String.class, Double.class, float.class, double.class, long.class, char.class)) {
                value = invokeGetter(instance, field.getName());
                addField(new TextField(value), currentRow, field.getName(), field.getType());
            }

            else
                printInfo("Nieobslugiwany typ");
        }
    }

    // Dodawanie elementow do pola
    private void addField(Node fieldType, int row, String valueName, Class<?> valueType) {
        Label label = new Label(" <<< " + valueName);

        GridPane.setRowIndex(fieldType, row);
        GridPane.setColumnIndex(fieldType, 0);
        GridPane.setRowIndex(label, row);
        GridPane.setColumnIndex(label, 1);

        classField.getChildren().addAll(fieldType, label);
        fieldsList.add(new ObjectData(fieldType, valueName, valueType));    // Dodanie do listy informacji o zmiennej
    }

    // Sprawdza czy pole jest podanego typu
    private boolean isFieldType(Field field, Class<?>... types) {
        for (Class<?> type : types) {
            if (field.getType() == type)
                return true;
        }
        return false;
    }

    private void setValue() {

        for (ObjectData objectData : fieldsList) {
            Node fieldType = objectData.getFieldType(); // Wczytanie typu pola
            String name = objectData.getName();         // Wczytanie nazwy zmiennej

            try {
                // invokeSetter dla TextFeild
                if (fieldType instanceof TextField) {
                    String value = ((TextField) fieldType).getText();

                    for(Method method : methods) {
                        if(isSetter(method, name)) {
                            method.invoke(instance, parseValue(value, objectData.getValueClass()));
                        }
                    }
                }

                // invokeSetter dla TextArea
                else if (fieldType instanceof TextArea) {
                    String value = ((TextArea) fieldType).getText();

                    for(Method method : methods) {
                        if(isSetter(method, name)) {
                            method.invoke(instance, parseValue(value, objectData.getValueClass()));
                        }
                    }
                }

                // invokeSetter dla ChoiceBox
                else if (fieldType instanceof ChoiceBox) {
                    Boolean value = ((CheckBox) fieldType).isSelected();

                    for(Method method : methods) {
                        if(isSetter(method, name)) {
                            method.invoke(instance, value);
                        }
                    }
                }

            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

        }

    }

    private String invokeGetter(Object obj, String variableName) {

        Object f = null;

        try {
            PropertyDescriptor pd = new PropertyDescriptor(variableName, obj.getClass());
            Method getter = pd.getReadMethod();
            f = getter.invoke(obj);
        } catch (InvocationTargetException | IntrospectionException | IllegalAccessException e) {
            e.printStackTrace();
            System.out.println("Blad przy getterze");
        }

        return f.toString();
    }

    // Sprawdza czy dana metoda jest setterem
    private static boolean isSetter(Method method, String s){
        String virableName = method.getName().toLowerCase();

        if(method.getName().startsWith("set") && virableName.endsWith(s) && method.getParameterCount() == 1
                && method.getReturnType().equals(void.class)){
            return true;
        }
        return false;
    }

    private void printInfo(String s) {
        ta_output.setText(s);
        ta_output.setWrapText(true);
    }

    private void clear() {
        classField.getChildren().clear();
        printInfo("");
    }

    // Metoda do zmiany typu
    private Object parseValue(String value, Class<?> c) {
        if (c == byte.class || c == Byte.class) {
            return Byte.parseByte(value);
        } else if (c == short.class || c == Short.class) {
            return Short.parseShort(value);
        } else if (c == int.class || c == Integer.class) {
            return Integer.parseInt(value);
        } else if (c == long.class || c == Long.class) {
            return Long.parseLong(value);
        } else if (c == float.class || c == Float.class) {
            return Float.parseFloat(value);
        } else if (c == double.class || c == Double.class) {
            return Double.parseDouble(value);
        } else if (c == char.class || c == Character.class) {
            return value.charAt(0);
        }
        return value;   // String
    }

}
