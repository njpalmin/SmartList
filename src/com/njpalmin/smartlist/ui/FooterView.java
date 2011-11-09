package com.njpalmin.smartlist.ui;

import com.njpalmin.smartlist.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FooterView extends LinearLayout
{
  private Context context = null;
  private LinearLayout textLayout = null;
  private Button button = null;
  private LinearLayout btnLayout = null;
  private TextView text1 = null;
  private TextView text2 = null;

  public FooterView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    
    context = paramContext;
    ((LayoutInflater)paramContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
    								.inflate(R.layout.footer, this, true);
    
    textLayout = (LinearLayout)findViewById(R.id.footer_text_wrapper);
    text1 = (TextView)findViewById(R.id.line1);
    text2 = (TextView)findViewById(R.id.line2);
    
    button = (Button)findViewById(R.id.button1);
    btnLayout = (LinearLayout)findViewById(R.id.footer_button_wrapper);
  }
  
  public final void useTextView()
  {
    textLayout.setVisibility(View.VISIBLE);
    btnLayout.setVisibility(View.GONE);
  }

  public final void useButtonView()
  {
	btnLayout.setVisibility(View.GONE);
    btnLayout.setVisibility(View.VISIBLE);
  }

  public final TextView getTextOne() {
	  return text1;
  }
  
  public final TextView getTextTwo() {
	  return text2;
  }
  
  public final Button getButton() {
	  return button;
  }
}