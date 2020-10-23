package com.wdeo3601.example;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import java.util.List;

import static android.widget.LinearLayout.HORIZONTAL;


/**
 * --------------------
 * | DropDownMenu(ConstraintLayout)  |
 * |  ----------------  |
 * | |      Tabs      | |
 * |  ----------------  |
 * |  ----------------  | ----------------- |
 * | |                | |                   |
 * | |                | |                   |
 * | | bottom frame   | |     top frame     |
 * | |ContentContainer| |    FrameLayout    |
 * | |                | |    menu + mask    |
 * | |                | |                   |
 * | |                | |                   |
 * |  ----------------  | ----------------- |
 * --------------------
 * Created by dongjunkun on 2015/6/17.
 */
public class DropDownMenu extends ConstraintLayout {

    //顶部菜单布局
    private LinearLayout tabMenuView;
    //底部容器，包含popupMenuViews，maskView
    private FrameLayout containerView;
    //弹出菜单父布局
    private FrameLayout popupMenuViews;
    //遮罩半透明View，点击可关闭DropDownMenu
    private View maskView;
    //tabMenuView里面选中的tab位置，-1表示未选中
    private int current_tab_position = -1;

    //tab选中颜色
    private int textSelectedColor = 0xff890c85;
    //tab未选中颜色
    private int textUnselectedColor = 0xff111111;
    //tab字体大小
    private int menuTextSize = 14;
    //tab标签上下padding
    private int menuTextPadding;
    private final Drawable[] icons = new Drawable[2];
    //图标的大小
    private int menuIconSize;
    //tab图标距离文字的间距
    private int drawablePadding;
    //tab选中图标
    private int menuSelectedIcon;
    //tab未选中图标
    private int menuUnselectedIcon;

    //内容 View 的 id
    private int contentViewId;

    //菜单高度百分比
    private float menuHeightPercent = 0f;

    //遮罩颜色
    private int maskColor = 0x88888888;

    private OnMenuStateChangeListener onMenuStateChangeListener = null;

    public DropDownMenu(Context context) {
        super(context, null);
    }

    public DropDownMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DropDownMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //为DropDownMenu添加自定义属性
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DropDownMenu);

        //接收 tab 属性
        menuTextSize = a.getDimensionPixelSize(R.styleable.DropDownMenu_ddMenuTextSize, menuTextSize);
        menuTextPadding = a.getDimensionPixelSize(R.styleable.DropDownMenu_ddTextPadding, menuTextPadding);
        menuIconSize = a.getDimensionPixelSize(R.styleable.DropDownMenu_ddMenuIconSize, menuIconSize);
        drawablePadding = a.getDimensionPixelSize(R.styleable.DropDownMenu_ddMenuIconPadding, drawablePadding);
        textSelectedColor = a.getColor(R.styleable.DropDownMenu_ddTextSelectedColor, textSelectedColor);
        menuSelectedIcon = a.getResourceId(R.styleable.DropDownMenu_ddMenuSelectedIcon, menuSelectedIcon);
        textUnselectedColor = a.getColor(R.styleable.DropDownMenu_ddTextUnselectedColor, textUnselectedColor);
        menuUnselectedIcon = a.getResourceId(R.styleable.DropDownMenu_ddMenuUnselectedIcon, menuUnselectedIcon);

        //下划线属性
        int underlineColor = a.getColor(R.styleable.DropDownMenu_ddUnderlineColor, 0);
        int underlineHeight = a.getDimensionPixelSize(R.styleable.DropDownMenu_ddUnderlineWidth, 0);

        //内容 View 的 id
        contentViewId = a.getResourceId(R.styleable.DropDownMenu_ddFrameContentView, -1);

        //菜单属性
        menuHeightPercent = a.getFloat(R.styleable.DropDownMenu_ddMenuHeightPercent, menuHeightPercent);

        //蒙板属性
        maskColor = a.getColor(R.styleable.DropDownMenu_ddMaskColor, maskColor);

        a.recycle();

        //初始化tabMenuView并添加到tabMenuView
        addTabMenuViewContainer(context);

        //为tabMenuView添加下划线
        addUnderLineIfNeeded(underlineColor, underlineHeight);
    }

    /**
     * xml 加载完成
     * inflate third step: add content container
     * then, invoke fourth inflate step
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View contentView = getViewById(contentViewId);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        params.topToBottom = R.id.dd_tab_menu_view;
        params.bottomToBottom = ConstraintSet.PARENT_ID;
        params.startToStart = ConstraintSet.PARENT_ID;
        params.endToEnd = ConstraintSet.PARENT_ID;
        contentView.setLayoutParams(params);
        //初始化containerView并将其添加到DropDownMenu
        addPopupAndMaskContainer();
    }

    /**
     * inflate first step: add tab container
     *
     * @param context
     */
    private void addTabMenuViewContainer(Context context) {
        tabMenuView = new LinearLayout(context);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topToTop = ConstraintSet.PARENT_ID;
        params.startToStart = ConstraintSet.PARENT_ID;
        params.endToEnd = ConstraintSet.PARENT_ID;
        tabMenuView.setId(R.id.dd_tab_menu_view);
        tabMenuView.setOrientation(HORIZONTAL);
        tabMenuView.setBackgroundColor(Color.WHITE);
        tabMenuView.setLayoutParams(params);
        addView(tabMenuView);
    }

    /**
     * inflate second step: add under line view
     *
     * @param underlineColor
     * @param underlineHeight
     */
    private void addUnderLineIfNeeded(int underlineColor, int underlineHeight) {
        if (underlineColor != 0) {
            View underLine = new View(getContext());
            LayoutParams underLineParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, underlineHeight);
            underLineParams.topToBottom = R.id.dd_tab_menu_view;
            underLineParams.startToStart = ConstraintSet.PARENT_ID;
            underLineParams.endToEnd = ConstraintSet.PARENT_ID;
            underLine.setLayoutParams(underLineParams);
            underLine.setBackgroundColor(underlineColor);
            addView(underLine);
        }
    }

    /**
     * inflate fourth step: add popup and mask view container
     */
    private void addPopupAndMaskContainer() {
        containerView = new FrameLayout(getContext());
        LayoutParams params = new LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 0);
        params.startToStart = contentViewId;
        params.endToEnd = contentViewId;
        params.topToTop = contentViewId;
        params.bottomToBottom = contentViewId;
        containerView.setLayoutParams(params);
        addView(containerView);
    }

    /**
     * 初始化DropDownMenu
     */
    public void setupDropDownMenu(List<String> tabTexts, List<View> popupViews) {
        //设置 tab 菜单
        setUpTabMenuIfNeeded(tabTexts);
        //设置蒙板
        setUpMaskView();
        //设置弹出菜单
        setUpPopupMenuViews(popupViews);
    }

    /**
     * 设置弹出菜单
     *
     * @param popupViews
     */
    private void setUpPopupMenuViews(List<View> popupViews) {
        popupMenuViews = new FrameLayout(getContext());
        int popupMenuHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
        if (menuHeightPercent != 0f)
            popupMenuHeight = (int) (DeviceUtils.getScreenHeight(getContext()) * menuHeightPercent);

        popupMenuViews.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                popupMenuHeight));
        popupMenuViews.setVisibility(GONE);
        containerView.addView(popupMenuViews);

        for (int i = 0; i < popupViews.size(); i++) {
            popupViews.get(i).setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            popupMenuViews.addView(popupViews.get(i));
        }
    }

    /**
     * 设置蒙板
     */
    private void setUpMaskView() {
        maskView = new View(getContext());
        maskView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        maskView.setBackgroundColor(maskColor);
        maskView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMenu();
            }
        });
        containerView.addView(maskView);
        maskView.setVisibility(GONE);
    }

    /**
     * 设置顶部 tab 菜单
     *
     * @param tabTexts
     */
    private void setUpTabMenuIfNeeded(List<String> tabTexts) {
        if (tabTexts.isEmpty()) {
            tabMenuView.setVisibility(View.GONE);
        } else {
            for (int i = 0; i < tabTexts.size(); i++) {
                addTab(tabTexts, i);
            }
        }
    }

    public boolean isSetup() {
        return popupMenuViews != null;
    }

    private void addTab(List<String> tabTexts, final int i) {
        final TextView tab = new DrawableCenterTextView(getContext());
        tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, menuTextSize);
        tab.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
        tab.setTextColor(textUnselectedColor);
        tab.setCompoundDrawablePadding(drawablePadding);
        initMenuIcons();
        tab.setCompoundDrawablesWithIntrinsicBounds(null, null, icons[0], null);
        tab.setText(tabTexts.get(i));
        tab.setPadding(0, menuTextPadding, 0, menuTextPadding);
        //添加点击事件
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switchMenu(i);
            }
        });
        tabMenuView.addView(tab);
    }

    /**
     * 改变选中tab文字
     */
    public void setTabText(List<String> texts) {
        if (tabMenuView.getVisibility() == GONE) return;
        if (texts.size() < tabMenuView.getChildCount())
            return;
        for (int i = 0; i < tabMenuView.getChildCount(); ++i)
            ((TextView) tabMenuView.getChildAt(i)).setText(texts.get(i));
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        if (current_tab_position != -1) {
            setMenuTab(current_tab_position, false);
            popupMenuViews.setVisibility(View.GONE);
            popupMenuViews.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_out));
            maskView.setVisibility(GONE);
            maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_mask_out));
            current_tab_position = -1;

            if (onMenuStateChangeListener != null)
                onMenuStateChangeListener.onMenuClose();
        }
    }

    /**
     * DropDownMenu是否处于可见状态
     */
    public boolean isShowing() {
        return current_tab_position != -1;
    }

    public int getCurrentTab() {
        return current_tab_position;
    }

    /**
     * 切换菜单
     */
    public void switchMenu(int clickPosition) {
        if (current_tab_position == -1) {
            popupMenuViews.setVisibility(View.VISIBLE);
            popupMenuViews.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_in));
            maskView.setVisibility(View.VISIBLE);
            maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_mask_in));
            popupMenuViews.getChildAt(clickPosition).setVisibility(View.VISIBLE);
            for (int i = 0; i < popupMenuViews.getChildCount(); ++i) {
                if (i == clickPosition)
                    popupMenuViews.getChildAt(i).setVisibility(View.VISIBLE);
                else
                    popupMenuViews.getChildAt(i).setVisibility(View.GONE);
            }
            current_tab_position = clickPosition;
            setMenuTab(clickPosition, true);

            if (onMenuStateChangeListener != null)
                onMenuStateChangeListener.onMenuShow(current_tab_position);
        } else {
            if (current_tab_position == clickPosition) {
                closeMenu();
            } else {
                //关闭原来的菜单
                setMenuTab(current_tab_position, false);
                popupMenuViews.getChildAt(current_tab_position).setVisibility(View.GONE);

                //打开新的菜单
                popupMenuViews.getChildAt(clickPosition).setVisibility(View.VISIBLE);
                setMenuTab(clickPosition, true);
                current_tab_position = clickPosition;

                if (onMenuStateChangeListener != null)
                    onMenuStateChangeListener.onMenuShow(current_tab_position);
            }
        }
    }

    private void initMenuIcons() {
        if (icons[0] == null) {
            icons[0] = resource2VectorDrawable(menuUnselectedIcon, menuIconSize);
            icons[1] = resource2VectorDrawable(menuSelectedIcon, menuIconSize);
        }
    }

    private void setMenuTab(int i, boolean isSelected) {
        if (tabMenuView.getVisibility() == GONE) return;

        int p = 0;
        if (isSelected)
            p = 1;

        TextView tv = (TextView) tabMenuView.getChildAt(i);
        if (isSelected)
            tv.setTextColor(textSelectedColor);
        else
            tv.setTextColor(textUnselectedColor);

        tv.setCompoundDrawablesWithIntrinsicBounds(null, null,
                icons[p], null);
    }

    /**
     * @param resourceId drawable resourceId
     * @param iconSize   pixel size
     * @return Resized bitmap
     */
    private Drawable resource2VectorDrawable(final int resourceId, int iconSize) {
        final Context context = getContext();
        final Drawable drawable = AppCompatResources.getDrawable(context, resourceId);

        if (drawable == null) {
            throw new Resources.NotFoundException("Resource not found : %s." + resourceId);
        }

        if (iconSize == 0) {
            iconSize = drawable.getIntrinsicWidth();
        }
        // Resize Bitmap
        return new BitmapDrawable(context.getResources(),
                Bitmap.createScaledBitmap(drawable2Bitmap(drawable, iconSize, iconSize), iconSize, iconSize, true));
    }

    /**
     * Convert to bitmap from drawable
     */
    private Bitmap drawable2Bitmap(final Drawable drawable, final int iconWidth, final int iconHeight) {
        final Bitmap bitmap = Bitmap.createBitmap(iconWidth, iconHeight, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public void setOnMenuStateChangeListener(OnMenuStateChangeListener onMenuStateChangeListener) {
        this.onMenuStateChangeListener = onMenuStateChangeListener;
    }

    public interface OnMenuStateChangeListener {
        void onMenuShow(int tabPosition);

        void onMenuClose();
    }

}
