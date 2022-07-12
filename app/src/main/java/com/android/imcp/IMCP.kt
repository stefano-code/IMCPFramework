package com.android.imcp

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri

open class IMCP  : ContentProvider() {
    protected val db = HashMap<String, Any>()

    override fun onCreate(): Boolean {
        return true
    }
    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        var _projection = projection
        if (_projection == null) {
            val fullProjection: Array<String>
            val keys: Set<String> = db.keys
            val keyArray = keys.toTypedArray()
            fullProjection =  keyArray
            _projection = fullProjection
        }
        val c = MatrixCursor(_projection)
        val n = _projection?.size
        val values = arrayOfNulls<Any>(n ?: 0)
        var i = 0
        _projection?.forEach { s -> values[i++] = db[s]  }
        c.addRow(values)
        return c
    }
    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val s = values?.valueSet()
        s?.forEach { e ->
            db[e.key] = e.value
        }
        context?.contentResolver?.notifyChange(uri, null, false)
        return null
    }
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        insert(uri, values)
        return values?.size() ?: 0
    }
    @Synchronized
    override fun getType(uri: Uri): String? = null
}