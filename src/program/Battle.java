package program;

import graphics.DebugDrawer;
import graphics.GUI;
import graphics.IntRect;
import graphics.RenderList;
import graphics.Renderer;
import graphics.Sprite;
import graphics.Texture;
import stages.Stage;

import java.util.ArrayList;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Vector2;

public class Battle
{
	private ArrayList<Hitbox> m_hitboxes = new ArrayList<Hitbox>();
	private Stage m_stage;

	private boolean m_visibleHitboxes = true;
	
	private CharacterController[] m_charControllers = new CharacterController [2];
	
	private Renderer m_renderer = new Renderer();
	RenderList m_renderList = new RenderList();
	
	private Thread battleThread;
	
	public Battle(Stage p_env)
	{
		setStage(p_env);
	}
	
	public Battle() {}

	public void setStage(Stage p_env)
	{
		m_stage = p_env;
		//Gotta get dem listeners
		m_stage.getPhysicsWorld().addListener(new HitboxListener());
		m_stage.getPhysicsWorld().addListener(new GroundListener());
	}
	
	public World getWorld()
	{ 
		return m_stage.getPhysicsWorld();
	}
	
	//is this hitbox stuff necessary? - see comment on GitHub
	public Hitbox[] getHitboxes()
	{
		return (Hitbox[]) m_hitboxes.toArray();
	}
	
	public void addHitbox(Hitbox p_hitbox)
	{
		m_hitboxes.add(p_hitbox);
	}
	
	//Port: Player *1*, Player *2*, etc.
	public void addCharacter(CharacterController p_controller, int p_port)
	{
		m_charControllers[p_port - 1] = p_controller;
		m_stage.getPhysicsWorld().addBody(p_controller.getCharacter().getBody());
		m_renderList.addDrawable(p_controller.getCharacter());
		p_controller.getCharacter().getBody().translate(3 + 4 * p_port, 0);
	}
	
	public int getCharacterCount()
	{
		return m_charControllers.length;
	}
	
	public void setVisibleHitboxes(boolean p_visible)
	{
		m_visibleHitboxes = p_visible;
	}
	
	private void update(float p_delta)
	{
		m_stage.getPhysicsWorld().updatev((double)(p_delta));
		
		for(int i = m_hitboxes.size() - 1; i >= 0; i--)
		{
			m_hitboxes.get(i).updateTimer(p_delta);
			
			if(!m_hitboxes.get(i).alive())
			{
				m_hitboxes.remove(i);
			}
		}
	}
	
	public void startBattle(GUI p_gui)
	{
		p_gui.setPage(m_renderer);
		
		battleThread = new Thread(new Runnable()
				{
					public void run()
					{
						gameLoop();
					}
				});
		
		battleThread.start();
	}
	
	public void endBattle()
	{
		battleThread.interrupt();
	}
	
	private void gameLoop()
	{
		m_stage.registerTerrainSprites(m_renderList);
		
		//test sprites
		Texture tex1 = new Texture();
		tex1.openResource("resources/images/birboi");
		
		Sprite sprite = new Sprite(tex1, "idle");
		sprite.setPosition(new Vector2(50, 20));
		m_renderList.addDrawable(sprite);
		
		Texture tex2 = new Texture();
		tex2.openResource("resources/images/jack");
		
		Sprite sprite2 = new Sprite(tex2, "idle");
		sprite2.setPosition(new Vector2(100, 50));
		m_renderList.addDrawable(sprite2);
		
		//debug
		DebugDrawer debugger = new DebugDrawer(m_stage.getPhysicsWorld());
		
		Clock gameClock = new Clock();
		float delta = 0;
		
		//game loop: should move somewhere else and only start
		//once the battle itself starts
		while(true)
		{ 
			//calculate delta
			delta = gameClock.getElapse();
			gameClock.restart();
			
			//clear the buffer
			m_renderer.clear();
			
			update(delta);
			//update the world
			//draw sprites
			m_renderList.draw(m_renderer);
			
			//debug
			if(m_visibleHitboxes)
				debugger.draw(m_renderer);
			
			//display the current frame
			m_renderer.display();
			
			//delay
			try {
				long totalNanos = 16666666 - (int)(gameClock.getElapse()*1e9f);
				if(totalNanos > 0)
				{
					int nanos = (int) (totalNanos % 1000000);
					long milis = (totalNanos - nanos) / 1000000;
					Thread.sleep(milis, (int)nanos);
				}
				//this one's simpler
				//Thread.sleep(16, 666666);
			} catch (InterruptedException e) {
				break;
			}
		}
	}
}
