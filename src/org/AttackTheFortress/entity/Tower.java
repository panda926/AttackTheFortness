package org.AttackTheFortress.entity;

import java.util.ArrayList;

import org.AttackTheFortress.Common;
import org.AttackTheFortress.DataManager;
import org.AttackTheFortress.Global;
import org.AttackTheFortress.TowerInfo;
import org.AttackTheFortress.views.GameView;
import org.cocos2d.nodes.Director;
import org.cocos2d.nodes.Sprite;
import org.cocos2d.types.CCPoint;
import org.cocos2d.utils.CCFormatter;

public class Tower extends Entity {
	
	private GameView m_view;
	public Sprite tower, tower_hp;

	private float rScale_width = 0.0f;
	private float rScale_height = 0.0f;
	private float m_Xrate, m_Yrate;
	
	public int upDamageLevel;
	public int upSpeedLevel;
	public int upRangeLevel;
	
	private int m_destroyIndex;
	
	public Tower( int nType, CCPoint pt, GameView view )
	{
		super(nType, pt);
		
		rScale_width = Global.g_rScale_x;
		rScale_height = Global.g_rScale_y;
		m_Xrate = Director.sharedDirector().winSize().width / 1024;
		m_Yrate = Director.sharedDirector().winSize().height / 768;
		m_destroyIndex = 0;

		m_view = view;
		
		DataManager dataManager = DataManager.shared();
		TowerInfo tower_info = dataManager.getTowerInforsAt(type-1);
		health = tower_info.max_life;
		speed = tower_info.speed;
		range = tower_info.range;
		damage = tower_info.damage;
		cost = tower_info.cost;
		
		tower = Sprite.sprite(new CCFormatter().format("gfx/Towers/tower%d.png", type));
		tower.setScaleX(rScale_width*0.6f); tower.setScaleY(rScale_height*0.6f);
		m_view.addChild(tower, 1);
		
		tower_hp = Sprite.sprite("gfx/Gameview/healthTgt.png");
		tower_hp.setScaleX(rScale_width); tower_hp.setScaleY(rScale_height);
		m_view.addChild(tower_hp, 1);
		
		size.width = tower.getWidth() * 0.6f;
		size.height = tower.getHeight() * 0.6f;
		
		state = Common.ENTITY_LIVE;
		maxLife = health;
	}
	
	public boolean rangeContainPoint( CCPoint pt )
	{
		float dist;
		dist = (float)Math.sqrt((pt.x-pos.x)*(pt.x-pos.x)+(pt.y-pos.y)*(pt.y-pos.y));
		return dist <= range * m_Yrate;
	}
	
	public Bullet launchBulletWithTarget( Entity target )
	{
		float angle;
		CCPoint endpos = target.pos;
		
		angle = (float)Math.atan((endpos.y-pos.y)/(endpos.x-pos.x));
		if( endpos.x < pos.x )
			angle = angle + (float)Math.atan(1)*4;
		CCPoint startpos = CCPoint.make(pos.x+15*(float)Math.cos(angle), pos.y+15*(float)Math.sin(angle));
		Bullet bullet = new Bullet(type-1, startpos, this, target, damage, speed, m_view);
		
		return bullet;
	}
	
	@Override
	public void lauchBulletAuto( Entity obj )
	{
		if( hasBullet )
			return;
		ArrayList<Entity> aryEnemy = m_view.getEnemyArray();
		ArrayList<Bullet> aryBullet = m_view.getBulletArray();
		
		for( int i=0; i<aryEnemy.size(); i++ ){
			Enemy enemy = (Enemy) aryEnemy.get(i);
			if( rangeContainPoint( enemy.pos ) && enemy.state > Common.ENTITY_IN_DESTROY ){
				hasBullet = true;
				// sound tower_launch
				DataManager.shared().playSound(DataManager.shared().SOUND_TOWER_LAUNCH);
				aryBullet.add(launchBulletWithTarget(enemy));
				return;
			}
		}
	}
	
	public void setPos( CCPoint pt )
	{
		pos = pt;
		tower.setPosition(pos.x, pos.y);
		if( state != Common.ENTITY_IN_DRAGGING )
		{
			tower_hp.setPosition(pos.x, pos.y - tower.getScaleX()*tower.getHeight()/2-2);
			//tower_hp.setContentSize(45*m_Xrate, 8*m_Yrate);
		}
	}
	
	@Override
	public boolean onHit( int dmg )
	{
		super.onHit(dmg);
		
		if( health <= 0 )
			return false;
		updateHealth();
		return true;
	}
	
	public void updateHealth()
	{
		tower_hp.setContentSize(45*m_Xrate*health/maxLife, tower_hp.getHeight());
	}
	
	public void destroy()
	{
		m_view.removeChild(tower, true);
		m_view.removeChild(tower_hp, true);
		tower = null;
		tower_hp = null;
		state = Common.ENTITY_IN_DESTROY;
		m_destroyIndex = 0;
		// sound
		m_view.schedule("animateDestroy",50 / 1000.0f);
	}
	
	public void animateDestroy()
	{
		if (m_destroyIndex >= 22) {
			state = Common.ENTITY_DESTROIED;
			m_view.unschedule("animateDestroy");
			return;
			//return YES;
		}
		m_destroyIndex ++;
		
	}
	
	public void release()
	{
		if( tower_hp != null )
			m_view.removeChild(tower_hp, true);
		m_view.removeChild(tower, true);
	}
}
