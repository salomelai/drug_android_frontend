package com.junting.drug_android_frontend.ui.takeRecords

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.junting.drug_android_frontend.DrugbagInfoActivity
import com.junting.drug_android_frontend.R
import com.junting.drug_android_frontend.PhotoTakeActivity


class TakeRecordsByTimePage(context: Context, container: ViewGroup) {

    val view: View
    private val context: Context
    private val container: ViewGroup


//    private val viewModel:



    init {
        this.context = context
        this.container = container
        this.view =
            LayoutInflater.from(context).inflate(R.layout.take_records_time_tab, container, false)
    }



}