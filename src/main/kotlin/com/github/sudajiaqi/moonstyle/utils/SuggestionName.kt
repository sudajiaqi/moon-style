package com.github.sudajiaqi.moonstyle.utils

import com.intellij.psi.PsiClass
import java.util.*

object SuggestionName {

    operator fun get(psiClass: PsiClass): String {
        val className = psiClass.name!!
        return className.substring(0, 1).lowercase(Locale.getDefault()) + className.substring(1)
    }
}
