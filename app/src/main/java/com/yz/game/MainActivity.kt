package com.yz.game

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val llTvView: LinearLayout = findViewById(R.id.ll_tv_view)
        val llBtnView: LinearLayout = findViewById(R.id.ll_btn_view)

        val game = Game(this)
        game.setAxle(4).setOnViewAdd(object : Game.OnAddView {
            override fun addTextView(tv: LinearLayout) {
                llTvView.addView(tv)
            }

            override fun addBtnView(btn: LinearLayout) {
                llBtnView.addView(btn)
            }

            @SuppressLint("ShowToast")
            override fun gameOver() {
                Toast.makeText(this@MainActivity.baseContext,"GameOver",Toast.LENGTH_LONG).show()
               /* llTvView.removeAllViews()
                llTvView.removeAllViewsInLayout()
                llBtnView.removeAllViews()
                llBtnView.removeAllViewsInLayout()
                game.initData()*/
            }

        }).initData()
    }
}
