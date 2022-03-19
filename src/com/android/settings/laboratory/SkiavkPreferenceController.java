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

public class SkiavkPreferenceController extends TogglePreferenceController {

    private static final String PROPERTY_RENDERER = "persist.sys.hwui.renderer";
    private static final String PROPERTY_USES_VULKAN = "ro.hwui.use_vulkan";
    private static final String SKIAGL = "skiagl";
    private static final String SKIAVK = "skiavk";

    public SkiavkPreferenceController(Context context, String key) {
        super(context, key);
    }

    @Override
    public int getAvailabilityStatus() {
        return AVAILABLE;
    }

    @Override
    public boolean isChecked() {
        String renderer = SystemProperties.get(PROPERTY_RENDERER);
        if ("".equals(renderer))
            return "true".equals(SystemProperties.get(PROPERTY_USES_VULKAN));
        else
            return SKIAVK.equals(renderer);
    }

    @Override
    public boolean setChecked(boolean isChecked) {
        SystemProperties.set(PROPERTY_RENDERER, isChecked ? SKIAVK : SKIAGL);
        SystemPropPoker.getInstance().poke();
        return true;
    }
}
