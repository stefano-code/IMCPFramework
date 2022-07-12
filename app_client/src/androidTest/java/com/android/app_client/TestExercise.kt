package com.android.app_client

import android.util.Log
import androidx.test.core.app.ApplicationProvider
import com.android.imcp.api.Observer
import com.android.imcplib.Exercise
import com.google.common.truth.Truth
import kotlinx.coroutines.*
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test

class TestExercise {
    companion object
    {
        const val TAG = "TestExercise"
        lateinit var exercise: Exercise // Exercise is only a proxy to a custom content provider create in another app

        @JvmStatic
        @BeforeClass
        fun setup() {  exercise = Exercise(ApplicationProvider.getApplicationContext())  }

        @JvmStatic
        @AfterClass
        fun tearDown() {
            Thread.sleep(5000)
            exercise.tearDown()
            Log.e(TAG, "End Test")
        }
    }

    @Test
    fun testObserver(){
        Log.e(TAG,"Start")
        runBlocking {
            exercise.notifyOnChange("Duration", object : Observer {
                override fun update() {
                    Log.e(TAG, "${exercise.getInt("Duration")}")
                }
            })
            CoroutineScope(Dispatchers.IO).launch {
                delay(1000)
                exercise.set("Duration", 100)
                delay(1000)
                exercise.set("Duration", 150)
            }

        }
        Truth.assertThat(true).isTrue()
    }
}