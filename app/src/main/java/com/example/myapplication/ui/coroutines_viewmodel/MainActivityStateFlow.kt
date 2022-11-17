package com.example.myapplication.ui.coroutines_viewmodel

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.example.myapplication.R
import com.example.myapplication.ui.handler_bundle.MainActivity
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class MainActivity : AppCompatActivity() {

    private val button: Button by lazy { findViewById(R.id.button) }
    private val textView: TextView by lazy { findViewById(R.id.text_view) }

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        CoroutineScope(Dispatchers.Main).launch {
            try {
                viewModel.textFlow.collect { textView.text = it }
            } catch (e: Throwable) {
                textView.text = "error"
            }
        }

        button.setOnClickListener {
            viewModel.onButtonClick()
        }
    }
}

private class MainViewModel : ViewModel() {
    private var inTerminalState: Boolean = false
    private var textChangeJob: Job? = null
    private val textMutableFlow: MutableStateFlow<String> = MutableStateFlow("init text")

    val textFlow: Flow<String> = textMutableFlow.asStateFlow()
    //  .flowOn(dispatcherProvider.default())

    fun onButtonClick() {
        textChangeJob?.cancel()
        if (!inTerminalState) {
            textChangeJob = viewModelScope.launch {
                delay(MainActivity.TIMEOUT.toDuration(DurationUnit.MILLISECONDS))

                Log.i("TRATATA", "onButtonClick")
                textMutableFlow.value = "final text"
                inTerminalState = true
            }
        }
    }

    override fun onCleared() {
        textChangeJob?.cancel()
        textChangeJob = null
    }
}
