package com.android.app_client

import android.net.Uri
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import com.android.imcp.api.IMCPProxy
import com.android.imcp.api.Observer
import com.android.imcp.api.User
import com.google.common.truth.Truth
import kotlinx.coroutines.*
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test

class TestUser {
    companion object
    {
        const val TAG = "TestUser"

        lateinit var user : User // User is only a proxy to a custom content provider create in another app

        @JvmStatic
        @BeforeClass
        fun setup() {
            user = User(ApplicationProvider.getApplicationContext())
        }

        @JvmStatic
        @AfterClass
        fun tearDown() {
            Thread.sleep(5000)
            user.tearDown()
            Log.e(TAG, "End Test")
        }
    }

    @Test
    fun testReadWrite(){
        Log.e(TAG,"Start")
        runBlocking {
            CoroutineScope(Dispatchers.IO).launch {
                user.set("Name", "Stefano")
                user.set("Age", 50)
            }
            delay(1000)
            Log.e(TAG, "name: ${user.getString("Name")}  Age: ${user.getInt("Age")}")
        }
        Truth.assertThat(true).isTrue()
    }
}