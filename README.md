# CustomEditText
A Collection of features are wrapped into single UI element. By adding a line code in xml, able to access these features,rectangular view with corner radius, font color etc.

# Features
1.EditText shape  -> rounded corner with radius and changing BG color, Border color.     
2.Clear icon visibility when typing   
3.Font change via xml     
4.Password visibility icon when inputtype in password mode.      
5.Change the password toggle color.  
6.Add country code in prefix (+91)  
7.Change prefix textcolor.
    
the above features are done through xml file.    

<a href="http://www.methodscount.com/?lib=com.libizo%3Acustomedittext%3A1.0.2"><img src="https://img.shields.io/badge/Methods count-84-e91e63.svg"/></a>  <a href="http://www.methodscount.com/?lib=com.libizo%3Acustomedittext%3A1.0.2"><img src="https://img.shields.io/badge/Size-34 KB-e91e63.svg"/></a>

<a href='https://bintray.com/rajagopalr3/CustomEditText/customedittext/_latestVersion'><img src='https://api.bintray.com/packages/rajagopalr3/CustomEditText/customedittext/images/download.svg'></a>

<a href='https://bintray.com/rajagopalr3/CustomEditText/customedittext?source=watch' alt='Get automatic notifications about new "customedittext" versions'><img src='https://www.bintray.com/docs/images/bintray_badge_color.png'></a>

![Screenshot](screenshot_1.png)


# Implementation    

# Using Jitpack:

[![](https://jitpack.io/v/Rajagopalr3/CustomEditText.svg)](https://jitpack.io/#Rajagopalr3/CustomEditText)

Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.Rajagopalr3:CustomEditText:6f095021b5'
	}



# Using JCenter(Bintray) - DEPRECATED

```

dependencies {
    implementation 'com.libizo:customedittext:1.0.6'
}


```
# Import as Module
Download the below .aar file and add as module into your project

[Donwload module])https://github.com/Rajagopalr3/CustomEditText/blob/master/customedittext-1.0.4.aar


```

dependencies {
     implementation project(':customedittext')
}

Add this into settings.gradle file:
include ':customedittext'

```

# XML

```
 <com.libizo.CustomEditText
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_margin="5dp"
            android:hint="First Name"
            android:inputType="phone"
            android:text="9876543210"
            app:libIZO_setPrefix="+91"
            app:libIZO_setPrefixTextColor="#FF4081"
            app:libIZO_clearIconTint="#8f1f15"
            app:libIZO_setBorderColor="#8f1f15"
            app:libIZO_setBorderView="true"
            app:libIZO_setClearIconVisible="true"
            app:libIZO_setCornerRadius="4dp"
            app:libIZO_setStrokeWidth="2dp"/>

                      
```


# Set Font
  Add your font files into assets folder. In string.xml files find your font files like below

```
    <string name="DroidSansBold">DroidSans-Bold.ttf</string>
    
```


# Attributes

 |        Attributes               |            Description            |         Default Value         |
 | ------------------------------- | -------------------------------   | --------------------------    |
 | libIZO_setBorderView            | if true rectangle view enabled    |  false in default             |
 | libIZO_setBorderColor           | set border color of edittext      |  grey color in default        |
 | libIZO_setStrokeWidth           | set border width in dp            |  1 dp in default              |
 | libIZO_setBackgroundColor       | set BG color of edittext          |  transparent color in default |
 | libIZO_setCornerRadius          | set corner radius of edittextview |  1 dp in default              |
 | libIZO_setFont                  | set font name in string           |  android's default font       |
 | libIZO_setClearIconVisible      | if true clear icon is enabled     |  false in default             |
 | libIZO_clearIconTint            | set clear icon tint color         |  grey color in default        |
 | libIZO_hideShowPasswordIconTint | set password icon tint color      |  grey color in default        |
 | libIZO_setPrefix                | set the country code as prefix    |  empty in default             |
 | libIZO_setPrefixTextColor       | set prefix text color             |  default color                |   
    
 # License
 
 ```
 
Copyright 2017 Rajagopal

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 
```  
    
