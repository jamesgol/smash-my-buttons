package stages;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.AABB;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

import graphics.Drawable;
import graphics.Renderer;
import graphics.Sprite;

public class TerrainPiece implements Drawable
{
	private Body m_body;
	private Sprite m_sprite;
	
	public TerrainPiece() {}
	
	public void setBody(Body p_body)
	{
		m_body = p_body;
		m_body.setUserData(this);
	}
	
	public Body getBody()
	{
		return m_body;
	}
	
	public void setSprite(Sprite p_sprite)
	{
		m_sprite = p_sprite;
		m_sprite.setPosition(getPosition());
	}
	
	private Vector2 getPosition()
	{
		AABB bodyBounds = m_body.createAABB();
		return new Vector2(bodyBounds.getMinX(), bodyBounds.getMinY()).multiply(32);
	}
	
	@Override
	public void draw(Renderer p_renderer)
	{
		m_sprite.draw(p_renderer);
	}
	
}