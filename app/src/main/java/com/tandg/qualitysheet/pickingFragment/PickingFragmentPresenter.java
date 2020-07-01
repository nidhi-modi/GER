package com.tandg.qualitysheet.pickingFragment;


import com.tandg.qualitysheet.di.DependencyInjector;
import com.tandg.qualitysheet.pruningFragment.PruningFragment;
import com.tandg.qualitysheet.pruningFragment.PruningFragmentContract;


/**
 * Created by root on 23/12/17.
 */

public class PickingFragmentPresenter implements PickingFragmentContract.Presenter {

    private PickingFragmentContract.View mView;


    public <T extends PickingFragment & PickingFragmentContract.View> PickingFragmentPresenter(T view) {
        this.mView = view;

        DependencyInjector.appComponent().inject(this);

    }


    @Override
    public void start() {

    }
}
