# YAML API

YAML API is a specialized library that simplifies working with YAML configuration files in Bukkit/Spigot/Paper plugins. It provides a robust and consistent interface for loading, saving, updating, and managing YAML-based configurations, along with advanced mapping capabilities for configuration sections and configurable units.

---

## Overview

The **YAML API** package offers a set of tools designed to handle YAML configuration files efficiently. It abstracts the complexity of file I/O and reflection-based configuration parsing, allowing you to focus on using configuration data in your plugin.

Key components include:

- **YAMLFile**: A class to load, save, and update YAML configuration files.
- **ResourceUtils**: Utility methods for handling resources and file operations.
- **Configurable & ConfigurableFile**: Interfaces and classes that provide easy access to the underlying `FileConfiguration`.
- **ConfigurableUnit**: An interface representing a configuration unit, useful for handling permissions or groups in the configuration.
- **Mappable, SectionMappable, UnitMappable & HashMappable**: A set of interfaces and classes for mapping configuration sections and units into Java collections.
- **YAMLUpdater**: A class that updates YAML files by merging default values and preserving comments.

---

## Key Features

- **Unified Configuration Management**:
  Provides a consistent API for reading, writing, and updating YAML configuration files.

- **Dynamic Mapping and Conversion**:
  Supports mapping configuration sections to custom units and collections, making it easier to work with complex configuration structures.

- **Resource and File Utilities**:
  Includes helper classes to simplify file loading, resource saving, and directory management.

- **Comment Preservation and Updates**:
  YAMLUpdater handles merging of default configuration values while preserving existing comments.

- **Reflection-Based Parsing**:
  Uses reflection to dynamically access configuration sections and values, ensuring compatibility across server versions.

---

## Usage Example

Below is an example demonstrating how to use the YAML API to load, update, and work with configuration files.

### Example: Using ConfigurableFile and YAMLFile

```java
package com.example.myplugin;

import me.croabeast.file.ConfigurableFile;
import me.croabeast.file.YAMLFile;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class MyPlugin extends JavaPlugin {

    private ConfigurableFile config;

    @Override
    public void onEnable() {
        try {
            // Create a new configuration file located in the "config" folder
            config = new ConfigurableFile(this, "config", "settings")
                // Optionally, override methods to control updatability or other behaviors
                {
                    @Override
                    public boolean isUpdatable() {
                        // Retrieve the "update" key from the configuration to decide if updates are allowed
                        return get("update", false);
                    }
                };
            // Save the default configuration if not present
            config.saveDefaults();
            // Update the configuration file (merges defaults and preserves comments)
            config.update();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Access configuration values
        String prefix = config.get("lang-prefix", "&e MyPlugin »&7");
        getLogger().info("Language prefix: " + prefix);
    }
}
```

### Example: Working with Mappable and SectionMappable

```java
package com.example.myplugin;

import me.croabeast.file.SectionMappable;
import me.croabeast.file.ConfigurableFile;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

public class MyPlugin extends JavaPlugin {

    private ConfigurableFile config;

    @Override
    public void onEnable() {
        try {
            config = new ConfigurableFile(this, "config", "settings");
            config.saveDefaults();
            config.update();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Assume we have a configuration section "advancements"
        ConfigurationSection section = config.getConfiguration().getConfigurationSection("advancements");
        if (section != null) {
            // Create a SectionMappable from the configuration section
            SectionMappable.Set sectionMap = SectionMappable.asSet(section.getValues(false));
            // Process the mapped configuration as needed
            getLogger().info("Loaded advancements: " + sectionMap);
        }
    }
}
```

---

## Conclusion

**YAML API** is a powerful library for managing YAML configurations in your Bukkit/Spigot/Paper plugins. It streamlines file operations, mapping, and updates while preserving comments and ensuring compatibility across server versions. Whether you are building simple configuration systems or working with complex, nested settings, YAML API provides the tools you need to efficiently manage your plugin’s configuration.

Happy coding!
— *CroaBeast*
