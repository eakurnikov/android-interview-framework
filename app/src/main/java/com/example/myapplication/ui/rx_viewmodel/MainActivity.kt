package com.example.myapplication.ui.rx_viewmodel

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.example.myapplication.R
import com.example.myapplication.ui.handler_bundle.MainActivity.Companion.TIMEOUT
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

private class MainActivity : AppCompatActivity() {

    private val button: Button by lazy { findViewById(R.id.button) }
    private val textView: TextView by lazy { findViewById(R.id.text_view) }

    private lateinit var viewModel: MainViewModel
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        disposable = viewModel.textSubject.subscribe(
            { text: String ->
                textView.text = text
            },
            { error: Throwable ->
                textView.text = "error"
            }
        )

        button.setOnClickListener {
            viewModel.onButtonClickWithDelay()
        }
    }
}

// Will he get the init value?
// Error handling
// Not scheduling actions if the state is terminal
// unsubscribing

// Test cases:
// 1) One click - delay - text changed. No changes after more clicks
// 2) Multiple clicks during the 5+ sec - delay 5 sec after the last click - text changed. No changes after more clicks
// 3) One click - rotation - text changed after 5 sec after last click not after the rotation. No changes after more clicks
// 4) One click - rotation - multiple clicks - text changed after 5 sec after last click not after the rotation. No changes after more clicks
class MainViewModel : ViewModel() {
    private var inTerminalState: Boolean = false
    private var disposable: Disposable? = null
    val textSubject: BehaviorSubject<String> = BehaviorSubject.createDefault("init text")

    fun onButtonClickWithDelay() {
        if (!inTerminalState) {
            disposable?.dispose()

            disposable = Completable.complete()
                .delay(TIMEOUT, TimeUnit.MILLISECONDS, Schedulers.computation())
                .subscribe {
                    Log.i("TRATATA", "onButtonClickWithDelay")
                    textSubject.onNext("final text")
                    inTerminalState = true
                }
        }
    }

    fun onButtonClickWithTimer() {
        if (!inTerminalState) {
            disposable?.dispose()

            disposable = Observable
                .timer(TIMEOUT, TimeUnit.MILLISECONDS, Schedulers.computation())
                .subscribe {
                    Log.i("TRATATA", "onButtonClickWithTimer")
                    textSubject.onNext("final text")
                    inTerminalState = true
                }
        }
    }

    override fun onCleared() {
        disposable?.dispose()
        disposable = null
    }
}
