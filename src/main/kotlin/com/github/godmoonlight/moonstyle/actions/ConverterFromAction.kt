package com.github.godmoonlight.moonstyle.actions

import com.github.godmoonlight.moonstyle.utils.ClassMapResult
import com.github.godmoonlight.moonstyle.utils.GenerateFromMethod
import com.github.godmoonlight.moonstyle.utils.GenerateMethod
import com.github.godmoonlight.moonstyle.utils.ProjectUtil
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.codeStyle.JavaCodeStyleManager


/**
 * @author jiaqi
 */
class ConverterFromAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val psiClass: PsiClass = ProjectUtil.getPsiClassFromContext(e) ?: return
        val generateConverterDialog = ConverterDialog(psiClass, from = true, to = false)
        generateConverterDialog.show()
        if (generateConverterDialog.isOK) {
            val classFrom = generateConverterDialog.convertFromClass
            generateConvertAs(psiClass, classFrom, generateConverterDialog.isInheritFields())
        }
    }

    override fun update(e: AnActionEvent) {
        val psiClass: PsiClass? = ProjectUtil.getPsiClassFromContext(e)
        e.presentation.isEnabled = psiClass != null
    }

    private fun generateConvertAs(to: PsiClass, from: PsiClass, inherited: Boolean) {
        WriteCommandAction.runWriteCommandAction(
            to.project,
            "Convert from " + from.qualifiedName,
            null,
            getExecute(to, from, inherited),
            to.containingFile
        )
    }

    fun getExecute(to: PsiClass, from: PsiClass?, inherited: Boolean): Runnable {
        return Runnable {
            val result = ClassMapResult.from(to, from!!, inherited)
            val action: GenerateMethod = GenerateFromMethod(result)
            val method = action.generate()
            val elementFactory = JavaPsiFacade.getElementFactory(to.project)
            val convertAs = elementFactory.createMethodFromText(method, to)
            val psiElement = to.add(convertAs)
            JavaCodeStyleManager.getInstance(to.project).shortenClassReferences(psiElement)
        }
    }
}
