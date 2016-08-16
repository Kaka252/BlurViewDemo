package com.zhouyou.blurred;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.zhouyou.blurred.utils.BlurBitmap;
import com.zhouyou.blurred.utils.BlurredUtil;

/**
 * 作者：ZhouYou
 * 日期：2016/8/16.
 */
public class BlurView extends RelativeLayout {

    /**
     * 模糊最大化值
     */
    private static final int ALPHA_MAX_VALUE = 255;

    /**
     * Context
     */
    private Context mContext;

    /**
     * 模糊后的ImageView
     */
    private ImageView mBlurredImg;

    /**
     * 原图ImageView
     */
    private ImageView mOriginImg;
    /**
     * 原图Bitmap
     */
    private Bitmap mOriginBitmap;

    /**
     * 模糊后的Bitmap
     */
    private Bitmap mBlurredBitmap;

    /**
     * 是否禁用模糊效果
     */
    private boolean isDisableBlurred;

    /**
     * 是否移动背景图片
     */
    private boolean isMove;

    public BlurView(Context context) {
        this(context, null);
    }

    public BlurView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BlurView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        initAttr(context, attrs);
    }

    private void init(Context context) {
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.view_blurred, this);
        mOriginImg = (ImageView) findViewById(R.id.iv_original_image);
        mBlurredImg = (ImageView) findViewById(R.id.iv_blurred_image);
    }

    private void initAttr(Context context, AttributeSet attrs) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BlurredView);
        Drawable drawable = typedArray.getDrawable(R.styleable.BlurredView_src);
        isMove = typedArray.getBoolean(R.styleable.BlurredView_move, false);
        isDisableBlurred = typedArray.getBoolean(R.styleable.BlurredView_disableBlurred, false);

        typedArray.recycle();

        // blur image
        if (null != drawable) {
            mOriginBitmap = BlurredUtil.drawableToBitmap(drawable);
            mBlurredBitmap = BlurBitmap.blur(context, mOriginBitmap);
        }

        // setVisibility
        if (!isDisableBlurred) {
            mBlurredImg.setVisibility(VISIBLE);
        }

        // setMove
        if (null != drawable) {
            setMove(context, isMove);
        }
    }

    /**
     * 设置背景图片移动效果
     * @param context   上下文对象
     * @param isMove    是否移动
     */
    private void setMove(Context context, boolean isMove) {
        if (isMove) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int height = point.y;
            setBlurredHeight(height, mOriginImg);
            setBlurredHeight(height, mBlurredImg);
        }
    }

    /**
     * 改变图片的高度
     *
     * @param height        图片的高度
     * @param imageView     imageview对象
     */
    private void setBlurredHeight(int height, ImageView imageView) {
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = height + 100;
        imageView.requestLayout();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setImageView();
    }

    /**
     * 填充ImageView.
     */
    private void setImageView() {
        mBlurredImg.setImageBitmap(mBlurredBitmap);
        mOriginImg.setImageBitmap(mOriginBitmap);
    }

    /**
     * 以代码的方式添加待模糊的图片
     *
     * @param blurredBitmap 待模糊的图片
     */
    public void setBlurredImg(Bitmap blurredBitmap) {
        if (null != blurredBitmap) {
            mOriginBitmap = blurredBitmap;
            mBlurredBitmap = BlurBitmap.blur(mContext, blurredBitmap);
            setImageView();
            setMove(mContext, isMove);
        }
    }

    /**
     * 以代码的方式添加待模糊的图片
     *
     * @param blurDrawable 待模糊的图片
     */
    public void setBlurredImg(Drawable blurDrawable) {
        if (null != blurDrawable) {
            mOriginBitmap = BlurredUtil.drawableToBitmap(blurDrawable);
            mBlurredBitmap = BlurBitmap.blur(mContext, mOriginBitmap);
            setImageView();
            setMove(mContext, isMove);
        }
    }

    /**
     * 设置模糊程度
     *
     * @param level 模糊程度, 数值在 0~100 之间.
     */
    public void setBlurredLevel(int level) {
        if (level < 0 || level > 100) {
            throw new IllegalStateException("No validate level, the value must be 0~100");
        }
        if (isDisableBlurred) {
            return;
        }
        mOriginImg.setAlpha((int) (ALPHA_MAX_VALUE - level * 2.55));
    }

    /**
     * 设置图片上移的距离
     *
     * @param height 向上移动的距离
     */
    public void setBlurredTop(int height) {
        mOriginImg.setTop(-height);
        mBlurredImg.setTop(-height);
    }

    /**
     * 显示模糊图片
     */
    public void showBlurredView() {
        mBlurredImg.setVisibility(VISIBLE);
    }

    /**
     * 禁用模糊效果
     */
    public void disableBlurredView() {
        isDisableBlurred = true;
        mOriginImg.setAlpha(ALPHA_MAX_VALUE);
        mBlurredImg.setVisibility(INVISIBLE);
    }

    /**
     * 启用模糊效果
     */
    public void enableBlurredView() {
        isDisableBlurred = false;
        mBlurredImg.setVisibility(VISIBLE);
    }
}
