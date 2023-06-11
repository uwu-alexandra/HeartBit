package com.heartbit_mobile.Settings;

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

public class SchimbareParolaTest {
    @Rule
    public TestRule scenario = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void waitFast() throws InterruptedException {
        Thread.sleep(1000);
        Espresso.onView(withId(R.id.navigation_settings)).perform(click());
        Espresso.onView(withId(R.id.schimbareParolaLayout)).perform(click());
    }

    @Test
    public void checkContent() {
        Espresso.onView(withId(R.id.schimbareParolaTitle)).check(matches(ViewMatchers.isDisplayed()));
        Espresso.onView(withId(R.id.closeDialogBtnPass)).check(matches(ViewMatchers.isDisplayed()));

        Espresso.onView(withId(R.id.parolaActuala)).check(matches(ViewMatchers.isDisplayed()));
        Espresso.onView(withId(R.id.parolaNoua)).check(matches(ViewMatchers.isDisplayed()));
        Espresso.onView(withId(R.id.comfirmareParolaNoua)).check(matches(ViewMatchers.isDisplayed()));
        Espresso.onView(withId(R.id.text_ajutator_parola)).check(matches(ViewMatchers.isDisplayed()));

    }

    @Test
    public void checkSendButton() {
        Espresso.onView(withId(R.id.salvareNouaParola))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .check(ViewAssertions.matches(ViewMatchers.isEnabled()))
                .check(ViewAssertions.matches(ViewMatchers.withText("Salvare noua parolă")))
                .perform(ViewActions.click());
        //cod dupa trimiterea solicitarii
    }

    @Test
    public void checkCancelButton() {
        Espresso.onView(withId(R.id.returnFromSchimbareParola))
                .check(matches(ViewMatchers.isDisplayed()))
                .check(matches(withText(R.string.cancel)));

        Espresso.onView(withId(R.id.returnFromSchimbareParola))
                .perform(click());

        Espresso.onView(ViewMatchers.withId(R.id.navigation_settings))
                .check(ViewAssertions.matches(ViewMatchers.isSelected()));
    }

    //test pentru chiar schimbare parola
}
