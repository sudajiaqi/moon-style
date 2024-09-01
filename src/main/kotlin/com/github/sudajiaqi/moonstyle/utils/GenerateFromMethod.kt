package com.github.sudajiaqi.moonstyle.utils

import com.intellij.psi.PsiPrimitiveType
import com.intellij.psi.util.PsiUtil


/**
 * @author jiaqi
 */
class GenerateFromMethod(private val mapResult: ClassMapResult) : GenerateMethod(mapResult) {
    private var fromName: String = SuggestionName[mapResult.from]

    override fun generate(): String {
        val toClassName = mapResult.to.qualifiedName
        val fromClassName = mapResult.from.qualifiedName
        val content = writeMappedFields() + mapResult.writeNotMappedFields()

        return """public static $toClassName from($fromClassName $fromName) { 
        >   if ($fromName == null) {
        >       return null;
        >   }
        >   $toClassName $toName = new $toClassName();
        >   $content
        >   return $toName;
        >}
        >""".trimMargin(">")

    }

    private fun writeMappedFields(): String {
        val builder = StringBuilder()
        for (fieldName in mapResult.mappedFields) {
            builder.append("$toName.${fieldName.value.second.name}($fromName.${fieldName.value.first.name}());\n")
        }
        for (field in mapResult.mappedConvertibleFields) {
            if (field.value.first.returnType is PsiPrimitiveType) {
                builder.append("$toName.${field.value.second.name}($fromName.${field.value.first.name}());\n")
                continue
            }
            builder.append("if($fromName.${field.value.first.name}() != null){\n")
            when {
                PsiUtil.resolveClassInClassTypeOnly(field.key.type)?.isEnum!! -> {
                    val psoClass = PsiUtil.resolveClassInClassTypeOnly(field.key.type)
                    val name = psoClass!!.qualifiedName
                    builder.append("$toName.${field.value.second.name}($name.valueOf($fromName.${field.value.first.name}()));\n")
                }
                PsiUtil.resolveClassInClassTypeOnly(field.value.first.returnType)!!.isEnum -> {
                    builder.append("$toName.${field.value.second.name}($fromName.${field.value.first.name}().name());\n")
                }
                else -> {
                    builder.append("$toName.${field.value.second.name}($fromName.${field.value.first.name}());\n")
                }
            }
            builder.append("}\n")
        }
        return builder.toString()
    }

    init {
        if (fromName == toName) {
            this.toName += "1"
        }
    }
}
