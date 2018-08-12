package characters;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.joint.WeldJoint;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Transform;
import org.dyn4j.geometry.Vector2;

import characters.characterStates.AttackState;
import characters.characterStates.WaitState;
import graphics.Sprite;
import graphics.Texture;
import program.Hitbox;
import program.Projectile;
import resourceManager.ResourceManager;

public class WallTheEncircler extends Character
{
	private static double position = 0;
	private float length = 1;
	private float height = 2.0f;
	
	public WallTheEncircler()
	{
		jumpImpulse = new Vector2(0, -35);
		runForce = new Vector2(60, 0);
		maxRunSpeed = 2.5f;
		
		Body swol = new Body();
		
		Transform t = new Transform();
		t.setTranslation(position, 0);
		swol.setTransform(t);
		position += 1;
		
		Rectangle getRekt = new Rectangle(length, height);
		getRekt.translate(1, 1);
		BodyFixture bf = new BodyFixture(getRekt);
		bf.setDensity(1.5);
		swol.addFixture(bf);
		swol.setMass(MassType.FIXED_ANGULAR_VELOCITY);
		
		setBody(swol);
		
		Texture textile = ResourceManager.getResource(Texture.class, "resources/images/Wall");
	
		Sprite mountainDew = new Sprite(textile);
		mountainDew.setAnimation("idle");
		
		setSprite(mountainDew);
	}
	
	private class JabState extends AttackState
	{
		private Hitbox m_hitbox = new Hitbox();
		private Rectangle m_rect;
		private BodyFixture m_fixture;
		
		public JabState()
		{
			super("jab");
			
			m_hitbox.setDuration(0.1f);
			m_hitbox.setDamage(2);
			m_hitbox.setHitstun(0f);
			m_hitbox.setBaseKnockback(new Vector2(14 * getFacing(), -1));
			m_hitbox.setScaledKnockback(new Vector2(2 * getFacing(), 0));
			
			m_rect = new Rectangle(1, 0.5);
			m_rect.translate(length + 0.5 * getFacing(), 1);
			
			m_fixture = new BodyFixture(m_rect);
		}
		
		protected void init()
		{
			addHitbox(m_hitbox);
			m_hitbox.addToFixture(m_fixture);
			m_body.addFixture(m_fixture);
			m_superArmour = true;
		}
		
		public void interrupt()
		{
			m_body.removeFixture(m_fixture);
			removeHitbox(m_hitbox);
			m_superArmour = false;
		}
		
		public void end()
		{
			m_body.removeFixture(m_fixture);
			removeHitbox(m_hitbox);
			m_superArmour = false;
		}
	}
	
	@Override
	public void jab() 
	{
		pushState(new JabState());
		pushState(new AttackState("jab", 0.12f));
	}
	
	private class TiltState extends AttackState
	{
		private Hitbox m_hitbox = new Hitbox();
		private Rectangle m_rect;
		private BodyFixture m_fixture;
		
		public TiltState()
		{
			super("tilt");
			setDuration(0.4f);
			m_hitbox.setDuration(0.01f);
			m_hitbox.setDamage(5);
			m_hitbox.setHitstun(0.5f);
			m_hitbox.setBaseKnockback(new Vector2(-7 * getFacing(), -20));
			m_hitbox.setScaledKnockback(new Vector2(-2 * getFacing(), -2));
			
			m_rect = new Rectangle(0.5, 1);
			m_rect.translate(length + 0.75 * getFacing(), 1.5);
			
			m_fixture = new BodyFixture(m_rect);
		}
		
		protected void init()
		{
			addHitbox(m_hitbox);
			m_hitbox.addToFixture(m_fixture);
			m_body.addFixture(m_fixture);
		}
		
		public void interrupt()
		{
			m_body.removeFixture(m_fixture);
			removeHitbox(m_hitbox);
		}
		
		public void end()
		{
			m_body.removeFixture(m_fixture);
			removeHitbox(m_hitbox);
		}
	}

	@Override
	public void tilt() 
	{
		pushState(new TiltState());
		pushState(new AttackState("tilt", 0.239f));
	}
	
	private class SmashState extends AttackState
	{
		private Hitbox m_hitbox = new Hitbox();
		private Rectangle m_rect;
		private BodyFixture m_fixture;
		
		public SmashState()
		{
			super("smash");
			setDuration(0.6f);
			m_hitbox.setDamage(8);
			m_hitbox.setBaseKnockback(new Vector2(-10 * getFacing(), -5));
			m_hitbox.setScaledKnockback(new Vector2(-8 * getFacing(), -4));
			m_hitbox.setDuration(0.5f);
			
			m_rect = new Rectangle(1, 1.6);
			m_rect.translate(length - 0.3 * getFacing(), 0.75);
			
			m_fixture = new BodyFixture(m_rect);
		}
		
		protected void init()
		{
			m_body.applyImpulse(new Vector2(5 * getFacing(), 0));
			addHitbox(m_hitbox);
			m_hitbox.addToFixture(m_fixture);
			m_body.addFixture(m_fixture);
		}
		
		public void interrupt()
		{
			m_body.removeFixture(m_fixture);
			removeHitbox(m_hitbox);
		}
		
		public void end()
		{
			m_body.removeFixture(m_fixture);
			removeHitbox(m_hitbox);
		}
	}

	@Override
	public void smash() 
	{
		pushState(new WaitState(1.0f));
		pushState(new SmashState());
	}
	
	private class ProjectileState extends AttackState
	{
		private Projectile chair;
		private Hitbox m_hitbox = new Hitbox();
		private Rectangle m_rect;
		private BodyFixture m_fixture;
		private Body chairBody = new Body();
		
		public ProjectileState()
		{
			super("projectile");
			
			Texture texMex = ResourceManager.getResource(Texture.class, "resources/images/chair");
			
			Sprite chairemAnime = new Sprite(texMex);
			chairemAnime.setAnimation("default");
			
			m_hitbox.setDuration(3.0f);
			m_hitbox.setDamage(8);
			m_hitbox.setHitstun(0.2f);
			m_hitbox.setBaseKnockback(new Vector2(getFacing(), 5));
			m_hitbox.setScaledKnockback(new Vector2(2*getFacing(), 6));
			
			m_rect = new Rectangle(0.5, 0.5);
			m_rect.translate(0, 0);
			
			chair = new Projectile(chairemAnime, m_hitbox);
			chair.setCharacter((Character) m_body.getUserData());
			
			m_fixture = new BodyFixture(m_rect);
			
			Transform t = new Transform();
			t.translate(chairBody.getTransform().getTranslation());
			t.translate(1, 4);
			
			chairBody.setTransform(t);
			chairBody.addFixture(m_fixture);
			chairBody.setMass(MassType.NORMAL);
			
			addProjectile(chair);
		}
		
		protected void init()
		{
			addHitbox(m_hitbox);
			m_hitbox.addToFixture(m_fixture);
			m_fixture.setSensor(false);
			chair.setBody(chairBody);
			chairBody.applyImpulse(new Vector2(4 * getFacing(), -2));
			chairBody.applyTorque(1.5);
			m_world.addBody(chairBody);
		}
		
		protected void onUpdate(float p_delta)
		{
			if(!m_hitbox.isAlive()) {
				chairBody.removeFixture(m_fixture);
				removeHitbox(m_hitbox);
				chairBody.removeAllFixtures();
				m_world.removeBody(chairBody);
			}
		}
	}

	@Override
	public void projectile() 
	{
		//someone time this correctly
		pushState(new ProjectileState());
		pushState(new AttackState("projectile", 0.3f));
		pushState(new WaitState(0.2f));
	}

	WeldJoint hold;
	
	@Override
	public void signature()
	{
		
		AttackState suplexSlam = new AttackState("signature_slam", -1f)
		{
			Hitbox m_hitbox = new Hitbox();
			Rectangle m_rect = new Rectangle(1, 2);
			BodyFixture m_fixture;
			
			@Override
			public void init()
			{
				m_hitbox.setDuration(3.0f);
				m_hitbox.setDamage(8);
				m_hitbox.setHitstun(0.2f);
				m_hitbox.setBaseKnockback(new Vector2(10 * getFacing(), -10));
				m_hitbox.setScaledKnockback(new Vector2(7 * getFacing(), -7));
				
				m_rect = new Rectangle(1.3, 1.3);
				m_rect.translate(1, 2);
				m_body.setLinearVelocity(0, 25);
				m_fixture = new BodyFixture(m_rect);
			}
			
			@Override
			public void interrupt() {
				m_body.removeFixture(m_fixture);
				removeHitbox(m_hitbox);
			}
			
			@Override
			public void end()
			{
				Hitbox welcomeToTheJam = new Hitbox();
				welcomeToTheJam.setDamage(8);
				welcomeToTheJam.setBaseKnockback(alignFacing(new Vector2(.5, -4)));
				welcomeToTheJam.setScaledKnockback(alignFacing(new Vector2(.3, -2)));
				welcomeToTheJam.setHitstun(.3f);
				
				m_opponent.takeHit(welcomeToTheJam);
				
				m_world.removeJoint(hold);
			}
			
			@Override
			protected void onUpdate(float p_delta)
			{
				if(m_body.getLinearVelocity().y == 0)
					endState();
			}
		};
		
		AttackState suplexThrow = new AttackState("signature_throw", 0.3f)
		{
			//somehow carry the target
			@Override
			public void init()
			{
				m_body.setLinearVelocity(-5*getFacing(), -15);
			}
			
			@Override
			public void end()
			{
				m_body.setLinearVelocity(0, 0);
			}
		};
		
		float dashDuration = 1f;
		
		AttackState suplexDash = new AttackState("signature_dash", dashDuration)
		{
			Hitbox m_hitbox = new Hitbox();
			Rectangle m_rect = new Rectangle(1, 2);
			BodyFixture m_fixture;
			
			@Override
			public void init()
			{
				m_body.applyImpulse(alignFacing(new Vector2(30, 0)));
				
				m_hitbox.setDamage(0);
				m_hitbox.setBaseKnockback(new Vector2(0, 0));
				m_hitbox.setScaledKnockback(new Vector2(0, 0));
				m_hitbox.setDuration(dashDuration);
				
				m_rect.translate(0.5, 1);
				m_fixture = new BodyFixture(m_rect);
				
				m_body.addFixture(m_fixture);
				m_hitbox.addToFixture(m_fixture);
				addHitbox(m_hitbox);
			}
			
			@Override
			public void interrupt()
			{
				if(m_fixture != null)
					m_body.removeFixture(m_fixture);
				removeHitbox(m_hitbox);
				m_body.setLinearVelocity(0, 0);
			}
			
			@Override
			public void end()
			{
				interrupt();
				//pushState(new AttackState("signature_dash", cooldown));
			}
			
			@Override
			protected void onUpdate(float p_delta)
			{
				if(!m_hitbox.isAlive() && getTimer() > 0)
				{	
					Vector2 pos = m_body.getTransform().getTranslation();
					Transform opT = m_opponent.getBody().getTransform();
					opT.setTranslation(pos + alignFacing(new Vector2(.5, 0)));
					
					m_opponent.getBody().setTransform(opT);
					
					hold = new WeldJoint(m_body, m_opponent.getBody(), new Vector2());
					m_world.addJoint(hold);
					
					popState();
					pushState(suplexSlam);
					pushState(new WaitState(0.01f));
					pushState(suplexThrow);
				}
			}
		};
		
		pushState(suplexDash);
	}

	@Override
	public void recover()
	{
		AttackState recoveryJump = new AttackState("recovery_jump", 0.4f)
		{
			@Override
			public void init()
			{
				m_body.setLinearVelocity(0, 0);
				m_body.applyImpulse(new Vector2(2*getFacing(), -45));
			}
			
			@Override
			public void end()
			{
				m_body.setLinearVelocity(0, 0);
			}
		};
		
		AttackState recoveryTumble = new AttackState("recovery_tumble", 0.3f)
		{
			@Override
			public void init()
			{
				m_body.setLinearVelocity(0, 0);
			}
			
			@Override
			protected void onUpdate(float p_delta) {
				m_body.applyImpulse(new Vector2(4 * getFacing(), 0));
			}
		};
		
		AttackState recoverySlam = new AttackState("recovery_slam", 0.3f)
		{
			private Hitbox m_hitbox = new Hitbox();
			private Rectangle m_rect;
			private BodyFixture m_fixture;
			
			@Override
			public void init()
			{
				m_body.setLinearVelocity(0, 0);
				m_body.applyImpulse(new Vector2(0, 40));
				
				m_hitbox.setDuration(0.2f);
				m_hitbox.setDamage(9);
				m_hitbox.setHitstun(0.6f);
				m_hitbox.setBaseKnockback(new Vector2(getFacing(), 20));
				m_hitbox.setScaledKnockback(new Vector2(getFacing(), 30));
			
				m_rect = new Rectangle(1.4, 0.4);
				m_rect.translate(1, 2);
				
				m_fixture = new BodyFixture(m_rect);
				
				addHitbox(m_hitbox);
				m_hitbox.addToFixture(m_fixture);
				m_body.addFixture(m_fixture);
			}
			
			@Override
			public void interrupt()
			{
				m_body.removeFixture(m_fixture);
				removeHitbox(m_hitbox);
			}
			
			@Override
			protected void onUpdate(float p_delta)
			{
				if(m_body.getLinearVelocity().y == 0 &&
						m_body.getLinearVelocity().y < 10)
					m_body.applyImpulse(new Vector2(getFacing(), -15));
				
			}
			
			@Override
			public void end()
			{
				interrupt();
			}
		};
		pushState(recoverySlam);
		pushState(recoveryTumble);
		pushState(recoveryJump);
	}

	@Override
	public String getName()
	{
		return Character.characterNames[3];
	}

}
