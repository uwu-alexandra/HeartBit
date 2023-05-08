package com.heartbit_mobile.Support;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.heartbit_mobile.MainActivity;
import com.heartbit_mobile.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

public class DialogSolicitareTest {
    @Rule
    public TestRule scenario = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void waitFast() throws InterruptedException {
        Thread.sleep(1000);
        Espresso.onView(ViewMatchers.withId(R.id.navigation_support)).perform(click());

        Espresso.onView(withId(R.id.solicitareLayout)).perform(click());
    }

    @Test
    public void checkContent() {
        Espresso.onView(withId(R.id.solicitare_title)).check(matches(ViewMatchers.isDisplayed()));
        Espresso.onView(withId(R.id.closeDialogBtn)).check(matches(ViewMatchers.isDisplayed()));

        Espresso.onView(withText(R.string.motiv_prompt)).check(matches(ViewMatchers.isDisplayed()));

        Espresso.onView(withId(R.id.motiv_spinner)).check(matches(ViewMatchers.isDisplayed()));

        Espresso.onView(withId(R.id.Detalii)).check(matches(ViewMatchers.isDisplayed()));

        Espresso.onView(withId(R.id.sendSolicitareBtn)).check(matches(ViewMatchers.isDisplayed()));
        Espresso.onView(withId(R.id.returnFromSolicitareBtn)).check(matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void checkSendButton() {
        Espresso.onView(withId(R.id.sendSolicitareBtn))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .check(ViewAssertions.matches(ViewMatchers.isEnabled()))
                .check(ViewAssertions.matches(ViewMatchers.withText("Trimite solicitarea")))
                .perform(ViewActions.click());
        //cod dupa trimiterea solicitarii
    }

    @Test
    public void checkCancelButton() {
        Espresso.onView(withId(R.id.returnFromSolicitareBtn))
                .check(matches(ViewMatchers.isDisplayed()))
                .check(matches(withText(R.string.cancel)));

        Espresso.onView(withId(R.id.returnFromSolicitareBtn))
                .perform(click());

        Espresso.onView(ViewMatchers.withId(R.id.navigation_support))
                .check(ViewAssertions.matches(ViewMatchers.isSelected()));
    }
    //cod spinner poate
}
