package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.lang.Exception
import java.net.SocketTimeoutException
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class CatsPresenter(
    private val catsFactsService: CatsFactsService,
    private val catsPicturesService: CatsPicturesService
) {

    private val presenterScope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))
    private val _catsLiveData: MutableLiveData<Result> = MutableLiveData<Result>()
    val catsLiveData: LiveData<Result> = _catsLiveData

    suspend fun onInitComplete() {
        presenterScope.launch {
            try {
                val factsResponse = async {
                    catsFactsService.getCatFact()
                }
                val picResponse = async {
                    catsPicturesService.getCatPicture()
                }

                _catsLiveData.value = Result.Success(
                    CatResponse(
                        fact = factsResponse.await(),
                        picture = picResponse.await()
                    )
                )

            } catch (e: Exception) {
                if (e is SocketTimeoutException) {
                    _catsLiveData.value = Result.Error("Не удалось получить ответ от сервера")
                } else {
                    _catsLiveData.value = Result.Error(e.message.orEmpty())
                }
            }
        }
    }

    fun onClear() {
        presenterScope.cancel()
    }
}