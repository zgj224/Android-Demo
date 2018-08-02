## Framework Binder
### 运行

首先将ServerDemo，ClientDemo可执行文件，以及ServerDemo.jar，ClientDemo.jar都push到手机。

	adb push ServerDemo /system/bin
	adb push ClientDemo /system/bin
	adb push ServerDemo.jar /system/framework
	adb push ClientDemo.jar /system/framework 

