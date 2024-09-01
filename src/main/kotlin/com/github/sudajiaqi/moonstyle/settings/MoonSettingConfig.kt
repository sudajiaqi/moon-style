package com.github.sudajiaqi.moonstyle.settings

data class ToJsonConfig(
    var comment: Boolean = false,
    var randomValue: Boolean = false,
    var enumValues: Boolean = false
)

data class ToYamlConfig(var randomValue: Boolean = false, var enumValues: Boolean = false)

enum class Type {
    JSON, YAML
}

data class TestData(
    var type: Type = Type.YAML,
    var connection: String,
    var camelCase: Boolean = true
)

data class MoonSettingConfig(
    var toJsonConfig: ToJsonConfig = ToJsonConfig(),
    var toYamlConfig: ToYamlConfig = ToYamlConfig()
)
