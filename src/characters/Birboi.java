
package characters;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Transform;
import org.dyn4j.geometry.Vector2;

import graphics.Sprite;
import graphics.Texture;
import program.Hitbox;
import characters.characterStates.*;

public class Birboi extends Character
{
	public Birboi()
	{
		Body birb = new Body();
		
		Transform t = new Transform();
		t.setTranslation(position, 0);
		birb.setTransform(t);
		position += 1;
		
		// Add the collision fixture
		Rectangle rect = new Rectangle(1, 1.5f);
		rect.translate(1, 1.25); // Set to topleft
		birb.addFixture(rect);
		birb.setMass(MassType.FIXED_ANGULAR_VELOCITY);

		setBody(birb);
		
		Texture tex = new Texture();
		tex.openResource("resources/images/birboi");
		
		Sprite sp = new Sprite(tex);
		sp.setAnimation("idle");
		
		setSprite(sp);
	}

	@Override
	public void jab()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tilt() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void smash()
	{
		// TODO Create hitboxes
		float duration = .5f;
		
		CharacterState smashStartup = new CharacterState("smash_startup")
				{
					@Override
					public void init()
					{
						getBody().setLinearVelocity(0, 0);
						getBody().setGravityScale(0);
					}
					
					@Override
					public void interrupt()
					{
						getBody().setGravityScale(1);
					}
					
					@Override
					public void end()
					{
						
					}
				};
		
		CharacterState smashFlight = new CharacterState("smash_fly", duration)
				{
					Hitbox m_hitbox = new Hitbox();
					Rectangle m_rect = new Rectangle(.6, 1.7);
					BodyFixture m_fixture;
					
					@Override
					public void init()
					{
						getBody().applyImpulse(new Vector2(12 * m_character.getFacing(), 0));
						m_hitbox.setDamage(15);
						m_hitbox.setBaseKnockback(new Vector2(8 * m_character.getFacing(), -5));
						m_hitbox.setScaledKnockback(new Vector2(1.5 * m_character.getFacing(), -2));
						m_hitbox.setDuration(duration);
						
						m_rect.translate(1 + .4 * m_character.getFacing(), 1.25);
						m_fixture = new BodyFixture(m_rect);
						getBody().addFixture(m_fixture);
						m_hitbox.addToFixture(m_fixture);
						addHitbox(m_hitbox);
					}
					
					@Override
					public void interrupt()
					{
						getBody().removeFixture(m_fixture);
						removeHitbox(m_hitbox);
						getBody().setGravityScale(1);
					}

					@Override
					public void end()
					{
						getBody().removeFixture(m_fixture);
						removeHitbox(m_hitbox);
						getBody().setGravityScale(1);
					}
					
					@Override
					public boolean activationTest()
					{
						return !m_hitbox.isAlive() && getTimer() > 0;
					}
					
					@Override
					public void activate()
					{
						setDuration(0);
						getBody().setLinearVelocity(0, 0);
					}
				};
		interruptStates(smashStartup);
		addState(smashFlight);
	}

	@Override
	public void projectile()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void signature() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void recover() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName()
	{
		return Character.characterNames[1];
	}
}
