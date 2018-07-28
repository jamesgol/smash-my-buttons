package graphics;

import java.util.List;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.AABB;
import org.dyn4j.geometry.Transform;
import org.dyn4j.geometry.Vector2;

import graphics.IntRect;
import java.awt.Color;

public class DebugDrawer implements Drawable
{
	private World m_world;
	
	public DebugDrawer(World p_world)
	{
		m_world = p_world;
	}
	
	@Override
	public void draw(Renderer p_renderer)
	{
		List<Body> bodies = m_world.getBodies();
		for(Body b : bodies)
		{
			List<BodyFixture> fixtures = b.getFixtures();
			for(BodyFixture f : fixtures)
			{
				//draw the thing
				//choose one
				AABB obj = f.getShape().createAABB();
				IntRect boundingBox = new IntRect(
						(int)(obj.getMinX()), (int)(obj.getMinY()),
						(int)(obj.getMaxX() - obj.getMinX()),
						(int)(obj.getMaxY() - obj.getMinY()));
				
				p_renderer.setTransform(b.getTransform()); // Use getTransform from the body for drawing things
				if(b.isAsleep()) {
					p_renderer.drawRect(boundingBox, Color.RED, 0.5f, 3);
				}else {
					p_renderer.drawRect(boundingBox, Color.BLUE, 0.5f, 3);
				}
				
				p_renderer.resetTransform();
			}
		}
	}
}