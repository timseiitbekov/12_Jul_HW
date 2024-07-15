package com.example.a12_jul_hw

import android.content.res.ColorStateList
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var constraintSet: ConstraintSet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val signInButton = findViewById<Button>(R.id.signinButton)

        adjustByKeyboardHeight()
        watchLoginValuesToActivateSignInButton(emailEditText, passwordEditText, signInButton)
        validateLoginCredentialsOnSignInButtonClick(emailEditText, passwordEditText, signInButton)
    }

    private fun openSuccessLoginLayoutWithDelay(errorMessageText: TextView) {
        errorMessageText.text = "Вы успешно зарегестрировались"
        errorMessageText.setTextColor(getColor(R.color.green))
        findViewById<LinearLayout>(R.id.entries).visibility = View.INVISIBLE
        findViewById<LinearLayout>(R.id.someInfoText).visibility = View.INVISIBLE
        findViewById<LinearLayout>(R.id.enterText).visibility = View.INVISIBLE
    }

    private fun validateLoginCredentialsOnSignInButtonClick(
        emailEditText: EditText,
        passwordEditText: EditText,
        signInButton: Button
    ) {
        signInButton.setOnClickListener{
            val errorMessageText = findViewById<TextView>(R.id.errorMessage)
            if (emailEditText.text.toString() != "admin" || passwordEditText.text.toString() != "admin") {
                errorMessageText.text = "Неправильный пароль или логин"
                errorMessageText.setTextColor(getColor(R.color.red))
            } else {
                openSuccessLoginLayoutWithDelay(errorMessageText)
            }
        }
    }

    private fun watchLoginValuesToActivateSignInButton(
        emailEditText: EditText,
        passwordEditText: EditText,
        signInButton: Button
    ) {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.println(Log.INFO, "text-change", "Watcher before text change do nothing")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.println(Log.INFO, "text-change", "Watcher on text change do nothing")
            }

            override fun afterTextChanged(s: Editable?) {
                if (emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()) {
                    signInButton.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            this@MainActivity,
                            R.color.orange
                        )
                    )
                } else {
                    signInButton.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            this@MainActivity,
                            R.color.grey
                        )
                    )
                }
            }
        }
        emailEditText.addTextChangedListener(textWatcher)
        passwordEditText.addTextChangedListener(textWatcher)
    }

    private fun adjustByKeyboardHeight() {
        constraintLayout = findViewById(R.id.main)
        constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)

        constraintLayout.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            constraintLayout.getWindowVisibleDisplayFrame(rect)
            val screenHeight = constraintLayout.rootView.height
            val keypadHeight = screenHeight - rect.bottom

            if (keypadHeight > screenHeight * 0.15) {
                changeVerticalBiasByKeyboardDisplay(0.4f)
            } else {
                changeVerticalBiasByKeyboardDisplay(0.9f)
            }
        }
    }

    private fun changeVerticalBiasByKeyboardDisplay(bias: Float) {
        constraintSet.setVerticalBias(R.id.dataInputContainer, bias)
        constraintSet.applyTo(constraintLayout)
    }
}