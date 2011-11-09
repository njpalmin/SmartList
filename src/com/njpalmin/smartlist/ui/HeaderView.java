package com.njpalmin.smartlist.ui;

import com.njpalmin.smartlist.R;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HeaderView extends LinearLayout
{
  private Context context = null;
  private InputTextView inputTextView = null;
  private LinearLayout titleBarLayout = null;
  private Button titleBtn = null;
  private ImageButton scanBtn = null;
  private ImageButton addBtn = null;

  public HeaderView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    
    context = paramContext;
    ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
    							.inflate(R.layout.header, this, true);
    inputTextView = (InputTextView)findViewById(R.id.input_text_bar);
    titleBarLayout = (LinearLayout)findViewById(R.id.title_bar);
    titleBtn = (Button)findViewById(R.id.title);
    scanBtn = (ImageButton)findViewById(R.id.scan_item);
    addBtn = (ImageButton)findViewById(R.id.add_item);
  }

  public final LinearLayout getTitleLayout()
  {
    return titleBarLayout;
  }

  public final Button getTitleBtn()
  {
    return titleBtn;
  }

  public final ImageButton getScanBtn()
  {
	  return scanBtn;
  }
  
  public final ImageButton getAddBtn()
  {
	  return addBtn;
  }

  public final InputTextView getInputView()
  {
      return inputTextView;
  }
}