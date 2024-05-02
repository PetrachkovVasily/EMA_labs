package com.example.checkbox_shop

import android.content.ComponentName
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.checkbox_shop.models.ItemData
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.isA
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CatalogueTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(CatalogueActivity::class.java)

    @Test
    fun testOneItemAddition() {

        onData(allOf(isA(ItemData::class.java)))
            .inAdapterView(withId(R.id.catalogue))
            .atPosition(0)
            .onChildView(withId(R.id.checkbox_add_to_cart))
            .perform(click())

        onData(isFooter())
            .inAdapterView(withId(R.id.catalogue))
            .onChildView(withId(R.id.cart_count))
            .check(matches(withText("1")))

    }


    @Test
    fun testAllItemAddition() {

        for(ind in 0 .. 24)
        {
            onData(allOf(isA(ItemData::class.java)))
                .inAdapterView(withId(R.id.catalogue))
                .atPosition(ind)
                .onChildView(withId(R.id.checkbox_add_to_cart))
                .perform(click())
        }


        onData(isFooter())
            .inAdapterView(withId(R.id.catalogue))
            .onChildView(withId(R.id.cart_count))
            .check(matches(withText("25")))

    }

    @Test
    fun testOneItemRemoval() {

        onData(allOf(isA(ItemData::class.java)))
            .inAdapterView(withId(R.id.catalogue))
            .atPosition(0)
            .onChildView(withId(R.id.checkbox_add_to_cart))
            .perform(click())


        onData(isFooter())
            .inAdapterView(withId(R.id.catalogue))
            .onChildView(withId(R.id.cart_count))
            .check(matches(withText("1")))


        onData(allOf(isA(ItemData::class.java)))
            .inAdapterView(withId(R.id.catalogue))
            .atPosition(0)
            .onChildView(withId(R.id.checkbox_add_to_cart))
            .perform(click())

        onData(isFooter())
            .inAdapterView(withId(R.id.catalogue))
            .onChildView(withId(R.id.cart_count))
            .check(matches(withText("0")))
    }

    @Test
    fun testAllItemRemoval() {

        for(ind in 0 .. 24)
        {
            onData(allOf(isA(ItemData::class.java)))
                .inAdapterView(withId(R.id.catalogue))
                .atPosition(ind)
                .onChildView(withId(R.id.checkbox_add_to_cart))
                .perform(click())
        }

        onData(isFooter())
            .inAdapterView(withId(R.id.catalogue))
            .onChildView(withId(R.id.cart_count))
            .check(matches(withText("25")))

        for(ind in 0 .. 24)
        {
            onData(allOf(isA(ItemData::class.java)))
                .inAdapterView(withId(R.id.catalogue))
                .atPosition(ind)
                .onChildView(withId(R.id.checkbox_add_to_cart))
                .perform(click())
        }

        onData(isFooter())
            .inAdapterView(withId(R.id.catalogue))
            .onChildView(withId(R.id.cart_count))
            .check(matches(withText("0")))
    }

    @Test
    fun testOneItemToCart() {

        onData(allOf(isA(ItemData::class.java)))
            .inAdapterView(withId(R.id.catalogue))
            .atPosition(0)
            .onChildView(withId(R.id.checkbox_add_to_cart))
            .perform(click())

        Intents.init()
        onData(isFooter())
            .inAdapterView(withId(R.id.catalogue))
            .onChildView(withId(R.id.to_cart))
            .perform(click())

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        intended(allOf(
            hasComponent(ComponentName(appContext, CartActivity::class.java)),
            hasExtraWithKey("items")
        ))
        Intents.release()

        onData(isHeader())
            .inAdapterView(withId(R.id.cart))
            .onChildView((withId(R.id.items_counter)))
            .check(matches(withText("1")))

    }

    @Test
    fun testAllItemsToCart() {

        for(ind in 0 .. 24)
        {
            onData(allOf(isA(ItemData::class.java)))
                .inAdapterView(withId(R.id.catalogue))
                .atPosition(ind)
                .onChildView(withId(R.id.checkbox_add_to_cart))
                .perform(click())
        }

        Intents.init()
        onData(isFooter())
            .inAdapterView(withId(R.id.catalogue))
            .onChildView(withId(R.id.to_cart))
            .perform(click())

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        intended(allOf(
            hasComponent(ComponentName(appContext, CartActivity::class.java)),
            hasExtraWithKey("items")
        ))
        Intents.release()

        onData(isHeader())
            .inAdapterView(withId(R.id.cart))
            .onChildView((withId(R.id.items_counter)))
            .check(matches(withText("25")))

    }



    private fun isFooter() : Matcher<String>
    {
        return allOf(isA(String::class.java), Matchers.`is`("Footer"))
    }
    private fun isHeader() : Matcher<String>
    {
        return allOf(isA(String::class.java), Matchers.`is`("Header"))
    }
}