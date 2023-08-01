package com.duhonglin.competitionguessapp.fragments

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.Manifest
import android.provider.MediaStore
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlin.math.*
import com.duhonglin.competitionguessapp.R
import com.duhonglin.competitionguessapp.data.MatchView
import com.duhonglin.competitionguessapp.data.models.Team
import com.duhonglin.competitionguessapp.databinding.FragmentAddBinding
import com.duhonglin.competitionguessapp.databinding.FragmentHomeBinding
import com.duhonglin.competitionguessapp.databinding.FragmentMatchBinding
import com.duhonglin.competitionguessapp.viewmodels.SharedViewModel
import com.duhonglin.competitionguessapp.viewmodels.TeamViewModel
import kotlin.random.Random


class MatchFragment : Fragment() {
    private var _binding: FragmentMatchBinding? = null
    private val binding: FragmentMatchBinding
        get() = _binding!!

    private val teamViewModel: TeamViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    private val args by navArgs<MatchFragmentArgs>()

    private lateinit var matchView: MatchView // 自定义View

    private val teams = mutableListOf<Team>() // 参赛队伍列表
    private var newround = 0 // 当前轮次

    var mediaPlayer = MediaPlayer()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMatchBinding.inflate(inflater, container, false) // 通过FragmentMatchBinding.inflate()获取View对象
        val view = binding.root // 从binding对象中获取View对象
        matchView = view.findViewById(R.id.match_view)
        // 从上一个Fragment获取传递的数据，这里使用getParcelableArray()方法
        setHasOptionsMenu(true)
        val teamArray = args.Team
        if (teamArray != null) {
            teams.addAll(teamArray) // 将数组转换为列表
            binding.textView.text = teams.size.toString()+"teams Game Begin"
        }
        else binding.textView.text = "error happen"
        // 设置按钮的点击事件监听器
        binding.startButton.setOnClickListener {
            startMatch() // 开始比赛
        }

        binding.nextButton.setOnClickListener {
            nextRound() // 下一轮
        }
        binding.resetButton.setOnClickListener {
            resetMatch() // 重新开始
        }
        val uri = Uri.parse("android.resource://" + requireContext().packageName + "/" + R.raw.backmusic2)
        mediaPlayer.setDataSource(requireContext(), uri)
        mediaPlayer.prepare()
        mediaPlayer.setLooping(true)
        val permission = Manifest.permission.READ_EXTERNAL_STORAGE
        val requestCode = 2
        if (ContextCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(permission), requestCode)
        }

        return view
    }

    // 开始比赛的方法
    private fun startMatch() {
        if (teams.size > 1) { // 如果队伍数量大于1，才能开始比赛
            newround = 1 // 设置轮次为1
            updateMatchView() // 更新对阵图显示
            binding.textView.text="Game Start"
        }
    }

    // 下一轮的方法
    private fun nextRound() {
        if (newround > 0 && newround < log2(teams.size.toDouble())+1) { // 如果已经开始比赛，并且还没有决出冠军，才能进行下一轮
            newround++ // 轮次加1
            //simulateRound() // 模拟本轮比赛结果
            updateMatchView() // 更新对阵图显示
            binding.textView.text="Next Game Start"
        }
        else if (newround.toDouble() == log2(teams.size.toDouble())+1){
            Toast.makeText(requireContext(), "冠军已产生", Toast.LENGTH_SHORT).show()
            val action = MatchFragmentDirections.actionMatchFragmentToChampionFragment(getChampion())
            findNavController().navigate(action)

        }
    }

    // 重新开始的方法
    private fun resetMatch() {
        if (newround > 0) { // 如果已经开始比赛，才能重新开始
            newround = 0 // 设置轮次为0
            teams.clear()
            val teamArray = args.Team
            teams.addAll(teamArray)
            teams.shuffle() // 打乱队伍列表的顺序
            updateMatchView() // 更新对阵图显示
            binding.textView.text="Game ReStart"
        }
    }

    // 模拟本轮比赛结果的方法
    private fun simulateRound() {
        //val winners = mutableListOf<Team>() // 创建一个胜者队伍列表
       // for (i in teams.indices step 2) { // 遍历每两个相邻的队伍
        //    val team1 = teams[i] // 获取第一个队伍
        //    val team2 = teams[i + 1] // 获取第二个队伍
         //   val factor = random.nextDouble() // 生成一个0到1之间的随机小数，作为随机因素
        //    val winner = if (team1.power * factor > team2.power * (1 - factor)) team1 else team2 // 根据战斗力和随机因素判断胜者
        //    winners.add(winner) // 将胜者添加到胜者队伍列表中
        //}
        //teams.clear() // 清空原有的队伍列表
        //teams.addAll(winners) // 将胜者队伍列表赋值给队伍列表
    }

    // 更新对阵图显示的方法
    private fun updateMatchView() {
        matchView.setTeams(teams) // 调用自定义View的方法，传递队伍列表数据
        matchView.setRound(newround) // 调用自定义View的方法，传递轮次数据
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.match_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.music-> {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "audio/*"
                startActivityForResult(intent, 1)
            }
            R.id.music2->{
                mediaPlayer.pause()
            }
            R.id.music3->{
                mediaPlayer.start()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val uri = data?.data
        if (uri != null) {
            val permission = Manifest.permission.READ_EXTERNAL_STORAGE
            val requestCode = 2
            if (ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED) {
                // permission granted, reset and set new data source
                mediaPlayer.reset()
                mediaPlayer.setDataSource(requireContext(), uri)
                mediaPlayer.prepare()
                mediaPlayer.setLooping(true)
                mediaPlayer.start()
            } else {
                // permission denied, request permission
                requestPermissions(arrayOf(permission), requestCode)
            }
        }
    }



    override fun onResume() {
        super.onResume()
        mediaPlayer.start()
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.pause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mediaPlayer.release()
    }

    fun getChampion(): Team {
        return matchView.getChampion()
    }


}