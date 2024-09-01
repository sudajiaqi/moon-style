package com.github.sudajiaqi.moonstyle.utils

import com.intellij.psi.PsiPrimitiveType
import com.intellij.psi.util.PsiUtil
import org.jetbrains.annotations.NotNull

class GenerateConverterMethod(private val mapResult: ClassMapResult) : GenerateMethod(mapResult) {
    private val toClassName: String? = mapResult.to.qualifiedName
    private val fromName: String = SuggestionName[mapResult.from]
    private val fromClassName: String? = mapResult.from.qualifiedName

    override fun generate(): String {
        val content = writeMappedFields() + mapResult.writeNotMappedFields()

        return """public static $toClassName to${mapResult.to.name}($fromClassName $fromName) {
                if ($fromName ==null) {
                    return null;
                }
                $toClassName $toName = new $toClassName();
                $content
                return $toName;
            }
        """.trimIndent()

    }

    @NotNull
    private fun writeMappedFields(): String {
        val builder = StringBuilder()

        for (field in mapResult.mappedFields) {
            builder.append("$toName.${field.value.second.name}($fromName.${field.value.first.name}());\n")
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
