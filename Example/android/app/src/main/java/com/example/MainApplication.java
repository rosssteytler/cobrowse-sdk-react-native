package com.example;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.view.View;
import android.view.inspector.WindowInspector;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.PackageList;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint;
import com.facebook.react.defaults.DefaultReactNativeHost;
import com.facebook.soloader.SoLoader;

import java.util.ArrayList;
import java.util.List;

import io.cobrowse.reactnative.CobrowseIO;
import io.cobrowse.reactnative.CobrowseIOModule;

public class MainApplication extends Application implements ReactApplication, CobrowseIO.RedactionDelegate {

  private final ReactNativeHost mReactNativeHost = new DefaultReactNativeHost(this) {
    @Override
    public boolean getUseDeveloperSupport() {
      return BuildConfig.DEBUG;
    }

    @Override
    protected List<ReactPackage> getPackages() {
      @SuppressWarnings("UnnecessaryLocalVariable")
      List<ReactPackage> packages = new PackageList(this).getPackages();
      // Packages that cannot be autolinked yet can be added manually here, for example:
      // packages.add(new MyReactNativePackage());
      return packages;
    }

    @Override
    protected String getJSMainModuleName() {
      return "index";
    }

    @Override
    protected boolean isNewArchEnabled() {
      return BuildConfig.IS_NEW_ARCHITECTURE_ENABLED;
    }

    @Override
    protected Boolean isHermesEnabled() {
      return BuildConfig.IS_HERMES_ENABLED;
    }
  };

  @Override
  public ReactNativeHost getReactNativeHost() {
    return mReactNativeHost;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    SoLoader.init(this, /* native exopackage */ false);

    if (BuildConfig.IS_NEW_ARCHITECTURE_ENABLED) {
      // If you opted-in for the New Architecture, we load the native entry point for this app.
      DefaultNewArchitectureEntryPoint.load();
    }
    ReactNativeFlipper.initializeFlipper(this, getReactNativeHost().getReactInstanceManager());

    CobrowseIOModule.delegate = this;
  }

  @Nullable
  @Override
  public List<View> redactedViews(@NonNull Activity activity) {
    ArrayList<View> redacted = new ArrayList<View>() {{
      add(activity.getWindow().getDecorView());
    }};

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      List<View> globalWindowViews = WindowInspector.getGlobalWindowViews();

      for(int i=0; i < globalWindowViews.size(); i++){
        ArrayList<View> changeLocationViews = new ArrayList<>();
        globalWindowViews.get(i).findViewsWithText(changeLocationViews, "Change Bundle Location", View.FIND_VIEWS_WITH_TEXT);
        if (changeLocationViews.size() > 0) {
          for (int j = 0; j < changeLocationViews.size(); j++) {
            redacted.add((View) changeLocationViews.get(j).getParent());
          }
        }
      }
    }

    return redacted;
  }

  @Nullable
  @Override
  public List<View> unredactedViews(@NonNull Activity activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      List<View> globalWindowViews = WindowInspector.getGlobalWindowViews();
      ArrayList<View> unredacted = new ArrayList<>();

      for(int i=0; i < globalWindowViews.size(); i++){
        ArrayList<View> configureBundlerViews = new ArrayList<>();
        globalWindowViews.get(i).findViewsWithText(configureBundlerViews, "Change Bundle Location", View.FIND_VIEWS_WITH_TEXT);
        if (configureBundlerViews.size() > 0) {
          for (int j = 0; j < configureBundlerViews.size(); j++) {
            unredacted.add((View) configureBundlerViews.get(j));
          }
        }
      }

      return unredacted;
    }

    return null;
  }
}
