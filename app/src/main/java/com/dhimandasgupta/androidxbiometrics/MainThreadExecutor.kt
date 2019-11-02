package com.dhimandasgupta.androidxbiometrics

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor

class MainThreadExecutor : Executor {
    private val handler = Handler(Looper.getMainLooper())
    private var command: Runnable? = null

    override fun execute(command: Runnable) {
        this.command = command
        handler.post(this.command)
    }

    fun shutDown() {
        handler.removeCallbacks(command)
    }
}