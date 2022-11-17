package com.example.myapplication.ui.coroutines_viewmodel

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.R
import com.example.myapplication.ui.handler_bundle.MainActivity
import kotlinx.coroutines.*
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class MainActivityLiveData : AppCompatActivity() {

    private val button: Button by lazy { findViewById(R.id.button) }
    private val textView: TextView by lazy { findViewById(R.id.text_view) }

    private lateinit var viewModel: MainViewModelLiveData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[MainViewModelLiveData::class.java]

        textView.text = viewModel.textLiveData.value

        try {
            viewModel.textLiveData.observe(this) { textView.text = it }
        } catch (e: Throwable) {
            textView.text = "error"
        }

        button.setOnClickListener {
            viewModel.onButtonClick()
        }
    }
}

private class MainViewModelLiveData : ViewModel() {
    private var inTerminalState: Boolean = false
    private var textChangeJob: Job? = null
    private val textMutableLiveData: MutableLiveData<String> = MutableLiveData("init text")

    val textLiveData: LiveData<String> = textMutableLiveData

    fun onButtonClick() {
        textChangeJob?.cancel()
        if (!inTerminalState) {
            textChangeJob = viewModelScope.launch {
                delay(MainActivity.TIMEOUT.toDuration(DurationUnit.MILLISECONDS))

                Log.i("TRATATA", "onButtonClick")
                textMutableLiveData.value = "final text"
                inTerminalState = true
            }
        }
    }

    override fun onCleared() {
        textChangeJob?.cancel()
        textChangeJob = null
    }
}
