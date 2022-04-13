package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import kotlinx.coroutines.runBlocking


class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var onButtonClickListener: (() -> Unit)? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            onButtonClickListener?.invoke()
        }
    }

    override fun populate(catResponse: CatResponse) {
        findViewById<TextView>(R.id.fact_textView).text = catResponse.fact.text

        val ivBasicImage: ImageView = findViewById<View>(R.id.picture_imageView) as ImageView
        Picasso.get().load(catResponse.picture.file).into(ivBasicImage)
    }

    override fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {

    fun populate(catResponse: CatResponse)
    fun showToast(message: String)
}