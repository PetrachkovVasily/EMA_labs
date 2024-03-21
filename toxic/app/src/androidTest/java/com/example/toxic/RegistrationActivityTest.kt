package com.example.toxic

import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class RegistrationActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(RegistrationActivity::class.java)

    private lateinit var decorView : View

    @Before
    public fun setUp(){
        activityRule.scenario.onActivity { ActivityScenario.ActivityAction<RegistrationActivity>()
        {
             fun perform(activity : RegistrationActivity) {
                decorView = activity.window.decorView;
            }
        }}
    }



    @Test
    fun correctRegistration() {
        Intents.init()

        onView(ViewMatchers.withId(R.id.phone)).perform(replaceText("+375333450292"))
        onView(ViewMatchers.withId(R.id.name)).perform(replaceText("Kirill"))
        onView(ViewMatchers.withId(R.id.last_name)).perform(replaceText("Meleshko"))

        onView(ViewMatchers.withId(R.id.reg_btn)).perform(click())

        intended(hasComponent(CallTaxiActivity::class.java.name))
        intended(hasExtraWithKey("userInfo"))

        Intents.release()
    }

    @Test
    fun incorrectPhoneRegistration() {

        onView(ViewMatchers.withId(R.id.phone)).perform(replaceText("asdas"))
        onView(ViewMatchers.withId(R.id.name)).perform(replaceText("Kirill"))
        onView(ViewMatchers.withId(R.id.last_name)).perform(replaceText("Meleshko"))

        onView(ViewMatchers.withId(R.id.reg_btn)).perform(click())


        onView(ViewMatchers.withId(R.id.reg_btn)).check(matches(isDisplayed()))
    }
}