package ru.deliver.deliverApp.Utils;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

/**
 * Created by 1 on 20.08.13.
 *
 * Редактирование текста с кнопкой внутри
 */
public final class EditWithDrawable extends EditText
{
    //-------------------------------
    //CONSTANTS
    //-------------------------------

    //-------------------------------
    //VARIABLES
    //-------------------------------

    private Drawable    mRight;
    private Rect        mBound;

    //-------------------------------
    //CONSTRUCTORS
    //-------------------------------
    public EditWithDrawable(Context context)
    {
        super(context);
    }

    public EditWithDrawable(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public EditWithDrawable(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }
    //-------------------------------
    //SUPER METHODS
    //-------------------------------

    @Override
    public void setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom)
    {
        if(right != null)
            mRight = right;

        super.setCompoundDrawables(left, top, right, bottom);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {

        if(event.getAction() == MotionEvent.ACTION_UP && mRight!=null)
        {
            mBound = mRight.getBounds();
            final int x = (int)event.getX();
            final int y = (int)event.getY();
            //check to make sure the touch event was within the bounds of the drawable
            if(x >= (this.getRight() - mBound.width()) && x <= (this.getRight() - this.getPaddingRight())
                    && y >= this.getPaddingTop() && y <= (this.getHeight()-this.getPaddingBottom()))
            {
                this.setText("");
                event.setAction(MotionEvent.ACTION_CANCEL);//use this to prevent the keyboard from coming up
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void finalize() throws Throwable
    {
        mRight = null;
        mBound = null;
        super.finalize();
    }

    //-------------------------------
    //METHODS
    //-------------------------------

    //-------------------------------
    //GETTERS/SETTERS
    //-------------------------------

    //-------------------------------
    //INNER CLASSES
    //-------------------------------
}
