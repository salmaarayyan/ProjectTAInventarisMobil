package com.example.inventarisshowroom

import android.app.Application
import com.example.inventarisshowroom.repositori.AppContainer
import com.example.inventarisshowroom.repositori.ShowroomContainer

class ShowroomApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = ShowroomContainer()
    }
}