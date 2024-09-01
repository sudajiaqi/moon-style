package com.github.sudajiaqi.moonstyle.services

import com.github.sudajiaqi.moonstyle.MoonStyle
import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project

@Service(Service.Level.PROJECT)
class MyProjectService(project: Project) {

    init {
        thisLogger().info(MoonStyle.message("projectService", project.name))
    }
}
