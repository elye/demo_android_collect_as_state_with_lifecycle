package com.example.flowlifecycle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.flowlifecycle.ui.theme.FlowLifecycleTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalLifecycleComposeApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlowLifecycleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val stateVariableWithLifecycle = viewModel.counter.collectAsStateWithLifecycle(0)
                    val stateVariableWithoutLifecycle = viewModel.counter.collectAsState(0)
                    val externalStateVariableWithLifecycle = viewModel.externalDataWithLifecycle.collectAsStateWithLifecycle(0)
                    val externalStateVariableWithoutLifecycle = viewModel.externalDataWithoutLifecycle.collectAsState(0)
                    val savedStateVariableWithLifecycle = viewModel.savedStateFlowWithLifecycle.collectAsStateWithLifecycle()
                    val savedStateVariableWithoutLifecycle = viewModel.savedStateFlowWithoutLifecycle.collectAsState()
                    val savedStateVariableWithLifecycleWithStatein = viewModel.savedStateFlowWithLifecycleWithStateIn.collectAsStateWithLifecycle()
                    val savedStateVariableWithoutLifecycleWithStatein = viewModel.savedStateFlowWithoutLifecycleWithStateIn.collectAsState()
                    Greeting("\n" + """
                        Within Flow With LifeCycle: ${stateVariableWithLifecycle.value}
                        Within Flow Without LifeCycle: ${stateVariableWithoutLifecycle.value}
                        Outside Flow With LifeCycle: ${externalStateVariableWithLifecycle.value}
                        Outside Flow Without LifeCycle: ${externalStateVariableWithoutLifecycle.value}
                        Saved Flow With LifeCycle: ${savedStateVariableWithLifecycle.value}
                        Saved Flow Without LifeCycle: ${savedStateVariableWithoutLifecycle.value}
                        Saved Flow With LifeCycle With StateIn: ${savedStateVariableWithLifecycleWithStatein.value}
                        Saved Flow Without LifeCycle With StateIn: ${savedStateVariableWithoutLifecycleWithStatein.value}
                        """.trimIndent() + "\n")

                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FlowLifecycleTheme {
        Greeting("Android")
    }
}


@Composable
fun <T> Flow<T>.myCollectAsStateWithLifecycle(
    initialValue: T,
    lifecycle: Lifecycle = LocalLifecycleOwner.current.lifecycle,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    context: CoroutineContext = EmptyCoroutineContext,
    postExecuteThis: (suspend () -> Unit)? = null
): State<T> {
    return produceState(initialValue, this, lifecycle, minActiveState, context) {
        lifecycle.repeatOnLifecycle(minActiveState) {
            if (context == EmptyCoroutineContext) {
                this@myCollectAsStateWithLifecycle.collect {
                    this@produceState.value = it
                    postExecuteThis?.invoke()
                }
            } else withContext(context) {
                this@myCollectAsStateWithLifecycle.collect {
                    this@produceState.value = it
                    postExecuteThis?.invoke()
                }
            }
        }
    }
}