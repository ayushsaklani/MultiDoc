package com.ayushsaklani.multidoc.dagger.components;

import android.app.Application;

import com.ayushsaklani.multidoc.MultiDocApplication;
import com.ayushsaklani.multidoc.dagger.modules.ActivityModule;
import com.ayushsaklani.multidoc.dagger.modules.MainModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

@Component(modules = {ActivityModule.class,
        MainModule.class,
        AndroidSupportInjectionModule.class})
@Singleton
public interface AppComponent {

    /* We will call this builder interface from our custom Application class.
     * This will set our application object to the AppComponent.
     * So inside the AppComponent the application instance is available.
     * So this application instance can be accessed by our modules
     * such as ApiModule when needed
     * */
    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }


    /*
     * This is our custom Application class
     * */
    void inject(MultiDocApplication multiDocApplication);
}
