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

            val choice = spinner.selectedItemPosition
            /*Toast.makeText(this, "Score  " + spinner.selectedItemPosition.toString()
                , Toast.LENGTH_LONG).show()*/

            countScore(4)

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

    private fun countScore(sum:Int){
        //Sum is the score point we are looking for.
        var tempBank: MutableList<Dice> = mutableListOf<Dice>()
        var count = 0

        for(dice in diceBank)
        {
            tempBank.add(dice)
        }

        count = sumUpTheDices(sum, tempBank)

        Toast.makeText(this, "Score  $count"
       , Toast.LENGTH_LONG).show()

    }
    /*
    private fun sumUpTheDices(valueToReach:Int, tempoBank:MutableList<Dice>): Int{

        var count = 0

        while(tempoBank.isNotEmpty())
            {
                var diceTouse = tempoBank[0]
                tempoBank.removeAt(0)

                var tempBank2: MutableList<Dice> = mutableListOf<Dice>()
                for(die in tempoBank)
                {
                    tempBank2.add(die)
                }
                if(diceTouse.value == valueToReach)
                {
                    count++
                }
                //If combo where found, refresh the bank
                else if (checkMoreCombos(diceTouse.value, valueToReach, tempBank2) == 1)
                {

                    tempoBank.clear()

                    for (die in tempBank2) {
                        tempoBank.add(die)
                    }

                    count++
                }
            }


         return count
    }*/

/*
    private fun checkMoreCombos(currentSum:Int,valueToReach:Int,tempoBank:MutableList<Dice>):Int
    {
        if(currentSum == valueToReach)
        {
            return 1
        }
        if(tempoBank.isNotEmpty())
        {
            //Go through the first iteration
            for(die in tempoBank)
            {
                var diceVal = die.value
                var sum = (diceVal + currentSum)


                if(sum == valueToReach)
                {
                    tempoBank.remove(die)
                    return 1
                }
                else if(sum > valueToReach)
                {
                    sum -= die.value
                }
                else
                {
                    var tempBank2: MutableList<Dice> = mutableListOf<Dice>()

                    for(die2 in tempoBank)
                    {
                        tempBank2.add(die2)
                    }
                    tempBank2.removeAt(0)

                    for(die3 in tempBank2)
                    {
                        if(checkMoreCombos(die3.value+currentSum,valueToReach,tempoBank) == 1)
                        {
                            tempoBank.remove(die3)
                            tempBank2.remove(die3)
                            return 1
                        }
                    }
                    if(checkMoreCombos(sum,valueToReach,tempoBank) == 1)
                    {
                        tempoBank.remove(die)
                        return 1
                    }
                }
            }

        }


        return 0
    }*/
    private fun rollDices()
    {
        for (dice in diceBank)
        {
            if(!dice.clicked)
            {
                dice.value = Random.nextInt(1,7)
                dice.button.setBackgroundResource(whiteDiceBackground[dice.value-1])
            }
        }
    }

    private fun getValues() {
        Toast.makeText(this, "Spinner 1 " + spinner.selectedItem.toString()
            , Toast.LENGTH_LONG).show()
    }
}