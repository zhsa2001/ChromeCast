package com.zhsa2001.chromecast

import android.content.Context
import com.google.android.gms.cast.framework.CastOptions
import com.google.android.gms.cast.framework.OptionsProvider
import com.google.android.gms.cast.framework.SessionProvider

class CastOptionsProvider: OptionsProvider {

    override fun getCastOptions(context: Context): CastOptions {
        return CastOptions.Builder()
            .build()
    }

    override fun getAdditionalSessionProviders(context: Context): MutableList<SessionProvider>? {
        return null
    }
}