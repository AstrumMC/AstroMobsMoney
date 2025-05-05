package dev.twice.astromobsmoney.registry;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.ThreadLocalRandom;

@Getter
@RequiredArgsConstructor
public class ValueRange {
    private final int min;
    private final int max;

    public int getRandom() {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}