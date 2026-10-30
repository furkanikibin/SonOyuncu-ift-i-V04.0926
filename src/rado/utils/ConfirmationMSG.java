package rado.utils;

import org.bukkit.entity.Player;

public class ConfirmationMSG {

    public static final String PREFIX = "§6TN §7§l> §r";

    public static void sendSuccess(Player player) {
        player.sendMessage(PREFIX + "§aBaşarıyla satın aldın.");
    }

    public static void sendNoMoney(Player player) {
        player.sendMessage(PREFIX + "§4Bunun ücretini karşılamıyorsun.");
    }

    public static void sendCancel(Player player) {
    }

    public static void sendAlreadyActive(Player player) {
        player.sendMessage(PREFIX + "§4Bu bölgede zaten bir çiftçi var");
    }
}