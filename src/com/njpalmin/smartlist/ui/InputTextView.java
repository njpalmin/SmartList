package com.njpalmin.smartlist.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.njpalmin.smartlist.R;

public class InputTextView extends LinearLayout
{
	private AutoCompleteTextView textView;
	private ImageButton optionBtn;
	private ImageButton voiceBtn;
	private ImageButton saveBtn;
	
	public InputTextView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		
		((LayoutInflater)context.getSystemService("layout_inflater"))
			.inflate(R.layout.input_text_view, this, true);
		
		optionBtn = (ImageButton)findViewById(R.id.options);
		
		voiceBtn = (ImageButton)findViewById(R.id.voice_input);
		
		saveBtn = (ImageButton)findViewById(R.id.save);
		saveBtn.setVisibility(View.GONE);
		
		textView = (AutoCompleteTextView)findViewById(R.id.input);
		
		refreshLastBtn();
	}
	  
	public void startVoiceReg(Activity activity)
	{
	    Intent intent = new Intent("android.speech.action.RECOGNIZE_SPEECH")
	    						.putExtra("android.speech.extra.LANGUAGE_MODEL", "free_form");
	    activity.startActivityForResult(intent, 102030);
	}
	
	public void handleVoiceRegResult(Activity activity, int resultCode, Intent data) {
    	ArrayList arrayList = data.getStringArrayListExtra("android.speech.extra.RESULTS");
    	
    	if (arrayList.size() == 0) return;
    	textView.setText((String)arrayList.get(0));
    }
	
	public ImageButton getOptionBtn() {
		return optionBtn;
	}
	
	public ImageButton getVoiceBtn(){
		return voiceBtn;
	}

	public EditText getTextView() {
		return textView;
	}

	
	private void refreshLastBtn() {
		String str = textView.getText().toString();
		if (str != null && !str.equals("")) {
			saveBtn.setVisibility(View.VISIBLE);
			voiceBtn.setVisibility(View.GONE);
			return;
		}
		
		saveBtn.setVisibility(View.GONE);
		voiceBtn.setVisibility(View.VISIBLE);
	}
	
}