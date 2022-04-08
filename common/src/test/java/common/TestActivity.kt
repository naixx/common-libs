package common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.flowOf

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MutableLiveData<Int>().observe()
        flowOf(1).observe()
        launch { }
    }
}

class TestExActivity() : AppCompatActivity(), CoroutineExceptionHandlerProvider {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MutableLiveData<Int>().observe()
        flowOf(1).observe()
        launch { }
    }

    override val exceptionHandler: CoroutineExceptionHandler
        get() = TODO("Not yet implemented")
}
