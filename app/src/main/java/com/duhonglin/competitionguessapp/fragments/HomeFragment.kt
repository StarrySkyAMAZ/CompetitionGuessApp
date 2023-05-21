package com.duhonglin.competitionguessapp.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.duhonglin.competitionguessapp.R
import com.duhonglin.competitionguessapp.databinding.FragmentHomeBinding
import java.io.File
import java.io.FileOutputStream
import kotlin.random.Random

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        binding.btnBegin.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_teamListFragment)
        }
        setHasOptionsMenu(true)
        // 从SharedPreferences中读取文件的路径
        val sp = requireActivity().getSharedPreferences("MyApp", Context.MODE_PRIVATE)
        // 从SharedPreferences中读取图片的URI或者路径，如果没有保存过，就使用一个默认值，例如null
        val imageUri = sp.getString("background", null)
        //+ 从SharedPreferences中读取图片的文件名，如果没有保存过，就使用一个默认值，例如null
        val imageFile = sp.getString("backgroundFile", null)
        if (imageUri != null && imageFile != null) {
            try {
                // 将字符串转换成URI，并获取图片的输出流
                //- val inputStream = requireContext().contentResolver.openInputStream(Uri.parse(imageUri))
                //+ 从内部存储中获取图片的输入流，用文件名来打开文件
                val inputStream = requireContext().openFileInput(imageFile)
                // 将输出流转换成Bitmap对象，并设置为背景图片
                val bitmap = BitmapFactory.decodeStream(inputStream)
                binding.backview.setImageBitmap(bitmap)
            } catch (e: Exception) {
                // 如果出现异常，就使用一个默认的图片，例如R.drawable.default_image，并打印异常信息
                binding.backview.setImageResource(R.drawable.ba)
                e.printStackTrace()
            }
        }
        else {
            // 如果没有保存过图片的URI或者路径，就使用一个默认的图片，例如R.drawable.default_image
            binding.backview.setImageResource(R.drawable.ba)
        }
        return binding.root
    }

    companion object{
        val IMAGE_SELECTION=1
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            // 如果点击了 change_background 菜单项
            R.id.menu_change -> {
                // 启动一个 Intent 来选择图片
                val intent = Intent(Intent.ACTION_PICK).apply {
                    type = "image/*"
                }
                startActivityForResult(intent, IMAGE_SELECTION)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 获取返回的图片的 URI
        val imageUri = data?.data

        if (imageUri != null) {
            try {
                //+ 获取图片的输入流，并创建一个临时文件名（你可以根据你自己的逻辑来生成文件名）
                val inputStream = requireContext().contentResolver.openInputStream(imageUri)
                val imageFile = "temp_image_${System.currentTimeMillis()}.jpg"
                //+ 获取图片的输出流，并将输入流中的数据写入到输出流中（你可以根据你自己的逻辑来设置压缩格式和质量）
                val outputStream = requireContext().openFileOutput(imageFile, Context.MODE_PRIVATE)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
                //+ 设置背景图片为刚刚保存到内部存储中的图片（你也可以用Bitmap对象来设置）
                binding.backview.setImageBitmap(bitmap)

                saveImageToSharedPreferences(imageUri, imageFile) //+ 传入文件名作为参数

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // 将图片的URI和文件名保存到SharedPreferences中 //+ 添加文件名作为参数和注释
    private fun saveImageToSharedPreferences(imageUri: Uri?, imageFile: String?) { //+ 添加文件名作为参数类型和名称
        // 获取SharedPreferences对象
        val sp = requireActivity().getSharedPreferences("MyApp", Context.MODE_PRIVATE)
        // 获取编辑器对象
        val editor = sp.edit()
        // 将图片的URI转换成字符串，并保存到SharedPreferences中
        editor.putString("background", imageUri.toString())
        //+ 将图片的文件名保存到SharedPreferences中
        editor.putString("backgroundFile", imageFile)
        // 提交修改
        editor.apply()
    }
}
