package dev.twice.astromobsmoney.utils;

import dev.twice.astromobsmoney.utils.color.*;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static final Pattern HEX_PATTERN = Pattern.compile("&#([0-9a-fA-F]{6})");
    private static final char COLOR_CHAR = ChatColor.COLOR_CHAR;
    private static Colorizer colorizer = new LegacyColorizer(); // Default

    public static void setupColorizer(ConfigurationSection config) {
        if (config == null) return;

        String type = config.getString("serializer", "LEGACY").toUpperCase(Locale.ENGLISH);
        switch (type) {
            case "MINIMESSAGE":
                colorizer = new MiniMessageColorizer();
                break;
            case "LEGACY_ADVANCED":
                colorizer = new LegacyAdvancedColorizer();
                break;
            case "LEGACY":
                colorizer = new LegacyColorizer();
                break;
            default:
                colorizer = new VanillaColorizer();
                break;
        }
    }

    public static String colorize(String text) {
        if (text == null || text.isEmpty()) return text;
        return colorizer.colorize(text);
    }

    public static String translateAlternateColorCodes(char altColorChar, String text) {
        if (text == null || text.isEmpty()) return text;

        char[] chars = text.toCharArray();
        for (int i = 0; i < chars.length - 1; i++) {
            if (chars[i] == altColorChar && "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx".indexOf(chars[i + 1]) > -1) {
                chars[i] = COLOR_CHAR;
                chars[i + 1] = Character.toLowerCase(chars[i + 1]);
            }
        }
        return new String(chars);
    }
}