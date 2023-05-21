package com.duhonglin.competitionguessapp.data.repository

import androidx.lifecycle.LiveData
import com.duhonglin.competitionguessapp.data.models.Team
import com.duhonglin.competitionguessapp.data.TeamDao

class TeamRepository(private val teamDao: TeamDao) {

    val allTeams: LiveData<List<Team>> = teamDao.getAllTeams()

    suspend fun insert(team: Team) {
        teamDao.insert(team)
    }

    suspend fun update(team: Team) {
        teamDao.update(team)
    }

    suspend fun delete(team: Team) {
        teamDao.delete(team)
    }

    suspend fun deleteAll() {
        teamDao.deleteAll()
    }
}
