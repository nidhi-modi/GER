package com.tandg.qualitysheet.di;


import com.tandg.qualitysheet.di.components.AppComponent;
import com.tandg.qualitysheet.di.components.DaggerAppComponent;
import com.tandg.qualitysheet.di.modules.AppModule;
import com.tandg.qualitysheet.di.modules.DatabaseModules;
import com.tandg.qualitysheet.utils.AppContext;

public class DependencyInjector {

    private static AppComponent appComponent;

    private DependencyInjector() {
    }

    public static void initialize(AppContext appContext) {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(appContext))
                .databaseModules(new DatabaseModules())
                .build();
    }

    public static AppComponent appComponent() {
        return appComponent;
    }
}
