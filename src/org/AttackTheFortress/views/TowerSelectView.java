package org.AttackTheFortress.views;

import org.AttackTheFortress.Global;
import org.cocos2d.events.TouchDispatcher;
import org.cocos2d.layers.Layer;
import org.cocos2d.nodes.Director;
import org.cocos2d.nodes.Sprite;
import org.cocos2d.types.CCPoint;
import org.cocos2d.types.CCRect;
import org.cocos2d.utils.CCFormatter;

import android.view.MotionEvent;

public class TowerSelectView extends Layer {
	
	private float rScale_width = 0.0f;
	private float rScale_height = 0.0f;
	
	private int m_selTower;
	
	private Sprite tsv;
	public Sprite tsv_tower = null;
	private GameView m_view;

	public TowerSelectView( GameView view) {
		// TODO Auto-generated constructor stub
		rScale_width = Global.g_rScale_x;
		rScale_height = Global.g_rScale_y;
		
		m_selTower = -1;
		
		setIsTouchEnabled(true);
		
		m_view = view;
		
		tsv = Sprite.sprite("gfx/Towerselectview/tsv_back.png");
		tsv.setScaleX(rScale_width); tsv.setScaleY(rScale_height);
		float x = CCRect.maxX(view.tower.getBoundingBox()) - CCRect.width(tsv.getBoundingBox())/2;
		float y = CCRect.minY(view.tower.getBoundingBox()) - CCRect.height(tsv.getBoundingBox())/2;
		tsv.setPosition(x, y);
		addChild(tsv, 0);
	}
	
	@Override
	public boolean ccTouchesBegan(MotionEvent event) {

        CCPoint currentPos = Director.sharedDirector().convertCoordinate(event.getX(), event.getY());
		
        CCRect temp = tsv.getBoundingBox();
		if( CCRect.containsPoint(temp, currentPos) ){
			float delta = currentPos.y - temp.origin.y;
			m_selTower = 5 - (int)(delta / (temp.size.height/6.0));
			showTsvTower();
		}else {
			int oldSel = m_selTower;
			m_selTower = -1;
			if (oldSel >= 0) {
				m_view.onSelectTower(oldSel + 1);
				m_view.ccTouchesBegan(event);
			}
		}

		return TouchDispatcher.kEventHandled;
	}
	
	@Override
	public boolean ccTouchesMoved(MotionEvent event) {
		
        CCPoint currentPos = Director.sharedDirector().convertCoordinate(event.getX(), event.getY());
		
        CCRect temp = tsv.getBoundingBox();
		if( CCRect.containsPoint(temp, currentPos) ){
			float delta = currentPos.y - temp.origin.y;
			m_selTower = 5 - (int)(delta / (temp.size.height/6.0));
			showTsvTower();
		}else {
			int oldSel = m_selTower;
			m_selTower = -1;
			showTsvTower();
			if (oldSel >= 0) {
				if( tsv_tower != null ){
					removeChild(tsv_tower, true);
					tsv_tower = null;
				}
				m_view.onSelectTower(oldSel + 1);
				m_view.ccTouchesMoved(event);
			}
		}

		return TouchDispatcher.kEventIgnored;
	}
	
	@Override
	public boolean ccTouchesEnded(MotionEvent event) {
		
		return TouchDispatcher.kEventHandled;
	}
	
	private void showTsvTower()
	{
		if( m_selTower >= 0 ){
			if( tsv_tower != null ){
				removeChild(tsv_tower, true);
				tsv_tower = null;
			}
			tsv_tower = Sprite.sprite(new CCFormatter().format("gfx/Towerselectview/tsv_tower%d.png", m_selTower+1));
			tsv_tower.setScaleX(rScale_width); tsv_tower.setScaleY(rScale_height);
			float x, y;
	        CCRect temp = tsv.getBoundingBox();
			x = temp.origin.x - CCRect.width(tsv_tower.getBoundingBox())/2;
			y = CCRect.minY(m_view.tower.getBoundingBox()) - temp.size.height / 6 * m_selTower - CCRect.height(tsv_tower.getBoundingBox())/2;
			if( m_selTower == 5 )
				tsv_tower.setPosition(x, CCRect.height(tsv_tower.getBoundingBox())/2);
			else
				tsv_tower.setPosition(x, y);
			addChild(tsv_tower, 0);
		}
	}
}
