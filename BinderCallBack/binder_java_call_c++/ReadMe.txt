//这里APP需要设置Permissive模式权限,不然会报Fatal
# adb root && adb remount

//设置Permissive模式
# adb shell setenforce 0

//查看
# adb shell getenforce
