package com.tandg.qualitysheet.qualitySheetActivity;


import com.tandg.qualitysheet.di.DependencyInjector;

/**
 * Created by root on 23/12/17.
 */

public class QualitySheetPresenter implements QualitySheetContract.Presenter {

    private QualitySheetContract.View mView;


    public <T extends QualitySheetActivity & QualitySheetContract.View> QualitySheetPresenter(T view) {
        this.mView = view;

        DependencyInjector.appComponent().inject(this);

    }


    @Override
    public void start() {

    }
}
