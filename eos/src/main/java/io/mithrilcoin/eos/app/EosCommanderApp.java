package io.mithrilcoin.eos.app;

import android.app.Application;
import android.content.Context;

import javax.inject.Inject;

import io.mithrilcoin.eos.BuildConfig;
import io.mithrilcoin.eos.data.EoscDataManager;
import io.mithrilcoin.eos.di.component.AppComponent;
import io.mithrilcoin.eos.di.component.DaggerAppComponent;
import io.mithrilcoin.eos.di.module.AppModule;
import timber.log.Timber;

/**
 * Created by swapnibble on 2017-11-03.
 */

public class EosCommanderApp extends Application {
    private AppComponent mAppComponent;

    @Inject
    EoscDataManager mDataManager;

    @Override
    public void onCreate() {
        super.onCreate();

        // https://android-developers.googleblog.com/2013/08/some-securerandom-thoughts.html
        PRNGFixes.apply();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        mAppComponent = DaggerAppComponent.builder()
                .appModule( new AppModule(this))
                .build();

        mAppComponent.inject( this );
    }

    public static EosCommanderApp get( Context context ){
        return (EosCommanderApp) context.getApplicationContext();
    }

    public AppComponent getAppComponent() { return mAppComponent; }
}
