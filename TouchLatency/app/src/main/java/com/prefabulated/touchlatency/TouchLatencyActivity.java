/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.prefabulated.touchlatency;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.app.Activity;
import android.os.Bundle;
import android.text.method.Touch;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

class TouchLatencyView extends View implements View.OnTouchListener {
  private static final String LOG_TAG = "TouchLatency";
  private static final int BACKGROUND_COLOR = 0xFF400080;
  private static final int INNER_RADIUS = 70;
  private static final int BALL_RADIUS = 100;

  private int mCount;
  private long mDelta;
  private long mInvalidateTime;
  private long mFirstDelt;
  private long mEventDelta;
  private long mPrevious;
  private long mCurrent;
  private long mTotalEventCount;
  private long mDownTime;
  private long mUpTime;
  private int framesCount = 0;
  private int framesCountAvg = 0;
  private long framesTimer = 0;
  private Context mContext;

  public TouchLatencyView(Context context, AttributeSet attrs) {
    super(context, attrs);
    setOnTouchListener(this);
    setWillNotDraw(false);
    mContext = context;
    mBluePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mBluePaint.setColor(0xFF0000FF);
    mBluePaint.setStyle(Paint.Style.FILL);
    mGreenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mGreenPaint.setColor(0xFF00FF00);
    mGreenPaint.setStyle(Paint.Style.FILL);
    mYellowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mYellowPaint.setColor(0xFFFFFF00);
    mYellowPaint.setStyle(Paint.Style.FILL);
    mRedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mRedPaint.setColor(0xFFFF0000);
    mRedPaint.setStyle(Paint.Style.FILL);

    mInfoPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mInfoPaint.setTextSize(25);
    mInfoPaint.setColor(0xFF00FF00);

    mTouching = false;

    mBallX = 100.0f;
    mBallY = 100.0f;
    mVelocityX = 2.0f;
    mVelocityY = 2.0f;


  }

  @Override
  public boolean onTouch(View view, MotionEvent event) {
    long now = android.os.SystemClock.uptimeMillis();
    int action = event.getActionMasked();
    if (action == MotionEvent.ACTION_DOWN ) {
      mTouching = true;
      mDelta = 0;
      mCount = 0;
      mInvalidateTime = 0;
      mEventDelta = 0;
      mTotalEventCount = 0;
      mPrevious = event.getEventTime();
      mCurrent = mPrevious;
      mDownTime = event.getEventTime();
      mUpTime = mDownTime;
      invalidate();
    } else if (action == MotionEvent.ACTION_MOVE) {
      if (mCount == 0) {
	mFirstDelt = now - event.getDownTime();
      }
      mPrevious = mCurrent;
      mCurrent = event.getEventTime();
      mEventDelta = mEventDelta + mCurrent - mPrevious;
      mTouching = true;
      mTotalEventCount = mTotalEventCount + event.getHistorySize();
      mCount++;
      mDelta = mDelta + now - event.getEventTime();
      long start = System.currentTimeMillis();
      invalidate();
      long end = System.currentTimeMillis();
      mInvalidateTime = mInvalidateTime + end - start;
      mUpTime = mCurrent;
    } else if (action == MotionEvent.ACTION_UP) {
      mTouching = false;
      invalidate();
      mUpTime = event.getEventTime();
      return true;
    } else {
      return true;
    }
    mTouchX = event.getX();
    mTouchY = event.getY();
    return true;
  }

  private void drawTouch(Canvas canvas) {
    if (!mTouching) {
      Log.d(LOG_TAG, "Filling background");
      canvas.drawColor(BACKGROUND_COLOR);
      return;
    }

    float deltaX = (mTouchX - mLastDrawnX);
    float deltaY = (mTouchY - mLastDrawnY);
    float scaleFactor = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY) * 1.5f;

    mLastDrawnX = mTouchX;
    mLastDrawnY = mTouchY;

    canvas.drawColor(BACKGROUND_COLOR);
    canvas.drawCircle(mTouchX, mTouchY, INNER_RADIUS + 3 * scaleFactor, mRedPaint);
    canvas.drawCircle(mTouchX, mTouchY, INNER_RADIUS + 2 * scaleFactor, mYellowPaint);
    canvas.drawCircle(mTouchX, mTouchY, INNER_RADIUS + scaleFactor, mGreenPaint);
    canvas.drawCircle(mTouchX, mTouchY, INNER_RADIUS, mBluePaint);
  }

  private void drawBall(Canvas canvas) {
    int width = canvas.getWidth();
    int height = canvas.getHeight();

    // Update position
    mBallX += mVelocityX;
    mBallY += mVelocityY;

    // Clamp and change velocity if necessary
    float left = mBallX - BALL_RADIUS;
    if (left < 0) {
      left = 0;
      mVelocityX *= -1;
    }

    float top = mBallY - BALL_RADIUS;
    if (top < 0) {
      top = 0;
      mVelocityY *= -1;
    }

    float right = mBallX + BALL_RADIUS;
    if (right > width) {
      right = width;
      mVelocityX *= -1;
    }

    float bottom = mBallY + BALL_RADIUS;
    if (bottom > height) {
      bottom = height;
      mVelocityY *= -1;
    }

    // Draw the ball
    canvas.drawColor(BACKGROUND_COLOR);
    //canvas.drawOval(left, top, right, bottom, mYellowPaint);
    canvas.drawOval(new RectF(left, top, right, bottom), mYellowPaint);
    invalidate();
  }

  @Override
  public void draw(Canvas canvas) {
    super.draw(canvas);
    long end = System.currentTimeMillis();
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    if (mMode == 0) {
      drawTouch(canvas);
      if (mCount != 0) {
	canvas.drawText(mContext.getString(R.string.touch_duration) + ":" + (mUpTime - mDownTime) + "ms, " +
			mContext.getString(R.string.touch_move_time) + ":" + mCount, 15, 60, mInfoPaint);
	canvas.drawText(mContext.getString(R.string.touch_first_delta) + ":" + mFirstDelt + "ms", 15, 90, mInfoPaint);
	canvas.drawText(mContext.getString(R.string.touch_move_delta) + ":" + (mEventDelta/mCount) + "ms", 15, 120, mInfoPaint);
	canvas.drawText(mContext.getString(R.string.touch_move_package) + ":" + (mTotalEventCount * 1.0/mCount), 15, 150, mInfoPaint);
      }
    } else {
      drawBall(canvas);
    }
    canvas.drawText("FPS: " + framesCountAvg + " fps", 15, 30, mInfoPaint);
    long now = System.currentTimeMillis();
    framesCount++;
    if (now - framesTimer > 1000) {
      framesTimer = now;
      framesCountAvg = framesCount;
      framesCount = 0;
    }
  }

  public void changeMode(MenuItem item) {
    final int NUM_MODES = 2;
    final String modes[] = {"Touch", "Ball"};
    mMode = (mMode + 1) % NUM_MODES;
    invalidate();
    item.setTitle(modes[mMode]);
  }

  private Paint mBluePaint, mGreenPaint, mYellowPaint, mRedPaint, mInfoPaint;
  private int mMode;

  private boolean mTouching;
  private float mTouchX, mTouchY;
  private float mLastDrawnX, mLastDrawnY;

  private float mBallX, mBallY;
  private float mVelocityX, mVelocityY;
}

public class TouchLatencyActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_touch_latency);
    mTouchView = (TouchLatencyView) findViewById(R.id.canvasView);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_touch_latency, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      mTouchView.changeMode(item);
    }

    return super.onOptionsItemSelected(item);
  }

  private TouchLatencyView mTouchView;
}
