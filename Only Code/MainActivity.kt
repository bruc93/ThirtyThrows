package com.bignerdranch.android.thirtythrows
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
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

    private lateinit var diceBank:List<Dice>

    private val scoreBank = mutableListOf(0,0,0,0,0,0,0,0,0,0)

    private var listOfChoices:MutableList<String> = mutableListOf()

    private val whiteDiceBackground = arrayOf(R.drawable.white1, R.drawable.white2, R.drawable.white3,R.drawable.white4,R.drawable.white5,R.drawable.white6)
    private val redDiceBackground = arrayOf(R.drawable.red1,R.drawable.red2,R.drawable.red3,R.drawable.red4,R.drawable.red5,R.drawable.red6)

    private lateinit var adapter:ArrayAdapter<*>
    //Counts amount of pairs per round
    private var count = 0
    //current choosen in spinner
    private var choice = 0

    private var roundsDone = 0
    //------------------------ DATA ---------------------------------

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
        var numberList = resources.getStringArray(R.array.numbers)

        for(x in numberList)
        {
            listOfChoices.add(x)
        }

        adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,listOfChoices)

        spinner.adapter = adapter


        var numOfThrows = 0
        throwButton.setOnClickListener {
            rollDices()
            numOfThrows++

            if(numOfThrows == 2)
            {
                showScoreButton()
            }

        }

        scoreButton.setOnClickListener{
            calculatingScore()
            numOfThrows = 0
            //Lock in the choice

        }
        //Saves the dices user press
        saveDice()

    }

    private fun showScoreButton(){
        throwButton.visibility = View.INVISIBLE
        scoreButton.visibility = View.VISIBLE
        for(dice in diceBank)
        {
            dice.clicked = false
            dice.button.setBackgroundResource(whiteDiceBackground[dice.value-1])
        }
        Toast.makeText(this, "Pick the dice/dices to be paired"
            , Toast.LENGTH_SHORT).show()
    }
    //Will pair the dices user selects and add them to score and keep count.
    private fun calculatingScore(){
        var anyDiceClicked = false

        if(count < 1)
        {
            choice = spinner.selectedItemPosition
        }
        for(dice in diceBank){
            if(dice.clicked)
            {
                anyDiceClicked = true
            }
        }

        //Check if user has selected any dices to be paired
        if(anyDiceClicked)
        {
            var sum = getSumOfDiceClicked()

            if(listOfChoices[choice] != "-")
            {
                if(sum <= 3 && choice == 0)
                {
                    addSelectedDicesToScore()
                }
                else if(sum == (choice+3))
                {
                    addSelectedDicesToScore()
                }

            }

        }
        //If not calculate current score from this round and end it
        else
        {
            for(dice in diceBank)
            {
                dice.button.visibility = View.VISIBLE
            }

            addScoreToBank()

            removeItemFromDropList(adapter.getItem(choice) as String,listOfChoices)

            rollDices()

            //RESET
            throwButton.visibility = View.VISIBLE
            scoreButton.visibility = View.INVISIBLE
            choice = 0
            count = 0
            roundsDone++

            Toast.makeText(this, "rounds done : $roundsDone/10"
                , Toast.LENGTH_SHORT).show()

            //Check if the game should end
            endGame()
        }
    }

    private fun addSelectedDicesToScore()
    {
        count++
        for(dice in diceBank){
            if(dice.clicked)
            {
                dice.clicked = false
                dice.button.visibility = View.INVISIBLE
            }
        }
    }

    private fun addScoreToBank()
    {
        if(choice == 0 && listOfChoices[0] != "-")
        {
            scoreBank[0] = count
            Toast.makeText(this, "Total Score = $count"
                , Toast.LENGTH_SHORT).show()
        }
        else if(listOfChoices[choice] != "-"){
            scoreBank[choice] = count * (choice + 3)
            Toast.makeText(this, "Total Score = ${scoreBank[choice]}"
                , Toast.LENGTH_SHORT).show()
        }
    }

    private fun getSumOfDiceClicked():Int
    {
        var sum = 0
        for(dice in diceBank){
            if(dice.clicked)
            {
                sum += dice.value
            }
        }
        return sum
    }

    private fun endGame()
    {
        var endOfGame = true
        for(x in listOfChoices)
            if(x != "-")
                endOfGame = false


        if(endOfGame || roundsDone >= 10)
        {
            val intent = Intent(this, ScoreActivity::class.java)
                intent.putExtra("LOW", scoreBank[0])
                intent.putExtra("4", scoreBank[1])
                intent.putExtra("5", scoreBank[2])
                intent.putExtra("6", scoreBank[3])
                intent.putExtra("7", scoreBank[4])
                intent.putExtra("8", scoreBank[5])
                intent.putExtra("9", scoreBank[6])
                intent.putExtra("10", scoreBank[7])
                intent.putExtra("11", scoreBank[8])
                intent.putExtra("12", scoreBank[9])
                var scoreSend = getTotalScore()
                intent.putExtra("TOTAL_SCORE", scoreSend)

            startActivity(intent)

            resetGame()
        }
    }

    private fun resetGame()
    {

        for((index,x) in listOfChoices.withIndex())
        {
            scoreBank[index] = 0

            if(index == 0)
            {
                listOfChoices[0] = "Low"
            }
            else
            {
                listOfChoices[index] = (index+3).toString()
            }

        }
        roundsDone = 0
    }

    private fun getTotalScore():Int
    {
        var sum = 0

        for(x in scoreBank)
        {
            sum += x
        }

        return sum
    }

    private fun removeItemFromDropList(choice:String, _listOfChoices:MutableList<String>){
        for((index,x) in _listOfChoices.withIndex())
        {
            if(x == choice)
            {
                _listOfChoices[index] = "-"
                break
            }
        }

        adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,listOfChoices)

        spinner.adapter = adapter

    }

    private fun initButtons()
    {
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

    private fun saveDice()
    {
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
                dice.value = Random.nextInt(1,7)
                dice.button.setBackgroundResource(whiteDiceBackground[dice.value-1])
            }
        }
    }

}
