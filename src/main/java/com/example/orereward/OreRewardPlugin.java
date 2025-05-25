package com.example.orereward;

import org.bukkit.plugin.java.JavaPlugin;

public class OreRewardPlugin extends JavaPlugin {
    private static OreRewardPlugin instance;
    private OreRewardConfig config;
    private boolean debugEnabled = false;

    public static OreRewardPlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        reloadOreConfig();
        getServer().getPluginManager().registerEvents(new OreRewardListener(this), this);
        SJZCommandExecutor executor = new SJZCommandExecutor(this);
        getCommand("sjz_orereward").setExecutor(executor);
        getCommand("sjz_orereward").setTabCompleter(executor);
        getLogger().info("SJZ_OreReward enabled.");
    }

    public void reloadOreConfig() {
        reloadConfig(); // 正確載入最新 config.yml
        this.config = new OreRewardConfig(getConfig());
    }

    public OreRewardConfig getOreRewardConfig() {
        return config;
    }

    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    public void setDebugEnabled(boolean enabled) {
        this.debugEnabled = enabled;
    }
}
