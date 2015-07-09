package com.gk.blockbunny.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gk.blockbunny.handlers.B2DVars;
import com.gk.blockbunny.main.Game;

public class HUD {
	
	private Player player;
	
	private TextureRegion[] blocks;
	
	public HUD(Player player) {
		this.player = player;
		
		Texture tex = Game.res.getTexture("HUD");
		
		blocks = new TextureRegion[3];
		for(int i = 0; i < blocks.length; i++) {
			blocks[i] = new TextureRegion(tex, 32 + i * 16, 0, 16, 16);
		}
	}
	
	public void render(SpriteBatch sb) {
		short bits = player.getBody().getFixtureList().first().getFilterData().maskBits;
		
		sb.begin();
		if((bits & B2DVars.BIT_RED) != 0) {
			sb.draw(blocks[0], 40, 200);
		}
		if((bits & B2DVars.BIT_GREEN) != 0) {
			sb.draw(blocks[1], 40, 200);
		}
		if((bits & B2DVars.BIT_BLUE) != 0) {
			sb.draw(blocks[2], 40, 200);
		}
		sb.end();
	}

}
