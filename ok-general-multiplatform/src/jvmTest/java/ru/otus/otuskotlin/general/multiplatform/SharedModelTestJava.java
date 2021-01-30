package ru.otus.otuskotlin.general.multiplatform;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SharedModelTestJava {
    @Test
    public void sharedModelTest() {
        SharedModel sm = new SharedModel("id-1", "name-1");
        assertEquals("id-1", sm.getId());
    }

    @Test
    public void kotlinFieldTest() {
        assertEquals("my name", new SharedModel().getMyName());
    }

    @Test
    public void jvmOverloadsTest() {
        assertEquals(new SharedModel("one", ""), new SharedModel("one"));
    }
}
