package com.wdeo3601.example

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wdeo3601.dropdownmenu.DropDownMenu

class MultiConditionsActivity : AppCompatActivity() {

    //region 模拟数据
    //tabs
    private val mTabs = listOf("城市", "年龄", "性别", "星座")

    //menu data
    private val mCities =
        listOf("不限", "武汉", "北京", "上海", "成都", "广州", "深圳", "重庆", "天津", "西安", "南京", "杭州")
    private val mAges = listOf("不限", "18岁以下", "18-22岁", "23-26岁", "27-35岁", "35岁以上")
    private val mSexes = listOf("不限", "男", "女")
    private val mConstellations = listOf(
        "不限",
        "白羊座",
        "金牛座",
        "双子座",
        "巨蟹座",
        "狮子座",
        "处女座",
        "天秤座",
        "天蝎座",
        "射手座",
        "摩羯座",
        "水瓶座",
        "双鱼座"
    )
    //endregion

    private val mDropDownMenu by lazy {
        findViewById<DropDownMenu>(R.id.dropdown_menu)
    }

    private val mPopupViews = mutableListOf<View>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_conditions)
        initDropdownMenu()
    }

    /**
     * 初始化下拉菜单
     */
    private fun initDropdownMenu() {
        //监听下拉菜单的状态
        mDropDownMenu.setOnMenuStateChangeListener(object :
            DropDownMenu.OnMenuStateChangeListener {
            override fun onMenuShow(tabPosition: Int) {
                Toast.makeText(
                    this@MultiConditionsActivity,
                    "onMenuShow,tabPosition=$tabPosition",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onMenuClose() {
                Toast.makeText(
                    this@MultiConditionsActivity,
                    "onMenuClose",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        mPopupViews.clear()
        //添加 popup 菜单
        addPopupMenuView(mCities)
        addPopupMenuView(mAges)
        addPopupMenuView(mSexes)
        addPopupMenuView(mConstellations)

        mDropDownMenu.setupDropDownMenu(mTabs, mPopupViews.toList())
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