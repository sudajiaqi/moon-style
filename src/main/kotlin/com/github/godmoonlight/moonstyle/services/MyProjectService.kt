package com.github.godmoonlight.moonstyle.services

import com.intellij.openapi.project.Project
import com.github.godmoonlight.moonstyle.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
