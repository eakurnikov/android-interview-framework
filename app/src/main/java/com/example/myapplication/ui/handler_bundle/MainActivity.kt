package com.example.myapplication.ui.handler_bundle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.example.myapplication.R
import java.lang.ref.WeakReference

// What if we need to set the new string securely?
// 1) Not store it in the app code or in the resources. Load from the server via the https.
// 2) Not store it on the device. If store it should be encrypted. For example androidx security lib, encrypted shared preferences or EncryptedFile.

class MainActivity : AppCompatActivity() {

    private val button: Button by lazy { findViewById(R.id.button) }
    private val textView: TextView by lazy { findViewById(R.id.text_view) }

    private val action = Action(WeakReference(this))
    private var pendingMessage: Message? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        savedInstanceState?.apply {
            textView.text = getString(TEXT_KEY)
            pendingMessage = getParcelable(MSG_KEY)
        }
        (pendingMessage?.callback as? Action)?.activity = WeakReference(this)

        button.setOnClickListener {
            val message = Message.obtain(handler, action).apply { what = RUNNABLE_TOKEN }
            this.pendingMessage = message
            handler.apply {
                removeMessages(RUNNABLE_TOKEN)
                sendMessageDelayed(message, TIMEOUT)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(TEXT_KEY, textView.text.toString())
        pendingMessage?.let { outState.putParcelable(MSG_KEY, it) }
        super.onSaveInstanceState(outState)
    }

    class Action(
        var activity: WeakReference<MainActivity>
    ): Runnable {
        override fun run() {
            Log.i("TRATATA", "action")
            activity.get()?.textView?.text = FINAL_TEXT
        }
    }

    companion object {
        const val TEXT_KEY: String = "TEXT_KEY"
        const val MSG_KEY: String = "MSG_KEY"
        const val FINAL_TEXT: String = "Goodbye world"
        const val TIMEOUT: Long = 5_000L
        const val RUNNABLE_TOKEN: Int = 111

        val handler = Handler(Looper.getMainLooper())
    }
}
