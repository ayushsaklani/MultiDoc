package com.ayushsaklani.multidoc.dagger.modules;

import com.ayushsaklani.multidoc.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {

    @ContributesAndroidInjector()
    abstract MainActivity contributeMainActivity();

}
