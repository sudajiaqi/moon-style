package com.github.godmoonlight.moonstyle.actions

import com.intellij.ide.highlighter.JavaFileType
import com.intellij.openapi.fileEditor.impl.EditorHistoryManager
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.LabeledComponent
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiClass
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.PsiShortNamesCache
import com.intellij.ui.TextFieldWithAutoCompletion
import com.intellij.util.textCompletion.TextFieldWithCompletion
import java.awt.Dimension
import java.awt.GridLayout
import javax.swing.JCheckBox
import javax.swing.JComponent
import javax.swing.JPanel

class ConverterDialog(private val psiClass: PsiClass, from: Boolean, to: Boolean) : DialogWrapper(psiClass.project) {
    private val dialog: JPanel
    private var toField: TextFieldWithAutoCompletion<String>? = null
    private var fromField: TextFieldWithAutoCompletion<String>? = null
    private val inheritFields: JCheckBox
    val convertToClass: PsiClass
        get() = extractPsiClass(toField)
    val convertFromClass: PsiClass
        get() = extractPsiClass(fromField)

    fun isInheritFields(): Boolean {
        return inheritFields.isSelected
    }

    override fun doValidate(): ValidationInfo? {
        var result: ValidationInfo? = null
        if (toField != null) {
            result = validateField(toField!!, "Target")
        }
        if (fromField != null) {
            result = validateField(fromField!!, "From")
        }
        return result
    }

    override fun createCenterPanel(): JComponent {
        return dialog
    }

    private fun createConverterDialog(): JPanel {
        title = "Select Classes for Conversion"
        val jPanel = JPanel()
        jPanel.layout = GridLayout(0, 1)
        return jPanel
    }

    private fun classNamesForAutocompletion(): List<String> {
        val history = EditorHistoryManager.getInstance(psiClass.project).files
            .map { obj: VirtualFile -> obj.nameWithoutExtension }
            .distinct()

        val projectFiles = FileTypeIndex.getFiles(
            JavaFileType.INSTANCE,
            GlobalSearchScope.allScope(psiClass.project)
        ).map { obj: VirtualFile -> obj.nameWithoutExtension }
        history.plus(projectFiles)
        return history
    }

    private fun createTextField(classNames: List<String>): TextFieldWithAutoCompletion<String> {
        val textField = TextFieldWithAutoCompletion.create(psiClass.project, classNames, true, null)
        textField.preferredSize = Dimension(WIDTH, BASE_LINE)
        textField.setOneLineMode(true)
        return textField
    }

    private fun extractPsiClass(textField: TextFieldWithAutoCompletion<String>?): PsiClass {
        val className = textField!!.text
        require(className.isNotBlank()) { "Should select class" }
        val resolvedClasses = PsiShortNamesCache.getInstance(psiClass.project)
            .getClassesByName(className, GlobalSearchScope.projectScope(psiClass.project))
        require(resolvedClasses.isNotEmpty()) { "No such class found: $className" }
        return resolvedClasses[0]
    }

    private fun validateField(field: TextFieldWithCompletion, name: String): ValidationInfo? {
        val className = field.text
        if (className.isEmpty()) {
            return ValidationInfo("$name class should be selected", field)
        }
        val resolvedClasses = PsiShortNamesCache.getInstance(psiClass.project)
            .getClassesByName(className, GlobalSearchScope.projectScope(psiClass.project))
        return if (resolvedClasses.isEmpty()) {
            ValidationInfo("Failed to find a class $className in the current project", field)
        } else null
    }

    companion object {
        private const val WIDTH = 400
        private const val BASE_LINE = 36
    }

    init {
        dialog = createConverterDialog()
        val classNamesForAutocompletion = classNamesForAutocompletion()
        inheritFields = JCheckBox("Use inherited fields")
        if (from) {
            fromField = createTextField(classNamesForAutocompletion)
            val convertFromComponent = LabeledComponent.create<TextFieldWithAutoCompletion<*>?>(
                fromField!!,
                "Convert From class"
            )
            dialog.add(convertFromComponent)
        }
        if (to) {
            toField = createTextField(classNamesForAutocompletion)
            val convertToComponent = LabeledComponent.create<TextFieldWithAutoCompletion<*>?>(
                toField!!,
                "Convert To class"
            )
            dialog.add(convertToComponent)
        }
        dialog.add(inheritFields)
        init()
    }
}
