package com.example.orereward;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class OreRewardListener implements Listener {
    private final OreRewardPlugin plugin;

    public OreRewardListener(OreRewardPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Material type = block.getType();

        // ✅ 檢查是否在 Residence 領地內且無 "build" 權限
        ClaimedResidence res = Residence.getInstance().getResidenceManager().getByLoc(block.getLocation());
        if (res != null && !res.getPermissions().playerHas(player, "destroy", true)) {
            return;
        }

        ItemStack tool = player.getInventory().getItemInMainHand();
        if (tool != null && tool.containsEnchantment(Enchantment.SILK_TOUCH)) return;

        OreRewardConfig config = plugin.getOreRewardConfig();
        if (!config.getDefinedOres().contains(type)) return;

        player.getScheduler().run(plugin,
            scheduledTask -> {
                double chance = config.getChance(type);
                if (plugin.isDebugEnabled()) {
                    Bukkit.getLogger().info("[SJZ_OreReward] " + player.getName() + " 挖了 " + type + " (機率: " + chance + ")");
                }

                if (Math.random() < chance) {
                    String customNameNbt = "\"{\\\"text\\\":\\\"石頭幣碎片\\\",\\\"italic\\\":false,\\\"bold\\\":true,\\\"color\\\":\\\"gray\\\"}\"";

                    String loreNbt = "[" +
                        "\"{\\\"text\\\":\\\"源自失落遺跡的貨幣\\\",\\\"color\\\":\\\"dark_gray\\\"}\"," +
                        "\"{\\\"text\\\":\\\"仍可在石家庄古商中流通\\\",\\\"color\\\":\\\"gray\\\"}\"," +
                        "\"{\\\"text\\\":\\\"散發著微弱的魔力...\\\",\\\"italic\\\":true,\\\"color\\\":\\\"gray\\\"}\"," +
                        "\"{\\\"text\\\":\\\" \\\"}\"" +
                    "]";

                    String itemModelNbt = "\"ticket_oredrop\"";

                    String command = String.format(
                        "give %s minecraft:phantom_membrane[custom_name=%s,lore=%s,item_model=%s]",
                        player.getName(),
                        customNameNbt,
                        loreNbt,
                        itemModelNbt
                    );

                    Bukkit.getGlobalRegionScheduler().run(plugin, task -> {
                        if (plugin.isDebugEnabled()) {
                            Bukkit.getLogger().info("[SJZ_OreReward] 發送指令: " + command);
                        }
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                    });
                }
            },
            () -> Bukkit.getLogger().warning("[SJZ_OreReward] Fallback for: " + player.getName())
        );
    }
}
