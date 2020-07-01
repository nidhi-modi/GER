package com.tandg.qualitysheet.utils;

import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.tandg.qualitysheet.R;
import com.tandg.qualitysheet.di.DependencyInjector;
import com.tandg.qualitysheet.di.components.AppComponent;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class AppContext extends MultiDexApplication {

	private static final String TAG = "AppContext";
	private static AppContext instance;
	private static Context appContext = null;
	private static AppComponent appComponent;

	@Override
	public void onCreate() {
		super.onCreate();
		instance=this;

		initializeCalligraphy();
		initDependencies();

		MultiDex.install(this);

	}

	private void initializeCalligraphy() {

		CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
				.setDefaultFontPath("fonts/roboto_regular_webfont.ttf")
				.setFontAttrId(R.attr.fontPath)
				.build());
	}

	private void initDependencies() {

		DependencyInjector.initialize(this);
		DependencyInjector.appComponent().inject(this);

	}

	public static AppContext getInstance() {
		return instance;
	}


	public static AppComponent getAppComponent(){
		// App Compent Object
		return appComponent;
	}

}
