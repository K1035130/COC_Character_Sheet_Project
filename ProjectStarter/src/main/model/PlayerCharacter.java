package model;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import persistence.Writable;

// The class is for character and it's name, occupation, age, skills, attributes
public class PlayerCharacter implements Writable {

    private String name;
    private String occupation;
    private int age;
    private String gender;
    private ArrayList<Skill> skills;
    private Map<Attribute, Integer> attributes;

    // EFFECT：build a new character, based on given name, occupation, age, gender
    public PlayerCharacter(String name, String occupation, int age, String gender) {
        this.name = name;
        this.occupation = occupation;
        this.age = age;
        this.gender = gender;
        this.attributes = new EnumMap<>(Attribute.class);
        skills = new ArrayList<>();

        for (Attribute attr : Attribute.values()) {
            attributes.put(attr, 0);
        }
    }

    // EFFECT: give character's gender
    public String getGender() {
        return this.gender;
    }

    // EFFECT: give character's name
    public String getName() {
        return this.name;
    }

    // EFFECT: give character's occupation
    public String getOccupation() {
        return this.occupation;
    }

    // EFFECT: give character's age
    public int getAge() {
        return this.age;
    }

    // REQUIRE: given key must be one of attribute.
    // EFFECT: give character's attribute value based on given key
    public int getAttribute(String key) {
        Attribute attr = Attribute.valueOf(key);
        return attributes.getOrDefault(attr, 0);
    }

    // EFFECT: give all attributes value of the character
    public Map<Attribute, Integer> getAllAttributes() {
        return attributes;
    }

    // EFFECT: give character's skill list
    public ArrayList<Skill> getSkills() {
        return skills;
    }

    // MODIFY: this
    // EFFECT: change character's name to the given name
    public void setName(String name) {
        EventLog.getInstance().logEvent(new Event("Change character name: "
                + this.name + "->" + name));
        this.name = name;
    }

    // REQUIRE：given gender must be one of Male, Female or Other
    // MODIFY: this
    // EFFECT: change character's gender to the given name
    public void setGender(String g) {
        EventLog.getInstance().logEvent(new Event("Change character " + getName() + " gender: " 
                + this.gender + "->" + g.toLowerCase()));
        this.gender = g.toLowerCase();
    }

    // MODIFY: this
    // EFFECT: change character's occupation to the given occupation
    public void setOccupation(String occ) {
        EventLog.getInstance().logEvent(new Event("Change character " + getName() + " occupation: "
                + this.occupation + "->" + occ));
        this.occupation = occ;
    }

    // REQUIRE: given age should >= 0
    // MODIFY: this
    // EFFECT: change character's age to the given age
    public void setAge(int age) {
        EventLog.getInstance().logEvent(new Event("Change character " + getName() + " age: "
                + this.age + "->" + age));
        this.age = age;
    }

    // REQUIRE: given key must be one of attribute
    // MODIFY: this
    // EFFECT: change character's attribute's value based on given key and value,
    // the max value
    // The derived attributes(HP,MP,SAN) can not set directly, actions for them are
    // useless.
    public void setAttribute(String key, int value) {
        int origV = getAttribute(key);
        Attribute attr = Attribute.valueOf(key);
        this.attributes.put(attr, value);
        calcDerivedAttr();
        EventLog.getInstance().logEvent(new Event("Change character " + getName() + " Attribute " + key + ": "
                + origV + "->" + getAttribute(key)));
    }

    // MODIFY: this
    // EFFECT: cauculate derived attribute based on the normal attribute，
    // the fractional part will be eliminated, the SAN will not higher than 99
    public void calcDerivedAttr() {
        int con = getAttribute("CON");
        int siz = getAttribute("SIZ");
        int pow = getAttribute("POW");

        attributes.put(Attribute.HP, (int) ((con + siz) / 10));
        attributes.put(Attribute.MP, (int) (pow / 5));
        attributes.put(Attribute.SAN, (int) Math.min(99, (pow * 5)));
    }

    // REQUIRE: the addValue >= 0
    // MODIFY: this
    // EFFECT: add new skill in skill list with the given addvalue, if already
    // exists, update.
    public void addSkill(String sn, int addValue) {
        if (!checkHaveSkill(sn)) {
            Skill skill = new Skill(sn, addValue);
            skills.add(skill);
        } else {
            for (Skill s : this.skills) {
                if (sn.equalsIgnoreCase(s.getName())) {
                    s.setAddValue(addValue);
                }
            }
        }
        EventLog.getInstance().logEvent(new Event("Update character " + getName() + " skill: " + sn));
    }

    // MODIFY: this
    // EFFECT: delete skill in skill list with the given skill's number
    public void deleteSkill(String skillname) {
        if (checkHaveSkill(skillname)) {
            Skill s = findSkill(skillname);
            this.skills.remove(s);
        }
        EventLog.getInstance().logEvent(new Event("Remove character skill: " + skillname));
    }

    // REQUIRE: given skill name must be one of skill in skills
    // EFFECT: delete skill in skill list with the given skill's number
    public Skill findSkill(String sn) {
        for (Skill s : this.skills) {
            if (s.getName().equalsIgnoreCase(sn)) {
                return s;
            }
        }
        return null;
    }

    // EFFECT: Produce true if the given skill name is exist in skills, otherwise
    // false
    public Boolean checkHaveSkill(String skillName) {
        for (Skill s : this.skills) {
            if (skillName.equalsIgnoreCase(s.getName())) {
                return true;
            }
        }
        return false;
    }

    // EFFECT: change current data into json
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", getName());
        json.put("occupation", getOccupation());
        json.put("age", getAge());
        json.put("gender", getGender());

        // attributes
        JSONObject attrObj = new JSONObject();
        for (Attribute attr : Attribute.values()) {
            attrObj.put(attr.name(), getAttribute(attr.name()));
        }
        json.put("attributes", attrObj);

        // skills
        JSONArray skillArray = new JSONArray();
        for (Skill s : getSkills()) {
            JSONObject skillObj = new JSONObject();
            skillObj.put("name", s.getName());
            skillObj.put("value", s.getAddValue());
            skillArray.put(skillObj);
        }
        json.put("skills", skillArray);

        return json;
    }

}
