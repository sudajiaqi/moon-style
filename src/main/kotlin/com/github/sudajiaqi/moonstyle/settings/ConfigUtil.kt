package com.github.sudajiaqi.moonstyle.settings

import com.github.sudajiaqi.moonstyle.services.MyApplicationService

object ConfigUtil {

    fun get(): MoonSettingConfig {
        return MyApplicationService.getInstance().config
    }
}
