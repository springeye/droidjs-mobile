package com.github.springeye.droidjs.di

import com.github.springeye.droidjs.DroidJsApplication
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface DiEntryPoint {
    fun  application():DroidJsApplication
}