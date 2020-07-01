package com.tandg.qualitysheet.pruningFragment;


import com.tandg.qualitysheet.di.DependencyInjector;


/**
 * Created by root on 23/12/17.
 */

public class PruningFragmentPresenter implements PruningFragmentContract.Presenter {

    private PruningFragmentContract.View mView;


    public <T extends PruningFragment & PruningFragmentContract.View> PruningFragmentPresenter(T view) {
        this.mView = view;

        DependencyInjector.appComponent().inject(this);

    }


    @Override
    public void start() {

    }
}
