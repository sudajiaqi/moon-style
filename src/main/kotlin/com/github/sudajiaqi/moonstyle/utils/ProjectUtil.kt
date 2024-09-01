package com.github.sudajiaqi.moonstyle.utils

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.codeStyle.CommonCodeStyleSettings.IndentOptions.retrieveFromAssociatedDocument
import com.intellij.psi.util.PsiTreeUtil

object ProjectUtil {
    fun getProjectIndentation(psiClass: PsiClass): String {
        val containingFile = psiClass.containingFile
        var indentOptions = CommonCodeStyleSettings.IndentOptions.DEFAULT_INDENT_OPTIONS

        val document = PsiDocumentManager.getInstance(psiClass.project).getDocument(containingFile)
        if (document != null) {
            indentOptions = retrieveFromAssociatedDocument(document)
        }

        return if (indentOptions.USE_TAB_CHARACTER) {
            "\t\t"
        } else {
            String(CharArray(2 * indentOptions.INDENT_SIZE)).replace("\u0000", " ")
        }
    }

    fun getPsiClassFromContext(e: AnActionEvent): PsiClass? {
        val psiFile: PsiFile? = e.getData(LangDataKeys.PSI_FILE)!!
        val editor: Editor? = e.getData(PlatformDataKeys.EDITOR)!!
        if (psiFile == null || editor == null) {
            return null
        }
        val offset: Int = editor.caretModel.offset
        val elementAt: PsiElement? = psiFile.findElementAt(offset)
        return PsiTreeUtil.getParentOfType(elementAt, PsiClass::class.java)
    }
}
