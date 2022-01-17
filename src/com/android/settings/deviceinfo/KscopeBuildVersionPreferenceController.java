/*
 * Copyright (C) 2022 Project Kaleidoscope
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

package com.android.settings.deviceinfo;

import android.content.Context;
import android.os.SystemProperties;

import androidx.preference.Preference;

import com.android.settings.core.BasePreferenceController;

public class KscopeBuildVersionPreferenceController extends BasePreferenceController {

    private String mKscopeBuildInfo;

    private String getKscopeBuildInfo(){
        String[] fullVersion = SystemProperties.get("ro.kscope.version").split("-");
        return fullVersion.length > 4 ? fullVersion[2] + " | " + fullVersion[4] + " (" + fullVersion[3] + ")" : null;
    }

    public KscopeBuildVersionPreferenceController(Context context, String key) {
        super(context, key);
        mKscopeBuildInfo = getKscopeBuildInfo();
    }

    @Override
    public int getAvailabilityStatus() {
        return mKscopeBuildInfo != null
                ? AVAILABLE : CONDITIONALLY_UNAVAILABLE;
    }

    @Override
    public CharSequence getSummary() {
        return mKscopeBuildInfo;
    }
}
