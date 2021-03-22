# moon-style

![Build](https://github.com/GodMoonLight/moon-style/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/15331-moon-stylesvg)](https://plugins.jetbrains.com/plugin/15331-moon-style)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/15331-moon-style.svg)](https://plugins.jetbrains.com/plugin/15331-moon-style)

<!-- Plugin description -->
This is a tool set for editor

### Usage:
#### Converter method
- Put the caret in any place within the class, press `Alt+Ins` or `Command + n`.
- select Converter method in menu. 
- In the dialog select the Class you want to convert To and select the class you want to convert From.
- Press Ok and converter method will be added to your current class.
- Plugin also writes in comments list of fields, that were not mapped (appropriate setter or getter is missing or different types).

#### To Json
- Open the java class which you want to generate a json object for
- Click or Right Click the class ,and select `To Json`
- Finally, you can paste the json to anywhere

#### To Yaml
- Open the java class which you want to generate a yaml object for
- Click or Right Click the class ,and select `To Yaml`
- Finally, you can paste the json to anywhere
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
