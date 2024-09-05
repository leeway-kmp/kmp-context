package io.github.leeway.kmp.context

import android.content.Context
import java.lang.ref.WeakReference

actual object KmpContext {

    private lateinit var refs: WeakReference<Context>

    internal fun setUp(context: Context) {
        refs = WeakReference(context)
    }

    fun get(): Context {
        if (KmpContext::refs.isInitialized.not())
            throw IllegalStateException("Context is not initialized")
        return refs.get()?.applicationContext
            ?: throw IllegalStateException("Context is not initialized")
    }

}