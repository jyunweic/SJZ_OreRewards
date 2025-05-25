package com.example.orereward;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SJZCommandExecutor implements CommandExecutor, TabCompleter {
    private final OreRewardPlugin plugin;

    public SJZCommandExecutor(OreRewardPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("sjzore.admin")) {
            sender.sendMessage(ChatColor.RED + "你沒有權限執行這個指令！");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.GOLD + "✦ 指令用法：/sjz_orereward <子指令>");
            sender.sendMessage(ChatColor.YELLOW + "  ➤ reload：" + ChatColor.GRAY + "重新載入設定");
            sender.sendMessage(ChatColor.YELLOW + "  ➤ list：" + ChatColor.GRAY + "列出支援礦物與機率");
            sender.sendMessage(ChatColor.YELLOW + "  ➤ debug：" + ChatColor.GRAY + "開關後台紀錄");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                plugin.reloadOreConfig();
                sender.sendMessage(ChatColor.GREEN + "✅ SJZ_OreReward 設定已重新載入！");
                break;
            case "list":
                sender.sendMessage(ChatColor.AQUA + "📦 支援的礦物與掉落機率：");
                for (Material mat : plugin.getOreRewardConfig().getDefinedOres()) {
                    double chance = plugin.getOreRewardConfig().getChance(mat);
                    sender.sendMessage(ChatColor.GRAY + " - " + ChatColor.YELLOW + mat.name()
                            + ChatColor.GRAY + "：掉落機率 " + ChatColor.GOLD + (chance * 100) + "%");
                }
                break;
            case "debug":
                boolean current = plugin.isDebugEnabled();
                plugin.setDebugEnabled(!current);
                sender.sendMessage(ChatColor.GREEN + "📋 後台紀錄已" + (current ? "關閉" : "開啟"));
                break;
            default:
                sender.sendMessage(ChatColor.RED + "未知的子指令！");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!sender.hasPermission("sjzore.admin")) return Collections.emptyList();
        if (args.length == 1) {
            return Arrays.asList("reload", "list", "debug").stream()
                    .filter(cmd -> cmd.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
