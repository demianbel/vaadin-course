package com.gpsolutions.vaadincourse;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

public class DateUtil {
    public static Date getDate(final LocalDate date) {
        return Optional.ofNullable(date).map(d -> Date.from(d.atStartOfDay()
                                                                    .atZone(ZoneId.systemDefault())
                                                                    .toInstant())).orElse(null);
    }

    public static LocalDate getLocalDate(final Date date) {
        return Optional.ofNullable(date).map(d -> d.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()).orElse(null);
    }
}
