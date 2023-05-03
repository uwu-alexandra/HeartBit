package com.heartbit_mobile;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;


@RunWith(Parameterized.class)
public class SupportFragmentTest {

    @Rule
    public TestRule scenario = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void waitFast() throws InterruptedException {
        Thread.sleep(1000);
        Espresso.onView(withId(R.id.navigation_support)).perform(click());
    }

    @Parameterized.Parameter()
    public String testName;

    @Parameterized.Parameter(1)
    public int text_id;

    @Parameterized.Parameter(2)
    public static String expectedText;

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"Titlul paginii", R.id.header_title, "Suport"},
                {"Tabel rand1", R.id.ghid_utilizare, "Ghid de utilizare"},
                {"Tabel rand2", R.id.solicitare, "Solicitare"},
                {"Text ajutor", R.id.contact, "Ai nevoie de un alt fel de ajutor? \\n\\n Sună la clinică! \\n 0255 200 200 \\n Luni–Vineri, orele: 09–17\\n\\n"},
        });
    }

    @Test
    public void testContentOf() throws InterruptedException {
        textIsDisplayedCorrectly(text_id, expectedText);
    }

    private static void textIsDisplayedCorrectly(int text_id, String expectedText) {
        Espresso.onView(withId(text_id)).check(matches(withText(expectedText)));
    }
}
