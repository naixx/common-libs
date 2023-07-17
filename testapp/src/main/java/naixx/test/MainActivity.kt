package naixx.test

import android.app.Activity
import android.os.Bundle
import com.github.naixx.L
import com.github.naixx.trees.ConsoleTree
import com.github.naixx.trees.CrashlyticsTree
import timber.log.Timber

class MainActivity:Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(CrashlyticsTree())

        L.e("qwe")
    }
}
