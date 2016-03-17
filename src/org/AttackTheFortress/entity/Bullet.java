package org.AttackTheFortress.entity;

import java.util.ArrayList;

import org.AttackTheFortress.Common;
import org.AttackTheFortress.DataManager;
import org.AttackTheFortress.Global;
import org.AttackTheFortress.views.GameView;
import org.cocos2d.actions.base.Action;
import org.cocos2d.actions.interval.Animate;
import org.cocos2d.nodes.Animation;
import org.cocos2d.nodes.Director;
import org.cocos2d.nodes.Sprite;
import org.cocos2d.types.CCPoint;
import org.cocos2d.types.CCRect;
import org.cocos2d.utils.CCFormatter;

public class Bullet {
	
	public int type, damage, state, speed;
	private int dstydeelay;
	private CCPoint ptStart, pos;
	private Entity entTarget, entSrc;
	
	private ArrayList<Entity> m_aryTarget;
	private ArrayList<Entity> m_arySrc;
	
	private GameView m_view;
	private Sprite bullets, blow = null;
	private Action action;

	private float rScale_width = 0.0f;
	private float rScale_height = 0.0f;
	private float m_Xrate, m_Yrate;
	
	static final int kTagAction = 1;

	public Bullet( int nType, CCPoint stPos, Entity src, Entity tgt, int dmg, int spd, GameView view )
	{
		rScale_width = Global.g_rScale_x;
		rScale_height = Global.g_rScale_y;
		
		m_Xrate = Director.sharedDirector().winSize().width / 1024;
		m_Yrate = Director.sharedDirector().winSize().height / 768;

		entTarget = tgt; entSrc = src; type = nType;
		m_view = view; ptStart = stPos; pos = ptStart;
		damage = dmg; speed = spd; state = Common.ENTITY_LIVE; dstydeelay = 0;
		
		String fileName = String.format("gfx/Bullets/bullet%d.png", type%6+1);
		bullets = Sprite.sprite(fileName);
		bullets.setScaleX(rScale_width); bullets.setScaleY(rScale_height);
		m_view.addChild(bullets, 2);
		
		entSrc.hasBullet = true;
		if( nType < 10 ){
			m_aryTarget = m_view.getEnemyArray();
			m_arySrc = m_view.getTowerArray();
		}
		else{
			m_aryTarget = m_view.getTowerArray();
			m_arySrc = m_view.getEnemyArray();
		}
	}
	
	public boolean fly()
	{
		float PI = 3.141592f;
		float dist = 0.0f, dx, dy;
		if( state == Common.ENTITY_DESTROIED )
			return true;
		if( state == Common.ENTITY_IN_DESTROY ){
			if( dstydeelay >= 22 ){
				state = Common.ENTITY_DESTROIED;
//				m_view.removeChild(blow, true);
//				blow = null;
				dstydeelay = 1;
				// ??
				if( entSrc != null && m_arySrc.indexOf(entSrc) != -1 ){
					entSrc.hasBullet = false;
					entSrc.onTargetInRange();
				}
				return true;
			}
			dstydeelay ++;
			
			if( dstydeelay == 1 ) {
				blow = Sprite.sprite("gfx/Blow/blow1.png");
				blow.setScaleX(rScale_width*0.4f); blow.setScaleY(rScale_height*0.4f);
				m_view.addChild(blow, 2);
				startAnimation();
			}
			blow.setPosition(pos.x, pos.y);
			if( entTarget != null && m_aryTarget.indexOf(entTarget) != -1 ){
				pos.x = entTarget.pos.x;
				pos.y = entTarget.pos.y;
				blow.setPosition(pos.x, pos.y);
			}
			return false;
		}
		if( entTarget == null || m_aryTarget.indexOf(entTarget) == -1 ){
			state = Common.ENTITY_DESTROIED;
			if( entSrc != null && m_arySrc.indexOf(entSrc) != -1 ){
				entSrc.hasBullet = false;
				entSrc.onTargetInRange();
			}
			return true;
		}
		dy = entTarget.pos.y - pos.y;
		dx = entTarget.pos.x - pos.x;
		dist = (float)Math.sqrt(dx*dx + dy*dy);
		float angle = (float)Math.atan(dy/dx);
		if( entTarget.pos.x < pos.x )
			angle = angle + PI;
		float steps = (speed / 2 + 5) * m_Xrate;
		if( steps > dist )
			steps = dist;
		pos.x = pos.x + steps * (float)Math.cos(angle);
		pos.y = pos.y + steps * (float)Math.sin(angle);
		bullets.setPosition(pos.x, pos.y);
		return checkReachedToTarget();
	}
	
	private void startAnimation()
	{
		Animation animation = new Animation("blow", 0.05f*22);
		for( int i=1; i<22; i++ )
			animation.addFrame(new CCFormatter().format("gfx/Blow/blow%d.png", i+1));
		action = Animate.action(animation);
		blow.runAction(action);
	}
	
	private boolean checkReachedToTarget()
	{
		CCRect tgRect = CCRect.make(entTarget.pos.x-10 * m_Xrate, entTarget.pos.y-10 * m_Yrate, 20 * m_Xrate, 20 * m_Yrate);
		if( CCRect.containsPoint(tgRect, pos) ){
			boolean killed = !entTarget.onHit(damage);
			if( killed ){
				if( type < 10 ){ // enemy
					// sound ??
					DataManager.shared().playSound(entTarget.type % 7 + 1);
					m_view.gold_value += entTarget.cost;
//					m_view.updateGold(m_view.gold_value);
					m_view.enemiesKilled ++;
					m_view.enemiesKilledForLevel ++;
					((Enemy) entTarget).destroy();
				}
				else{ // tower
					m_aryTarget.remove(entTarget);
					((Tower) entTarget).release();
//					entTarget = null;
				}
			}
			m_view.removeChild(bullets, true);
			state = Common.ENTITY_IN_DESTROY;
			return true;
		}
		return false;
	}
	
	public void release()
	{
		if( action != null )
		{
			if( !action.isDone() )
				blow.stopAction(action);
		}
		m_view.removeChild(blow, true);
//		 blow = null;
	}
}
