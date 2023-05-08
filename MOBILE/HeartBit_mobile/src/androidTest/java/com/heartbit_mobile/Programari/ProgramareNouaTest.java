package com.heartbit_mobile.Programari;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
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


public class ProgramareNouaTest {
    @Rule
    public TestRule scenario = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void waitFast() throws InterruptedException {
        Thread.sleep(1000);
        Espresso.onView(ViewMatchers.withId(R.id.navigation_calendar)).perform(click());

        Espresso.onView(withId(R.id.programareNoua)).perform(click());
    }

    @Test
    public void checkContent() {
        Espresso.onView(withId(R.id.prg_new_dialog_header))
                .check(matches(ViewMatchers.isDisplayed()))
                .check(matches(ViewMatchers.withText(R.string.prg_new)));

        Espresso.onView(withId(R.id.Specialitate))
                .check(matches(ViewMatchers.isDisplayed()))
                .check(matches(ViewMatchers.withHint(R.string.specialitate_progr)));

        Espresso.onView(withId(R.id.Data))
                .check(matches(ViewMatchers.isDisplayed()))
                .check(matches(ViewMatchers.withHint(R.string.data_prog)));


        Espresso.onView(withId(R.id.locatie_spinner))
                .check(matches(ViewMatchers.isDisplayed()));

        Espresso.onView(withId(R.id.Medic))
                .check(matches(ViewMatchers.isDisplayed()))
                .check(matches(ViewMatchers.withHint(R.string.medic_prg)));


        Espresso.onView(withId(R.id.sendCerereBtn))
                .check(matches(ViewMatchers.isDisplayed()))
                .check(matches(ViewMatchers.withText(R.string.send_reuquest)));

        Espresso.onView(withId(R.id.returnFromProgramareBtn))
                .check(matches(ViewMatchers.isDisplayed()))
                .check(matches(ViewMatchers.withText(R.string.cancel)));
    }

    @Test
    public void checkSendButton() {
        Espresso.onView(withId(R.id.sendCerereBtn))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .check(ViewAssertions.matches(ViewMatchers.isEnabled()))
                .check(ViewAssertions.matches(ViewMatchers.withText(R.string.send_reuquest)))
                .perform(ViewActions.click());
        //cod dupa trimiterea solicitarii
    }

    @Test
    public void checkCancelButton() {
        Espresso.onView(withId(R.id.returnFromProgramareBtn))
                .check(matches(ViewMatchers.isDisplayed()))
                .check(matches(withText(R.string.cancel)));

        Espresso.onView(withId(R.id.returnFromProgramareBtn))
                .perform(click());

        Espresso.onView(ViewMatchers.withId(R.id.navigation_calendar))
                .check(ViewAssertions.matches(ViewMatchers.isSelected()));
    }
    //cod spinner poate
}
