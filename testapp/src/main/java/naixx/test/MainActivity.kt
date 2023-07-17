package naixx.test

import android.app.Activity
import android.os.Bundle
import com.github.naixx.trees.CrashlyticsTree
import timber.log.L

class MainActivity:Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // L.plant(CrashlyticsTree())
        L.plant(L.DebugTree())

        L.e("qwe")
    }
}
