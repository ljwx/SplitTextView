# SplitTextView

build.gradle 引入
```
implementation 'io.github.ljwx:SplitTextView:0.3.5'
```

效果预览

![](https://github.com/ljwx/BlogImage/blob/master/split_textview_demo.jpg)

```
<LinearLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#3000"
    android:orientation="vertical"
    android:padding="5dp">
    
    <com.ljwx.view.SplitTextView
        android:id="@+id/split"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginLeft="10dp"
        android:background="@color/white"
        android:paddingLeft="16dp"
        android:paddingRight="10dp"
        android:text="TextView "
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:stvCenterColor="@android:color/holo_green_light"
        app:stvCenterMarginLeft="12dp"
        app:stvCenterSize="18dp"
        app:stvCenterText="中文"
        app:stvDrawableLeft="@mipmap/ic_launcher"
        app:stvDrawableLeftSize="30dp"
        app:stvDrawableLeftTextPadding="10dp"
        app:stvDrawableRight="@mipmap/ic_launcher"
        app:stvDrawableRightTextPadding="30dp"
        app:stvLeftText="asdf"
        app:stvRightBold="true"
        app:stvRightSize="14dp"
        app:stvRightText="中文" />
        
    <com.ljwx.view.SplitTextView
        android:id="@+id/split2"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginLeft="10dp"
        android:layout_marginTop="10dp"
        android:background="@color/white"
        android:paddingLeft="16dp"
        android:paddingRight="10dp"
        android:text="TextView"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:stvCenterColor="@android:color/holo_green_light"
        app:stvCenterSize="18dp"
        app:stvCenterText="中文"
        app:stvDrawableRight="@mipmap/ic_launcher"
        app:stvDrawableRightTextPadding="30dp"
        app:stvLeftText="asdf"
        app:stvRightBold="true"
        app:stvRightSize="14dp"
        app:stvRightText="中文" />
</LinearLayout>


// MainActivity
val stv = findViewById<SplitTextView>(R.id.split
stv.setTextCenter("中间文字")
stv.setColorLeft(Color.parseColor("#000000"))
```


属性值
```
// 左边文字属性 
// 如果颜色大小等没有单独设置,则使用TexitView设置的值
<attr name="stvLeftText" format="string" />             // 文字内容
<attr name="stvLeftColor" format="color" />             // 文字颜色
<attr name="stvLeftSize" format="dimension" />          // 文字大小
<attr name="stvLeftBold" format="boolean" />            // 文字加粗

// 中间文字属性
<attr name="stvCenterText" format="string" />
<attr name="stvCenterColor" format="color" />
<attr name="stvCenterSize" format="dimension" />
<attr name="stvCenterBold" format="boolean" />
<attr name="stvCenterMarginLeft" format="dimension" />  // 中间文字的左边间隔
<attr name="stvCenterMarginRight" format="dimension" /> // 中间文字的右边间隔

// 右边文字属性
<attr name="stvRightText" format="string" />
<attr name="stvRightColor" format="color" />
<attr name="stvRightSize" format="dimension" />
<attr name="stvRightBold" format="boolean" />

// 左边Drawable属性
<attr name="stvDrawableLeft" format="reference" />      // 左边图标
<attr name="stvDrawableLeftSize" format="dimension" />  // 左边图标大小(宽高一致)
<attr name="stvDrawableLeftTextPadding" format="dimension" />   // 左边图标与右边文字的间距

// 右边Drawable属性
<attr name="stvDrawableRight" format="reference" />
<attr name="stvDrawableRightSize" format="dimension" />
<attr name="stvDrawableRightTextPadding" format="dimension" />
```
