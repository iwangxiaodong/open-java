package com.openle.our.core;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

/**
 *
 * @author xiaodong
 */
public class CoreDateTime {

    public static OffsetDateTime getOffsetDateTimeByTimeBasedUUID(UUID uuid) {
        return OffsetDateTime.ofInstant(getInstantByTimeBasedUUID(uuid),
                ZoneOffset.systemDefault());
    }

    public static Instant getInstantByTimeBasedUUID(UUID uuid) {
        return Instant.ofEpochMilli((uuid.timestamp() - 122192928000000000L) / 10000);
    }
}
