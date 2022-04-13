package otus.homework.coroutines

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface CatsPicturesService {

    @GET("meow")
    suspend fun getCatPicture(): Picture
}