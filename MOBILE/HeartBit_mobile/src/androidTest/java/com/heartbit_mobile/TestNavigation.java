package com.heartbit_mobile;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;


@RunWith(Parameterized.class)
public class TestNavigation {

    @Rule
    public TestRule scenario = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void waitFast() throws InterruptedException {
        Thread.sleep(1000);
    }

    @Parameterized.Parameter(0)
    public String testName;

    @Parameterized.Parameter(1)
    public int navigation_fragment;

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"Suport Fragment", R.id.navigation_support},
                {"Programari Fragment", R.id.navigation_calendar},
                {"Acasa Fragment", R.id.navigation_home},
                {"Dosar Fragment", R.id.navigation_dashboard},
                {"Setari Fragment", R.id.navigation_settings}
        });
    }

    @Test
    public void testNavigation() throws InterruptedException {
        navigateToFragment(navigation_fragment);
    }

    private static void navigateToFragment(int navigation_fragment) {
        Espresso.onView(withId(navigation_fragment)).perform(click());
        Espresso.onView(withId(navigation_fragment)).check(matches(isDisplayed()));
    }
}
