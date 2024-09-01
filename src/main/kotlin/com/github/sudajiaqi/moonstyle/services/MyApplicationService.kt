package com.github.sudajiaqi.moonstyle.services

import com.github.sudajiaqi.moonstyle.MoonStyle
import com.github.sudajiaqi.moonstyle.settings.MoonSettingConfig
import com.github.sudajiaqi.moonstyle.settings.ToJsonConfig
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "com.github.sudajiaqi.moonstyle.services.MyApplicationService",
    storages = [Storage("Moon-SettingsPlugin.xml")]
)
class MyApplicationService : PersistentStateComponent<MyApplicationService> {
    init {
        thisLogger().info(MoonStyle.message("applicationService"))
    }

    var config: MoonSettingConfig = MoonSettingConfig(ToJsonConfig())

    companion object {
        fun getInstance(): MyApplicationService {
            return ApplicationManager.getApplication().getService(MyApplicationService::class.java)
        }
    }

    override fun getState(): MyApplicationService {
        return this
    }

    override fun loadState(state: MyApplicationService) {
        XmlSerializerUtil.copyBean(state, this)
    }
}
