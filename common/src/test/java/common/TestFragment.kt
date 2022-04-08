package common

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.flowOf

class TestFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MutableLiveData<Int>().observe()
        MutableLiveData<Int>().observeOnce()
        flowOf(1).observe()
        launch { }
    }
}

class TestExFragment : Fragment(), CoroutineExceptionHandlerProvider {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MutableLiveData<Int>().observe()
        MutableLiveData<Int>().observeOnce()
        flowOf(1).observe()
        launch { }
    }

    override val exceptionHandler: CoroutineExceptionHandler
        get() = TODO("Not yet implemented")
}
