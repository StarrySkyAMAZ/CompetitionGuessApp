package com.duhonglin.competitionguessapp.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.duhonglin.competitionguessapp.R
import com.duhonglin.competitionguessapp.databinding.FragmentTeamListBinding
import com.duhonglin.competitionguessapp.viewmodels.SharedViewModel
import com.duhonglin.competitionguessapp.viewmodels.TeamViewModel


class TeamListFragment : Fragment() {
    private var _binding: FragmentTeamListBinding? = null
    private val binding: FragmentTeamListBinding
        get() = _binding!!

    private val adapter: TeamlistAdapter by lazy {
       TeamlistAdapter(requireActivity())
    }
    private val teamViewModel : TeamViewModel by viewModels()
    private val sharedViewModel : SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTeamListBinding.inflate(layoutInflater)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        sharedViewModel.emptyDatabase.observe(viewLifecycleOwner) {
            showEmptyDatabaseViews(it)
        }
        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_teamListFragment_to_addFragment)
        }
        teamViewModel.allTeams.observe(viewLifecycleOwner){
            adapter.setTeam(it)
        }
        binding.btnGame.setOnClickListener {
            startMatch()
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
                //+ 从内部存储中获取图片的输入流，用文件名来打开文件
                val inputStream = requireContext().openFileInput(imageFile)
                // 将输出流转换成Bitmap对象，并设置为背景图片
                val bitmap = BitmapFactory.decodeStream(inputStream)
                binding.backimage.setImageBitmap(bitmap)
            } catch (e: Exception) {
                // 如果出现异常，就使用一个默认的图片，例如R.drawable.default_image，并打印异常信息
                binding.backimage.setImageResource(R.drawable.ba)
                e.printStackTrace()
            }
        }
        else {
            // 如果没有保存过图片的URI或者路径，就使用一个默认的图片，例如R.drawable.default_image
            binding.backimage.setImageResource(R.drawable.ba)
        }
        return binding.root
    }

    // 当用户点击开始比赛按钮时，执行以下的方法
    private fun startMatch() {
        val adapter = binding.recyclerView.adapter as TeamlistAdapter // 获取RecyclerView的Adapter的引用，并转换为TeamAdapter类型
        val teams = adapter.getAllTeams().toTypedArray() // 调用Adapter的方法，获取队伍列表
        val action = TeamListFragmentDirections.actionTeamListFragmentToMatchFragment(teams) // 创建一个NavDirections对象，传入队伍列表
        if (teams.size==0){
            Toast.makeText(requireContext(), "没有队伍！", Toast.LENGTH_SHORT).show()
        }
        else if((teams.size and (teams.size - 1)) == 0 ) {
            if (teams.size == 1) Toast.makeText(requireContext(), "请设置正确的队伍数量为2的次方", Toast.LENGTH_SHORT).show()
            else findNavController().navigate(action)
        }// 使用Jetpack Navigation来切换到FragmentMatch
            else Toast.makeText(requireContext(), "请设置正确的队伍数量为2的次方", Toast.LENGTH_SHORT).show()
    }

    companion object{
        val IMAGE_SELECTION=1
    }

    private fun showEmptyDatabaseViews(emptyDatabase: Boolean) {
        if (emptyDatabase) {
            binding.noTeamTextView.visibility = View.VISIBLE
        } else {
            binding.noTeamTextView.visibility = View.INVISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.teamlist_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // 如果点击了 change_background 菜单项
            R.id.menu_change -> {
                // 启动一个 Intent 来选择图片
                val intent = Intent(Intent.ACTION_PICK).apply {
                    type = "image/*"
                }
                startActivityForResult(intent, IMAGE_SELECTION)

            }
            R.id.menu_delete -> {
                    DeleteAll()
            }
        }
            return super.onOptionsItemSelected(item)
    }

    private fun DeleteAll(){
        val builder = AlertDialog.Builder(requireContext())
            .setPositiveButton("Yes") { _, _ ->
                teamViewModel.deleteAll()
                Toast.makeText(requireContext(), "成功删除", Toast.LENGTH_SHORT).show()

            }.setNegativeButton("No") { _, _ ->
            }.setTitle("删除全部?")
            .setMessage("真的删除？")
            .create()
            .show()
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
                binding.backimage.setImageBitmap(bitmap)

                saveImageToSharedPreferences(imageUri, imageFile) //+ 传入文件名作为参数

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // 将图片的URI和文件名保存到SharedPreferences中
    // + 添加文件名作为参数和注释
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