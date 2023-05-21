package com.duhonglin.competitionguessapp.fragments

import androidx.recyclerview.widget.DiffUtil
import com.duhonglin.competitionguessapp.data.models.Team

class TeamUtil (
    private val oldList: List<Team>,
    private val newList: List<Team>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] === newList[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem.id == newItem.id &&
                    oldItem.name == newItem.name &&
                    oldItem.power == newItem.power &&
                    oldItem.image == newItem.image
        }

    }