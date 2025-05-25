package com.example.orereward;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class OreRewardConfig {
    private final Map<Material, Double> oreChances = new HashMap<>();

    public OreRewardConfig(FileConfiguration config) {
        if (config.contains("ores")) {
            for (String key : config.getConfigurationSection("ores").getKeys(false)) {
                try {
                    Material material = Material.valueOf(key);
                    double chance = config.getDouble("ores." + key);
                    oreChances.put(material, chance);
                } catch (IllegalArgumentException ignored) {}
            }
        }
    }

    public Set<Material> getDefinedOres() {
        return oreChances.keySet();
    }

    public double getChance(Material material) {
        return oreChances.getOrDefault(material, 0.0);
    }

    public Map<Material, Double> getOreDropChances() {
        return oreChances;
    }

    public Material getFragmentMaterial() {
        return Material.PAPER;
    }

    public String getFragmentName() {
        return "§a石頭幣碎片";
    }
}
