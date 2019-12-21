package com.example.helloworldapplication

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CardInformationActivityTest {

    @get:Rule
    var mainActivityRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setUp() {
    }

    private fun openACard(i: Int) {
        onView(withId(R.id.recyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<MyRecyclerViewAdapter.ViewHolder>(
                i,
                click()
            )
        )
    }

    @Test
    fun deleteCard() {
        openACard(MainActivity.cards!!.size - 1)
        onView(withId(R.id.deleteButton)).perform(scrollTo(), click())
    }

    @Test
    fun editCard() {
        openACard(MainActivity.cards!!.size - 1)

        // Edit Card Name
        onView(withId(R.id.cardNameTextView)).perform(longClick())
        onView(withText("Edit")).perform(click())
        onView(withId(R.id.editText1)).perform(replaceText("New Card Name 2"))
        onView(withId(R.id.button)).perform(click())

        // Edit Card Number
        onView(withId(R.id.cardNumberTextView)).perform(longClick())
        onView(withText("Edit")).perform(click())
        onView(withId(R.id.editText1)).perform(replaceText("9789984564"))
        onView(withId(R.id.button)).perform(click())

        // Edit name
        onView(withId(R.id.nameOnCardTextView)).perform(longClick())
        onView(withText("Edit")).perform(click())
        onView(withId(R.id.editText1)).perform(replaceText("Nishant New"))
        onView(withId(R.id.button)).perform(click())

        // Edit CVV
        onView(withId(R.id.cvvTextView)).perform(longClick())
        onView(withText("Edit")).perform(click())
        onView(withId(R.id.editText1)).perform(replaceText("987"))
        onView(withId(R.id.button)).perform(click())

        onView(withId(R.id.saveButton)).perform(scrollTo(), click())
    }

    /*@After
    fun tearDown() {
    }*/
}