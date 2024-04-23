package com.example.ui;

import java.awt.Color;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public final class UIDefaults {

	public static final Color ACCENT_COLOR = Color.decode("#9333ea");
	public static final DecimalFormat MONETARY_FORMAT = new DecimalFormat("#,###.00 $");
	public static final Border EMPTY_BORDER_INSETS_5 = new EmptyBorder(5, 5, 5, 5);
	public static final DateTimeFormatter COMMON_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

}
