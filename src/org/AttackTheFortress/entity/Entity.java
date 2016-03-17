package org.AttackTheFortress.entity;

import org.AttackTheFortress.Common;
import org.cocos2d.types.CCPoint;
import org.cocos2d.types.CCSize;

public class Entity {

	public int type;		// tower:1 ......... enemy:0
	public int health;
	public int damage;
	public int range;
	public int speed;
	public int state;
	public int cost;
	public int maxLife;
	
	public boolean hasBullet;
	
	public CCPoint pos;
	public CCSize size;

	public Entity( int Type, CCPoint pt )
	{
		pos = pt;
		type = Type;
		health = damage = range = speed = 0;
		state = Common.ENTITY_LIVE;
		hasBullet = false;
		
		size = CCSize.make(0, 0);
	}
	
	public boolean onHit( int dmg )
	{
		health -= dmg;
		return health > 0;
	}
	
	public void onTargetInRange()
	{
		if( !hasBullet )
			lauchBulletAuto( this );
	}

	public void lauchBulletAuto(Entity obj) {
		// TODO Auto-generated method stub
		
	}
}
