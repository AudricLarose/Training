package entrainement.timer.p7_go4lunch.Bases;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import entrainement.timer.p7_go4lunch.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }



    @Test
    public  void isFaceBookHere(){
        onView(ViewMatchers.withId(R.id.cardF))
                .check(matches(isDisplayed()));
    }
    @Test
    public  void isGmailhere(){
        onView(ViewMatchers.withId(R.id.cardF))
                .check(matches(isDisplayed()));
    }
    @Test
    public void ElementClickGmail() {
        ViewInteraction cardView = onView(
                allOf(withId(R.id.cardG),
                        childAtPosition(
                                allOf(withId(R.id.drawer_layout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                3),
                        isDisplayed()));
        cardView.perform(click());
    }
    @Test
    public void ElementClickFacebook() {
        ViewInteraction cardView = onView(
                allOf(withId(R.id.cardF),
                        childAtPosition(
                                allOf(withId(R.id.drawer_layout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                3),
                        isDisplayed()));
        cardView.perform(click());
    }
//    @Test
//    public  void retour_arriere(){
//        onView(withId(R.id.ajout))
//                .perform(click());
//        onView(ViewMatchers.withId(R.id.imageView2))
//                .perform(click());
//        onView(ViewMatchers.withId(R.id.recycleViewId))
//                .check(matches(isDisplayed()));
//    }
//    @Test
//    public  void ajout_perso(){
//        onView(withId(R.id.ajout))
//                .perform(click());
//        onView(ViewMatchers.withId(R.id.ajout_reunion))
//                .check(matches(isDisplayed()));
//    }
//    @Test
//    public  void valider(){
//        onView(withId(R.id.ajout))
//                .perform(click());
//        onView(ViewMatchers.withId(R.id.valider))
//                .check(matches(isDisplayed()));
//    }
    @After
    public void tearDown() throws Exception {

    }
}
