package com.example.toxic

import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.internal.inject.InstrumentationContext
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CallTaxiActivityTest {

    private val intent : Intent = Intent(InstrumentationRegistry.getInstrumentation().targetContext, CallTaxiActivity::class.java)
        .putExtra("userInfo", UserData("test", "test", "test"))

    @get:Rule
    val activityRule = ActivityScenarioRule<CallTaxiActivity>(intent)


    @Test
    fun isButtonInvisible() {

        Espresso.onView(ViewMatchers.withId(R.id.call_taxi_btn)).check(matches(not(isDisplayed())))

    }
}