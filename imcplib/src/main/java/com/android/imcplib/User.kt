package com.android.imcp.api

import android.content.Context
import android.net.Uri

//SS val CONTENT_URI = Uri.parse("content://com.technogym.android.imcptest.AUTHORITY/item")

class User (ctx : Context) : IMCPProxy(ctx, Uri.parse("content://com.android.imcp.User.AUTHORITY/item"))


