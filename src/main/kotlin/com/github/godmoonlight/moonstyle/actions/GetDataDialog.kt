package com.github.godmoonlight.moonstyle.actions

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import java.awt.GridLayout
import javax.swing.JComponent
import javax.swing.JPanel

class GetDataDialog(project: Project) : DialogWrapper(project) {

    private val dialog: JPanel

    /**
     * Factory method. It creates panel with dialog options. Options panel is located at the
     * center of the dialog's content pane. The implementation can return `null`
     * value. In this case there will be no options panel.
     */
    override fun createCenterPanel(): JComponent? {
        return dialog
    }

    init {
        title = "Select Classes for Conversion"
        dialog = JPanel(GridLayout(0, 1))


        init()
    }
}
