package com.github.sudajiaqi.moonstyle.utils

abstract class GenerateMethod(mapResult: ClassMapResult) {
    protected var toName: String = SuggestionName[mapResult.to]

    abstract fun generate(): String
}
