package com.bignerdranch.android.thirtythrows

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var spinner:Spinner
    private lateinit var throwButton:Button
    private lateinit var scoreButton:Button
    //Dice Buttons
    private lateinit var diceButton1:Button
    private lateinit var diceButton2:Button
    private lateinit var diceButton3:Button
    private lateinit var diceButton4:Button
    private lateinit var diceButton5:Button
    private lateinit var diceButton6:Button

    private lateinit var diceBank:List<Dice>

    private val whiteDiceBackground = arrayOf(R.drawable.white1, R.drawable.white2, R.drawable.white3,R.drawable.white4,R.drawable.white5,R.drawable.white6)
    private val redDiceBackground = arrayOf(R.drawable.red1,R.drawable.red2,R.drawable.red3,R.drawable.red4,R.drawable.red5,R.drawable.red6)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinner = findViewById(R.id.spinner1)
        throwButton = findViewById(R.id.throw_button)
        scoreButton = findViewById(R.id.score_button)

        scoreButton.visibility = View.INVISIBLE
        //get dice buttons
        initButtons()
        //Setting content for spinner
        val adapter = ArrayAdapter.createFromResource(this,R.array.numbers,android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        var numOfThrows = 0
        throwButton.setOnClickListener {
            rollDices()
            numOfThrows++

            if(numOfThrows == 3)
            {
                throwButton.visibility = View.INVISIBLE
                scoreButton.visibility = View.VISIBLE
            }

        }
        scoreButton.setOnClickListener {
            numOfThrows = 0
            throwButton.visibility = View.VISIBLE
            scoreButton.visibility = View.INVISIBLE
        }

        //Saves the dices user press
        saveDice()

    }

    private fun initButtons(){
        diceButton1 = findViewById(R.id.dice_button1)
        diceButton2 = findViewById(R.id.dice_button2)
        diceButton3 = findViewById(R.id.dice_button3)
        diceButton4 = findViewById(R.id.dice_button4)
        diceButton5 = findViewById(R.id.dice_button5)
        diceButton6 = findViewById(R.id.dice_button6)

        diceBank = listOf(
            Dice(1,false,diceButton1),
            Dice(2,false,diceButton2),
            Dice(3,false,diceButton3),
            Dice(4,false,diceButton4),
            Dice(5,false,diceButton5),
            Dice(6,false,diceButton6)
        )
    }

    private fun saveDice(){
        for (dice in diceBank)
            dice.button.setOnClickListener {
                if(!dice.clicked){
                    dice.clicked = true
                    dice.button.setBackgroundResource(redDiceBackground[dice.value-1])
                }
                else
                {
                    dice.clicked = false
                    dice.button.setBackgroundResource(whiteDiceBackground[dice.value-1])
                }

            }
    }

    private fun rollDices()
    {
        for (dice in diceBank)
        {
            if(!dice.clicked)
            {
                dice.value = Random.nextInt(1,6)
                dice.button.setBackgroundResource(whiteDiceBackground[dice.value-1])
            }
        }
    }

    private fun getValues() {
        Toast.makeText(this, "Spinner 1 " + spinner.selectedItem.toString()
            , Toast.LENGTH_LONG).show()
    }
}