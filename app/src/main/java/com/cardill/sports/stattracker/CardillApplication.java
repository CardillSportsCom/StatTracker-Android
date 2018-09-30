package com.cardill.sports.stattracker;

import android.app.Activity;
import android.app.Application;

import com.cardill.sports.stattracker.game.data.GameRepository;
import com.cardill.sports.stattracker.offline.domain.services.jobs.JobManagerFactory;
import com.facebook.stetho.Stetho;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import timber.log.Timber;

public class CardillApplication extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Inject
    GameRepository gameRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            Stetho.initializeWithDefaults(this);
        }

        DaggerAppComponent.builder().create(this).inject(this);

        JobManagerFactory.getJobManager(this);
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}