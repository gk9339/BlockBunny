package com.gk.blockbunny.states;

import static com.gk.blockbunny.handlers.B2DVars.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
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
		
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		b2dr.render(world, b2dcam.combined);
		
	}
	public void dispose() {
		
	}

}
