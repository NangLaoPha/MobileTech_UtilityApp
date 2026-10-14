// File: app/src/main/java/au/edu/jcu/assessment/utilityapp/data/model/Quote.kt
package au.edu.jcu.assessment.utilityapp.data.model

import com.google.gson.annotations.SerializedName

/**
 * Data class representing a daily quote fetched from the ZenQuotes API.
 * Maps API fields "q" (quote text) and "a" (author name).
 */
data class Quote(
    @SerializedName("q")
    val text: String = "",

    @SerializedName("a")
    val author: String = ""
)
