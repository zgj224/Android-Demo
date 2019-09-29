#include <utils/Log.h>
#include <media/AudioRecord.h>
#include <stdlib.h>

using namespace android;

#ifdef LOG_TAG
#undef LOG_TAG
#endif
#define LOG_TAG "AudioRecordTest"

static pthread_t    g_AudioRecordThread;
static pthread_t *  g_AudioRecordThreadPtr = NULL;

volatile bool  g_bQuitAudioRecordThread = false;
volatile int   g_iInSampleTime = 0;
int 	       g_iNotificationPeriodInFrames = 8000/10;

static void * AudioRecordThread(int sample_rate, int channels, void *fileName)
{
  uint64_t  inHostTime = 0;
  void *    inBuffer = NULL;
  audio_source_t  inputSource = AUDIO_SOURCE_MIC;
  audio_format_t  audioFormat = AUDIO_FORMAT_PCM_16_BIT;
  audio_channel_mask_t 	channelConfig  = AUDIO_CHANNEL_IN_MONO;
  int bufferSizeInBytes;
  int sampleRateInHz = sample_rate; //8000; //44100;
  android::AudioRecord *pAudioRecord = NULL;
  FILE *g_pAudioRecordFile = NULL;
  char *strAudioFile = (char *)fileName;

  int iNbChannels = channels;	// 1 channel for mono, 2 channel for streo
  int iBytesPerSample = 2; 	// 16bits pcm, 2Bytes
  int frameSize = 0;	// frameSize = iNbChannels * iBytesPerSample
  size_t  minFrameCount = 0;	// get from AudroRecord object
  int iWriteDataCount = 0;	// how many data are there write to file

  // log the thread id for debug info
  ALOGD("%s  Thread ID  = %d  \n", __FUNCTION__,  pthread_self());

  g_iInSampleTime = 0;
  g_pAudioRecordFile = fopen(strAudioFile, "wb+");
  if (iNbChannels == 2) {
    channelConfig = AUDIO_CHANNEL_IN_STEREO;
  }
  printf("sample_rate = %d, channels = %d, iNbChannels = %d, channelConfig = 0x%x\n", sample_rate, channels, iNbChannels, channelConfig);

  frameSize 	= iNbChannels * iBytesPerSample;

  android::status_t status = android::AudioRecord::getMinFrameCount(&minFrameCount, sampleRateInHz, audioFormat, channelConfig);

  if(status != android::NO_ERROR)
  {
    ALOGE("%s  AudioRecord.getMinFrameCount fail \n", __FUNCTION__);
    goto exit ;
  }

  ALOGE("sampleRateInHz = %d minFrameCount = %d iNbChannels = %d channelConfig = 0x%x frameSize = %d ",
	sampleRateInHz, minFrameCount, iNbChannels, channelConfig, frameSize);

  bufferSizeInBytes = minFrameCount * frameSize;
  inBuffer = malloc(bufferSizeInBytes);
  if(inBuffer == NULL)
    {
      ALOGE("%s  alloc mem failed \n", __FUNCTION__);
      goto exit ;
    }

  g_iNotificationPeriodInFrames = sampleRateInHz/10;

  pAudioRecord  = new android::AudioRecord(String16("Record")); //Android7.1 api
  //pAudioRecord  = new AudioRecord(); //Android4.4 api
  if(NULL == pAudioRecord)
    {
      ALOGE(" create native AudioRecord failed! ");
      goto exit;
    }

  pAudioRecord->set( inputSource,
		     sampleRateInHz,
		     audioFormat,
		     channelConfig,
		     0,
		     NULL, //AudioRecordCallback,
		     NULL,
		     0,
		     true,
		     AUDIO_SESSION_ALLOCATE);

  if(pAudioRecord->initCheck() != android::NO_ERROR)
    {
      ALOGE("AudioTrack initCheck error!");
      goto exit;
    }

  if(pAudioRecord->start()!= android::NO_ERROR)
    {
      ALOGE("AudioTrack start error!");
      goto exit;
    }

  while (!g_bQuitAudioRecordThread)
    {
      int readLen = pAudioRecord->read(inBuffer, bufferSizeInBytes);
      int writeResult = -1;

      if(readLen > 0)
	{
	  iWriteDataCount += readLen;
	  if(NULL != g_pAudioRecordFile)
	    {
	      writeResult = fwrite(inBuffer, 1, readLen, g_pAudioRecordFile);
	      if(writeResult < readLen)
		{
		  ALOGE("Write Audio Record Stream error");
		}
	    }

	  //ALOGD("readLen = %d  writeResult = %d  iWriteDataCount = %d", readLen, writeResult, iWriteDataCount);
	}
      else
	{
	  ALOGE("pAudioRecord->read  readLen = 0");
	}
    }

 exit:
  if(NULL != g_pAudioRecordFile)
    {
      fflush(g_pAudioRecordFile);
      fclose(g_pAudioRecordFile);
      g_pAudioRecordFile = NULL;
    }

  if(pAudioRecord)
    {
      pAudioRecord->stop();
      //delete pAudioRecord;
      //pAudioRecord == NULL;
    }

  if(inBuffer)
    {
      free(inBuffer);
      inBuffer = NULL;
    }

  ALOGD("%s  Thread ID  = %d  quit\n", __FUNCTION__,  pthread_self());
  return NULL;
}

int main(int argc, char **argv)
{
  if (argc != 4)
    {
      printf("Usage:\n");
      printf("%s <sample_rate> <channels> <out_file>\n", argv[0]);
      return -1;
    }
  AudioRecordThread(strtol(argv[1], NULL, 0), strtol(argv[2], NULL, 0), argv[3]);
  return 0;
}
