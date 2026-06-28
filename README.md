# Pulse Meteor

**A premium PvP client-side Minecraft mod for Fabric 1.21.1**

Pulse Meteor seamlessly blends the visual style and PvP-enhancing features of **Pulse Visuals** with the modular utility system and feature set of **Meteor Client**.

---

## Installation

1. Install **Fabric Loader** for Minecraft 1.21.1
2. Download **Fabric API** for Minecraft 1.21.1
3. Download the latest **Pulse Meteor** release
4. Place both `fabric-api.jar` and `pulsemeteor.jar` into your `mods/` folder
5. Launch the game

### Requirements
- Minecraft 1.21.1
- Fabric Loader >= 0.16.9
- Fabric API >= 0.106.0
- Java 21 or higher

### Optional Dependencies
- **ModMenu** - GUI config screen integration
- **Sodium** - Performance optimization (compatible)
- **Lithium** - Performance optimization (compatible)

---

## Feature Overview

### 🎨 Pulse Visuals Features
| Feature | Description |
|---------|-------------|
| **Hit Indicators** | Crisp damage indicators with directional arrows and pulse effects |
| **Block Overlays** | Neon/pulse animations on PvP blocks (obsidian, beds, crystals) |
| **ViewModel Changer** | Adjust hand/item position, scale, and rotation |
| **NoHurtCam** | Removes camera shake when hit |
| **Custom FOV** | Dynamic FOV based on speed/combat state |
| **Trajectories** | Predict projectile paths (pearls, arrows, eggs) |
| **Custom Nametags** | Colored nametags with health bars above players |
| **Pulse Animations** | Glow effects on crosshair and GUI elements |

### ⚔️ Combat Modules
| Module | Description |
|--------|-------------|
| **KillAura** | Auto-attack nearest entity (configurable range, CPS, rotation) |
| **CrystalAura** | Auto-place and break end crystals |
| **AutoTotem** | Auto-equip totem of undying |
| **BowAim** | Auto-aim bow at target with trajectory prediction |
| **TriggerBot** | Attack when crosshair is over entity |

### 🏃 Movement Modules
| Module | Description |
|--------|-------------|
| **Flight** | Creative, Vanilla, and Packet flight modes |
| **Speed** | Strafe, BHop, and YPort speed modes |
| **Step** | Auto-step up to 3 blocks |
| **NoFall** | Packet/GroundSpoof fall damage negation |
| **Jesus** | Walk on water and lava |
| **Scaffold** | Auto-place blocks beneath you |

### 🛠️ Player Modules
| Module | Description |
|--------|-------------|
| **AutoEat** | Auto-eat when hunger is low |
| **AutoMine** | Auto-mine targeted blocks |
| **AutoFarm** | Auto-harvest and replant crops |
| **ChestAura** | Auto-open and loot nearby chests |

### 👀 Render Modules
| Module | Description |
|--------|-------------|
| **ESP** | Entity highlighting with color customization |
| **XRay** | Show ores through stone |
| **Tracers** | Lines from player to entities |
| **Chams** | Render entities through walls |
| **StorageESP** | Highlight chests, shulkers, barrels |

### 🌐 World Modules
| Module | Description |
|--------|-------------|
| **FreeCam** | Detach camera and fly around |
| **Tunnel** | Auto-mine straight tunnels |

### ⚙️ Utility Modules
| Module | Description |
|--------|-------------|
| **AutoReconnect** | Auto-reconnect on disconnect |
| **AntiAFK** | Random movements to avoid AFK kick |
| **InventoryCleaner** | Auto-sort and throw damaged items |

---

## Controls

| Key | Action |
|-----|--------|
| `Right Shift` | Open ClickGUI |
| `Right Control` | Open HUD Editor |
| `N/A` | Module-specific keybinds (configurable in GUI) |

---

## GUI

Press **Right Shift** to open the **ClickGUI**, a modern, sleek interface with:
- Category-organized module panels
- Toggle modules with left-click
- Open settings with right-click
- Set keybinds with middle-click
- Slider controls for numeric settings
- Mode cycling for multi-option settings

Press **Right Control** to open the **HUD Editor**, which allows:
- Drag-and-drop HUD element positioning
- Right-click to toggle element visibility

---

## Configuration

Config files are stored in `.minecraft/config/pulsemeteor/`:
- `modules.json` - Module settings and toggle states
- `keybinds.json` - Custom keybind assignments

### Profiles
Multiple profiles can be created for different play styles:
- **Anarchy** - All combat modules, bypass settings
- **Vanilla** - Minimal modules, safe settings
- **PvP** - Balanced combat and visual enhancements

---

## Compatibility

| Mod | Status |
|-----|--------|
| Sodium | ✅ Compatible |
| Lithium | ✅ Compatible |
| Phosphor | ✅ Compatible |
| Iris Shaders | ✅ Compatible (with minor visual overlap) |
| OptiFabric | ⚠️ May have rendering conflicts |
| Other utility mods | ✅ Generally compatible |

---

## Building from Source

```bash
# Clone the repository
git clone https://github.com/pulsemeteor/pulsemeteor.git

# Navigate to project directory
cd pulsemeteor

# Build the mod
./gradlew build

# The built JAR will be in build/libs/
```

---

## License

This project is licensed under the MIT License - see the LICENSE file for details.

---

## Credits

Roxitwink: chief creator and scripter
