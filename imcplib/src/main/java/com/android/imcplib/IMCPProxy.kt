package com.android.imcp.api

import android.content.ContentProviderClient
import android.content.ContentValues
import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.Looper
import com.android.imcplib.MultiMap
import kotlin.collections.ArrayList

open class IMCPProxy(val ctx : Context, val uri : Uri) :
                    ContentObserver(Handler(Looper.getMainLooper()))
{
    private val cp : ContentProviderClient?
    private var cache : HashMap<String, String>
    private var observers : MultiMap<String, Observer>
    private var changed : ArrayList<String>

    init {
        cp = ctx.contentResolver.acquireContentProviderClient(uri)
        ctx.contentResolver.registerContentObserver(uri,
            true, this)
        cache = HashMap<String, String>()
        observers = MultiMap<String, Observer>()
        changed = ArrayList<String>()
    }
    override fun onChange(selfChange: Boolean) {
        refresh()
    }

    @Synchronized
    private fun refresh() {
        changed.clear()
        val c = cp?.query(uri, null, null,
                            null, null)
        if(c != null) {
            if(c.moveToFirst()) {
                val cn = c.columnCount
                for(i in 0 until cn)
                {
                    val k = c.getColumnName(i) ?: ""
                    val o = c. getString(i) ?: ""
                    if(!cache.containsKey(k) || cache[k] != o) {
                        cache[k] = o
                        changed.add(k)
                    }
                }
            }
            c.close()
            for(s in changed) {
                var foo = observers.getValues(s)
                for(fo in foo)
                    fo.update()
            }
        }
    }

    @Synchronized
    fun notifyOnChange(field: String, observer : Observer)
    {
        observers.add(field, observer)
        observer.update()
    }

    @Synchronized
    fun removeNotifyOnChange(field: String, observer : Observer)
    {
        observers.remove(field, observer)
    }

    @Synchronized
    fun getString(fieldCode: String): String {
        return cache[fieldCode] ?: ""
    }

    @Synchronized
    fun getInt(fieldCode: String): Int {
        return cache[fieldCode]?.toInt() ?: -1
    }

    @Synchronized
    fun set(fieldCode: String, value: String) {
        val cv = ContentValues()
        cv.put(fieldCode, value)
        cp?.update(uri, cv,null, null)
    }

    @Synchronized
    fun set(fieldCode: String, value: Int) {
        val cv = ContentValues()
        cv.put(fieldCode, value)
        cp?.update(uri, cv,null, null)
    }

    @Synchronized
    fun tearDown() {
        cache.clear()
        changed.clear()
        observers.clear()
        cp?.release()
        ctx.contentResolver.unregisterContentObserver(this)
    }
}