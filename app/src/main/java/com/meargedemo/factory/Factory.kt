package com.meargedemo.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.meargedemo.MainActivityVM

@Suppress("UNCHECKED_CAST")
class Factory(
    private var context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainActivityVM::class.java)) {
            return MainActivityVM(context) as T
        }
        throw IllegalThreadStateException("ExceptionHeld")
    }
}
