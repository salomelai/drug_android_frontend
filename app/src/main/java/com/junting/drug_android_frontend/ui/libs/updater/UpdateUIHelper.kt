package com.junting.drug_android_frontend.ui.libs.updater;

import android.content.BroadcastReceiver;
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

class UpdateUIHelper {

    companion object {
        fun listenInt(context: Context, eventName: String, listener: UpdateUIListener<Int>): BroadcastReceiver {
            val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
                override fun onReceive(p0: Context?, p1: Intent?) {
                    if (p1 != null) {
                        listener.onUpdate(p1.getIntExtra("number", 0))
                    }
                }
            }
            val filter = IntentFilter(eventName)
            LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceiver, filter)
            return broadcastReceiver
        }

        fun notify(context: Context, eventName: String, number: Int) {
            val intent = Intent(eventName)
            intent.putExtra("number", number)
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
        }

        fun unListen(context: Context, receiver: BroadcastReceiver) {
            LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver)
        }
    }
}
