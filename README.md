
# # CategoryDialog

## Demo

<img src="https://github.com/mgcoco/CategoryDialog/blob/master/screen/1.gif" width="250px" />

## Gradle Dependency

```
allprojects {
    repositories {
      ...
      maven { url 'https://jitpack.io' }
    }
}

dependencies {
	implementation 'com.github.mgcoco:CategoryDialog:v1.0'
}
```

## Basic
```
//Sometimes subtype data is very huge, so we need to call api again to get subtype data
new CategoryPickerDialog<? extends BaseCategoryModel>(context).setCategoryData(new ArrayList<? extends BaseCategoryModel>(), new CategoryPickerDialog.startRequestSubtypeListener() {  
	  @Override  
	  public void startReuqest(BaseCategoryModel model, CategoryPickerDialog.OnRequestSubtypeListener listener) {  
		  //If you already had subtype data, just call "done" callback to start animation
		  listener.done(new ArrayList<? extends BaseCategoryModel>());  
	  }  
}).setOnPickListener(new CategoryPickerDialog.OnPickCompletedListener() {  
	  @Override  
	  public void onPick(BaseCategoryModel category, BaseCategoryModel subType) {  
		  //Category and subType might be null if nothing selected
		  Toast.makeText(MainActivity.this, category.getName() + ", " + subType.getName(), Toast.LENGTH_LONG).show();  
	  }  
}).setSeletedColor(Color.RED)
.setUnSelectedColor(Color.BLACK)
.setDividerColor(Color.GRAY).show();

