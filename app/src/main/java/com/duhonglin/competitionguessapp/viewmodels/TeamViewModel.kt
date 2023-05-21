package com.duhonglin.competitionguessapp.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.duhonglin.competitionguessapp.data.AppDatabase
import com.duhonglin.competitionguessapp.data.models.Team
import com.duhonglin.competitionguessapp.data.repository.TeamRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TeamViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TeamRepository
    private val team = AppDatabase.getInstance(application).teamDao()
    val allTeams: LiveData<List<Team>>

    init {
        repository = TeamRepository(team)
        allTeams = repository.allTeams
    }

    fun insert(team: Team) = viewModelScope.launch {
        repository.insert(team)
    }
    fun update(team: Team) = viewModelScope.launch {
        repository.update(team)
    }
    fun delete(team: Team) = viewModelScope.launch {
        repository.delete(team)
    }
    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }
}
