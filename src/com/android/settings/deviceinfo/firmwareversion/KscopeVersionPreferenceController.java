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

package com.android.settings.deviceinfo.firmwareversion;

import android.content.Context;
import android.os.SystemProperties;

import androidx.preference.Preference;

import com.android.settings.core.BasePreferenceController;

public class KscopeVersionPreferenceController extends BasePreferenceController {

    private String mKscopeVersion;

    private String getKscopeMainVersion(){
        String[] fullVersion = SystemProperties.get("ro.kscope.version").split("-");
        return fullVersion.length > 4 ? fullVersion[1] + " | " + fullVersion[4] + " (" + fullVersion[3] + ")" : null;
    }

    public KscopeVersionPreferenceController(Context context, String key) {
        super(context, key);
        mKscopeVersion = getKscopeMainVersion();
    }

    @Override
    public int getAvailabilityStatus() {
        return mKscopeVersion != null
                ? AVAILABLE : CONDITIONALLY_UNAVAILABLE;
    }

    @Override
    public CharSequence getSummary() {
        return mKscopeVersion;
    }
}
