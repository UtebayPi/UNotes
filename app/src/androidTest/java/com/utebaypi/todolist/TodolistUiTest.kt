package com.utebaypi.todolist

import android.view.View
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@MediumTest
class TodolistUiTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    @After
    fun clearDB() {
        InstrumentationRegistry.getTargetContext().deleteDatabase("note_database")
    }
    @Test
    fun application_making_new_note_should_be_shown() {
        onView(withId(R.id.add_button)).perform(click())
        onView(withId(R.id.title_input)).perform(replaceText("How is it?"))
        onView(withId(R.id.content_input)).perform(replaceText("Is everything alright?"))
        onView(isRoot()).perform(waitFor(500))
        onView(withId(R.id.action_button)).perform(click())
        onView(withText("How is it?")).perform(click())
        onView(withId(R.id.title_text)).check(matches(withText("How is it?")))
        onView(withId(R.id.content_text)).check(matches(withText("Is everything alright?")))
    }
}

fun waitFor(delay: Long): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> = isRoot()
        override fun getDescription(): String = "wait for $delay milliseconds"
        override fun perform(uiController: UiController, v: View?) {
            uiController.loopMainThreadForAtLeast(delay)
        }
    }
}