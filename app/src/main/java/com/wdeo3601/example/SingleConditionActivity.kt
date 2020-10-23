package com.wdeo3601.example

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class SingleConditionActivity : AppCompatActivity() {

    //region 模拟数据

    //menu data
    private val mCities =
        listOf("不限", "武汉", "北京", "上海", "成都", "广州", "深圳", "重庆", "天津", "西安", "南京", "杭州")
    //endregion

    private val mBtSwitchMenu by lazy {
        findViewById<MaterialButton>(R.id.bt_switch_menu)
    }

    private val mDropDownMenu by lazy {
        findViewById<DropDownMenu>(R.id.dropdown_menu)
    }

    private val mPopupViews = mutableListOf<View>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_condition)
        initDropdownMenu()
        mBtSwitchMenu.setOnClickListener {
            mDropDownMenu.switchMenu(0)
        }
    }


    /**
     * 初始化下拉菜单
     */
    private fun initDropdownMenu() {
        //监听下拉菜单的状态
        mDropDownMenu.setOnMenuStateChangeListener(object :
            DropDownMenu.OnMenuStateChangeListener {
            override fun onMenuShow(tabPosition: Int) {
                updateSwitchButtonText(true)
                Toast.makeText(
                    this@SingleConditionActivity,
                    "onMenuShow,tabPosition=$tabPosition",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onMenuClose() {
                updateSwitchButtonText(false)
                Toast.makeText(
                    this@SingleConditionActivity,
                    "onMenuClose",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        mPopupViews.clear()
        //添加 popup 菜单
        addPopupMenuView(mCities)

        mDropDownMenu.setupDropDownMenu(emptyList(), mPopupViews.toList())
    }

    /**
     * 更新按钮状态
     */
    private fun updateSwitchButtonText(isMenuOpen: Boolean) {
        mBtSwitchMenu.text = if (isMenuOpen)
            "点击关闭菜单"
        else
            "点击打开菜单"
    }

    private fun addPopupMenuView(menuData: List<String>) {
        val recyclerView = RecyclerView(baseContext)
        recyclerView.layoutManager = LinearLayoutManager(baseContext)
        val menuAdapter = MenuAdapter(menuData)
        menuAdapter.onItemClick = {
            mDropDownMenu.closeMenu()
            Toast.makeText(baseContext, it, Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = menuAdapter
        mPopupViews.add(recyclerView)
    }
}