package com.example.volt.screen;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.example.volt.R;

public class SimpleFAQView extends ConstraintLayout implements FAQView {

    private final Button hideButton;

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Creates a new {@link SimpleFAQView}.</li>
     * </ul>
     * </p>
     * @param context   The application's {@link Context}, used to inflate the view and access resources.
     * @param attrs     The {@link AttributeSet} containing XML-defined attributes for this view.
     */
    public SimpleFAQView(Context context, AttributeSet attrs) {
        super(context, attrs);
        /* Sets the layout of the view */
        View screen = LayoutInflater.from(context).inflate(R.layout.simple_faq_screen, this, true);

        /* Initializes the child views */
        this.hideButton = screen.findViewById(R.id.FaqScreenButton);
    }

    @Override
    public void show() {
        this.setVisibility(View.VISIBLE);
    }

    @Override
    public void hide() {
        this.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setOnHideListener(View.OnClickListener onClickListener) {
        this.hideButton.setOnClickListener(onClickListener);
    }
}