package com.github.godmoonlight.moonstyle.utils

abstract class GenerateMethod(mapResult: ClassMapResult) {
    protected var toName: String = SuggestionName[mapResult.to]

    abstract fun generate(): String
}
