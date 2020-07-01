package com.tandg.qualitysheet.di.modules;

import android.content.Context;

import com.tandg.qualitysheet.database.dataSource.QualityInfoDataSource;

import dagger.Module;
import dagger.Provides;

/**
 * Created by root on 29/11/17.
 */

@Module
public class DatabaseModules {

@Provides
QualityInfoDataSource provideQualityInfoDataSource(Context context){
    return new QualityInfoDataSource(context);
}


}
