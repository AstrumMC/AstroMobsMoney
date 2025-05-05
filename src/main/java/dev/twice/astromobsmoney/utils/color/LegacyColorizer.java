package dev.twice.astromobsmoney.utils.color;

import dev.twice.astromobsmoney.utils.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LegacyColorizer implements Colorizer {
    private static final Pattern HEX_PATTERN = Pattern.compile("&#([a-fA-F0-9]{6})");
    private static final char COLOR_CHAR = '§';

    @Override
    public String colorize(String message) {
        if (message == null || message.isEmpty()) {
            return message;
        }

        Matcher matcher = HEX_PATTERN.matcher(message);
        StringBuilder builder = new StringBuilder();
        int lastEnd = 0;

        while (matcher.find()) {

            builder.append(message, lastEnd, matcher.start());

            String hexCode = matcher.group(1);

            builder.append(COLOR_CHAR).append('x');
            for (char c : hexCode.toCharArray()) {
                builder.append(COLOR_CHAR).append(c);
            }

            lastEnd = matcher.end();
        }

        builder.append(message.substring(lastEnd));

        String result = builder.toString();
        char[] chars = result.toCharArray();

        for (int i = 0; i < chars.length - 1; i++) {
            if (chars[i] == '&' && "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx".indexOf(chars[i + 1]) > -1) {
                chars[i] = COLOR_CHAR;
                chars[i + 1] = Character.toLowerCase(chars[i + 1]);
            }
        }

        return new String(chars);
    }
}