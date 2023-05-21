package com.duhonglin.competitionguessapp.fragments

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.duhonglin.competitionguessapp.R
import com.duhonglin.competitionguessapp.data.models.Team
import com.duhonglin.competitionguessapp.databinding.FragmentAddBinding
import com.duhonglin.competitionguessapp.databinding.FragmentChangeBinding
import com.duhonglin.competitionguessapp.viewmodels.SharedViewModel
import com.duhonglin.competitionguessapp.viewmodels.TeamViewModel

class ChangeFragment : Fragment() {
    private var _binding: FragmentChangeBinding? = null
    private val binding: FragmentChangeBinding
        get() = _binding!!

    private val teamViewModel: TeamViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    private val args by navArgs<ChangeFragmentArgs>()

    private val IMAGE_SELECTION=2
    private var ifself=0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentChangeBinding.inflate(inflater,container,false)
        binding.NewTeamName.setText(args.NewTeam.name)
        binding.TeamNewPower.setText(args.NewTeam.power.toString())
        binding.Cspinner.setSelection(sharedViewModel.whichPictoINT(args.NewTeam.image))
        binding.btnChanged.setOnClickListener {
            upDate()
            findNavController().navigate(R.id.action_changeFragment_to_teamListFragment)
        }
        binding.btnDelete.setOnClickListener {
            deleteDate()
            findNavController().navigate(R.id.action_changeFragment_to_teamListFragment)
        }
        binding.btnSelf.setOnClickListener {
            if(binding.NewTeamName.text.toString ().isNullOrEmpty ()){
                Toast.makeText(requireContext(), "请先输入队名", Toast.LENGTH_SHORT).show()}
            else {
                ifself = 1
                val intent = Intent(Intent.ACTION_PICK).apply {
                    type = "image/*"
                }
                startActivityForResult(intent,IMAGE_SELECTION)
            }
        }
        return binding.root
    }

    private fun upDate() {
        // Insert the team into the database
        val Judge =
            !(binding.NewTeamName.text.toString().isEmpty() || binding.TeamNewPower.text.toString()
                .isEmpty())
        if (Judge) {
            val teamName = binding.NewTeamName.text.toString()
            val teamPower = binding.TeamNewPower.text.toString().toInt()
            var teampic = binding.Cspinner.selectedItem.toString()
            if(ifself==1){
                teampic = "Self"
            }
            val team = Team(args.NewTeam.id, teamName, teamPower,teampic)
            teamViewModel.update(team)
            Toast.makeText(requireContext(), "Team is changed.", Toast.LENGTH_SHORT).show()
        } else Toast.makeText(requireContext(), "No Team.", Toast.LENGTH_SHORT).show()
    }

    private fun deleteDate() {
            val teamName = binding.NewTeamName.text.toString()
            val teamPower = binding.TeamNewPower.text.toString().toInt()
            val teampic = binding.Cspinner.selectedItem.toString()
            val team = Team(args.NewTeam.id, teamName, teamPower,teampic)
            teamViewModel.delete(team)
            Toast.makeText(requireContext(), "Team is deleted.", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 获取返回的图片的 URI
        val imageUri = data?.data

        if (imageUri != null) {
            // 获取图片的输入流，并创建一个临时文件名（你可以根据你自己的逻辑来生成文件名）
            val inputStream = requireContext().contentResolver.openInputStream(imageUri)
            val imageFile = "temp_image_${System.currentTimeMillis()}.jpg"
            // 获取图片的输出流，并将输入流中的数据写入到输出流中（你可以根据你自己的逻辑来设置压缩格式和质量）
            val outputStream = requireContext().openFileOutput(imageFile, Context.MODE_PRIVATE)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            saveTeamImageUrlToDatabase(imageFile);
        }
    }

    private fun saveTeamImageUrlToDatabase(imageFile:String){
        // 获取SharedPreferences对象
        val sp = requireActivity().getSharedPreferences("MyApp", Context.MODE_PRIVATE)
        // 获取编辑器对象
        val editor = sp.edit()
        // 将图片的文件名保存到SharedPreferences中
        Toast.makeText(requireContext(), "Save", Toast.LENGTH_SHORT).show()
        editor.putString(binding.NewTeamName.text.toString(), imageFile)
        // 提交修改
        editor.apply()
    }
}