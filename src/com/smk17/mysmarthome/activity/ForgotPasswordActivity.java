package com.smk17.mysmarthome.activity;

import com.smk17.mysmarthome.R;

import android.os.Bundle;
import android.view.View;

public class ForgotPasswordActivity extends RegisterActivity{
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tvTitle.setText(R.string.forgot_password_title);
		tvRecall.setText(R.string.forgot_password_recall);
		etPassword.setVisibility(View.GONE);
		isForgotPassword = true;
	}

	@Override
	protected void backtrack() {
		super.backtrack();
		etPassword.setVisibility(View.GONE);
		tvTitle.setText(R.string.forgot_password_title);
		tvRecall.setText(R.string.forgot_password_recall);
		isForgotPassword = true;
	}
	
}
