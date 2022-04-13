package otus.homework.coroutines

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface CatsFactsService {

    @GET("random?animal_type=cat")
    suspend fun getCatFact(): Fact
}