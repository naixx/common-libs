package common

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.flowOf

class TestViewModel : ViewModel() {
    init {
        flowOf(1).observe { }
        launch { }
    }
}

class TestExceptionViewModel : ViewModel(), CoroutineExceptionHandlerProvider {
    init {
        flowOf(1).observe { }
        launch { }
    }

    override val exceptionHandler: CoroutineExceptionHandler
        get() = TODO("Not yet implemented")
}
