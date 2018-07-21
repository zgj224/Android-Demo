#指定版本号
cmake_minimum_required(VERSION 3.4.1)

#编译、添加需要的库文件
add_library( #设置要加载的动态库名称
             native-lib

             #设置为动态库so
             SHARED

             # 指定要编译的源码
             # C++实现JNI
             # src/main/cpp/native-lib.cpp
             # C实现JNI
             src/main/cpp/native-lib.c
             )

#设置生成的so动态库最后输出的路径（测试无效果）
#set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR}/app/src/main/jniLibs/${ANDROID_ABI})

#链接系统库
find_library(log-lib log )

#链接自己编译的库
target_link_libraries( # Specifies the target library.
                       native-lib

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )