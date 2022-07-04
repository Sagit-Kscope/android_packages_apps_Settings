/*
 * Copyright (C) 2022 Project Kaleidoscope
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.android.settings.laboratory;

import android.content.Context;
import android.os.SystemProperties;

import com.android.settings.core.TogglePreferenceController;
import com.android.settings.R;
import com.android.settingslib.development.SystemPropPoker;

public class WebViewPreferenceController extends TogglePreferenceController {

    private static final String WEBVIEW_PROPERTY = "persist.sys.allow_webview_selection";

    public WebViewPreferenceController(Context context, String key) {
        super(context, key);
    }

    @Override
    public int getAvailabilityStatus() {
        return AVAILABLE;
    }

    @Override
    public boolean isChecked() {
        return SystemProperties.getBoolean(WEBVIEW_PROPERTY, false);
    }

    @Override
    public boolean setChecked(boolean isChecked) {
        SystemProperties.set(WEBVIEW_PROPERTY, Boolean.toString(isChecked));
        SystemPropPoker.getInstance().poke();
        return true;
    }

    @Override
    public int getSliceHighlightMenuRes() {
        return NO_RES;
    }
}
