package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()
    private val viewModel by viewModels<CatsViewModel>(factoryProducer = {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return CatsViewModel(
                    catsFactsService = diContainer.catFactService,
                    catsPicturesService = diContainer.catPictureService
                ) as T
            }
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        viewModel.catsLiveData.observe(this) {
            when (it) {
                is Result.Success<*> -> view.populate(it.data as CatResponse)
                is Result.Error -> view.showToast(it.message)
            }
        }

        view.onButtonClickListener = {
            viewModel.onInitComplete()
        }
    }
}
