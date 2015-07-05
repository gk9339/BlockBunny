package com.gk.blockbunny.handlers;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.gk.blockbunny.handlers.MyInput;

public class MyInputProcessor extends InputAdapter{
	
	public boolean keyDown(int k) {
		if(k == Keys.Z) {
			MyInput.setKey(MyInput.BUTTON1, true);
		}
		if(k == Keys.X) {
			MyInput.setKey(MyInput.BUTTON2, true);
		}
		return true;
	}
	
	public boolean keyUp(int k) {
		if(k == Keys.Z) {
			MyInput.setKey(MyInput.BUTTON1, false);
		}
		if(k == Keys.X) {
			MyInput.setKey(MyInput.BUTTON2, false);
		}
		return true;
	}

}
