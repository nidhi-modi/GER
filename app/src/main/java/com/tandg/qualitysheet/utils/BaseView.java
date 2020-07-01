package com.tandg.qualitysheet.utils;

public interface  BaseView {


	void showProgress();
	void hideProgress();
	void showError(String message);
	void showMessage(String message);
	void finish();
}
