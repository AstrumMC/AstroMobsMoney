package dev.twice.astromobsmoney.utils.color;

public class LegacyAdvancedColorizer implements Colorizer {
    private static final char COLOR_CHAR = '§';
    private static final boolean[] HEX_CHARS = new boolean[128];
    private static final boolean[] COLOR_CHARS_FLAGS = new boolean[128];

    static {

        for (char c = '0'; c <= '9'; c++) HEX_CHARS[c] = true;
        for (char c = 'a'; c <= 'f'; c++) HEX_CHARS[c] = true;
        for (char c = 'A'; c <= 'F'; c++) HEX_CHARS[c] = true;

        for (char c = '0'; c <= '9'; c++) COLOR_CHARS_FLAGS[c] = true;
        for (char c = 'a'; c <= 'f'; c++) COLOR_CHARS_FLAGS[c] = true;
        for (char c = 'A'; c <= 'F'; c++) COLOR_CHARS_FLAGS[c] = true;
        for (char c : "rRkKlLmMnNoO".toCharArray()) COLOR_CHARS_FLAGS[c] = true;
    }

    @Override
    public String colorize(String message) {
        if (message == null || message.isEmpty()) {
            return message;
        }

        final StringBuilder builder = new StringBuilder(message.length() + 32);
        final char[] chars = message.toCharArray();
        boolean isColor = false, isHashtag = false;

        for (int i = 0; i < chars.length; ) {
            final char currentChar = chars[i];

            if (isHashtag) {
                isHashtag = false;
                if (i + 6 <= chars.length && isValidHexCode(chars, i, 6)) {

                    builder.append(COLOR_CHAR).append('x');
                    for (int j = 0; j < 6; j++) {
                        builder.append(COLOR_CHAR).append(Character.toLowerCase(chars[i + j]));
                    }
                    i += 6;
                    continue;
                } else {
                    builder.append("&#");
                }
            } else if (isColor) {
                isColor = false;
                if (currentChar == '#') {
                    isHashtag = true;
                    i++;
                    continue;
                }
                if (isValidColorCharacter(currentChar)) {
                    builder.append(COLOR_CHAR).append(Character.toLowerCase(currentChar));
                    i++;
                    continue;
                }
                builder.append('&');
            } else if (currentChar == '&') {
                isColor = true;
                i++;
            } else {
                builder.append(currentChar);
                i++;
            }
        }

        if (isColor) {
            builder.append('&');
        } else if (isHashtag) {
            builder.append("&#");
        }

        return builder.toString();
    }

    private boolean isValidHexCode(char[] chars, int start, int length) {
        int end = Math.min(start + length, chars.length);
        for (int i = start; i < end; i++) {
            if (!isHexChar(chars[i])) {
                return false;
            }
        }
        return end - start == length;
    }

    private boolean isHexChar(char c) {
        return c < 128 && HEX_CHARS[c];
    }

    private boolean isValidColorCharacter(char c) {
        return c < 128 && COLOR_CHARS_FLAGS[c];
    }
}