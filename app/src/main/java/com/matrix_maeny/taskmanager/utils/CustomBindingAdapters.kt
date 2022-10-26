package com.matrix_maeny.taskmanager.utils

import android.annotation.SuppressLint
import android.view.View
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.matrix_maeny.taskmanager.R

@BindingAdapter("loadTint")
fun loadTint(compoundButton: CompoundButton, isChecked: Boolean) {
    if (isChecked) {
        compoundButton.buttonTintList =
            compoundButton.context.getColorStateList(R.color.checked_button_tint)
    } else compoundButton.buttonTintList =
        compoundButton.context.getColorStateList(R.color.unchecked_button_tint)

    compoundButton.isChecked = isChecked
}

@BindingAdapter("loadImage")
fun loadImage(imageView: ImageView, isChecked: Boolean) {

    if (isChecked) {
        imageView.setImageResource(R.drawable.check)
    } else imageView.setImageResource(R.drawable.chart)
}

@SuppressLint("SetTextI18n")
@BindingAdapter("isRefresh")
fun isRefresh(refreshLayout: SwipeRefreshLayout, shouldRefresh: Boolean){
    refreshLayout.isRefreshing = shouldRefresh
}