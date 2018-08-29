#Android 知识点
1.Android的启动模式的使用场景，为什么要使用这种特定的启动模式
	
	模式说明：

	standard 标准模式（默认模式）每次激活activity时都会创建activity实例，并放入任务栈中；

	singleTop 栈顶复用模式 如果在任务栈的栈顶正好存在该activity的实例，就重用该实例（会调用实例的onNewIntent()）,否则就会创建新的实例并放入栈顶，即使栈中已经存在该Activity的实例，只要不在栈顶，都会创建新的实例；

	singleTask 栈内复用模式 如果在栈中已经由该activity的实例，就重用该实例（会调用实例的onNewIntent()），重用时，会让该实例回到栈顶，因此在它上面的实例将会被移出栈，如果栈中不存在该实例，将会创建新的实例入栈； 

	singleInstance 单例模式 在一个新栈中创建该activity的实例，并让多个应用共享该栈中的该activity实例。一旦该模式的activity实例已经存在于某个栈中，任何应用再激活该activity时都会重用该栈中的实例，其效果相当于多个应用共享一个应用，不管谁激活该activity都会进入同一个应用中

	使用场景：
	singleTop:适合接收通知启动的内容显示页面
	singleTask:适合作为程序入口点，比如应用程序中展示的主页（homepage）；假设用户在主页跳转到其他页面，运行多次操作后想返回到主页，假设不使用SingleTask模式，在点击返回的过程中会多次看到主页，这明显就是设计不合理
	singleInstance:适合需要与程序分离开的页面,比如可以从其他应用启动该页面;比如闹钟的提醒
	standard：普通activity

	task:任务，task是一个具有栈结构的对象，一个task可以管理多个activity，启动一个应用，也就创建一个与之对应的task.


2.Android机型的适配，屏幕的适配，原因，本质，怎么处理？
	
	基本概念：
	屏幕尺寸：手机的对角线长度 单位：英寸inch ; 1英寸=2.54cm
	屏幕分辨率：指手机屏幕的竖直方向和水平方向的像素个数；比如常见的720 X 1280 ； 1080 X 1920等等
	屏幕像素密度：dpi(dots per inch )指每英寸的像素点数
	密度无关像素：dp (density-independent pixel)
	独立比例像素：sp (scale-independent pixel)

	屏幕像素密度（dpi） = （宽平方 + 高平方） 的开方 / 屏幕尺寸
	px = density * dp = dpi/ 160 * dp

	原因：由于Android 系统的开放性 ，任何的用户、开发者，OEM厂商，运营商都可以对系统进行定制 于是很容易产生了碎片化：
	1.Android系统的碎片化；比如：小米MIUI、魅族flyme、华为EMUI
	2.屏幕的尺寸的碎片化：5.0寸、5.5、。。。。。
	3.屏幕分辨率的碎片化：1080 * 1920 、 720 * 1280.。。。。
	碎片化的问题会导致同一元素在不同手机上显示不同的问题
	
	屏幕适配的本质就是：使得某一元素在Android不同尺寸、不同分辨率的手机上显示相同的效果

	怎么适配：
	1、布局适配：多采用Relative Layout、约束布局、（最小宽度限定符，屏幕方向限定符）
	2、布局组件适配 ：使用wrap_content、match_parent、weight
	3、图片资源适配:使用.9图，图片采用一套主流的xxhdpi就可以了
	4、用户界面流程适配

	这些都是太麻烦的操作，其实只要你抓住两点：
	1.支持以宽或者高一个维度去适配，保持该维度与设计图上一致
	2.支持dp和sp单位设置

	假设设计图上按Google标准的设计来进行 ，360dp,那些控件也是以dp来设计，那么怎么来控制其他机型各种屏幕的适配呢？
	1.首先确认是以宽还是高为维度进行适配，假设以宽为维度，那么就需要将其他机型的宽都定义360dp,与设计图一致，当然其他机型的屏幕分辨率也是可以知道的，通过DisplayMetrics可以求的，那么只需要修改density的值就可以了，重新设置给DisplayMetrics
	targetDensity = appDisplayMetrics.widthPixels/设计稿的宽（dp）
	targetDensityDpi = 160 * targetDensity
	2.重新把目标的密度设置回给DisplayMetrics


3.Android的框架MVC、MVP、MVVM的原理说明，本质区别，使用场景？

	MVC:Model-View-Controller 针对Android方面来说
	View:对应于xml布局文件
	Controller:指Activity或者fragment ，需要处理业务逻辑，又处理UI
	Model:指实体模型

	MVC的处理逻辑：View传送指令到Controller,接着controller去通知model更新数据，model更新数据后（回调）通知View更新，View根据更新的数据做出显示，这是MVC的工作原理

	优点：分离一部分耗时业务到model，结构更加清晰，缺点：activity作为controller随着业务的增加，代码也会越来越臃肿，并且Model与View可以直接交互，这种耦合对于测试非常的麻烦

 	MVP：Model-View-Presenter 针对Android方面来说
	View:对应Activity或者Fragment
	Model:实体模型（处理网络请求，数据库等）
	Presenter:负责完成View与Model之间的交互和业务逻辑
	
	MVP的处理逻辑：View接收用户的请求，传递请求给Presenter，presenter做逻辑处理，通知model做相关业务，model修改后通知presenter数据变化，presenter更新View

	优点：逻辑清楚，便于阅读，功能添加容易，维护成本低；缺点：相关类比较多，代码量增加，另外如果业务繁杂，那么presenter也会很臃肿。另外还有View与Presenter相互持有引用相互回调，通过接口交互，接口的粒度不好控制

	MVC与MVP的比较
	最明显的是MVP的View与model层不再直接交互，完全的解耦，另外Activity或者fragment在MVP中充当View层的角色

	MVVM：Model-View-ViewModel 针对Android方面来说
	Model:实体模型
	View：Activity或者Fragment
	VM：负责View和Model之间的通信，以此分离视图和数据
	优点：通过使用databinding实现了双向的数据绑定；view功能进一步强化；缺点：数据绑定使得bug很难被定位；对于过大的项目，数据绑定需要花费更多的内存
	
	MVP与MVVM的比较：
	presenter被ViewModel替代 ，viewModel借助了Data Binding的方式实现了双向的数据绑定，减少了视图模块和控制模块的耦合

	注意：ViewModel仅仅专注于业务的逻辑处理，只做和业务逻辑和业务相关的事，UI相关的事情不写在这里面，ViewModel层不会持有任何控件的引用，更不会在ViewModel中通过UI控件的引用去做更新UI的事情


4.Android网络层的一些相关协议HTTP，SOCKET，TCP，推送保活

	

5.在项目中实现了什么

6.如何防止其他应用调用我们的应用的Activity、service

7.ANR的原因，项目中实际产生的场景有哪些？

8.数据库的了解