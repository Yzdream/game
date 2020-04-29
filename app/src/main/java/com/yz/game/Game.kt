package com.yz.game

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import java.util.Random

class Game internal constructor(private val context: Context) : View.OnClickListener {

    companion object {
        //雷
        private const val MINES = -1
        //空白
        private const val EMPTY = 0

        //矩阵x、y轴  x表示行  y表示列
        private var mapX: Int = 0
        private var mapY: Int = 0
        //矩阵二维数组
        private var map: Array<IntArray>? = null

        //矩阵方格轴
        private var axle = 4
        //产生随机数决定雷的位置
        private var random: Random? = null
    }

    //随机数的数量
    private var minesCount = 0
    //界面方格集合
    private var btnList: Array<Array<Button?>>? = null

    private var addView: OnAddView? = null

    fun setAxle(axle: Int): Game {
        Game.axle = axle
        mapY = axle
        mapX = mapY
        return this
    }

    fun setOnViewAdd(addView: OnAddView): Game {
        this.addView = addView
        return this
    }

    fun initData() {
        minesCount = 0
        mapY = axle
        mapX = mapY
        map = null
        //初始化二维数组
        map = Array(mapX) { IntArray(mapY) }

        getRandom()

        for (x in 0..(mapX - 1)) {
            for (y in 0..(mapY - 1)) {
                if (map!![x][y] == MINES) {
                    //左上方
                    if (x != 0 && y != 0 && map!![x - 1][y - 1] != MINES) {
                        map!![x - 1][y - 1] += 1
                    }
                    //正左方
                    if (y != 0 && map!![x][y - 1] != MINES) {
                        map!![x][y - 1] += 1
                    }
                    //左下方
                    if (x < (mapX - 1) && y != 0 && map!![x + 1][y - 1] != MINES) {
                        map!![x + 1][y - 1] += 1
                    }
                    //正上方
                    if (x != 0 && map!![x - 1][y] != MINES) {
                        map!![x - 1][y] += 1
                    }
                    //正下方
                    if (x < (mapX - 1) && map!![x + 1][y] != MINES) {
                        map!![x + 1][y] += 1
                    }
                    //右上方
                    if (x != 0 && y < (mapY - 1) && map!![x - 1][y + 1] != MINES) {
                        map!![x - 1][y + 1] += 1
                    }
                    //正右方
                    if (y < (mapY - 1) && map!![x][y + 1] != MINES) {
                        map!![x][y + 1] += 1
                    }
                    //右下方
                    if (x < (mapX - 1) && y < (mapY - 1) && map!![x + 1][y + 1] != MINES) {
                        map!![x + 1][y + 1] += 1
                    }
                }
            }
        }
        initView()
    }

    private fun initView() {
        btnList = Array(mapX) { arrayOfNulls<Button>(mapY) }
        val width = context.resources.displayMetrics.widthPixels / axle
        val params = LinearLayout.LayoutParams(width, width)
        for (x in 0..(mapX - 1)) {
            val tvs = LinearLayout(context)
            tvs.orientation = LinearLayout.HORIZONTAL
            val btns = LinearLayout(context)
            btns.orientation = LinearLayout.HORIZONTAL
            for (y in 0..(mapY - 1)) {
                //底层的TextView
                val tvView = TextView(context)
                tvView.layoutParams = params
                tvView.gravity = Gravity.CENTER
                if (map!![x][y] == EMPTY)
                    tvView.text = " "
                else if (map!![x][y] != EMPTY) {
                    tvView.text = if (map!![x][y] == -1) "*" else map!![x][y].toString()
                }
                tvs.addView(tvView)
                // 底层的Button
                val btn = Button(context)
                btn.layoutParams = params
                btn.tag = x * 10 + y
                btn.setOnClickListener(this)
//                 btn.setOnLongClickListener(this)
                btnList!![x][y] = btn
                btns.addView(btn)
            }
            if (addView != null) {
                addView!!.addTextView(tvs)
                addView!!.addBtnView(btns)
            }
        }
    }

    override fun onClick(btn: View) {
        val position = btn.tag as Int
        val x = position / 10
        val y = position % 10

        for (i in 0..(mapX - 1)) {
            for (j in 0..(mapY - 1)) {
                if (j == mapY - 1)
                    System.out.println("  $i==$j   " + map!![i][j])
                else
                    System.out.print("  $i==$j   " + map!![i][j])
            }
        }
        // 隐藏按钮，显示底下的数据
        btn.visibility = View.INVISIBLE
        //判断点击的是否是随机数
        if (isOver(x, y)) {
            if (addView != null) {
                addView!!.gameOver()
            }
        }
        // 判断点击的如果是空白的
        if (map!![x][y] == EMPTY) {
            //显示空白区域
            showEmpty(x, y)
        }
    }

    private fun showEmpty(x: Int, y: Int) {
        btnInvisible(x, y)
        //左上方
        if (x != 0 && y != 0 && map!![x - 1][y - 1] == EMPTY && btnList!![(x - 1)][(y - 1)]!!.visibility != View.INVISIBLE) {
            showEmpty(x - 1, y - 1)
        } else if (x != 0 && y != 0) {
            btnInvisible(x - 1, y - 1)
        }
        //正左方
        if (y != 0 && map!![x][y - 1] == EMPTY && btnList!![x][(y - 1)]!!.visibility != View.INVISIBLE) {
            showEmpty(x, y - 1)
        } else if (y != 0) {
            btnInvisible(x, y - 1)
        }
        //左下方
        if (x < (mapX - 1) && y != 0 && map!![x + 1][y - 1] == EMPTY && btnList!![(x + 1)][(y - 1)]!!.visibility != View.INVISIBLE) {
            showEmpty(x + 1, y - 1)
        } else if (x < (mapX - 1) && y != 0) {
            btnInvisible(x + 1, y - 1)
        }
        //正上方
        if (x != 0 && map!![x - 1][y] == EMPTY && btnList!![(x - 1)][y]!!.visibility != View.INVISIBLE) {
            showEmpty(x - 1, y)
        } else if (x != 0) {
            btnInvisible(x - 1, y)
        }
        //正下方
        if (x < (mapX - 1) && map!![x + 1][y] == EMPTY && btnList!![(x + 1)][y]!!.visibility != View.INVISIBLE) {
            showEmpty(x + 1, y)
        } else if (x < (mapX - 1)) {
            btnInvisible(x + 1, y)
        }
        //右上方
        if (x != 0 && y < (mapY - 1) && map!![x - 1][y + 1] == EMPTY && btnList!![(x - 1)][(y + 1)]!!.visibility != View.INVISIBLE) {
            showEmpty(x - 1, y + 1)
        } else if (x != 0 && y < (mapY - 1)) {
            btnInvisible(x - 1, y + 1)
        }
        //正右方
        if (y < (mapY - 1) && map!![x][y + 1] == EMPTY && btnList!![x][(y + 1)]!!.visibility != View.INVISIBLE) {
            showEmpty(x, y + 1)
        } else if (y < (mapY - 1)) {
            btnInvisible(x, y + 1)
        }
        //右下方
        if (x < (mapX - 1) && y < (mapY - 1) && map!![x + 1][y + 1] == EMPTY && btnList!![(x + 1)][(y + 1)]!!.visibility != View.INVISIBLE) {
            showEmpty(x + 1, y + 1)
        } else if (x < (mapX - 1) && y < (mapY - 1)) {
            btnInvisible(x + 1, y + 1)
        }
    }

    /**
     * 根据x、y坐标隐藏btn
     */
    private fun btnInvisible(x: Int, y: Int) {
        btnList!![x][y]!!.visibility = View.INVISIBLE
    }

    /**
     * 是否结束
     * x y 轴坐标
     */
    private fun isOver(x: Int, y: Int): Boolean {
        // OVER
        if (map!![x][y] == MINES) {
            for (i in 0..(mapX - 1)) {
                for (j in 0..(mapY - 1)) {
                    btnList!![i][j]!!.visibility = View.INVISIBLE
                }
            }
            return true
        }
        return false
    }

    /**
     * 初始化随机
     * 生成axle个随机数
     */
    private fun getRandom() {
        if (random == null)
            random = Random()
        //随机数量为axle
        for (index in 1..axle) {
            //避免生成重复的随机数
            if (map!![random!!.nextInt(mapX)][random!!.nextInt(mapY)] == EMPTY) {
                map!![random!!.nextInt(mapX)][random!!.nextInt(mapY)] = MINES
                minesCount++
                //如果已经产生axle个随机数就不在生成随机数
                if (minesCount == axle)
                    break
            } else {
                getRandom()
                break
            }
        }
    }

    interface OnAddView {

        fun addTextView(tv: LinearLayout)

        fun addBtnView(btn: LinearLayout)

        fun gameOver()
    }

}

