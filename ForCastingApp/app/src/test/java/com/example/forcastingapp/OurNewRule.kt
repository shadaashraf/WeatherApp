package com.example.forcastingapp

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class OurNewRule(val dispature: TestCoroutineDispatcher = TestCoroutineDispatcher()): TestWatcher(), TestCoroutineScope by TestCoroutineScope(dispature) {

    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(dispature)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
        cleanupTestCoroutines()
    }
}
