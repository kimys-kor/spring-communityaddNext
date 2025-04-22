package com.community.api.common.utils.time;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public final class ServerTime {
    public static OffsetDateTime now() {
        return OffsetDateTime.now(ZoneId.of("Asia/Seoul"));
    }
    public static LocalDateTime nowAsLocalDateTime() {
        return now().atZoneSameInstant(ZoneId.of("Europe/Athens")).toLocalDateTime();
    }
}
