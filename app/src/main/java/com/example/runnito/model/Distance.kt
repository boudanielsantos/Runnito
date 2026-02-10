package com.example.runnito.model

enum class Distance(val displayName: String, val intValue: Int) {
    THREE_KM("3KM", 3),
    FIVE_KM("5KM", 5),
    TEN_KM("10KM", 10),
    TWENTY_ONE_KM("HALF-MARATHON", 21),
    FORTY_TWO_KM("FULL MARATHON", 42);


    companion object {
        fun fromDisplayName(value: String): Distance? {
            return entries.find { it.displayName.equals(value, ignoreCase = true) }
        }
    }
}

