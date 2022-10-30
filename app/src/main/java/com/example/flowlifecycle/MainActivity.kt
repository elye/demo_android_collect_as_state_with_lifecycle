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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.flowlifecycle.ui.theme.FlowLifecycleTheme

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
                    val savedStateVariableWithLifecycle = viewModel.savedStateFlowWithLifecycle.collectAsStateWithLifecycle(0)
                    val savedStateVariableWithoutLifecycle = viewModel.savedStateFlowWithoutLifecycle.collectAsState(0)
                    Greeting("\n" + """
                        Within Flow With LifeCycle: ${stateVariableWithLifecycle.value}
                        Within Flow Without LifeCycle: ${stateVariableWithoutLifecycle.value}
                        Outside Flow With LifeCycle: ${externalStateVariableWithLifecycle.value}
                        Outside Flow Without LifeCycle: ${externalStateVariableWithoutLifecycle.value}
                        Saved Flow With LifeCycle: ${savedStateVariableWithLifecycle.value}
                        Saved Flow Without LifeCycle: ${savedStateVariableWithoutLifecycle.value}
                        """.trimIndent())
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