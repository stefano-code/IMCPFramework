package com.android.imcplib

import android.content.Context
import android.net.Uri
import com.android.imcp.api.IMCPProxy

class Exercise (ctx : Context) : IMCPProxy(ctx, Uri.parse("content://com.android.imcp.Exercise.AUTHORITY/item")) {
}

