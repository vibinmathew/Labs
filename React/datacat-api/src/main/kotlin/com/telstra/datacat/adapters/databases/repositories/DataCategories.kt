package com.telstra.datacat.adapters.databases.repositories

import com.fasterxml.jackson.annotation.JsonValue

enum class DataCategories(val text: String, val id: Int, val required: Boolean, val hidden: Boolean) {
    Others("Others", -1, true, false),
    Uncategorised("Uncategorised", 0, true, true),
    Antivirus("Antivirus", 1, true, false),
    Cloud("Cloud Storage & Backup", 2, true, false),
    Email("Email & Messaging", 3, true, false),
    FileSharing("File Sharing", 4, true, false) {
        override fun parentCategory(): DataCategories {
            return General
        }
    },
    Gaming("Gaming", 5, true, false),
    General("General Usage", 6, true, false),
    Maps("Maps & Navigation", 7, true, false),
    Marketing("Marketing & Advertising", 8, true, true),
    Social("Social Media", 9, true, false),
    Streaming("Streaming Video & Audio", 10, true, false),
    SystemUpdates("System & Application Updates", 11, true, false),
    Telstra("Telstra Applications", 12, false, false),
    VOIP("VOIP", 13, true, true);

    @JsonValue
    fun getCategoryText(): String {
        return this.text
    }

    /**
     * Gives an opportunity to group categories
     */
    open fun parentCategory(): DataCategories {
        return this
    }

    companion object {
        fun getById(id: Int): DataCategories?{
            DataCategories.values().forEach {
                if (it.id == id) {
                    return it
                }
            }
            return null
        }
    }
}




