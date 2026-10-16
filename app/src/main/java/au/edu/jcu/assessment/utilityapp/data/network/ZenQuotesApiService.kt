// File: app/src/main/java/au/edu/jcu/assessment/utilityapp/data/network/ZenQuotesApiService.kt
package au.edu.jcu.assessment.utilityapp.data.network

import au.edu.jcu.assessment.utilityapp.data.model.Quote
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

/**
 * Retrofit interface defining the endpoint for the free ZenQuotes API.
 */
interface ZenQuotesApiService {
    // Returns the date-locked Quote of the Day
    @GET("today")
    suspend fun getDailyQuote(): List<Quote>

    // Returns a new random quote each time
    @GET("random")
    suspend fun getRandomQuote(): List<Quote>
}

/**
 * Singleton object providing the configured Retrofit API service instance.
 */
object RetrofitInstance {
    private const val BASE_URL = "https://zenquotes.io/api/"

    val api: ZenQuotesApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ZenQuotesApiService::class.java)
    }
}
