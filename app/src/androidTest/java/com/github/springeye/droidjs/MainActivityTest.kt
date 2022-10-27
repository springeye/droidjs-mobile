package com.github.springeye.droidjs

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class MainActivityTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)
    @Before
    fun init() {
        hiltRule.inject()
    }
    @Inject
    lateinit var jsRuntime: JSRuntime
    @Test
    fun testFSFile(){

    }
}