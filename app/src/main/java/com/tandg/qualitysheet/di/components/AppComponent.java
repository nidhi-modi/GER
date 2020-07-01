package com.tandg.qualitysheet.di.components;


import com.tandg.qualitysheet.clippingFragment.ClippingFragment;
import com.tandg.qualitysheet.clippingFragment.ClippingFragmentPresenter;
import com.tandg.qualitysheet.deleafingFragment.DeleafingFragment;
import com.tandg.qualitysheet.deleafingFragment.DeleafingFragmentPresenter;
import com.tandg.qualitysheet.droppingFragment.DroppingFragment;
import com.tandg.qualitysheet.droppingFragment.DroppingFragmentPresenter;
import com.tandg.qualitysheet.di.modules.AppModule;
import com.tandg.qualitysheet.di.modules.DatabaseModules;
import com.tandg.qualitysheet.pickingFragment.PickingFragment;
import com.tandg.qualitysheet.pickingFragment.PickingFragmentPresenter;
import com.tandg.qualitysheet.pruningFragment.PruningFragment;
import com.tandg.qualitysheet.pruningFragment.PruningFragmentPresenter;
import com.tandg.qualitysheet.qualitySheetActivity.QualitySheetActivity;
import com.tandg.qualitysheet.qualitySheetActivity.QualitySheetPresenter;
import com.tandg.qualitysheet.twistingFragment.TwistingFragment;
import com.tandg.qualitysheet.twistingFragment.TwistingFragmentPresenter;
import com.tandg.qualitysheet.utils.AppContext;

import javax.inject.Singleton;

import dagger.Component;


/**
 * Created by root on 27/11/17.
 */



@Singleton
@Component(modules = {AppModule.class, DatabaseModules.class})
public interface AppComponent {

    void inject(AppContext appContext);

    void inject(QualitySheetPresenter presenter);

    void inject(QualitySheetActivity activity);

    void inject(DroppingFragmentPresenter presenter);

    void inject(DroppingFragment fragment);

    void inject(ClippingFragmentPresenter presenter);

    void inject(ClippingFragment fragment);

    void inject(DeleafingFragmentPresenter presenter);

    void inject(DeleafingFragment fragment);

    void inject(PruningFragmentPresenter presenter);

    void inject(PruningFragment fragment);

    void inject(TwistingFragmentPresenter presenter);

    void inject(TwistingFragment fragment);

    void inject(PickingFragmentPresenter presenter);

    void inject(PickingFragment fragment);


    /*void inject(MainActivityPresenter presenter);

    void inject(HomeFragmentPresenter presenter);

    void inject(DetailFragmentPresenter presenter);

    void inject(DetailFragment fragment);

    void inject(HomeFragment fragment);*/





}
