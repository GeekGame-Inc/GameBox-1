package com.tenone.gamebox.view.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.tenone.gamebox.R;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;

public class DrawView extends View {
    private Context mContext;
    private WindowManager windowManager;
    private Rect rect;
    private WindowManager.LayoutParams gmBallParams;
    private Paint ballPaint;
    private float startX, startY, tempX, tempY;
    private Bitmap bitmap;
    public int width = 150;
    public int height = 50;
    public int screenWidth;
    public int screenHeight;
    private int titleBarHeigh = 0;

    public DrawView(Context context) {
        this( context, null );
    }

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        this( context, attrs, 0 );
    }

    public DrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super( context, attrs, defStyleAttr );
        init( context );
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw( canvas );
        canvas.drawBitmap( bitmap, rect, rect, ballPaint );
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getRawX();
                startY = event.getRawY();
                tempX = event.getRawX();
                tempY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (windowManager != null) {
                    float x = event.getRawX() - startX;
                    float y = event.getRawY() - startY;
                    int[] location = new int[2];
                    this.getLocationOnScreen( location );
                    int bx = location[0];
                    int by = location[1];
                    if (event.getRawX() > 0 && event.getRawX() < screenWidth) {
                        // ����ƫ������ˢ����ͼ
                        if (bx < 0) {
                            gmBallParams.x = 0;
                        } else if (bx > screenWidth - width) {
                            gmBallParams.x = screenWidth - width;
                        } else {
                            gmBallParams.x += x;
                        }
                        if (by < (DisplayMetricsUtils.getStatusBarHeight( getContext() ) + titleBarHeigh)) {
                            gmBallParams.y = DisplayMetricsUtils.getStatusBarHeight( getContext() ) + titleBarHeigh;
                        } else if (by > screenHeight - height) {
                            gmBallParams.y = screenHeight - height;
                        } else {
                            gmBallParams.y += y;
                        }
                        update();
                        startX = event.getRawX();
                        startY = event.getRawY();
                    }
                    location = null;
                }
                break;
            case MotionEvent.ACTION_UP:
                float endX = event.getRawX();
                float endY = event.getRawY();
                // �����ʼ������������������ֵ����6�����أ������ظõ���¼�,����������ݣ����¼�����OnClickListener��������
                if (Math.abs( endX - tempX ) > 6 && Math.abs( endY - tempY ) > 6) {
                    return true;
                }
                break;
        }
        return super.onTouchEvent( event );
    }

    private void init(Context context) {
        this.mContext = context;
        windowManager = (WindowManager) context
                .getSystemService( Context.WINDOW_SERVICE );
        screenWidth = DisplayMetricsUtils.getScreenWidth( context );
        screenHeight = DisplayMetricsUtils.getScreenHeight( context );
        width = DisplayMetricsUtils.dipTopx( context, 50 );
        height = DisplayMetricsUtils.dipTopx( context, 50 );
        ballPaint = new Paint();
        Bitmap src = BitmapFactory.decodeResource( context.getResources(),
                R.drawable.icon_now_share );
        // ��ͼƬ�ü���ָ����С
        bitmap = Bitmap.createScaledBitmap( src, width, height, true );
        rect = new Rect( 0, 0, bitmap.getWidth(), bitmap.getHeight() );
        if (gmBallParams == null) {
            gmBallParams = new WindowManager.LayoutParams();
            gmBallParams.width = width;
            gmBallParams.height = height;
            gmBallParams.gravity = Gravity.TOP | Gravity.START;
            gmBallParams.type = WindowManager.LayoutParams.TYPE_APPLICATION;
            gmBallParams.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
            gmBallParams.format = PixelFormat.RGBA_8888;
            gmBallParams.x = screenWidth - DisplayMetricsUtils.dipTopx( context,20 )-width;
            gmBallParams.y = screenHeight - DisplayMetricsUtils.dipTopx( context,20 )-height;
        }
    }


    public void update() {
        if (windowManager != null) {
            windowManager.updateViewLayout( this, gmBallParams );
        }
    }

    public void hide() {
        if (windowManager != null) {
            windowManager.removeView( this );
        }
    }

    public void show() {
        if (windowManager != null) {
            windowManager.addView( this, gmBallParams );
        }
    }


    public void setTitleBarHeigh(int titleBarHeigh) {
        this.titleBarHeigh = titleBarHeigh;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension( width, height );
    }

}

