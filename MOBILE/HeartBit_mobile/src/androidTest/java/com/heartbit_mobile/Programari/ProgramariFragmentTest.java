package com.heartbit_mobile.Programari;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.equalToIgnoringWhiteSpace;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.heartbit_mobile.MainActivity;
import com.heartbit_mobile.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;


@RunWith(Parameterized.class)
public class ProgramariFragmentTest {

    @Rule
    public TestRule scenario = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void waitFast() throws InterruptedException {
        Thread.sleep(1000);
        Espresso.onView(withId(R.id.navigation_calendar)).perform(click());
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
                {"Titlul paginii", R.id.header_title_progr, "Programările mele"},
                {"Tabel rand1", R.id.programareNoua, "Progr. nouă"},
                {"Tabel rand2", R.id.recomandari, "Recomandări"},
                {"Text ajutor", R.id.programari_progrNoua, "Programări"},
                {"Progr trecute", R.id.pastBtn, "Trecute"},
                {"Progr viitoare", R.id.futureBtn, "Viitoare"},
        });
    }

    @Test
    public void checkContentOf() throws InterruptedException {
        textIsDisplayedCorrectly(text_id, expectedText);
    }

    private static void textIsDisplayedCorrectly(int text_id, String expectedText) {
        Espresso.onView(withId(text_id)).check(matches(withText(equalToIgnoringWhiteSpace(expectedText))));
    }
}

