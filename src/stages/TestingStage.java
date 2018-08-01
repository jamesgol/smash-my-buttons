package stages;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Transform;
import org.dyn4j.geometry.Vector2;

import graphics.Sprite;
import graphics.Texture;

public class TestingStage extends Stage
{
	public TestingStage()
	{
		World aWorld = new World();
		aWorld.setGravity(new Vector2(0, 5));
		//set up world
		setPhysicsWorld(aWorld);
		m_name = "TestingStage";
		createLand();
	}
	
	private void createLand()
	{
		Rectangle groundShape = new Rectangle(20, 1);
		
		Body groundBody = new Body();
		Transform t = new Transform();
		t.setTranslation(0, 6);
		groundBody.setTransform(t);
		groundBody.addFixture(groundShape);
		groundBody.setMassType(MassType.INFINITE);
		
		Texture groundTexture = new Texture();
		groundTexture.openResource("resources/images/testing_ground");
			
		Sprite groundSprite = new Sprite(groundTexture, "ground");
			
		TerrainPiece ground = new TerrainPiece();
		ground.setBody(groundBody);
		ground.setSprite(groundSprite);
			
		getPhysicsWorld().addBody(groundBody);
			
		m_terrain.add(ground);
	}
}
