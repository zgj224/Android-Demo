#!/bin/bash
PACKAGE=android.hardware.led@1.0
#LOC=hardware/interfaces/led/1.0/default
LOC=./hardware/interfaces/led/1.0/default
# make hidl-gen -j16

hidl-gen -o $LOC -Lc++-impl  -randroid.hidl:system/libhidl/transport $PACKAGE
#hidl-gen -o $LOC -Landroidbp-impl -randroid.hardware:hardware/interfaces -randroid.hidl:system/libhidl/transport $PACKAGE

