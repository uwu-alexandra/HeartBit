package com.heartbit_mobile;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

public class DialogGhidDeUtilizareTest {
    @Rule
    public TestRule scenario = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void waitFast() throws InterruptedException {
        Thread.sleep(1000);
        Espresso.onView(withId(R.id.navigation_support)).perform(click());

        Espresso.onView(withId(R.id.ghidLayout)).perform(click());
    }

    @Test
    public void checkContent() throws InterruptedException {
        Espresso.onView(ViewMatchers.withId(R.id.ghid_utilizare_title))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        Espresso.onView(withId(R.id.ghid_utilizare_title))
                .check(matches(withText("Ghid de utilizare")));

        Matcher<String> paragraphMatcher = allOf(
                containsString("1. Autentificare utilizatori"),
                containsString("2. Colectarea datelor de la pacient"),
                containsString("3. Procesarea datelor"));

        Espresso.onView(withId(R.id.ghid_utilizare_content))
                .check(matches(withText(paragraphMatcher)));
    }

    @Test()
    public void checkBackButton() {
        Espresso.onView(ViewMatchers.withId(R.id.scrollView)).perform(ViewActions.swipeUp());

        Espresso.onView(ViewMatchers.withId(R.id.returnFromGhidBtn))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        Espresso.onView(ViewMatchers.withId(R.id.returnFromGhidBtn))
                .check(matches(withText("Înapoi")));

        Espresso.onView(ViewMatchers.withId(R.id.returnFromGhidBtn)).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.navigation_support))
                .check(ViewAssertions.matches(ViewMatchers.isSelected()));
    }
}
