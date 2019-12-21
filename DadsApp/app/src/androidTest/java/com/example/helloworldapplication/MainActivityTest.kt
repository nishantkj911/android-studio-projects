package com.example.helloworldapplication

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@LargeTest
class MainActivityTest {

    @get:Rule
    var activityTestScenario = ActivityTestRule(MainActivity::class.java)

    @Test
    fun viewAllCards() {
        for (i in 0 until MainActivity.cards!!.size)
//            viewCard(i)
            login()
    }

    @Test
    fun viewCard() {
//        TODO("find a way to log in to the app and then test if all the cards are opening")

        Espresso.onView(withId(R.id.recyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<MyRecyclerViewAdapter.ViewHolder>(
                MainActivity.cards!!.size - 1,
                click()
            )
        )
    }

    private fun login() {

    }

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }
}