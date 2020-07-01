package com.tandg.qualitysheet.deleafingFragment;


import com.tandg.qualitysheet.di.DependencyInjector;
import com.tandg.qualitysheet.droppingFragment.DroppingFragment;
import com.tandg.qualitysheet.droppingFragment.DroppingFragmentContract;

/**
 * Created by root on 23/12/17.
 */

public class DeleafingFragmentPresenter implements DeleafingFragmentContract.Presenter {

    private DeleafingFragmentContract.View mView;


    public <T extends DeleafingFragment & DeleafingFragmentContract.View> DeleafingFragmentPresenter(T view) {
        this.mView = view;

        DependencyInjector.appComponent().inject(this);

    }


    @Override
    public void start() {

    }
}
