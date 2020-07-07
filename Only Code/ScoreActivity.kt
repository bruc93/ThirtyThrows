package com.bignerdranch.android.thirtythrows

import android.annotation.SuppressLint
import android.content.ClipData.newIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.w3c.dom.Text

class ScoreActivity : AppCompatActivity() {
    private lateinit var lowTextView: TextView
    private lateinit var fourTextView: TextView
    private lateinit var fiveTextView: TextView
    private lateinit var sixTextView: TextView
    private lateinit var sevenTextView: TextView
    private lateinit var eightTextView: TextView
    private lateinit var nineTextView: TextView
    private lateinit var tenTextView: TextView
    private lateinit var elevenTextView: TextView
    private lateinit var twelveTextView: TextView
    private lateinit var scoreTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)

        lowTextView = findViewById(R.id.textViewLow)
        fourTextView = findViewById(R.id.textView4)
        fiveTextView = findViewById(R.id.textView5)
        sixTextView = findViewById(R.id.textView6)
        sevenTextView = findViewById(R.id.textView7)
        eightTextView = findViewById(R.id.textView8)
        nineTextView = findViewById(R.id.textView9)
        tenTextView = findViewById(R.id.textView10)
        elevenTextView = findViewById(R.id.textView11)
        twelveTextView = findViewById(R.id.textView12)
        scoreTextView = findViewById(R.id.textViewScore)

        initScore()

    }

    @SuppressLint("SetTextI18n")
    private fun initScore(){
        lowTextView.text = "Low = " + intent.getIntExtra("LOW", -999).toString()
        fourTextView.text = "4 = " + intent.getIntExtra("4", -999).toString()
        fiveTextView.text = "5 = " + intent.getIntExtra("5", -999).toString()
        sixTextView.text = "6 = " + intent.getIntExtra("6", -999).toString()
        sevenTextView.text = "7 = " + intent.getIntExtra("7", -999).toString()
        eightTextView.text = "8 = " + intent.getIntExtra("8", -999).toString()
        nineTextView.text = "9 = " + intent.getIntExtra("9", -999).toString()
        tenTextView.text = "10 = " + intent.getIntExtra("10", -999).toString()
        elevenTextView.text = "11 = " + intent.getIntExtra("11", -999).toString()
        twelveTextView.text = "12 = " + intent.getIntExtra("12", -999).toString()
        scoreTextView.text = "Score: " + intent.getIntExtra("TOTAL_SCORE", -999).toString()
    }
}