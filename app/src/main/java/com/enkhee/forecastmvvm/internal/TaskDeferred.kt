package com.enkhee.forecastmvvm.internal

import android.util.Log
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred

fun <T> Task<T>.asDeferred(): Deferred<T>{
    Log.v("TaskDef", "Started")
    val deferred = CompletableDeferred<T>()

    this.addOnSuccessListener { result ->
        deferred.complete(result)
    }

    this.addOnFailureListener {exception ->
        deferred.completeExceptionally(exception)
    }

    Log.v("TaskDef", "Finished")
    return deferred
}