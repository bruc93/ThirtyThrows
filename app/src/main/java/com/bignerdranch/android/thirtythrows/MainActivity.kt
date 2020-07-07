package com.bignerdranch.android.thirtythrows
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import java.time.Duration
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    //------------------------ DATA ---------------------------------
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

    private lateinit var diceButtonBank:List<Button>

    private val whiteDiceBackground = arrayOf(R.drawable.white1, R.drawable.white2, R.drawable.white3,R.drawable.white4,R.drawable.white5,R.drawable.white6)
    private val redDiceBackground = arrayOf(R.drawable.red1,R.drawable.red2,R.drawable.red3,R.drawable.red4,R.drawable.red5,R.drawable.red6)
    private lateinit var adapter:ArrayAdapter<*>

    //------------------------ DATA ---------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        val toast = Toast.makeText(applicationContext,"", Toast.LENGTH_SHORT)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var thirtyThrowViewModel = ViewModelProvider(this).get(ThirtyThrowViewModel::class.java)

        spinner = findViewById(R.id.spinner1)
        throwButton = findViewById(R.id.throw_button)
        scoreButton = findViewById(R.id.score_button)

        if(thirtyThrowViewModel.getNumOfThrows() != 3)
        {
            scoreButton.visibility = View.INVISIBLE
            throwButton.visibility = View.VISIBLE

        }
        else
        {
            scoreButton.visibility = View.VISIBLE
            throwButton.visibility = View.INVISIBLE
        }

        initButtons(thirtyThrowViewModel)

        //Setting content for spinner
        var numberList = resources.getStringArray(R.array.numbers)

        thirtyThrowViewModel.initListChoice(numberList)


        adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,thirtyThrowViewModel.getChoiceOfList())

        spinner.adapter = adapter


        throwButton.setOnClickListener {
            rollDices(thirtyThrowViewModel)
            if(thirtyThrowViewModel.getNumOfThrows() == 3)
            {
                thirtyThrowViewModel.unclickAllDices()
                for((index, dice) in diceButtonBank.withIndex())
                {
                    dice.setBackgroundResource(whiteDiceBackground[thirtyThrowViewModel.getDiceValue(index)-1])
                }
                showScoreButton()
                toast.setText("Pick the dice/dices to be paired")
                toast.show()
            }

        }

        scoreButton.setOnClickListener{

            if(!thirtyThrowViewModel.calculatingScore(spinner.selectedItemPosition))
            {
                endGame(thirtyThrowViewModel)
                for((index,dice) in diceButtonBank.withIndex()){  // TO-DO <-- fix correct
                    if(thirtyThrowViewModel.getDiceValue(index) == 0)
                    {
                        dice.visibility = View.INVISIBLE
                    }
                }
            }
            else
            {
                for(dice in diceButtonBank)
                {
                    dice.visibility = View.VISIBLE
                }

                rollDices(thirtyThrowViewModel)

                toast.setText("Total Score  " + thirtyThrowViewModel.getTotalScore())
                toast.show()

                throwButton.visibility = View.VISIBLE
                scoreButton.visibility = View.INVISIBLE

                adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,thirtyThrowViewModel.getChoiceOfList())

                spinner.adapter = adapter

                endGame(thirtyThrowViewModel)
            }
            //Lock in the choice
        }
        //Saves the dices user press
        for ((index,dice) in diceButtonBank.withIndex())
            dice.setOnClickListener {
                if(thirtyThrowViewModel.clickADice(index))
                {
                    dice.setBackgroundResource(redDiceBackground[thirtyThrowViewModel.getDiceValue(index)-1])
                }
                else
                {
                    dice.setBackgroundResource(whiteDiceBackground[thirtyThrowViewModel.getDiceValue(index)-1])
                }
            }
        //saveDice()

    }

    private fun showScoreButton()
    {
        throwButton.visibility = View.INVISIBLE
        scoreButton.visibility = View.VISIBLE

    }



    private fun endGame(thirtyViewMod:ThirtyThrowViewModel)
    {
        var endOfGame = thirtyViewMod.getIfAllValuesUsed()

        if(endOfGame || thirtyViewMod.getRoundsDone() >= 10)
        {
            val intent = Intent(this, ScoreActivity::class.java)
                intent.putExtra("LOW", thirtyViewMod.getScoreFromBank(0))
                intent.putExtra("4", thirtyViewMod.getScoreFromBank(1))
                intent.putExtra("5", thirtyViewMod.getScoreFromBank(2))
                intent.putExtra("6", thirtyViewMod.getScoreFromBank(3))
                intent.putExtra("7", thirtyViewMod.getScoreFromBank(4))
                intent.putExtra("8", thirtyViewMod.getScoreFromBank(5))
                intent.putExtra("9", thirtyViewMod.getScoreFromBank(6))
                intent.putExtra("10", thirtyViewMod.getScoreFromBank(7))
                intent.putExtra("11", thirtyViewMod.getScoreFromBank(8))
                intent.putExtra("12", thirtyViewMod.getScoreFromBank(9))
                var scoreSend = thirtyViewMod.getTotalScore()
                intent.putExtra("TOTAL_SCORE", scoreSend)

            startActivity(intent)

            thirtyViewMod.resetGame()
        }
    }


    private fun initButtons(thirtyViewMod: ThirtyThrowViewModel)
    {
        diceButton1 = findViewById(R.id.dice_button1)
        diceButton2 = findViewById(R.id.dice_button2)
        diceButton3 = findViewById(R.id.dice_button3)
        diceButton4 = findViewById(R.id.dice_button4)
        diceButton5 = findViewById(R.id.dice_button5)
        diceButton6 = findViewById(R.id.dice_button6)

        diceButtonBank = listOf(
            diceButton1,diceButton2,diceButton3,diceButton4,diceButton5,diceButton6
        )

        for ((index,dice) in diceButtonBank.withIndex())
        {
            if(thirtyViewMod.getDiceValue(index) == 0)
            {
                dice.visibility = View.INVISIBLE
            }
            else if(!thirtyViewMod.getDiceClicked(index))
            {
                dice.setBackgroundResource(whiteDiceBackground[thirtyViewMod.getDiceValue(index)-1])
            }
            else
            {
                dice.setBackgroundResource(redDiceBackground[thirtyViewMod.getDiceValue(index)-1])
            }
        }


    }

    private fun rollDices(thirtyViewMod: ThirtyThrowViewModel)
    {
        thirtyViewMod.rollDices()

        for ((index,dice) in diceButtonBank.withIndex())
        {
            if(!thirtyViewMod.getDiceClicked(index))
            {
                dice.setBackgroundResource(whiteDiceBackground[thirtyViewMod.getDiceValue(index)-1])
            }
        }

    }

}
