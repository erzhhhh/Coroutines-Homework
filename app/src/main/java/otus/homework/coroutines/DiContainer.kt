package otus.homework.coroutines

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private val catFactsRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://cat-fact.herokuapp.com/facts/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val catFactService by lazy { catFactsRetrofit.create(CatsFactsService::class.java) }

    private val catPictureRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://aws.random.cat/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val catPictureService by lazy { catPictureRetrofit.create(CatsPicturesService::class.java) }
}