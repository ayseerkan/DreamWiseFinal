package com.example.dreamwise

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters

class CustomWorker(var context: Context, var workerParams: WorkerParameters) : Worker(context, workerParams) {
    val tagforLogcat = "WorkerEx"

    override fun doWork(): ListenableWorker.Result {
        val num: Int = inputData.getInt("num", 1)
        val name: String = inputData.getString("name").toString()

        return try {
            Log.d(tagforLogcat, "doWork Called, inputs: $num $name")
            var sum = 0
            for (i in 1..num) {
                sum += i
                try {
                    Thread.sleep(100)
                } catch (e: InterruptedException) {
                    throw RuntimeException(e)
                }
            }
            // Create the output of the worker
            val outputData = Data.Builder().putInt("result", sum).build()
            Utils.sendNotification(context, sum.toString() + "")
            Log.d(tagforLogcat, "End of worker")

            // Show toast on the main thread
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(context, "Work completed with result: $sum", Toast.LENGTH_LONG).show()
            }

            ListenableWorker.Result.success(outputData)
        } catch (throwable: Throwable) {
            Log.d(tagforLogcat, "Error Sending Notification: ${throwable.message}")

            // Show error toast on the main thread
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(context, "Work failed: ${throwable.message}", Toast.LENGTH_LONG).show()
            }

            ListenableWorker.Result.failure()
        }
    }
}
