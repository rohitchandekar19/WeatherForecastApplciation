package com.rc.weatherforecastapplication.home.presentation.view.listeners

import android.view.View

interface ClickListener<in T> {
    fun onClick(view: View, item: T)
}