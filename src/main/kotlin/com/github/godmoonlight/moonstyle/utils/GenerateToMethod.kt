package com.github.godmoonlight.moonstyle.utils

import com.intellij.psi.PsiPrimitiveType
import com.intellij.psi.util.PsiUtil


class GenerateToMethod(private val mapResult: ClassMapResult) : GenerateMethod(mapResult) {

    override fun generate(): String {

        val toClassName = mapResult.to.name!!
        val content = writeMappedFields() + mapResult.writeNotMappedFields()

        return """public $toClassName to$toClassName() {
                    $toClassName $toName = new $toClassName();
                    $content
                    return $toName;
                }
        """.trimIndent()

    }


    private fun writeMappedFields(): String {
        val builder = StringBuilder()
        for (field in mapResult.mappedFields) {
            builder.append("$toName.${field.value.second.name}(this.${field.key.name});\n")
        }
        for (field in mapResult.mappedConvertibleFields) {
            if (field.value.first.returnType is PsiPrimitiveType) {
                builder.append("$toName.${field.value.second.name}(this.${field.key.name});\n")
                continue
            }
            builder.append("if(this.${field.key.name} != null){\n")
            when {
                PsiUtil.resolveClassInClassTypeOnly(field.key.type)?.isEnum!! -> {
                    val psoClass = PsiUtil.resolveClassInClassTypeOnly(field.key.type)
                    val name = psoClass!!.qualifiedName
                    builder.append("$toName.${field.value.second.name}($name.valueOf(this.${field.key.name}));\n")
                }
                PsiUtil.resolveClassInClassTypeOnly(field.value.first.returnType)!!.isEnum -> {
                    builder.append("$toName.${field.value.second.name}(this.${field.key.name}.name());\n")
                }
                else -> {
                    builder.append("$toName.${field.value.second.name}(this.${field.key.name});\n")
                }
            }
            builder.append("}\n")
        }
        return builder.toString()
    }
}
