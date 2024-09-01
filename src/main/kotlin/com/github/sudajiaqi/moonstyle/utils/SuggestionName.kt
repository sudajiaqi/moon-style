package com.github.sudajiaqi.moonstyle.utils

import com.intellij.psi.PsiClass

object SuggestionName {

    operator fun get(psiClass: PsiClass): String {
        val className = psiClass.name!!
        return className.substring(0, 1).toLowerCase() + className.substring(1)
    }
}
