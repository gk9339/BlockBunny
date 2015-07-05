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
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.gk.blockbunny.handlers.B2DVars;
import com.gk.blockbunny.handlers.GameStateManager;
import com.gk.blockbunny.handlers.MyContactListener;
import com.gk.blockbunny.main.Game;

public class Play extends GameState{
	
	private World world;
	private Box2DDebugRenderer b2dr;
	
	private OrthographicCamera b2dcam;
	
	public Play(GameStateManager gsm) {
		
		super(gsm);
		
		world = new World(new Vector2(0, -9.81f), true);
		world.setContactListener(new MyContactListener());
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
		fdef.filter.maskBits = B2DVars.BIT_BOX | B2DVars.BIT_BALL;
		body.createFixture(fdef).setUserData("Ground");;
		
		bdef.position.set(160 / PPM, 200 / PPM);
		bdef.type = BodyType.DynamicBody;
		body = world.createBody(bdef);
		
		//box
		shape.setAsBox(5 / PPM, 5 / PPM);
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_BOX;
		fdef.filter.maskBits = B2DVars.BIT_GROUND;
		body.createFixture(fdef).setUserData("Box");;
		
		bdef.position.set(153 / PPM, 220 / PPM);
		body = world.createBody(bdef);
		
		//circle
		CircleShape cshape = new CircleShape();
		cshape.setRadius(5 / PPM);
		fdef.shape = cshape;
		fdef.filter.categoryBits = B2DVars.BIT_BALL;
		fdef.filter.maskBits = B2DVars.BIT_GROUND;
		body.createFixture(fdef).setUserData("Ball");
		
		b2dcam = new OrthographicCamera();
		b2dcam.setToOrtho(false, Game.V_WIDTH / PPM, Game.V_HEIGHT / PPM);
		
	}
	
	public void handleInput() {
		
	}
	public void update(float dt) {
		
		world.step(dt, 6, 2);
		
	}
	public void render() {
		
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		b2dr.render(world, b2dcam.combined);
		
	}
	public void dispose() {
		
	}

}
