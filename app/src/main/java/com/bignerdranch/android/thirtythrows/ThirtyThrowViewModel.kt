package com.bignerdranch.android.thirtythrows

import android.R
import android.content.res.Resources
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModel
import kotlin.math.round
import kotlin.random.Random

class ThirtyThrowViewModel:ViewModel(){
    private var beenInit = false
    private val scoreBank = mutableListOf(0,0,0,0,0,0,0,0,0,0)
    private var listOfChoices:MutableList<String> = mutableListOf()
    private var count = 0
    private var choice = 0
    private var roundsDone = 0
    private var numOfThrows = 0
    private var diceBank = listOf(

        Dice(1,false),
        Dice(2,false),
        Dice(3,false),
        Dice(4,false),
        Dice(5,false),
        Dice(6,false)
    )

    public fun initListChoice(choiceList:Array<String>){
        if(!beenInit){
            for(x in choiceList){
                listOfChoices.add(x)
            }

            beenInit = true
        }
    }

    public fun getNumOfThrows():Int{
        return numOfThrows
    }
    public fun getIfAllValuesUsed():Boolean{
        var allUsed = false
        for(x in listOfChoices)
            if(x != "-")
                allUsed = false

        return allUsed
    }
    public fun getDiceClicked(index:Int):Boolean
    {
       return diceBank[index].clicked
    }

    public fun getScoreFromBank(index:Int):Int
    {
        return scoreBank[index]
    }

    public fun getTotalScore():Int
    {
        var sum = 0

        for(x in scoreBank)
        {
            sum += x
        }

        return sum
    }

    public fun resetGame()
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

    public fun rollDices()
    {
        numOfThrows++
        for (dice in diceBank)
        {
            if(!dice.clicked)
            {
                dice.value = Random.nextInt(1,7)
            }
        }
    }

    public fun getDiceValue(index: Int):Int{
       return diceBank[index].value
    }

    public fun clickADice(index:Int):Boolean
    {

        diceBank[index].clicked = !diceBank[index].clicked

        return diceBank[index].clicked

    }

    public fun unclickAllDices()
    {
        for(dice in diceBank)
        {
            dice.clicked = false
        }
    }

    public fun getIfAnyDiceClicked():Boolean{
        var anyDiceClicked = false

        for(dice in diceBank){
            if(dice.clicked)
            {
                anyDiceClicked = true
            }
        }
        return anyDiceClicked
    }

    public fun getChoiceOfList():MutableList<String>{
        return listOfChoices
    }
    public fun getCurrentChoice():Int{
        return choice
    }
    public fun getRoundsDone():Int{
        return roundsDone
    }

    //Returns bool if the round should be ended or keep pairing
    public fun calculatingScore(itemSelected:Int):Boolean{
        var endRound = false

        if(count < 1)
        {
            choice = itemSelected
        }

        //Check if user has selected any dices to be paired
        if(getIfAnyDiceClicked())
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
            endRound = false

        }
        //If not calculate current score from this round and end it
        else
        {
            if(listOfChoices[choice] != "-") {
                addScoreToBank()

                rollDices()
                removeItemFromDropList()

                numOfThrows = 0
                choice = 0
                count = 0
                roundsDone++

                endRound = true
            }
        }

        return endRound
    }

    private fun removeItemFromDropList(){
        for((index,x) in listOfChoices.withIndex())
        {
            if(index == choice && listOfChoices[index] != "-")
            {

                listOfChoices[index] = "-"
            }
        }
    }

    private fun addScoreToBank()
    {
        if(choice == 0 && listOfChoices[0] != "-")
        {
            scoreBank[0] = count
        }

        else if(listOfChoices[choice] != "-"){
            scoreBank[choice] = count * (choice + 3)
        }
    }


    private fun addSelectedDicesToScore()
    {
        count++
        for(dice in diceBank){  // TO-DO <-- fix correct
            if(dice.clicked)
            {
                dice.clicked = false
                dice.value = 0
            }
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

}