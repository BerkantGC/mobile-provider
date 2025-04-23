package com.mobileprovider.api.enumaration;

public enum Month {
    JANUARY(1),
    FEBRUARY(2),
    MARCH(3),
    APRIL(4),
    MAY(5),
    JUNE(6),
    JULY(7),
    AUGUST(8),
    SEPTEMBER(9),
    OCTOBER(10),
    NOVEMBER(11),
    DECEMBER(12);

    private final int monthNumber;

    // Constructor
    Month(int monthNumber) {
        this.monthNumber = monthNumber;
    }

    public int getMonthNumber() {
        return monthNumber;
    }

    // (Optional), method to get the Month by its number
    public static Month fromInt(int i) {
        for (Month month : Month.values()) {
            if (month.getMonthNumber() == i) {
                return month;
            }
        }
        throw new IllegalArgumentException("Invalid month number: " + i);
    }
}
