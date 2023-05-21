package com.duhonglin.competitionguessapp.fragments

import android.content.Context
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.duhonglin.competitionguessapp.R
import com.duhonglin.competitionguessapp.databinding.FragmentChampionBinding
import com.duhonglin.competitionguessapp.databinding.FragmentTeamListBinding
import com.duhonglin.competitionguessapp.viewmodels.SharedViewModel
import com.duhonglin.competitionguessapp.viewmodels.TeamViewModel

class ChampionFragment : Fragment() {

    private var _binding: FragmentChampionBinding? = null
    private val binding: FragmentChampionBinding
        get() = _binding!!

    private val teamViewModel : TeamViewModel by viewModels()
    private val sharedViewModel : SharedViewModel by viewModels()

    private val args by navArgs<ChampionFragmentArgs>()
    private val mediaPlayer = MediaPlayer()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentChampionBinding.inflate(inflater, container, false)
        binding.ChampionTeam.setText(args.champion.name)
        binding.ChampionView.scaleType = ImageView.ScaleType.FIT_CENTER
        binding.btnBack.setOnClickListener{
            Toast.makeText(requireContext(), "预测完成", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_championFragment_to_teamListFragment)
        }

        val uri = Uri.parse("android.resource://" + requireContext().packageName + "/" + R.raw.championmusic)
        mediaPlayer.setDataSource(requireContext(), uri)
        mediaPlayer.prepare()
        mediaPlayer.setLooping(true)
        mediaPlayer.start()
        // 从SharedPreferences中读取文件的路径
        val sp = requireActivity().getSharedPreferences("MyApp", Context.MODE_PRIVATE)
        // 从SharedPreferences中读取图片的文件名，如果没有保存过，就使用一个默认值，例如null
        val imageFile = sp.getString(binding.ChampionTeam.text.toString(), null)
        if (imageFile != null) {
            try {
                val inputStream = requireActivity().openFileInput(imageFile)
                // 将输出流转换成Bitmap对象，并设置为背景图片
                val bitmap = BitmapFactory.decodeStream(inputStream)
                if(args.champion.image =="Self")binding.ChampionView.setImageBitmap(bitmap)
            } catch (e: Exception) {
            }
        }
        when(args.champion.image) {
            "Bit"->binding.ChampionView.setImageResource(R.drawable.bitxiaohui)
            "G2"->binding.ChampionView.setImageResource(R.drawable.g2_glitter)
            "Faze"->binding.ChampionView.setImageResource(R.drawable.faze_glitter)
            "Navi"->binding.ChampionView.setImageResource(R.drawable.navi_glitter)
            "Monte"->binding.ChampionView.setImageResource(R.drawable.mont_glitter)
            "Ence"->binding.ChampionView.setImageResource(R.drawable.ence_glitter)
            "Vit"->binding.ChampionView.setImageResource(R.drawable.vita_glitter)
            "Heroic"->binding.ChampionView.setImageResource(R.drawable.hero_glitter)
            "FuRIC"->binding.ChampionView.setImageResource(R.drawable.furi_glitter)
            "Ine"->binding.ChampionView.setImageResource(R.drawable.nein_glitter)
            "NIP"->binding.ChampionView.setImageResource(R.drawable.nip_glitter)
            "APEAKS"->binding.ChampionView.setImageResource(R.drawable.apex_glitter)
            "GL"->binding.ChampionView.setImageResource(R.drawable.gl_glitter)
            "ITB"->binding.ChampionView.setImageResource(R.drawable.itb_glitter)
            "TL"->binding.ChampionView.setImageResource(R.drawable.liq_glitter)
            "FNC"->binding.ChampionView.setImageResource(R.drawable.fntc_glitter)
            "BNE"->binding.ChampionView.setImageResource(R.drawable.bne_glitter)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mediaPlayer.release()
    }
}