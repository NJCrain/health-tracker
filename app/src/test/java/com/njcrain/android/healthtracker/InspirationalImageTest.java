package com.njcrain.android.healthtracker;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class InspirationalImageTest {
    InspirationalImage test;

    @Test
    public void testConstructor() {
        test = new InspirationalImage(R.drawable.image_1, "test caption");

        assertNotNull(test);
        assertEquals(R.drawable.image_1, test.getId());
        assertEquals("test caption", test.getCaption());
    }

    @Test
    public void testSetters() {
        test = new InspirationalImage(R.drawable.image_1, "test caption");
        test.setCaption("new caption");
        test.setId(0);

        assertEquals("new caption", test.getCaption());
        assertEquals(0, test.getId());
    }
}