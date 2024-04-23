package com.example.core;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public final class DateUtil {

	public static LocalDateTime asEndLocalDateTime(LocalDate localDate) {
		return LocalDateTime.of(localDate, LocalTime.of(23, 59, 59));
	}

	public static LocalDateTime asStartLocalDateTime(LocalDate localDate) {
		return LocalDateTime.of(localDate, LocalTime.of(0, 0, 0));
	}
}
