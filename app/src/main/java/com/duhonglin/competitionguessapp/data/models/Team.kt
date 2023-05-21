package com.duhonglin.competitionguessapp.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "team")
data class Team(
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    var name: String="Team",
    var power: Int=50,
    var image: String
) : Parcelable
