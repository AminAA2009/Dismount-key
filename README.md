# Dismount Key

Dismount Key is a lightweight Minecraft mod that adds a dedicated keybind for dismounting from rideable entities and vehicles.

## About

By default, Minecraft uses the **Sneak** key to dismount from an entity you are riding. This mod changes that behavior and provides a separate **Dismount** keybind instead, allowing you to use Sneak without accidentally dismounting.

The Dismount key can be changed like any other Minecraft keybind through the **Controls** menu.

### Dismount Belt

Dismount Belt is an optional extra feature that gives you more control over when you can dismount.

It provides two modes:

#### Normal Mode

In Normal Mode, you have a separate Dismount Belt keybind, which is set to **`B`** by default.

Pressing this key fastens or unfastens the belt. When the belt is fastened, pressing the Dismount key will not dismount you. You must unfasten the belt first.

You can also enable an option to automatically fasten the belt when mounting an entity.

#### Two-Key Mode

In Two-Key Mode, you must hold both the Dismount Belt and Dismount keys at the same time to dismount.

This mode is useful if you want dismounting to require a deliberate two-key combination.

Dismount Belt can be completely disabled through the configuration. When disabled, the mod only provides its main Dismount feature.

---

## Requirements

- **Minecraft:** 1.20.1
- **Mod Loader:** Minecraft Forge
- **Forge:** 47.4.10 or compatible versions
- **Java:** Java 17

## Installation

1. Download the `.jar` file from [Modrinth](https://modrinth.com/mod/dismount-key) or the latest [Release](https://github.com/AminAA2009/Dismount-key/releases) on GitHub.
2. Make sure you have Minecraft Forge 1.20.1 installed.
3. Place the downloaded `.jar` file into your `mods` folder.
4. Launch Minecraft.

This mod is also designed to be used in **Forge 1.20.1 modpacks**.

## Configuration

Dismount Key provides a client-side configuration that allows you to customize the **Dismount** and **Dismount Belt** features.

### Dismount

- **Show Dismount Message** — Controls whether the "Press <key> to Dismount" message is shown when mounting a vehicle.

### Dismount Belt

- **Enabled** — Enables or disables the Dismount Belt feature and its keybind.
- **Mode** — Determines how the Dismount Belt works:
  - **Normal** — Press the Dismount Belt key to fasten or unfasten the belt.
  - **Two-Key** — Hold the Dismount Belt and Dismount keys together to dismount.
- **Auto Belt on Mount** — Automatically fastens the belt when mounting a vehicle. This option only applies to Normal Mode.
- **Show Belt Messages** — Controls whether Dismount Belt status and instruction messages are displayed.

The **Dismount** and **Dismount Belt** keybinds can also be changed through**Minecraft** → **Options** → **Controls**.

## Building from Source

This project uses the standard Minecraft Forge development environment.

To build the project, you will need:

- **Java 17**
- **Git**
- An internet connection to download Gradle and Minecraft/Forge dependencies

After cloning the repository, you can build the project using Gradle. The resulting `.jar` file will be placed in the `build/libs/` directory.

This project does not depend on a specific IDE. You can use your preferred IDE or editor, such as Visual Studio Code, IntelliJ IDEA, or Eclipse.

### For Developers

The project is kept relatively simple and organized into separate components.

Keybind handling, the Dismount Belt system, client/server communication, and Mixins related to the default dismount behavior are separated into their respective files to make the project easier to understand and modify.

## License

This project is licensed under the **MIT License**.

The full license text is available in the [`LICENSE`](https://github.com/AminAA2009/Dismount-key/blob/main/LICENSE) file.

## Disclaimer

Dismount Key is an independent and unofficial project for Minecraft and is not affiliated with or endorsed by Mojang Studios or Minecraft Forge.

This project is provided **as-is** without any guarantee of support, compatibility with all mods, or continued development.
