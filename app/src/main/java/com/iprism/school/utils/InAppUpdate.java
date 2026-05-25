package com.iprism.school.utils;

import android.app.Activity;
import android.content.IntentSender;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;

public class InAppUpdate {

    public static AppUpdateManager mAppUpdateManager;
    private static final int UPDATE_REQUEST_CODE = 100;

    public static void initUpdate(Activity activity){
        mAppUpdateManager = AppUpdateManagerFactory.create(activity);
        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo result){
                if(result.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && result.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)){

                    try{
                        mAppUpdateManager.startUpdateFlowForResult(result,AppUpdateType.IMMEDIATE, activity, UPDATE_REQUEST_CODE);
                    } catch (IntentSender.SendIntentException e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static void initResult (Activity activity, int requestCode, int resultCode) {
        if(requestCode == UPDATE_REQUEST_CODE ){
            if(resultCode != Activity.RESULT_OK){
                activity.finishAffinity();
            }
        }
    }


    public static void initResume(Activity activity){
        if (mAppUpdateManager == null) {
            mAppUpdateManager = AppUpdateManagerFactory.create(activity);
        }
        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>(){
            @Override
            public void onSuccess(AppUpdateInfo result){
                if(result.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
                        || result.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE){
                    try{
                        mAppUpdateManager.startUpdateFlowForResult(result,AppUpdateType.IMMEDIATE, activity ,UPDATE_REQUEST_CODE);
                    } catch (IntentSender.SendIntentException e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
