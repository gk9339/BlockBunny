package com.gk.blockbunny.states;

import static com.gk.blockbunny.handlers.B2DVars.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.gk.blockbunny.handlers.B2DVars;
import com.gk.blockbunny.handlers.GameStateManager;
import com.gk.blockbunny.handlers.MyContactListener;
import com.gk.blockbunny.handlers.MyInput;
import com.gk.blockbunny.main.Game;

public class Play extends GameState{
	
	private World world;
	private Box2DDebugRenderer b2dr;
	
	private OrthographicCamera b2dcam;
	
	private Body playerBody;
	private MyContactListener cl;
	
	private TiledMap tileMap;
	private float tileSize;
	private OrthogonalTiledMapRenderer tmr;
	
	public Play(GameStateManager gsm) {
		
		super(gsm);
		
		cl =  new MyContactListener();
		
		world = new World(new Vector2(0, -9.81f), true);
		world.setContactListener(cl);
		b2dr = new Box2DDebugRenderer();
		
		BodyDef bdef = new BodyDef();
		bdef.position.set(160 / PPM,120 / PPM);
		bdef.type = BodyType.StaticBody;//unaffected by forces
		Body body = world.createBody(bdef);
		
		//platform
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(50 / PPM, 5 / PPM);
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_GROUND;
		fdef.filter.maskBits = B2DVars.BIT_PLAYER;
		body.createFixture(fdef).setUserData("Ground");;
		
		//player
		bdef.position.set(160 / PPM, 200 / PPM);
		bdef.type = BodyType.DynamicBody;
		playerBody = world.createBody(bdef);
		
		shape.setAsBox(5 / PPM, 5 / PPM);
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_GROUND;
		playerBody.createFixture(fdef).setUserData("Player");;
		
		//foot sensor
		shape.setAsBox(2 / PPM, 2 / PPM, new Vector2(0, -5 / PPM), 0);
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_GROUND;
		fdef.isSensor = true;
		playerBody.createFixture(fdef).setUserData("foot");
		
		b2dcam = new OrthographicCamera();
		b2dcam.setToOrtho(false, Game.V_WIDTH / PPM, Game.V_HEIGHT / PPM);
		
		//----------TILED----------//
		
		//load map
		tileMap = new TmxMapLoader().load("res/maps/test.tmx");
		tmr = new OrthogonalTiledMapRenderer(tileMap);
		
		TiledMapTileLayer layer = (TiledMapTileLayer) tileMap.getLayers().get("red");
		
		tileSize = layer.getTileWidth();
		
		for(int row = 0; row < layer.getHeight(); row++) {
			for(int col = 0; col < layer.getWidth(); col++) {
				
				//get cell
				Cell cell = layer.getCell(col, row);
				
				//check cell
				if(cell == null) continue;
				if(cell.getTile() == null) continue;
				
				//create body and fixture from cell
				bdef.type = BodyType.StaticBody;
				bdef.position.set(
						(col + 0.5f) * tileSize / PPM,
						(row + 0.5f) * tileSize / PPM);
				
				ChainShape cs = new ChainShape();
				Vector2[] v = new Vector2[3];
				v[0] = new Vector2(
						-tileSize / 2 / PPM, -tileSize / 2 / PPM);
				v[1] = new Vector2(
						-tileSize / 2 / PPM, tileSize / 2 / PPM);
				v[2] = new Vector2(
						tileSize / 2 / PPM, tileSize / 2 / PPM);
				cs.createChain(v);
				
				fdef.friction = 0;
				fdef.shape = cs;
				fdef.filter.categoryBits = 1;
				fdef.filter.maskBits = -1;
				fdef.isSensor = false;
				world.createBody(bdef).createFixture(fdef);
			}
		}
		
	}
	
	public void handleInput() {
		
		//player jump
		if(MyInput.isPressed(MyInput.BUTTON1)) {
			if(cl.isPlayerOnGround()) {
				playerBody.applyForceToCenter(0, 200, true);
			}
		}
	}
	public void update(float dt) {
		
		handleInput();
		
		world.step(dt, 6, 2);
		
	}
	public void render() {
		//clear screen
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//draw tile map
		tmr.setView(cam);
		tmr.render();
		
		//render world with cam
		b2dr.render(world, b2dcam.combined);
		
	}
	public void dispose() {
		
	}

}
