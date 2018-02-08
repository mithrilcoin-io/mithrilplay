package io.mithrilcoin.eos.di.module;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import dagger.Module;
import dagger.Provides;
import io.mithrilcoin.eos.di.ActivityContext;

/**
 * Created by swapnibble on 2017-08-24.
 */
@Module
public class ActivityModule {
    private AppCompatActivity mActivity;

    public ActivityModule( AppCompatActivity activity) { mActivity = activity; }

    @Provides
    @ActivityContext
    Context provideContext() {
        return mActivity;
    }

    @Provides
    AppCompatActivity provideActivity() {
        return mActivity;
    }

}
