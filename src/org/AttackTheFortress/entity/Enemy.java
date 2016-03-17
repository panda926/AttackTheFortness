package org.AttackTheFortress.entity;

import java.util.ArrayList;

import org.AttackTheFortress.Common;
import org.AttackTheFortress.DataManager;
import org.AttackTheFortress.Global;
import org.AttackTheFortress.views.GameView;
import org.cocos2d.nodes.Director;
import org.cocos2d.nodes.TextureManager;
import org.cocos2d.nodes.TextureNode;
import org.cocos2d.opengl.Texture2D;
import org.cocos2d.types.CCMacros;
import org.cocos2d.types.CCPoint;
import org.cocos2d.types.CCRect;

public class Enemy extends Entity {
	
	public boolean shootable;
	private int m_numCharac;
	private int nCharacIndx;
	private int m_destroyIndex;
	private int nextNode;
	
	private GameView m_layer;
	private Texture2D tex_enemy, tex_enemy_hp;
	private TextureNode texnode_enemy, texnode_enemy_hp;
	private CCPoint temp_pt;
	
	private float rScale_width = 0.0f;
	private float rScale_height = 0.0f;
	
	private float imgAngle;
	private float m_Xrate, m_Yrate;

	public Enemy( int nType, CCPoint pt, GameView view, int c )
	{
		super(nType, pt);
		
		m_numCharac = c;
		m_layer = view;
		nextNode = 1;
		nCharacIndx = 0;
		
		rScale_width = Global.g_rScale_x;
		rScale_height = Global.g_rScale_y;
		
		m_Xrate = Director.sharedDirector().winSize().width / 1024;
		m_Yrate = Director.sharedDirector().winSize().height / 768;

		texnode_enemy = new TextureNode();
		String fileName = String.format("gfx/Enemies/enemy%d_1.png", type+1);
		tex_enemy = TextureManager.sharedTextureManager().addImage(fileName);
		texnode_enemy.setScaleX(rScale_width); texnode_enemy.setScaleY(rScale_height);
		texnode_enemy.setTexture(tex_enemy);
		texnode_enemy.setRotation(90);
		
		texnode_enemy_hp = new TextureNode();
		fileName = String.format("gfx/Gameview/healthEmy.png");
		tex_enemy_hp = TextureManager.sharedTextureManager().addImage(fileName);
		texnode_enemy_hp.setTexture(tex_enemy_hp);
		texnode_enemy_hp.setScaleX(rScale_width); texnode_enemy_hp.setScaleY(rScale_height);
		
		state = Common.ENTITY_CREATED;
	}
	
	public boolean moveOneStep()
	{
		if( state <= Common.ENTITY_DESTROIED )
			return false;
		if( state == Common.ENTITY_CREATED ){
			state = Common.ENTITY_LIVE;
			m_layer.addChild(texnode_enemy, 0);
			m_layer.addChild(texnode_enemy_hp, 0);
			
			ArrayList<CCPoint> roadPath = m_layer.getRoadPath();
			CCPoint first = roadPath.get(0);
			temp_pt = first;
			pos.x = temp_pt.x; pos.y = temp_pt.y;
		}
		ArrayList<CCPoint> roadPath = m_layer.getRoadPath();
		CCPoint target = roadPath.get(nextNode);
		CCRect trt = CCRect.make(target.x - speed, target.y - speed, speed*2, speed*2);
		if( CCRect.containsPoint(trt, pos) ){
			nextNode ++;
			if( nextNode >= roadPath.size() ){
				nextNode = 1;
				temp_pt = roadPath.get(0);
				pos.x = temp_pt.x; pos.y = temp_pt.y;
				m_layer.anEnemyEscaped();
			}
			target = roadPath.get(nextNode);
		}
		float PI = (float)Math.atan(1)*4;
		float angle = (float)Math.atan((target.y - pos.y)/(target.x - pos.x));
		if( target.x < pos.x )
			angle = angle + PI;
		imgAngle = angle;
		pos.x = pos.x + speed * (float)Math.cos(angle) * m_Xrate * 1.6f;
		pos.y = pos.y + speed * (float)Math.sin(angle) * m_Yrate * 1.6f;
		
		nCharacIndx ++;
		nCharacIndx = nCharacIndx % m_numCharac;
		draw();
		if( state == Common.ENTITY_IN_DESTROY ){
			m_destroyIndex ++;
			if( m_destroyIndex >= 16 )
				return false;
		}
		
		return true;
	}
	
	private void draw()
	{
		if( state == Common.ENTITY_LIVE || state == Common.ENTITY_IN_DESTROY ){
			
			String fileName = String.format("gfx/Enemies/enemy%d_%d.png", type+1, nCharacIndx+1);
			Texture2D temp = TextureManager.sharedTextureManager().addImage(fileName);
			texnode_enemy.setTexture(temp);
			float degress = CCMacros.CC_RADIANS_TO_DEGREES(imgAngle);
			texnode_enemy.setRotation(90 - degress);
			texnode_enemy.setPosition(pos.x, pos.y);
			
			CCPoint pt = CCPoint.make(pos.x, pos.y+temp.getHeight()*rScale_height/2+1);
			texnode_enemy_hp.setPosition(pt.x, pt.y);
		}
	}
	
	public boolean rangeContainPoint( CCPoint pt )
	{
		float dist;
		
		dist = (float)Math.sqrt((pt.x-pos.x)*(pt.x-pos.x)+(pt.y-pos.y)*(pt.y-pos.y));
		
		return dist <= range * m_Yrate;
	}
	
	private Tower getTargetInRange()
	{
		ArrayList<Entity> aryTower = m_layer.getTowerArray();
		
		for( int i=0; i<aryTower.size(); i++ ){
			Tower tower = (Tower) aryTower.get(i);
			if( rangeContainPoint(tower.pos) && tower.state == Common.ENTITY_LIVE )
				return tower;
		}
		return null;
	}
	
	@Override
	public void lauchBulletAuto( Entity obj )
	{
		if( !shootable )
			return;
		
		Tower tower = getTargetInRange();
		ArrayList<Bullet> aryBullet = m_layer.getBulletArray();
		
		if( tower != null ){
			aryBullet.add(launchBulletWithTarget(tower));
			hasBullet = true;
			DataManager.shared().playSound(DataManager.shared().SOUND_ENEMY_LAUNCH);
			// sound enemy_launch
		}
	}
	
	private Bullet launchBulletWithTarget( Entity target )
	{
		float angle;
		CCPoint end = target.pos;
		
		angle = (float)Math.atan((end.y-pos.y)/(end.x-pos.x));
		if( end.x < pos.x )
			angle = angle + (float)Math.atan(1)*4;
		CCPoint start = CCPoint.make(pos.x+15*(float)Math.cos(angle) * m_Xrate, pos.y+15*(float)Math.sin(angle) * m_Yrate);
		Bullet bullet = new Bullet(13, start, this, target, damage, 7, m_layer);
		
		return bullet;
	}
	
	public void kill()
	{
	}
	
	public void destroy()
	{
		state = Common.ENTITY_IN_DESTROY;
		m_destroyIndex = 1;
		DataManager.shared().playSound(DataManager.shared().SOUND_EXPLODE);
	}
	
	@Override
	public boolean onHit( int dmg )
	{
		super.onHit(dmg);
		
		if( health < 0 )
			health = 0;
		
		float width = 45 * m_Xrate * health / maxLife;
		texnode_enemy_hp.setScaleX(width/texnode_enemy_hp.getWidth());
		texnode_enemy_hp.setPosition(texnode_enemy.getPositionX()-texnode_enemy.getWidth()/2+width/2, texnode_enemy_hp.getPositionY());
		
		return health > 0;
	}
	
	public void release()
	{
		m_layer.removeChild(texnode_enemy, true);
		m_layer.removeChild(texnode_enemy_hp, true);
		
//		texnode_enemy = null;
//		texnode_enemy_hp = null;
	}
}
