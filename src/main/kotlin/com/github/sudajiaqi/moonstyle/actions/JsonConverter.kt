package com.github.sudajiaqi.moonstyle.actions

import com.github.sudajiaqi.moonstyle.settings.ConfigUtil
import com.github.sudajiaqi.moonstyle.settings.ToJsonConfig
import com.github.sudajiaqi.moonstyle.utils.Notifier
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

class JsonConverter : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val editor = e.dataContext.getData(CommonDataKeys.EDITOR)
        val project = editor!!.project
        val referenceAt =
            e.dataContext.getData(CommonDataKeys.PSI_FILE)!!.findElementAt(editor.caretModel.offset)

        val selectedClass: PsiClass =
            PsiTreeUtil.getContextOfType<PsiElement>(referenceAt, PsiClass::class.java) as PsiClass
        val toJson: ToJsonConfig = ConfigUtil.get().toJsonConfig

        val kv: KV<String, Any> =
            FieldResolver(toJson.comment, toJson.randomValue, toJson.enumValues)
                .getFields(selectedClass)
        val json: String = kv.toPrettyJson()
        val selection = StringSelection(json)
        val clipboard = Toolkit.getDefaultToolkit().systemClipboard
        clipboard.setContents(selection, selection)
        val message = "Convert " + selectedClass.name + " to JSON success, copied to clipboard."
        Notifier.notify(project, message, NotificationType.INFORMATION)
    }
}
