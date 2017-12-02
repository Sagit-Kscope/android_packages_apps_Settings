/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings;

import android.app.settings.SettingsEnums;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;

import androidx.preference.ListPreference;
import androidx.preference.Preference;

import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.display.BrightnessLevelPreferenceController;
import com.android.settings.display.CameraGesturePreferenceController;
import com.android.settings.display.LiftToWakePreferenceController;
import com.android.settings.display.ScreenSaverPreferenceController;
import com.android.settings.display.ShowOperatorNamePreferenceController;
import com.android.settings.display.TapToWakePreferenceController;
import com.android.settings.display.ThemePreferenceController;
import com.android.settings.display.VrDisplayPreferenceController;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.search.SearchIndexable;

import ink.kaleidoscope.support.preferences.SecureSettingListPreference;
import ink.kaleidoscope.support.preferences.SystemSettingListPreference;

import java.util.ArrayList;
import java.util.List;

@SearchIndexable(forTarget = SearchIndexable.ALL & ~SearchIndexable.ARC)
public class DisplaySettings extends DashboardFragment
        implements Preference.OnPreferenceChangeListener {
    private static final String TAG = "DisplaySettings";

    private static final String KEY_STATUS_BAR_AM_PM = "status_bar_am_pm";
    private static final String KEY_STATUS_BAR_BATTERY_STYLE = "status_bar_battery_style";
    private static final String KEY_STATUS_BAR_SHOW_BATTERY_PERCENT = "status_bar_show_battery_percent";
    private static final String KEY_PROXIMITY_ON_WAKE = "proximity_on_wake";

    SecureSettingListPreference mStatusBarAmPm;
    SecureSettingListPreference mBatteryStyle;
    SystemSettingListPreference mShowPercentage;

    @Override
    public int getMetricsCategory() {
        return SettingsEnums.DISPLAY;
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    protected int getPreferenceScreenResId() {
        return R.xml.display_settings;
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        mStatusBarAmPm = findPreference(KEY_STATUS_BAR_AM_PM);
        mBatteryStyle = findPreference(KEY_STATUS_BAR_BATTERY_STYLE);
        mShowPercentage = findPreference(KEY_STATUS_BAR_SHOW_BATTERY_PERCENT);

        mBatteryStyle.setOnPreferenceChangeListener(this);

        if (!requireContext().getResources().getBoolean(
                com.android.internal.R.bool.config_proximityCheckOnWake)) {
            removePreference(KEY_PROXIMITY_ON_WAKE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (DateFormat.is24HourFormat(requireContext())) {
            mStatusBarAmPm.setEnabled(false);
            mStatusBarAmPm.setSummary(R.string.status_bar_am_pm_unavailable);
        }

        updateStates();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ((ListPreference)preference).setValue((String)newValue);
        updateStates();
        return false;
    }

    private void updateStates() {
        if ("2".equals(mBatteryStyle.getValue()))
            mShowPercentage.setEnabled(false);
        else
            mShowPercentage.setEnabled(true);
    }

    @Override
    protected List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        return buildPreferenceControllers(context, getSettingsLifecycle());
    }

    @Override
    public int getHelpResource() {
        return R.string.help_uri_display;
    }

    private static List<AbstractPreferenceController> buildPreferenceControllers(
            Context context, Lifecycle lifecycle) {
        final List<AbstractPreferenceController> controllers = new ArrayList<>();
        controllers.add(new CameraGesturePreferenceController(context));
        controllers.add(new LiftToWakePreferenceController(context));
        controllers.add(new ScreenSaverPreferenceController(context));
        controllers.add(new TapToWakePreferenceController(context));
        controllers.add(new VrDisplayPreferenceController(context));
        controllers.add(new ShowOperatorNamePreferenceController(context));
        controllers.add(new ThemePreferenceController(context));
        controllers.add(new BrightnessLevelPreferenceController(context, lifecycle));
        return controllers;
    }

    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider(R.xml.display_settings) {

                @Override
                public List<String> getNonIndexableKeys(Context context) {
                    List<String> keys = super.getNonIndexableKeys(context);
                    if (!context.getResources().getBoolean(
                            com.android.internal.R.bool.config_proximityCheckOnWake)) {
                        keys.add(KEY_PROXIMITY_ON_WAKE);
                    }
                    return keys;
                }

                @Override
                public List<AbstractPreferenceController> createPreferenceControllers(
                        Context context) {
                    return buildPreferenceControllers(context, null);
                }
            };
}
