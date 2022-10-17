package com.practice.blackfridayanimation.models

data class AchievementTicket(
    val id: String,
    val type: Type,
    val description: String,
    val status: Status,
    val quantity: Int,
    val assetUrl: String? = null
) {
    enum class Type(private val value: String?) {
        ACHIEVEMENT_TICKET_TYPE_INVALID("ACHIEVEMENT_TICKET_TYPE_INVALID"),
        A("A"),
        B("B"),
        C("C"),
        D("D"),
        E("E"),
        F("F"),
        G("G"),
        H("H"),
        I("I");

        companion object {
            fun of(value: String): Type {
                return values().firstOrNull { it.value == value } ?: ACHIEVEMENT_TICKET_TYPE_INVALID
            }
        }
    }

    enum class Status(val value: String) {
        ACHIEVEMENT_TICKET_STATUS_INVALID("ACHIEVEMENT_TICKET_STATUS_INVALID"),
        INACTIVE("INACTIVE"),
        ACTIVE("ACTIVE"),
        USED("USED");

        companion object {
            fun of(value: String): Status {
                return Status.values().firstOrNull { it.value == value }
                    ?: ACHIEVEMENT_TICKET_STATUS_INVALID
            }
        }
    }
}
