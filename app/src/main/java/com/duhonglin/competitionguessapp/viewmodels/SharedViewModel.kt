package com.duhonglin.competitionguessapp.viewmodels

import android.app.Application
import android.graphics.Color
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.duhonglin.competitionguessapp.data.models.Team
import com.duhonglin.competitionguessapp.data.models.Teampic

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    val emptyDatabase: MutableLiveData<Boolean> = MutableLiveData(false)
    fun whichPic(teampic: String): Teampic {
        return when(teampic) {
            "BIT" -> {
                Teampic.BIT
            }
            "G2" -> {
                Teampic.BIT
            }
            "Faze" -> {
                Teampic.BIT
            }
            "Navi" -> {
                Teampic.BIT
            }
            "Monte" -> {
                Teampic.BIT
            }
            "Ence" -> {
                Teampic.BIT
            }
            "Vit" -> {
                Teampic.BIT
            }
            "Heroic" -> {
                Teampic.BIT
            }
            "FuRIC" -> {
                Teampic.BIT
            }
            "Ine" -> {
                Teampic.BIT
            }
            "NIP" -> {
                Teampic.BIT
            }
            "APEAKS" -> {
                Teampic.BIT
            }
            "GL" -> {
                Teampic.BIT
            }
            "ITB" -> {
                Teampic.BIT
            }
            "TL" -> {
                Teampic.BIT
            }
            "FNC" -> {
                Teampic.BIT
            }
            else -> {
                Teampic.BNE
            }
        }
    }
    fun whichPictoINT(teampic2: String):Int{
        return when(teampic2){
            "BIT"->0
            "G2"->1
            "Faze"->2
            "Navi"->3
            "Monte" ->4
            "Ence"->5
            "Vit"->6
            "Heroic"->7
            "FuRIC"->8
            "Ine"->9
            "NIP"->10
            "APEAKS"->11
            "GL"->12
            "ITB"->13
            "TL"->14
            "FNC"->15
            else -> 16
        }
    }
}