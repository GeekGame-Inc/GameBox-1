package com.tenone.gamebox.view.custom;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RoundImageView extends ImageView {
	//Բ�Ǵ�С��Ĭ��Ϊ10
	private int mBorderRadius = 10;
	private Paint mPaint;
	// 3x3 ������Ҫ������С�Ŵ�
	private Matrix mMatrix;
	//��Ⱦͼ��ʹ��ͼ��Ϊ����ͼ����ɫ
	private BitmapShader mBitmapShader;

	public RoundImageView(Context context) {
		this( context, null );
	}

	public RoundImageView(Context context, AttributeSet attrs) {
		this( context, attrs, 0 );
	}

	public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super( context, attrs, defStyleAttr );
		mMatrix = new Matrix();
		mPaint = new Paint();
		mPaint.setAntiAlias( true );
	}


	@Override

	protected void onDraw(Canvas canvas) {
		if (getDrawable() == null) {
			return;
		}
		Bitmap bitmap = drawableToBitamp( getDrawable() );
		mBitmapShader = new BitmapShader( bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP );
		float scale = 1.0f;
		if (!(bitmap.getWidth() == getWidth() && bitmap.getHeight() == getHeight())) {
			// ���ͼƬ�Ŀ���߸���view�Ŀ�߲�ƥ�䣬�������Ҫ���ŵı��������ź��ͼƬ�Ŀ�ߣ�һ��Ҫ��������view�Ŀ�ߣ�������������ȡ��ֵ��
			scale = Math.max( getWidth() * 1.0f / bitmap.getWidth(),
					getHeight() * 1.0f / bitmap.getHeight() );
		}
		// shader�ı任��������������Ҫ���ڷŴ������С
		mMatrix.setScale( scale, scale );
		// ���ñ任����
		mBitmapShader.setLocalMatrix( mMatrix );
		// ����shader
		mPaint.setShader( mBitmapShader );
		canvas.drawRoundRect( new RectF( 0, 0, getWidth(), getHeight() ), mBorderRadius, mBorderRadius, mPaint );
	}


	private Bitmap drawableToBitamp(Drawable drawable) {
		if (drawable instanceof BitmapDrawable) {
			BitmapDrawable bd = (BitmapDrawable) drawable;
			return bd.getBitmap();
		}
		// �����ò�ΪͼƬ��Ϊ��ɫʱ����ȡ��drawable��߻������⣬���е�Ϊ��ɫʱ���ȡ�ؼ��Ŀ��
		int w = drawable.getIntrinsicWidth() <= 0 ? getWidth() : drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight() <= 0 ? getHeight() : drawable.getIntrinsicHeight();
		Bitmap bitmap = Bitmap.createBitmap( w, h, Bitmap.Config.ARGB_8888 );
		Canvas canvas = new Canvas( bitmap );
		drawable.setBounds( 0, 0, w, h );
		drawable.draw( canvas );
		return bitmap;
	}

	public void setmBorderRadius(int mBorderRadius) {
		this.mBorderRadius = mBorderRadius;
		invalidate();
	}
}

