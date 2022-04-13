package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import java.lang.Exception
import java.lang.RuntimeException
import java.net.SocketTimeoutException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CatsViewModel(
    private val catsFactsService: CatsFactsService,
    private val catsPicturesService: CatsPicturesService
) : ViewModel() {

    private val _catsLiveData: MutableLiveData<Result> = MutableLiveData<Result>()
    val catsLiveData: LiveData<Result> = _catsLiveData

    init {
        onInitComplete()
    }

    // при проверке ошибок почему-то только в первый раз отдает корректную ошибку, затем "Parent job is cancelling"
    fun onInitComplete() {
        viewModelScope.launch(CoroutineExceptionHandler { coroutineContext, throwable ->
            CrashMonitor.trackWarning()
        }) {
            try {
                val factsResponse = async(Dispatchers.IO) {
                    catsFactsService.getCatFact()
                }
                val picResponse = async(Dispatchers.IO) {
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
}