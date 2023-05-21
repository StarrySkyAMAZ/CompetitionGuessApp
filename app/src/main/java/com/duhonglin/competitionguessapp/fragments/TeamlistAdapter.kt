package com.duhonglin.competitionguessapp.fragments

import android.content.Context
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.duhonglin.competitionguessapp.R
import com.duhonglin.competitionguessapp.data.models.Team
import com.duhonglin.competitionguessapp.databinding.TeamItemBinding


class TeamlistAdapter(val context: Context): RecyclerView.Adapter<TeamlistAdapter.TeamViewHolder>() {

    private var teams = emptyList<Team>()
    class TeamViewHolder(private val binding: TeamItemBinding,val context: Context) : RecyclerView.ViewHolder(binding.root) {
        fun bind(team: Team) {
            binding.teamName.setText(team.name)
            binding.teamPower.setText(team.power.toString())
            binding.teampic.setText(team.image)
            // 从SharedPreferences中读取文件的路径
            val sp = context.getSharedPreferences("MyApp", Context.MODE_PRIVATE)
            // 从SharedPreferences中读取图片的文件名，如果没有保存过，就使用一个默认值，例如null
            val imageFile = sp.getString(binding.teamName.text.toString(), null)
            if (imageFile != null) {
                try {
                    val inputStream = context.openFileInput(imageFile)
                    // 将输出流转换成Bitmap对象，并设置为背景图片
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    if(team.image =="Self")binding.imageView.setImageBitmap(bitmap)
                } catch (e: Exception) {
                }
            }

            when(team.image) {
                "G2"->binding.imageView.setImageResource(R.drawable.g2_glitter)
                "Faze"->binding.imageView.setImageResource(R.drawable.faze_glitter)
                "Navi"->binding.imageView.setImageResource(R.drawable.navi_glitter)
                "Monte"->binding.imageView.setImageResource(R.drawable.mont_glitter)
                "Ence"->binding.imageView.setImageResource(R.drawable.ence_glitter)
                "Vit"->binding.imageView.setImageResource(R.drawable.vita_glitter)
                "Heroic"->binding.imageView.setImageResource(R.drawable.hero_glitter)
                "FuRIC"->binding.imageView.setImageResource(R.drawable.furi_glitter)
                "Ine"->binding.imageView.setImageResource(R.drawable.nein_glitter)
                "NIP"->binding.imageView.setImageResource(R.drawable.nip_glitter)
                "APEAKS"->binding.imageView.setImageResource(R.drawable.apex_glitter)
                "GL"->binding.imageView.setImageResource(R.drawable.gl_glitter)
                "ITB"->binding.imageView.setImageResource(R.drawable.itb_glitter)
                "TL"->binding.imageView.setImageResource(R.drawable.liq_glitter)
                "FNC"->binding.imageView.setImageResource(R.drawable.fntc_glitter)
                "BNE"->binding.imageView.setImageResource(R.drawable.bne_glitter)
            }
        }
    }

    // 创建新的ViewHolder对象，使用视图绑定的inflate方法来加载每个列表项的布局
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        val binding = TeamItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TeamViewHolder(binding,context)
    }

    // 绑定ViewHolder对象和数据，调用bind方法来更新每个列表项的视图
    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        val team = teams[position]
        holder.itemView.setOnLongClickListener{
            holder.itemView.findNavController().navigate(TeamListFragmentDirections.actionTeamListFragmentToChangeFragment(team))
            true
        }
        holder.bind(team)
    }

    // 返回数据集的大小
    override fun getItemCount() = teams.size

    fun setTeam(team:List<Team>) {
        val teamUtil=TeamUtil(teams,team)
        val teamResult=DiffUtil.calculateDiff(teamUtil)
        this.teams=team
        teamResult.dispatchUpdatesTo(this)
    }

    fun getAllTeams(): List<Team> {
        return teams
    }

}

