package com.coc.sheet.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class SkillTest {

    @Test
    void totalValueIsAddValuePlusBase() {
        Skill skill = new Skill("Listen", 20);
        assertEquals(20, skill.getAddValue());
        assertEquals(25, skill.getTotalValue());
    }

    @Test
    void totalValueUpdatesWhenAddValueChanges() {
        Skill skill = new Skill("Search", 0);
        assertEquals(5, skill.getTotalValue());

        skill.setAddValue(30);
        assertEquals(35, skill.getTotalValue());
    }
}
