package com.tandg.qualitysheet.helper;


import com.tandg.qualitysheet.constants.AppConstants;
import com.tandg.qualitysheet.constants.DatabaseConstants;

/**
 * Created by root on 23/12/17.
 */

public interface HelperInterface extends AppConstants, DatabaseConstants {

    ApplicationHelper getHelper();
}
