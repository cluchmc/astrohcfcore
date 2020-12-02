package net.frozenorb.foxtrot.util;

import org.bukkit.*;
import java.util.*;

public final class CC
{
    public static final String BLUE;
    public static final String AQUA;
    public static final String YELLOW;
    public static final String RED;
    public static final String GRAY;
    public static final String GOLD;
    public static final String GREEN;
    public static final String WHITE;
    public static final String BLACK;
    public static final String BOLD;
    public static final String ITALIC;
    public static final String STRIKE_THROUGH;
    public static final String RESET;
    public static final String MAGIC;
    public static final String DARK_BLUE;
    public static final String DARK_AQUA;
    public static final String DARK_GRAY;
    public static final String DARK_GREEN;
    public static final String DARK_PURPLE;
    public static final String DARK_RED;
    public static final String LIGHT_PURPLE;
    public static final String CHAT_BAR;
    public static final String MENU_BAR;
    public static final String SB_BAR;
    public static final String CONSOLE_NAME;
    public static final String BLANK_LINE = "§8 §8 §1 §3 §3 §7 §8 §r";
    public static final String prefix = "§4§lAstro §7┃§r";
    public static final String GRAY_LINE = " §7┃ ";

    public static String translate(final String in) {
        return ChatColor.translateAlternateColorCodes('&', in);
    }

    public static List<String> translate(final List<String> lines) {
        final List<String> toReturn = new ArrayList<String>();
        for (final String line : lines) {
            toReturn.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        return toReturn;
    }

    public static List<String> translate(final String[] lines) {
        final List<String> toReturn = new ArrayList<String>();
        for (final String line : lines) {
            if (line != null) {
                toReturn.add(ChatColor.translateAlternateColorCodes('&', line));
            }
        }
        return toReturn;
    }

    static {
        BLUE = ChatColor.BLUE.toString();
        AQUA = ChatColor.AQUA.toString();
        YELLOW = ChatColor.YELLOW.toString();
        RED = ChatColor.RED.toString();
        GRAY = ChatColor.GRAY.toString();
        GOLD = ChatColor.GOLD.toString();
        GREEN = ChatColor.GREEN.toString();
        WHITE = ChatColor.WHITE.toString();
        BLACK = ChatColor.BLACK.toString();
        BOLD = ChatColor.BOLD.toString();
        ITALIC = ChatColor.ITALIC.toString();
        STRIKE_THROUGH = ChatColor.STRIKETHROUGH.toString();
        RESET = ChatColor.RESET.toString();
        MAGIC = ChatColor.MAGIC.toString();
        DARK_BLUE = ChatColor.DARK_BLUE.toString();
        DARK_AQUA = ChatColor.DARK_AQUA.toString();
        DARK_GRAY = ChatColor.DARK_GRAY.toString();
        DARK_GREEN = ChatColor.DARK_GREEN.toString();
        DARK_PURPLE = ChatColor.DARK_PURPLE.toString();
        DARK_RED = ChatColor.DARK_RED.toString();
        LIGHT_PURPLE = ChatColor.LIGHT_PURPLE.toString();
        MENU_BAR = ChatColor.STRIKETHROUGH.toString() + "------------------------";
        CHAT_BAR = ChatColor.STRIKETHROUGH.toString() + "------------------------------------------------";
        SB_BAR = ChatColor.STRIKETHROUGH.toString() + "----------------------";
        CONSOLE_NAME = "§4§lConsole";
    }
}
