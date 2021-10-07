package com.github.godmoonlight.moonstyle.actions

import com.github.godmoonlight.moonstyle.utils.ClassMapResult
import com.github.godmoonlight.moonstyle.utils.GenerateToMethod
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
class ConverterToAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val psiClass: PsiClass = ProjectUtil.getPsiClassFromContext(e) ?: return
        val generateConverterDialog = ConverterDialog(psiClass, from = false, to = true)
        generateConverterDialog.show()
        if (generateConverterDialog.isOK) {
            val classTo = generateConverterDialog.convertToClass
            generateConvertAs(classTo, psiClass, generateConverterDialog.isInheritFields())
        }
    }

    override fun update(e: AnActionEvent) {
        val psiClass: PsiClass? = ProjectUtil.getPsiClassFromContext(e)
        e.presentation.isEnabled = psiClass != null
    }

    private fun generateConvertAs(to: PsiClass, from: PsiClass, inherited: Boolean) {
        WriteCommandAction.runWriteCommandAction(
            from.project,
            "Convert to " + to.qualifiedName,
            null,
            getExecute(to, from, inherited),
            from.containingFile
        )
    }

    fun getExecute(to: PsiClass?, from: PsiClass, inherited: Boolean): Runnable {
        return Runnable {
            val result = ClassMapResult.from(to!!, from, inherited)
            val action = GenerateToMethod(result)
            val method: String = action.generate()
            val elementFactory = JavaPsiFacade.getElementFactory(from.project)
            val convertAs = elementFactory.createMethodFromText(method, from)
            val psiElement = from.add(convertAs)
            JavaCodeStyleManager.getInstance(from.project).shortenClassReferences(psiElement)
        }
    }
}
