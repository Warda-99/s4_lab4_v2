package test;

public class Character {
    private String name = "Warda";
    private String charakterClass = "Mage";
    private String race = "human";
    private Integer intelligence = 10;
    private int strength = 2;
    private int agility = 5;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCharakterClass(String charakterClass) {
        this.charakterClass = charakterClass;
    }

    public String getCharakterClass() {
        return charakterClass;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getRace() {
        return race;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getStrength() {
        return strength;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    public int getAgility() {
        return agility;
    }

    @Override
    public String toString() {
        return "Character:\n name = " + name + ",\n charakter class = " + charakterClass + ",\n race = " + race + ",\n intelligence = " + intelligence + ",\n strength = " + strength + ",\n agility = " + agility + ".";
    }
}
