# moon-style

![Build](https://github.com/GodMoonLight/moon-style/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/15331-moon-stylesvg)](https://plugins.jetbrains.com/plugin/15331-moon-style)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/15331-moon-style.svg)](https://plugins.jetbrains.com/plugin/15331-moon-style)

<!-- Plugin description -->
This is a tool set for editor

### Usage:
#### Converter method

1. Put the caret in any place within the class, press **Alt+Ins** or **Command + n**.
2. select **Converter method** in menu or use the shortcut **Ctrl+Alt+G**.
   3.In the dialog select the Class you want to convert <b>To</b> and select the class you want to convert **From**.
4. Press **Ok** and converter method will be added to your current class.
5. Plugin also writes in comments list of fields, that were not mapped (appropriate setter or getter is missing or different types).


#### Converter to method
1. Put the caret in any place within the class, press **Alt+Ins** or **Command + n**.
2. select **Converter to method** in menu.
3. In the dialog select the Class you want to convert **To**.
4. Press **Ok** and converter method will be added to your current class.
5. Plugin also writes in comments list of fields, that were not mapped (appropriate setter or getter is missing or different types).


#### Converter from method
1. Put the caret in any place within the class, press **Alt+Ins** or **Command + n**
2. select **Converter from method** in menu.
3. In the dialog select the Class you want to convert **From**.
4. Press **Ok** and converter method will be added to your current class.
5. Plugin also writes in comments list of fields, that were not mapped (appropriate setter or getter is missing or different types).

#### To Json
1. Open the java class which you want to generate a json object for
2. Click or Right Click the class ,and select `To Json`
3. Finally, you can paste the json to anywhere

#### To Yaml
1. Open the java class which you want to generate a yaml object for
2. Click or Right Click the class ,and select `To Yaml`
3. Finally, you can paste the json to anywhere

<!-- Plugin description end -->

## Installation

- Using IDE built-in plugin system:
  
  <kbd>Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "moon-style"</kbd> >
  <kbd>Install Plugin</kbd>
  
- Manually:

  Download the [latest release](https://github.com/GodMoonLight/moon-style/releases/latest) and install it manually using
  <kbd>Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
