#  ExpandableCardView Version 1.0.1

Sample
![Alt Text](https://github.com/mahimrocky/ExpandableCardView/blob/master/gif/example.gif)

Following instructions you have to follow to use Expandable Card view

# Root Gradle
```sh
    allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

# App Gradle:

```sh
dependencies {
	        implementation 'com.github.mahimrocky:ExpandableCardView:1.0.1'
	}
```

# XML Section

```sh
<com.skyhope.expandcollapsecardview.ExpandCollapseCard
        android:id="@+id/collapse"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:item_inner_view="@layout/item_text"
        app:title="This is title"
        app:title_color="#000000"
        app:title_size="16sp" />
```

### By below options you can change or customize the view

| Attributes | Purpose |
| ------ | ------ |
| ```app:title```|  To change main title|
| ```app:title_size```|  To change main title size|
| ```app:title_color```|  To change main title color|
| ```app:title_background_color```|  To change title bar background color|
| ```app:expand_icon```|  To change expand icon|
| ```app:collapse_icon```|  To change collapse icon|
| ```app:item_inner_view```|  To set your custom inner view|

### By below java options you can change property
| Method | Purpose |
| ------ | ------ |
|``` setTitleTextColor(int color) ```| Change title text color|
|``` setTitleText(String text) ```| Change title text|
|``` getChildView() ```| This method give you your innerview reference by this you can change or find your inner view property|
|``` isExpand() ```| To find card is expand opr not|

### How to acccess inner view element:

So here is a trick to get your inner layout others or child view access. You have t use ``` getChildView() ``` method. Its give you a ``` view ``` By this ``` view ``` you will get your inner view property. Example: 
```sh
Button btn = (Button)view.findViewById(R.id.button_id);
```

### To get cards expand collapse listener 
``` Implement ExpandCollapseListener``` In your activity or fragment section
and call
```sh 
    ExpandCollapseCard collapseCard = findViewById(R.id.collapse);
        collapseCard.initListener(this);
```
you will get this like
```sh
@Override
    public void onExpandCollapseListener(boolean isExpand) {
        Log.d("expand_listener", "isExpand: " + isExpand);
    }
```

# Happy coding
