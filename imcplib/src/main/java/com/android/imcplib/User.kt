package com.android.imcp.api

import android.content.Context
import android.net.Uri

class User (ctx : Context) : IMCPProxy(ctx, Uri.parse("content://com.android.imcp.User.AUTHORITY/item"))


