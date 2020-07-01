package com.tandg.qualitysheet.di.modules;

import android.content.Context;

import com.tandg.qualitysheet.helper.ApplicationHelper;
import com.tandg.qualitysheet.helper.HelperInterface;
import com.tandg.qualitysheet.utils.AppContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


/**
 * Created by root on 27/11/17.
 */

@Module
public class AppModule implements HelperInterface {

    private final AppContext application;

    public AppModule(AppContext application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return application;
    }

    @Provides
    AppContext provideApplication() {
        return application;
    }

    @Override
    public ApplicationHelper getHelper() {
        return ApplicationHelper.getInstance();
    }
}
