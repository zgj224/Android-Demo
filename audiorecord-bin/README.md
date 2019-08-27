1.录制单/双通道: 
# ./AudioRecordTest 44100 2 my.pcm  

2.转换pcm到wav：就是给pcm加上头信息，不然播放器不识别.
# ./pcm2wav my.pcm 44100 2 my.wav  

3.播放wav: 
# tinyplay my.wav  
  
