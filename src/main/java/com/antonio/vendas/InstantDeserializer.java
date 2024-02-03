package com.antonio.vendas;

import java.time.Instant;

public class InstantDeserializer {

    public static Instant fromString(String value) {
        return Instant.parse(value);
    }
}
