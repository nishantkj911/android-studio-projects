package com.example.helloworldapplication

import android.widget.DatePicker
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@Suppress("JAVA_CLASS_ON_COMPANION")
class CardInformationActivityTest {

    @get:Rule
    var mainActivityRule = ActivityTestRule(MainActivity::class.java)

    private fun openACard(i: Int) {
        onView(withId(R.id.recyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<MyRecyclerViewAdapter.ViewHolder>(
                i,
                click()
            )
        )
    }

    @Test
    fun openACard() {
        onView(withId(R.id.recyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<MyRecyclerViewAdapter.ViewHolder>(
                MainActivity.cards.size - 1,
                click()
            )
        )
    }

    @Test
    fun deleteCard() {
        openACard(MainActivity.cards.size - 1)
        onView(withId(R.id.deleteButton)).perform(scrollTo(), click())
    }

    private fun deleteCard(i: Int) {
        openACard(i)
        onView(withId(R.id.deleteButton)).perform(scrollTo(), click())
    }

    @Test
    fun deleteAllCards() {
        for (i in 0 until MainActivity.cards.size)
            deleteCard(0)
    }

    @Test
    fun editCard() {
        openACard(MainActivity.cards.size - 1)

        // Edit Card Name
        onView(withId(R.id.cardNameTextView)).perform(longClick())
        onView(withText("Edit")).perform(click())
        onView(withId(R.id.editText1)).perform(replaceText("New Card Name"))
        onView(withId(R.id.button)).perform(click())

        // Edit Card Number
        onView(withId(R.id.cardNumberTextView)).perform(longClick())
        onView(withText("Edit")).perform(click())
        onView(withId(R.id.editText1)).perform(replaceText("4561978531246421"))
        onView(withId(R.id.button)).perform(click())

        // Edit name
        onView(withId(R.id.nameOnCardTextView)).perform(longClick())
        onView(withText("Edit")).perform(click())
        onView(withId(R.id.editText1)).perform(replaceText("Nishant Kartikeya Joshaybhatla"))
        onView(withId(R.id.button)).perform(click())

        // Edit CVV
        onView(withId(R.id.cvvTextView)).perform(longClick())
        onView(withText("Edit")).perform(click())
        onView(withId(R.id.editText1)).perform(replaceText("987"))
        onView(withId(R.id.button)).perform(click())

        // Edit PIN
        onView(withId(R.id.pinTextView)).perform(longClick())
        onView(withText("Edit")).perform(click())
        onView(withId(R.id.editText1)).perform(replaceText("2147"))
        onView(withId(R.id.button)).perform(click())

        // Edit ValidThru
        onView(withId(R.id.validFromTextView)).perform(longClick())
        onView(withText("Edit")).perform(click())
        onView(withClassName(equalTo(DatePicker::class.qualifiedName))).perform(
            PickerActions.setDate(
                2013,
                8,
                12
            )
        )
        onView(withText("OK")).perform(click())

        // Edit ValidFrom
        onView(withId(R.id.validThruTextView)).perform(longClick())
        onView(withText("Edit")).perform(click())
        onView(withClassName(equalTo(DatePicker::class.qualifiedName))).perform(
            PickerActions.setDate(
                2013,
                8,
                12
            )
        )
        onView(withText("OK")).perform(click())

        // Edit Grid information
        // Delete a grid value
        onView(
            allOf(
                withParent(withParent(withId(R.id.gridTable))),
                withParent(withParentIndex(0)),
                withParent(withId(R.id.tableRow)),
                withId(R.id.deleteGridValueButton)
            )
        ).perform(click())

        // Edit a grid value
        onView(
            allOf(
                withParent(withParent(withId(R.id.gridTable))),
                withParent(withParentIndex(0)),
                withParent(withId(R.id.tableRow)),
                withId(R.id.editGridValueButton)
            )
        ).perform(click())
        onView(withId(R.id.gridValueEditTB)).perform(typeText("128"))
        onView(withId(R.id.editGridValueDialogButton)).perform(click())

        onView(withId(R.id.saveButton)).perform(scrollTo(), click())
    }

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }
}