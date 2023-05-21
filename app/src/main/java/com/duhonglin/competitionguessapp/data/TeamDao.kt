package com.duhonglin.competitionguessapp.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.duhonglin.competitionguessapp.data.models.Team

@Dao
interface TeamDao {

    @Insert
    suspend fun insert(team: Team)

    @Update
    suspend fun update(team: Team)

    @Delete
    suspend fun delete(team: Team)

    @Query(("DELETE FROM team"))
    suspend fun deleteAll()

    @Query("SELECT * FROM team")
    fun getAllTeams(): LiveData<List<Team>>

    @Query("SELECT * FROM team WHERE id = :id")
    suspend fun getTeamById(id: Int): Team

}
