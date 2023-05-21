package com.duhonglin.competitionguessapp.data

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.duhonglin.competitionguessapp.data.models.Team
import com.duhonglin.competitionguessapp.fragments.ChampionFragmentArgs
import com.duhonglin.competitionguessapp.fragments.MatchFragmentDirections
import java.lang.Math.random
import kotlin.math.log2
import kotlin.math.pow

class MatchView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val paint = Paint() // 画笔
    private val teams = mutableListOf<Team>() // 参赛队伍列表
    private var winners = mutableListOf<Team>() // 胜出队伍列表
    private var round = 0 // 当前轮次
    var randomArray1: IntArray? = null
    var randomArray2: IntArray? = null



    // 初始化方法，设置画笔的属性
    init {
        paint.color = Color.BLACK // 设置画笔颜色为黑色
        paint.strokeWidth = 5f // 设置画笔宽度为5像素
        paint.textSize = 40f // 设置画笔字体大小为40像素
        paint.textAlign = Paint.Align.CENTER // 设置画笔字体对齐方式为居中
    }

    // 绘制方法，根据队伍列表和轮次来绘制对阵图
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat() // 获取View的宽度
        val height = height.toFloat() // 获取View的高度
        if (round==0){
            if (randomArray1 != null) {
                randomArray1 = IntArray(10) { (-20..20).random() }
                randomArray2 = IntArray(10) { (-20..20).random() }
            }
        }
        if (winners.size > 1 && round > 0) { // 如果队伍数量大于1，并且已经开始比赛，才绘制对阵图
            val maxround = log2(winners.size.toDouble()).toInt() + 1
            // 在更新视图时检查该变量是否已经赋值
            if (randomArray1 == null) {
                // 如果没有赋值，就生成一个新的随机数数组并赋值给该变量
                randomArray1 = IntArray(10) { (-20..20).random() }
                randomArray2 = IntArray(10) { (-20..20).random() }
            }

            //上面代码必须放在这里面不然app卡死
            val gap = height / winners.size // 计算每个队伍之间的间隙
            val wgap = width / maxround // 计算每轮之间的间隙
            val margin = gap / 4 // 计算每个队伍的边距
            for (r in 1..round) {//绘制每轮视图
                if(r!=maxround||round==1) {
                    for (i in 0 until (winners.size - 1) step 2.0.pow(r.toDouble())
                        .toInt()) { // 遍历每两个相邻的队伍

                        val team1 = winners[i] // 获取第一个队伍
                        val team2 = winners[i + 2.0.pow((r - 1).toDouble()).toInt()] // 获取第二个队伍

                        val x1 = r * wgap - margin// 计算第一个队伍的横坐标，根据轮次向左偏移，并根据动画进度向右偏移，并减去边距以留出空间绘制名称
                        val y1 = gap * ((2.00.pow((r - 1).toDouble())-1.0)/2.0+2.0.pow(r.toDouble()).toInt()*i/2.0.pow(r.toDouble()))  + margin  // 计算第一个队伍的纵坐标，根据索引向下偏移

                        val x2 = r * wgap - margin// 计算第二个队伍的横坐标，与第一个队伍相同
                        val y2 = gap * ((2.0.pow((r - 1).toDouble())-1.0)/2.0+2.0.pow(r.toDouble()).toInt()*i/2.0.pow(r.toDouble())+ 2.0.pow((r - 1).toDouble()).toInt())  + margin // 计算第二个队伍的纵坐标，根据索引向下偏移

                        canvas.drawLine(x1 - margin, y1.toFloat(), x2- margin, y2.toFloat() - paint.textSize,
                            paint.apply { color = Color.BLACK } // 设置画笔颜色为黑色，并绘制两个队伍之间的连线
                        )

                        canvas.drawText(team1.name, x1- margin, y1.toFloat(),
                            paint.apply {
                                color = if (team1.power+ randomArray1!![i/2.0.pow(r.toDouble()).toInt()+r] >= team2.power+randomArray2!![i/2.0.pow(r.toDouble()).toInt()+r]) Color.GREEN else Color.RED
                            }) // 根据战斗力设置画笔颜色为绿色或红色，并绘制第一个队伍的名称
                        canvas.drawText(
                            team2.name, x2 - margin, y2.toFloat(),
                            paint.apply {
                                color = if (team2.power+randomArray2!![i/2.0.pow(r.toDouble()).toInt()+r] > team1.power+ randomArray1!![i/2.0.pow(r.toDouble()).toInt()+r]) Color.GREEN else Color.RED
                            }) // 根据战斗力设置画笔颜色为绿色或红色，并绘制第二个队伍的名称
                        var winner = if (team1.power+randomArray1!![i/2.0.pow(r.toDouble()).toInt()+r]>=team2.power+randomArray2!![i/2.0.pow(r.toDouble()).toInt()+r]) team1 else team2
                        if (team2.power+randomArray2!![i/2.0.pow(r.toDouble()).toInt()+r]>team1.power+randomArray1!![i/2.0.pow(r.toDouble()).toInt()+r]) {
                            winners[i].name = winners[i + 2.0.pow((r - 1).toDouble()).toInt()].name
                            winners[i].power = winners[i + 2.0.pow((r - 1).toDouble()).toInt()].power
                            winners[i].image = winners[i + 2.0.pow((r - 1).toDouble()).toInt()].image
                        }
                            val x3 = (r + 1) * wgap  - margin// 计算胜者的横坐标，根据轮次向右偏移，并居中对齐两个队伍之间的连线，并根据动画进度向左偏移
                            val y3 = (y1 + y2-paint.textSize) / 2// 计算胜者的纵坐标，取两个队伍之间的中点，并稍微向上偏移
                            canvas.drawLine(x1- margin, y3.toFloat(), x3-margin-40f, y3.toFloat(),
                                paint.apply { color = Color.BLACK } // 设置画笔颜色为黑色，并绘制胜者的连线
                            )
                    }
                }
                else if (r==maxround){
                    val champion=winners[0]
                    val x=r * wgap  - margin
                    val y=gap * ((2.0.pow((r - 1).toDouble())-1.0)/2.0)  + margin
                    canvas.drawText(
                        champion.name,
                        x - margin,
                        y.toFloat(),
                        paint.apply { color =  Color.GREEN
                        })
                }
            }
        }
    }

    // 设置队伍列表的方法，用于从Fragment传递数据
    fun setTeams(teams: List<Team>) {
        this.teams.clear() // 清空原有的列表
        this.teams.addAll(teams) // 添加新的列表
        winners.clear()
        for (team in teams) { // 遍历每个队伍
            val newTeam = Team(team.id, team.name, team.power, team.image) // 创建一个新的队伍对象，并复制原来队伍的属性
            winners.add(newTeam)
        }
        invalidate() // 重绘View
    }

    // 设置轮次的方法，用于从Fragment传递数据
    fun setRound(round: Int) {
        this.round = round; // 设置轮次
    }

    fun getChampion(): Team {
        return winners[0]
    }
}
