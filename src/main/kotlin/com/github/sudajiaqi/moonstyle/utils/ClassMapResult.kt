package com.github.sudajiaqi.moonstyle.utils

import com.github.sudajiaqi.moonstyle.utils.ProjectUtil.getProjectIndentation
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiField
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiModifier
import com.intellij.psi.util.PropertyUtil
import com.intellij.psi.util.PsiUtil
import java.util.*

class ClassMapResult(var from: PsiClass, var to: PsiClass) {

    private var useInherited: Boolean = false

    private var autoConvert: Boolean = true

    val mappedFields: LinkedHashMap<PsiField, Pair<PsiMethod, PsiMethod>> = LinkedHashMap()

    val mappedConvertibleFields: LinkedHashMap<PsiField, Pair<PsiMethod, PsiMethod>> =
        LinkedHashMap()

    private val notMappedToFields: MutableList<String> = LinkedList()

    private val notMappedFromFields: MutableList<String> = LinkedList()

    private fun addMappedField(field: PsiField, getter: PsiMethod, setter: PsiMethod) {
        this.mappedFields[field] = Pair(getter, setter)
    }

    private fun processToFields() {
        for (toField in getFields(this.to, useInherited)) {
            val fieldName = toField.name
            val toSetter = findSetter(toField)
            val fromGetter = findGetter(this.from, fieldName, useInherited)
            when {
                toSetter == null || fromGetter == null -> {
                    this.notMappedToFields.add(fieldName)
                }
                isMatchingFieldType(toField, fromGetter) -> {
                    this.addMappedField(toField, fromGetter, toSetter)
                }
                canConvert(toField, fromGetter) -> {
                    this.mappedConvertibleFields[toField] = Pair(fromGetter, toSetter)
                }
                else -> {
                    this.notMappedToFields.add(fieldName)
                }
            }
        }
    }

    private fun processFromFields() {
        val names1 = this.mappedFields.keys.map { it.name }
        val names2 = this.mappedConvertibleFields.keys.map { it.name }
        for (fromField in getFields(from, useInherited)) {
            val fromFieldName = fromField.name
            if (!names1.contains(fromFieldName) &&
                !names2.contains(fromFieldName)
            ) {
                this.notMappedFromFields.add(fromFieldName)
            }
        }
    }

    companion object {

        fun from(to: PsiClass, from: PsiClass, inherited: Boolean): ClassMapResult {
            val result = ClassMapResult(from, to)
            result.useInherited = inherited
            result.processToFields()
            result.processFromFields()
            return result
        }

        private fun getFields(clazz: PsiClass, useInherited: Boolean): List<PsiField> {
            return if (useInherited) {
                clazz.allFields
            } else {
                clazz.fields
            }.filter { !it.hasModifierProperty(PsiModifier.STATIC) }
        }

        private fun findSetter(psiClass: PsiClass, field: String, inherited: Boolean): PsiMethod? {
            val name = "set" + field.substring(0, 1).toUpperCase() + field.substring(1)
            val setters = psiClass.findMethodsByName(name, inherited)
            return if (setters.size == 1) {
                setters[0]
            } else null
        }

        private fun findGetter(psiField: PsiField): PsiMethod? {
            return PropertyUtil.findGetterForField(psiField)
        }

        private fun findSetter(psiField: PsiField): PsiMethod? {
            return PropertyUtil.findSetterForField(psiField)
        }

        private fun findGetter(psiClass: PsiClass, field: String, inherited: Boolean): PsiMethod? {
            return PropertyUtil.findPropertyGetter(psiClass, field, false, inherited)
        }
    }

    private fun isMatchingFieldType(toField: PsiField, fromGetter: PsiMethod): Boolean {
        val fromType = fromGetter.returnType
        val toType = toField.type
        if (fromType != null && toType.isAssignableFrom(fromType)) {
            return true
        }
        return false
    }

    private fun canConvert(toField: PsiField, fromGetter: PsiMethod): Boolean {
        val fromType = fromGetter.returnType
        val toType = toField.type
        var result = false
        if (fromType != null) {
            when {
                fromType.isConvertibleFrom(fromType) -> {
                    result = true
                }
                autoConvert -> {
                    val a = PsiUtil.resolveClassInClassTypeOnly(toType)!!.isEnum
                    result = a || PsiUtil.resolveClassInClassTypeOnly(fromType)!!.isEnum
                }
            }
        }
        return result
    }

    private fun writeNotMappedFields(notMappedFields: List<String>, psiClass: PsiClass?): String {
        if (notMappedFields.isEmpty()) {
            return ""
        }
        val indentation = getProjectIndentation(psiClass!!)
        val builder = StringBuilder()

        builder.append("\n $indentation// Not mapped ${psiClass.name} fields: \n ")
        for (notMappedField in notMappedFields) {
            builder.append("$indentation// $notMappedField\n")
        }

        return builder.toString()
    }

    fun writeNotMappedFields(): String {
        val a = writeNotMappedFields(notMappedFromFields, from)
        val b = writeNotMappedFields(notMappedToFields, to)
        return "$a$b"
    }
}
