
package com.churrascoapp.utils;

import java.util.UUID;

public class UUIDUtil {
    /**
     * Gera um UUID aleatório
     * @return UUID aleatório
     */
    public static UUID randomId() {
        return UUID.randomUUID();
    }
    
    /**
     * Gera um UUID aleatório como String (para compatibilidade)
     * @return UUID aleatório em formato String
     */
    public static String randomIdString() {
        return UUID.randomUUID().toString();
    }
}
