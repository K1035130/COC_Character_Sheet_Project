package model;

// this class is for the skill a character have
public class Skill {

    private static final int BASE = 5; // The default value of skill

    private String name; // The name value of skill, essential
    private int totalValue; // The skill value, should lower/equal the MAX, larger/equal than BASE.
    private int addValue; // The value player given

    public Skill(String name) {
        this.name = name;
        this.totalValue = BASE;
        this.addValue = 0;
    }

    // REQUIRE: a String and an intage larger than 0
    // EFFECT: creat a new skill
    public Skill(String name, int addValue) {
        this.name = name;
        this.totalValue = addValue + BASE;
        this.addValue = addValue;
    }

    // EFFECT: return the name of skill
    public String getName() {
        return name;
    }

    // EFFECT: return the added value of the skill
    public int getAddValue() {
        return addValue;
    }

    // EFFECT: return the total value of the skill
    public int getTotalValue() {
        return totalValue;
    }

    // MODIFY: this
    // EFFECT: update the name of a skill
    public void setName(String name) {
        this.name = name;
    }

    // MODIFY: this
    // EFFECT: update the addvalue
    public void setAddValue(int addValue) {
        this.addValue = addValue;
        this.totalValue = this.addValue + BASE;
    }

    // EFFECT: To ensure the GUI displays correctly, the toString function requires modification.
    @Override
    public String toString() {
        return name + " : " + getTotalValue();
    }

}
