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
import com.duhonglin.competitionguessapp.R
import com.duhonglin.competitionguessapp.data.models.Team
import com.duhonglin.competitionguessapp.databinding.FragmentAddBinding
import com.duhonglin.competitionguessapp.databinding.TeamItemBinding
import com.duhonglin.competitionguessapp.viewmodels.SharedViewModel
import com.duhonglin.competitionguessapp.viewmodels.TeamViewModel
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.net.MediaType
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpResponse
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpPost
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.client.DefaultHttpClient
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.OutputStream
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import kotlin.random.Random

class AddFragment : Fragment() {
    private var _binding: FragmentAddBinding? = null
    private val binding: FragmentAddBinding
        get() = _binding!!

    private val teamViewModel: TeamViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()
    var ifself =0
    var count = 0
    private val IMAGE_SELECTION=2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        binding.btnYes.setOnClickListener {
            insertDate()
            findNavController().navigate(R.id.action_addFragment_to_teamListFragment)
        }
        binding.btnRandom.setOnClickListener {
            insertRandomData()
        }
        //队标选择
        binding.btnDuibiao.setOnClickListener {
            if(binding.TeamName.text.toString ().isNullOrEmpty ()){
                Toast.makeText(requireContext(), "请先输入队名", Toast.LENGTH_SHORT).show()}
            else {
                ifself = 1
                val intent = Intent(Intent.ACTION_PICK).apply {
                    type = "image/*"
                }
                startActivityForResult(intent, IMAGE_SELECTION)
            }
        }
        return binding.root
    }

    private fun insertDate() {
        // Insert the team into the database
        val Judge =
            !(binding.TeamName.text.toString().isEmpty() || binding.TeamPower.text.toString()
                .isEmpty())
        if (Judge) {
            val teamName = binding.TeamName.text.toString()
            val teamPower = binding.TeamPower.text.toString().toInt()
            var teampic = binding.Xspinner.selectedItem.toString()
            if(ifself==1){
                teampic = "Self"
            }
            val team = Team(0, teamName, teamPower,teampic)
            teamViewModel.insert(team)
            Toast.makeText(requireContext(), "Team is added.", Toast.LENGTH_SHORT).show()
        } else Toast.makeText(requireContext(), "No Team.", Toast.LENGTH_SHORT).show()
    }

    private fun insertRandomData() {
            var stringarray:Array<String> = arrayOf("BIT","G2","Faze","Navi","Monte","Ence","Vit","Heroic","FuRIC","Ine","NIP","APEAKS","GL","ITB","TL","FNC","BNE")
            val chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
            val teamName = StringBuilder ().apply { repeat (3) { append (chars.random ()) } }.toString ()
            val teamPower = (50..100).random()
            val i=(0..16 ).random()
            binding.TeamName.setText(teamName)
            binding.TeamPower.setText (teamPower.toString())
            binding.Xspinner.setSelection(i)
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
        editor.putString(binding.TeamName.text.toString(), imageFile)
        // 提交修改
        editor.apply()
    }

}