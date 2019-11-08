#include <utils/Log.h>
#include <media/AudioRecord.h>
#include <stdlib.h>
using namespace android;
#define LOG_TAG "AudioRecordTest"
int g_iNotificationPeriodInFrames = 8000/10;

static void *AudioRecordThread(int sample_rate, int channels, void *fileName){
  void *inBuffer = NULL;
  int bufferSizeInBytes;
  int iBytesPerSample = 2; 	// 16bits pcm, 2Bytes
  int frameSize = 0;	// frameSize = channels * iBytesPerSample
  size_t  minFrameCount = 0;	// get from AudroRecord object
  int iWriteDataCount = 0;	// how many data are there write to file
  FILE *g_pAudioRecordFile = NULL;
  AudioRecord *pAudioRecord = NULL;
  audio_channel_mask_t channelConfig  = AUDIO_CHANNEL_IN_MONO;

  g_pAudioRecordFile = fopen((char *)fileName, "wb+");
  
  channelConfig = audio_channel_in_mask_from_count(channels);//将channels通道数转换成掩码传下去,在hal层再把掩码转换成通道数

  printf("sample_rate = %d, channels = %d, channelConfig = 0x%x\n", sample_rate, channels, channelConfig);
  frameSize = channels * iBytesPerSample;
  android::status_t status = android::AudioRecord::getMinFrameCount(&minFrameCount, sample_rate, AUDIO_FORMAT_PCM_16_BIT, channelConfig);
  if(status != android::NO_ERROR){
    ALOGE("%s  AudioRecord.getMinFrameCount fail \n", __FUNCTION__);
    goto exit ;
  }
  ALOGE("sample_rate = %d minFrameCount = %d channels = %d channelConfig = 0x%x frameSize = %d ", sample_rate, minFrameCount, channels, channelConfig, frameSize);
  bufferSizeInBytes = minFrameCount * frameSize;
  inBuffer = malloc(bufferSizeInBytes);

  g_iNotificationPeriodInFrames = sample_rate/10;
  pAudioRecord  = new android::AudioRecord(String16("Record"));//Android7.0+
  //pAudioRecord  = new AudioRecord(); //Android4.4
  pAudioRecord->set(AUDIO_SOURCE_MIC,
		    sample_rate,
		    AUDIO_FORMAT_PCM_16_BIT,
		    channelConfig,
		    0,
		    NULL, //AudioRecordCallback,
		    NULL,
		    0,
		    true,
		    AUDIO_SESSION_ALLOCATE);

  if(pAudioRecord->initCheck() != android::NO_ERROR){
    ALOGE("AudioTrack initCheck error!");
    goto exit;
  }

  if(pAudioRecord->start()!= android::NO_ERROR){
    ALOGE("AudioTrack start error!");
    goto exit;
  }

  while(true){
    int readLen = pAudioRecord->read(inBuffer, bufferSizeInBytes);
    int writeResult = -1;

    if(readLen > 0){
      iWriteDataCount += readLen;
      if(NULL != g_pAudioRecordFile){
	writeResult = fwrite(inBuffer, 1, readLen, g_pAudioRecordFile);
	if(writeResult < readLen)
	  ALOGE("Write Audio Record Stream error");
      }
    }else{
      ALOGE("pAudioRecord->read  readLen = 0");
    }
  }

 exit:
  if(NULL != g_pAudioRecordFile){
    fflush(g_pAudioRecordFile);
    fclose(g_pAudioRecordFile);
    g_pAudioRecordFile = NULL;
  }

  if(pAudioRecord)
    pAudioRecord->stop();

  if(inBuffer){
    free(inBuffer);
    inBuffer = NULL;
  }
  ALOGD("%s  Thread ID  = %d  quit\n", __FUNCTION__,  pthread_self());
  return NULL;
}

int main(int argc, char **argv){
  if (argc != 4){
    printf("Usage:\n");
    printf("%s <sample_rate> <channels> <out_file>\n", argv[0]);
    return -1;
  }
  AudioRecordThread(strtol(argv[1], NULL, 0), strtol(argv[2], NULL, 0), argv[3]);
  return 0;
}
