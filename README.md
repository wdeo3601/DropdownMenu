# DropdownMenu

基于 [dongjunkun/DropDownMenu](https://github.com/dongjunkun/DropDownMenu) 修改：
1. 使用 `ConstraintLayout` 重构 DropdownMenu
2. 支持 `tab 模式` 和 `无 tab 模式`
3. xml 布局更加灵活
4. 新增菜单打开关闭的回调监听
5. 新增属性 `ddMenuHeightPercent` 设置弹出菜单高度占屏幕百分比(0~1.0)

### 效果展示

![tab 模式](../dropdown_menu_1.gif)
![无 tab 模式](../dropdown_menu_2.gif)

### 使用

##### 从 `build.gradle` 引入

```
dependencies {
  implementation 'com.wdeo3601:drop-down-menu:1.0.5'
}
```

##### tab 模式

* xml 布局

```
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

        <!--  //... topBar -->

    <!--  app:ddFrameContentView，内容 View 的id，DropDownMenu 拿到后在内部为内容区域进行布局  -->
    <com.wdeo3601.example.DropDownMenu
        android:id="@+id/dropdown_menu"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        app:ddFrameContentView="@id/content_container"
        app:ddMenuHeightPercent="0.5"
        app:ddMenuIconPadding="8dp"
        app:ddMenuSelectedIcon="@drawable/brush_ic_drop_up"
        app:ddMenuTextSize="14dp"
        app:ddMenuUnselectedIcon="@drawable/brush_ic_drop_down"
        app:ddTextPadding="12dp"
        app:ddTextSelectedColor="@android:color/holo_blue_light"
        app:ddTextUnselectedColor="@color/black"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintTop_toBottomOf="@id/tv_title">

        <!--  //content view,such as RecyclerView/LinearLayout/.. -->
        <LinearLayout
            android:id="@+id/content_container"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:orientation="vertical">
        </LinearLayout>
    </com.wdeo3601.example.DropDownMenu>

</androidx.constraintlayout.widget.ConstraintLayout>
```

* 代码里使用

```
    /**
     * 初始化下拉菜单
     */
    private fun initDropdownMenu() {
        //监听下拉菜单的状态
        mDropDownMenu.setOnMenuStateChangeListener(object :
            DropDownMenu.OnMenuStateChangeListener {
            override fun onMenuShow(tabPosition: Int) {
                //do something
            }

            override fun onMenuClose() {
                //do something
            }
        })

        //add popupMenuView
        ...

        mDropDownMenu.setupDropDownMenu(mTabs, mPopupViews.toList())
    }
```

##### 无 tab 模式

* xml 布局

```
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <!--  //... topBar -->

    <com.google.android.material.button.MaterialButton
        android:id="@+id/bt_switch_menu"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_margin="16dp"
        android:text="点击打开菜单"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/tv_title" />

    <!--  app:ddFrameContentView，内容 View 的id，DropDownMenu 拿到后在内部为内容区域进行布局  -->
    <com.wdeo3601.example.DropDownMenu
        android:id="@+id/dropdown_menu"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        app:ddFrameContentView="@id/content_container"
        app:ddMenuHeightPercent="0.5"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintTop_toBottomOf="@id/bt_switch_menu">

        <!--  //content view,such as RecyclerView/LinearLayout/.. -->
        <LinearLayout
            android:id="@+id/content_container"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:orientation="vertical">

            ...

        </LinearLayout>
    </com.wdeo3601.example.DropDownMenu>

</androidx.constraintlayout.widget.ConstraintLayout>
```

* 代码里使用

```

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_condition)
        initDropdownMenu()
        mBtSwitchMenu.setOnClickListener {
            //打开下拉菜单
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
            }

            override fun onMenuClose() {
                updateSwitchButtonText(false)
            }
        })

        mPopupViews.clear()
        //添加 popup 菜单
        addPopupMenuView(mCities)

        // tabs 传空列表，就会隐藏 tab 布局
        mDropDownMenu.setupDropDownMenu(emptyList(), mPopupViews.toList())
    }
```

### License

    Copyright 2020 wdeo3601

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
