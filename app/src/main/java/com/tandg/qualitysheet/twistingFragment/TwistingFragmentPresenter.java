package com.tandg.qualitysheet.twistingFragment;


import com.tandg.qualitysheet.di.DependencyInjector;
import com.tandg.qualitysheet.pruningFragment.PruningFragment;
import com.tandg.qualitysheet.pruningFragment.PruningFragmentContract;


/**
 * Created by root on 23/12/17.
 */

public class TwistingFragmentPresenter implements TwistingFragmentContract.Presenter {

    private TwistingFragmentContract.View mView;


    public <T extends TwistingFragment & TwistingFragmentContract.View> TwistingFragmentPresenter(T view) {
        this.mView = view;

        DependencyInjector.appComponent().inject(this);

    }


    @Override
    public void start() {

    }
}
