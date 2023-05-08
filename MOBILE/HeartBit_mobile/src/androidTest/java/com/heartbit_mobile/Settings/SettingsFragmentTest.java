package com.heartbit_mobile.Settings;

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
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class SettingsFragmentTest {

    @Rule
    public TestRule scenario = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void waitFast() throws InterruptedException {
        Thread.sleep(1000);
        Espresso.onView(withId(R.id.navigation_settings)).perform(click());
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
                {"Titlul paginii", R.id.header_title_settings, "Setări aplicaţie"},
                {"Subtitlul paginii", R.id.setari_subtitle, "Setări"},
                {"Tabel rand1", R.id.schimbareParola, "Schimbare Parolă"},
                {"Tabel rand2", R.id.deconectareCont, "Deconectare Cont"},
                {"Tabel rand3", R.id.stergereCont, "Ştergere cont"},
                {"Subtitlul paginii 2", R.id.setari_subtitle2, "Setări permisiuni"},
                {"Tabel rand4", R.id.permisiuneBluetooth, "Permisiune BT"},
                {"Tabel rand5", R.id.cadruLegal, "Cadru legal"},

        });
    }

    @Test
    public void testContentOf() throws InterruptedException {
        textIsDisplayedCorrectly(text_id, expectedText);
    }

    private static void textIsDisplayedCorrectly(int text_id, String expectedText) {
        Espresso.onView(withId(text_id)).check(matches(withText(equalToIgnoringWhiteSpace(expectedText))));
    }
}