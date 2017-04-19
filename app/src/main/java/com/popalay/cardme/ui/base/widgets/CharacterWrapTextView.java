package com.popalay.cardme.ui.base.widgets;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;

public class CharacterWrapTextView extends AppCompatTextView {

    private static final String TAG = "CharacterWrapTextView";
    private int textWidth;

    public CharacterWrapTextView(Context context) {
        super(context);
    }

    public CharacterWrapTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CharacterWrapTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        applyLetterSpacing(text);
        super.setText(text, type);
    }

    private void applyLetterSpacing(CharSequence text) {
        final Rect bounds = new Rect();
        getPaint().getTextBounds(text.toString(), 0, text.length(), bounds);
        textWidth = bounds.width();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int width = getMeasuredWidth();
        //TODO calc correct letter spacing
        Log.d(TAG, "onMeasure: " + getLetterSpacing());
        final float spacing = 1f - (float) textWidth / width - 0.1f;
        Log.d(TAG, "onMeasure: " + getLetterSpacing());
        setLetterSpacing(spacing);
    }
}