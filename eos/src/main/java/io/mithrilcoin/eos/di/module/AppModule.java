package io.mithrilcoin.eos.di.module;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.mithrilcoin.eos.data.local.db.AppDatabase;
import io.mithrilcoin.eos.data.local.repository.EosAccountRepository;
import io.mithrilcoin.eos.data.local.repository.EosAccountRepositoryImpl;
import io.mithrilcoin.eos.data.prefs.PreferencesHelper;
import io.mithrilcoin.eos.data.remote.EosdApi;
import io.mithrilcoin.eos.data.remote.HostInterceptor;
import io.mithrilcoin.eos.data.remote.model.chain.SignedTransaction;
import io.mithrilcoin.eos.data.remote.model.api.Message;
import io.mithrilcoin.eos.data.remote.model.types.TypeAccountPermission;
import io.mithrilcoin.eos.data.wallet.EosWalletManager;
import io.mithrilcoin.eos.di.ApplicationContext;
import io.mithrilcoin.eos.util.RefValue;
import io.mithrilcoin.eos.util.StringUtils;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by swapnibble on 2017-08-24.
 */
@Module
public class AppModule {

    private final Application mApp;

    public AppModule( Application application) { mApp = application;}

    @Provides
    Application provideApp() { return mApp; }

    @Provides
    @ApplicationContext
    Context provideAppContext() { return mApp; }


    @Provides
    @Singleton
    HostInterceptor providesHostInterceptor() {
        return new HostInterceptor();
    }

    @Provides
    @Singleton
    OkHttpClient providesOkHttpClient(HostInterceptor interceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
    }

    @Provides
    @Singleton
    Gson providesGson() {
        return new GsonBuilder()
                .registerTypeAdapterFactory( new Message.GsonTypeAdapterFactory() )
                .registerTypeAdapterFactory( new TypeAccountPermission.GsonTypeAdapterFactory() )
                .registerTypeAdapterFactory( new SignedTransaction.GsonTypeAdapterFactory() )
                .excludeFieldsWithoutExposeAnnotation()
                .create();
    }


    private static final String ENDPOINT = "http://testnet1.eos.io";

    @Provides
    @Singleton
    EosdApi providesEosService(Gson gson, OkHttpClient okHttpClient, PreferencesHelper preferencesHelper) {
        RefValue<Integer> portRef = new RefValue<>(0);
        String addr = preferencesHelper.getEosdConnInfo( portRef );

        String url = StringUtils.isEmpty( addr ) ? ENDPOINT : ( "http://"+addr+":"+portRef.data);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( url )
                .addConverterFactory( GsonConverterFactory.create( gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // retrofit 용 rxjava2 adapter
                .client( okHttpClient )
                .build();

        return retrofit.create( EosdApi.class);
    }

    @Provides
    @Singleton
    EosWalletManager providesWalletManager() {
        return new EosWalletManager();
    }

    @Provides
    @Singleton
    AppDatabase provideAppDatabase( @ApplicationContext  Context context){
        return Room.databaseBuilder( context.getApplicationContext(), AppDatabase.class, "eosc.db")
                .build();
    }

    @Provides
    @Singleton
    EosAccountRepository provideAccountRepository(AppDatabase database ) {
        return new EosAccountRepositoryImpl(database);
    }
}
