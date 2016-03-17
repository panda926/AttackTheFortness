package org.AttackTheFortress.views;

import java.util.ArrayList;

import org.AttackTheFortress.Common;
import org.AttackTheFortress.DataManager;
import org.AttackTheFortress.Global;
import org.AttackTheFortress.entity.Bullet;
import org.AttackTheFortress.entity.Enemy;
import org.AttackTheFortress.entity.Entity;
import org.AttackTheFortress.entity.Tower;
import org.cocos2d.events.TouchDispatcher;
import org.cocos2d.layers.Layer;
import org.cocos2d.menus.Menu;
import org.cocos2d.menus.MenuItem;
import org.cocos2d.menus.MenuItemImage;
import org.cocos2d.menus.MenuItemToggle;
import org.cocos2d.nodes.Director;
import org.cocos2d.nodes.Label;
import org.cocos2d.nodes.Scene;
import org.cocos2d.nodes.Sprite;
import org.cocos2d.transitions.FadeTransition;
import org.cocos2d.types.CCPoint;
import org.cocos2d.types.CCRect;
import org.cocos2d.utils.CCFormatter;
import org.xmlpull.v1.XmlPullParser;

import android.app.Activity;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;

public class GameView extends Layer {

	private int[]					maxlevel_map = {15, 24, 24, 29, 29, 14};
	private int 					map_id = -1;
	private int 					game_mode = -1;
	private String 					bgPath, btnPath, mapPath;
	private float 					rScale_width = 0.0f;
	private float 					rScale_height = 0.0f;
	private float 					m_Xrate, m_Yrate;
	private String 					btn_no, btn_pr, map, desc, info;
	
	public MenuItem 				tower;
	private Label 					gold, lives, level;
	private Sprite 					range_ena, range_dis;
	public  Sprite					temp_range;
	private NoticeView 				notice_view = null;
	
	public 	int 					gold_value, lives_value, level_value, m_nTotalLevels;
	private int[] 					enemy_charac_nums = {8,20,10,10,13,20,10,15,14,18};
	public 	int 					enemiesKilled, enemiesKilledForLevel;
	private int 					m_nEnemyMove, m_nEnemyForLevel;
	private int 					m_towerTypeInDrag, m_gameState;
	
	private boolean 				tower_enable = false;
	private boolean					bScheduleBulletFly = false, bScheduleEnemyMove = false;
		
	private XmlPullParser 			parser;
	private ArrayList<CCPoint> 		m_points = null;
	private ArrayList<Entity> 		m_aryTower = null;
	private ArrayList<Bullet> 		m_aryBullet = null;
	private ArrayList<Entity> 		m_aryEnemy = null;
	
	public GameView( int map_idx, int mode ) {
		// TODO Auto-generated constructor stub
		setIsTouchEnabled(true);
		
		DataManager.shared().pauseMusic(0);
		
		map_id = map_idx + 1;
		game_mode = mode;
		
		bgPath = "gfx/Gameview/";
		btnPath = "gfx/buttons/";
		mapPath = "gfx/Maps/";

		rScale_width = Global.g_rScale_x;
		rScale_height = Global.g_rScale_y;
		
		gold_value = 0;
		level_value = 1;
		lives_value = 20;
		enemiesKilled = 0;
		m_nTotalLevels = maxlevel_map[map_idx];
		
		m_Xrate = getWidth() / 1024;
		m_Yrate = getHeight() / 768;

		if( m_points == null )
			m_points = new ArrayList<CCPoint>();
		if( m_aryTower == null )
			m_aryTower = new ArrayList<Entity>();
		if( m_aryBullet == null )
			m_aryBullet = new ArrayList<Bullet>();
		if( m_aryEnemy == null )
			m_aryEnemy = new ArrayList<Entity>();
		m_points.clear();
		cleanArray(m_aryTower);
		cleanArray(m_aryEnemy);
		cleanArray(m_aryBullet);
		
		m_towerStateView = null;
		m_selectedTower = null;
		m_gameState = Common.GAME_INITIAL;

		Activity cur = Director.sharedDirector().getActivity();
		switch( map_id )
		{
		case 1:
			parser = cur.getResources().getXml(Common.round1_id);	break;
		case 2:
			parser = cur.getResources().getXml(Common.round2_id);	break;
		case 3:
			parser = cur.getResources().getXml(Common.round3_id);	break;
		case 4:
			parser = cur.getResources().getXml(Common.round4_id);	break;
		case 5:
			parser = cur.getResources().getXml(Common.round5_id);	break;
		case 6:
			parser = cur.getResources().getXml(Common.round6_id);	break;
		}
		
		readRoundXML(1);
		loadResource();
		String title = String.format("Level: %d", level_value);
		showNoticeView(title, desc, info);
		m_gameState = Common.GAME_LOADED;
		DataManager.shared().playMusic(1);
	}
	
	private void setMapString()
	{
		map = String.format("%smap%d.png", mapPath, map_id);
	}
	
	private void setButtonString( String name )
	{
		btn_no = String.format("%s%s%d.png", btnPath, name, 1);
		btn_pr = String.format("%s%s%d.png", btnPath, name, 2);
	}

	private void loadResource()
	{
		/* map */
		setMapString();
		Sprite mapBg = Sprite.sprite(map);
		mapBg.setScaleX(rScale_width); mapBg.setScaleY(rScale_height);
		mapBg.setPosition(getWidth()/2, getHeight()/2);
		addChild(mapBg, -1);

		/* background */
		Sprite bg = Sprite.sprite(bgPath + "back.png");
		bg.setScaleX(rScale_width); bg.setScaleY(rScale_height);
		bg.setPosition(getWidth()/2, getHeight()/2);
		addChild(bg, 1);
		
		/* control */
		setButtonString( "BtnMainMenu" );
		MenuItem main_menu = MenuItemImage.item(btn_no, btn_pr, this, "onMainMenu");
		main_menu.setScaleX(rScale_width); main_menu.setScaleY(rScale_height);
		main_menu.setPosition(5*rScale_width + main_menu.getWidth()*rScale_width/2, 317*rScale_height - main_menu.getHeight()*rScale_height/2);
		
		Sprite label = Sprite.sprite(btnPath + "LabelBG.png");
		label.setScaleX(rScale_width); label.setScaleY(rScale_height);
		label.setPosition(main_menu.getPositionX() + main_menu.getWidth()*rScale_width/2 + label.getWidth()*rScale_width/2, main_menu.getPositionY());
		addChild(label, 1);
		
		String gold_str = String.format("Gold: %d", gold_value);
        gold = Label.label(gold_str, "DroidSans", 12);
        gold.setScaleX(rScale_width); gold.setScaleY(rScale_height);
        float x = label.getPositionX() - label.getWidth()*rScale_width/2;
        gold.setPosition(x + 30*rScale_width, label.getPositionY());
        addChild(gold, 1);
        
		String lives_str = String.format("Lives: %d", lives_value);
        lives = Label.label(lives_str, "DroidSans", 12);
        lives.setScaleX(rScale_width); lives.setScaleY(rScale_height);
        lives.setPosition(x + 85*rScale_width, label.getPositionY());
        addChild(lives, 1);
        
		String level_str = String.format("Level: %d", level_value);
        level = Label.label(level_str, "DroidSans", 12);
        level.setScaleX(rScale_width); level.setScaleY(rScale_height);
        level.setPosition(x + 140*rScale_width, label.getPositionY());
        addChild(level, 1);
        
		setButtonString( "BtnPlayLevel" );
		MenuItem playLevel = MenuItemImage.item(btn_no, btn_pr, this, "onPlayLevel");
		playLevel.setScaleX(rScale_width); playLevel.setScaleY(rScale_height);
		playLevel.setPosition(label.getPositionX() + label.getWidth()*rScale_width/2 + playLevel.getWidth()*rScale_width/2, main_menu.getPositionY());
		
		setButtonString( "BtnTower" );
		tower = MenuItemImage.item(btn_no, btn_pr, this, "onTower");
		tower.setScaleX(rScale_width); tower.setScaleY(rScale_height);
		tower.setPosition(playLevel.getPositionX() + playLevel.getWidth()*rScale_width/2 + tower.getWidth()*rScale_width/2, main_menu.getPositionY());
		
		MenuItem sound_on = MenuItemImage.item(btnPath+"sound.png", btnPath+"sound.png");
		MenuItem sound_off = MenuItemImage.item(btnPath+"sound_disable.png", btnPath+"sound_disable.png");
		MenuItem item_sound;
		if( !Common.sound_state )
			item_sound = MenuItemToggle.item(this, "onMute", sound_off, sound_on);
		else
			item_sound = MenuItemToggle.item(this, "onMute", sound_on, sound_off);
		item_sound.setScaleX(rScale_width); item_sound.setScaleY(rScale_height);
		item_sound.setPosition(458.5f*rScale_width, 21.5f*rScale_height);

		Menu menu = Menu.menu(main_menu, playLevel, tower, item_sound);
		menu.setPosition(0, 0);
		
		addChild(menu, 1);
		
		range_ena = Sprite.sprite("gfx/range_enable.png");
		range_ena.setScaleX(rScale_width); range_ena.setScaleY(rScale_height);
		range_ena.setPosition(0, 0);
		addChild(range_ena, 1);
		
		range_dis = Sprite.sprite("gfx/range_disable.png");
		range_dis.setScaleX(rScale_width); range_dis.setScaleY(rScale_height);
		range_dis.setPosition(0, 0);
		addChild(range_dis, 1);
		range_dis.setVisible(false);
		
		temp_range = range_ena;
		temp_range.setVisible(false);
	}
	
	private void readRoundXML( int level )
	{
		enemiesKilledForLevel = 0;
		float x, y;
		boolean level_state = false;
		x = y = 0.0f;
		try{
			while( parser.next() != XmlPullParser.END_DOCUMENT )
			{
				String name = parser.getName();
				if( name == null )
					continue;
				if( name.equals("direction_points") && parser.getAttributeCount() != -1 && level == 1 )
				{
					String x_str = parser.getAttributeValue(0);
					String y_str = parser.getAttributeValue(1);
					x = Integer.valueOf(x_str).intValue();
					y = Integer.valueOf(y_str).intValue();
					
					x *= m_Xrate; y *= m_Yrate;
			        CCPoint pt = Director.sharedDirector().convertCoordinate(x, y);
					m_points.add(CCPoint.make(pt.x, pt.y));
				}
				else if( name.equals("money") && parser.getAttributeCount() != -1 && level == 1 )
				{
					String money_str = parser.getAttributeValue(0);
					gold_value = Integer.valueOf(money_str).intValue();
				}
				else if( name.equals("Level") && parser.getAttributeCount() != -1 )
				{
					String number = parser.getAttributeName(0);
					if( (number != null) && number.equals("number") )
					{
						String level_str = parser.getAttributeValue(0);
						if( Integer.valueOf(level_str).intValue() != level )
							continue;
						level_value = Integer.valueOf(level_str).intValue();
						level_state = true;
					}
				}
				else if( name.equals("Enemy") && level_state && parser.getAttributeCount() != -1 )
				{
					int type = Integer.valueOf(parser.getAttributeValue(0)).intValue();
					
					float ratio = 1;
					if( game_mode == 3 )
						ratio = 1.07f;
					else if( game_mode == 1 )
						ratio = 0.8f;
					
					Enemy enemy = new Enemy(type, CCPoint.make(0, 0), this, enemy_charac_nums[type]);
					enemy.shootable = Integer.valueOf(parser.getAttributeValue(6)).intValue() > 0;
					enemy.maxLife = enemy.health = (int)(Integer.valueOf(parser.getAttributeValue(2)).intValue() * ratio);
					enemy.speed = Integer.valueOf(parser.getAttributeValue(1)).intValue();
					enemy.damage = Integer.valueOf(parser.getAttributeValue(3)).intValue();
					enemy.range = (int)(Integer.valueOf(parser.getAttributeValue(4)).intValue()*1.28);
					enemy.cost = Integer.valueOf(parser.getAttributeValue(5)).intValue();
					m_aryEnemy.add(enemy);
				}
				else if( name.equals("desc") && level_state && parser.getAttributeCount() != -1 )
				{
					String string = parser.getAttributeName(0);
					if( (string != null) && string.equals("string") )
					{
						desc = new CCFormatter().format("%s", parser.getAttributeValue(0));
						Enemy enemy = (Enemy) m_aryEnemy.get(0);
						m_nEnemyForLevel = m_aryEnemy.size();
						info = new CCFormatter().format("%d creeps  HP:%d", m_nEnemyForLevel, enemy.maxLife);
						level_state = false;
						break;
					}
				}
			}
		}catch (Exception e){
			Log.e("ReadXMLResourceFile", e.getMessage(), e);
		}
	}
	
	private void showNoticeView( String title, String content, String info )
	{
		if( notice_view != null )
		{
			notice_view.noticeClose();
			notice_view = null;
		}
		notice_view = new NoticeView();
		addChild(notice_view, 1);
		
		notice_view.addTitle(title);
		notice_view.addContent(content);
		notice_view.addInfo(info);
	}
	
	//private Sprite tsv = null;
	private TowerSelectView tsv_layer = null;
	public void onTower()
	{
		tower_enable = !tower_enable;
		
		if( tower_enable )
		{
			//float x = tower.getPositionX() + tower.getWidth()/2;
			//float y = tower.getPositionY() - tower.getHeight()/2;
			tsv_layer = new TowerSelectView(this);
			addChild(tsv_layer, 1);
		}
		else
		{
			removeChild(tsv_layer, true);
			tsv_layer = null;
		}
		if (m_towerStateView != null) {
			m_selectedTower = null;
			releaseTowerState();
			drawTowerRange();
		}
	}
	
	public void onPlayLevel()
	{
		if (m_towerStateView != null) {
			m_selectedTower = null;
			releaseTowerState();
			drawTowerRange();
		}
		if (m_gameState == Common.GAME_LEVEL_START)
			return;
		m_gameState = Common.GAME_LEVEL_START;
		
		m_nEnemyMove = 0;
		if( !bScheduleBulletFly )
		{
			bScheduleBulletFly = true;
			schedule("bulletFlyProcess", 0.03f);
		}
		if( !bScheduleEnemyMove )
		{
			bScheduleEnemyMove = true;
			schedule("enemyMoveProcess", 0.05f);
		}
		if( notice_view != null )
		{
			notice_view.onExit();
			notice_view = null;
		}
	}
	
	public void bulletFlyProcess( float dt )
	{
		if (m_aryBullet == null)
			return;
		int k = 0;
		while (k < m_aryBullet.size()) {
			Bullet objBullet = (Bullet)m_aryBullet.get(k);
			objBullet.fly();
			if (objBullet.state == Common.ENTITY_DESTROIED) {
				m_aryBullet.remove(objBullet);
				objBullet.release();
				objBullet = null;
				continue;
			}
			k++;
		}
		k = 0;
	}
	
	public void enemyMoveProcess( float dt )
	{
		if (m_aryEnemy == null)
			return;
		
		if (m_nEnemyForLevel <= enemiesKilledForLevel) { // all the enemies is killed!
			onNextLevel();
		}
		int i = 0;
		boolean update = false;
		while (i < m_aryEnemy.size()) {
			Enemy objEnemy = (Enemy)m_aryEnemy.get(i);
			if (objEnemy.state == Common.ENTITY_CREATED && m_nEnemyMove < 23)
				break;
			boolean stop = objEnemy.state == Common.ENTITY_CREATED;
			if (!objEnemy.moveOneStep()) {
				m_aryEnemy.remove(objEnemy);
				objEnemy.release();
				objEnemy = null;
				update = true;
				continue;
			}
			if (objEnemy.state <= Common.ENTITY_IN_DESTROY) {
				i++;
				continue;
			}
			
			int t_count = m_aryTower.size();
			for (int j = 0; j < t_count; j++) {
				Tower objTower = (Tower)m_aryTower.get(j);
				if (objTower.rangeContainPoint(objEnemy.pos)) {
					objTower.onTargetInRange();
				}
				if (objEnemy.rangeContainPoint(objTower.pos)) {
					objEnemy.onTargetInRange();
				}
			}
			
			if (stop) {
				
				break;
			}
			i ++;
			
		}
		if (m_nEnemyMove >= 23)
			m_nEnemyMove = 0;
		m_nEnemyMove ++;
		if (update)
			updateGameInfomations();
	}
	
	private void onNextLevel()
	{
		m_gameState = Common.GAME_LEVEL_END;
		if (bScheduleEnemyMove) {
			unschedule("enemyMoveProcess");
			bScheduleEnemyMove = false;
		}
		
		if (bScheduleBulletFly) {
			unschedule("bulletFlyProcess");
			bScheduleBulletFly = false;
		}
		
		cleanArray( m_aryBullet );
		cleanArray( m_aryEnemy );
		m_aryBullet.clear();
		m_aryEnemy.clear();
		
		if (level_value >= m_nTotalLevels) {// Current Level is the final level!!!
			onRoundEndWithState(true);
			return;
		}
		
		level_value ++;
		
		m_gameState = Common.GAME_LEVEL_LOADED;
		//m_nEnemyMove = 0;
		//load level informations
		readRoundXML(level_value);
		String title = String.format("Level: %d", level_value);
		showNoticeView(title, desc, info);

		for (int i = 0; i < m_aryTower.size(); i++) {
			Tower objTower = (Tower)m_aryTower.get(i);
			objTower.hasBullet = false;
		}
		
		//show information about next level;
		updateGameInfomations();
	}
	
	private void onRoundEndWithState( boolean succed )
	{
		// will
    	Scene scene = Scene.node();
    	ScoreView score = new ScoreView();
    	if( succed )
    		score.setOption(succed, map_id, game_mode, level_value, enemiesKilled, gold_value, lives_value);
    	else
    		score.setOption(succed, map_id, game_mode, level_value, enemiesKilled, gold_value, lives_value);
        scene.addChild(score, 0);
    	Director.sharedDirector().replaceScene(new FadeTransition(1.0f, scene));
	}
	
	public void onMainMenu()
	{
    	Scene scene = Scene.node();
        scene.addChild(new MapView(), 0);
    	Director.sharedDirector().replaceScene(new FadeTransition(1.0f, scene));
	}
	
	public void onMute()
	{
		Common.sound_state = !Common.sound_state;
		if( !Common.sound_state )
			DataManager.shared().pauseMusic(1);
		else
			DataManager.shared().playMusic(1);
	}

	public ArrayList<CCPoint> getRoadPath()
	{
		return m_points;
	}
	
	public ArrayList<Entity> getTowerArray()
	{
		return m_aryTower;
	}
	
	public ArrayList<Bullet> getBulletArray()
	{
		return m_aryBullet;
	}
	
	public ArrayList<Entity> getEnemyArray()
	{
		return m_aryEnemy;
	}
	
	public void anEnemyEscaped()
	{
		lives_value --;
		updateGameInfomations();
		if (lives_value <= 0) { // game over
			m_gameState = Common.GAME_ROUND_FAILED;
			if (bScheduleEnemyMove) {
				unschedule("enemyMoveProcess");
				bScheduleEnemyMove = false;
			}
			
			if (bScheduleBulletFly) {
				unschedule("bulletFlyProcess");
				bScheduleBulletFly = false;
			}
			onRoundEndWithState(false);
		}
	}
	
	public void updateGold( int value )
	{
		gold.setString(new CCFormatter().format("Gold: %d", value));
	}

	private Tower m_towerInDrag = null, m_selectedTower;
	private boolean m_bDragging, m_bBuildable;
	private TowerStateView m_towerStateView = null;
	public void onSelectTower( int nType )
	{
		m_towerTypeInDrag = nType;
		if( m_towerInDrag != null )
			m_towerInDrag = null;
		m_towerInDrag = new Tower(m_towerTypeInDrag, CCPoint.make(0, 0), this);
		m_towerInDrag.state = Common.ENTITY_IN_DRAGGING;
		m_bDragging = true;
	}

	@Override
	public boolean ccTouchesBegan(MotionEvent event) {
		
        CCPoint currentPos = Director.sharedDirector().convertCoordinate(event.getX(), event.getY());
		m_bDragging = false;
		if( m_towerTypeInDrag >= 1 ){
			m_selectedTower = null;
			m_towerInDrag.setPos(currentPos);
			m_bDragging = true;
			m_bBuildable = true;
			
			for( int i=0; i<m_points.size()-1; i++ ){
				CCPoint a = m_points.get(i);
				CCPoint b = m_points.get(i+1);
				if( distFromPt2LineAB(currentPos, a, b) < (m_towerInDrag.tower.getHeight()+50)/2 ){
					m_bBuildable = false;
					break;
				}
			}
			if( m_towerStateView != null ){
				releaseTowerState();
			}
		} else if( m_aryTower != null ){
			int idx;
			for( idx=0; idx<m_aryTower.size(); idx++ ){
				Tower objTower = (Tower) m_aryTower.get(idx);
				CCRect rect = CCRect.make(objTower.pos.x - objTower.size.width /2, 
						 objTower.pos.y - objTower.size.height/2,
						 objTower.size.width, objTower.size.height);
				if (CCRect.containsPoint(rect, currentPos)) {
					if (m_selectedTower == objTower) {
						return false;
					}
					if (m_selectedTower != null) {
						m_selectedTower.state = Common.ENTITY_LIVE;
						m_selectedTower.setPos(m_selectedTower.pos);
					} 
					
					m_selectedTower = objTower;
					m_selectedTower.state = Common.ENTITY_SELECTED;
					m_selectedTower.setPos(m_selectedTower.pos);
					
					if(m_towerStateView != null)
						releaseTowerState();
					
					if (m_towerStateView == null) {
						m_towerStateView = new TowerStateView(objTower, CCPoint.make(currentPos.x,currentPos.y), this);
						addChild(m_towerStateView, 3);
						
					} 
					if( notice_view != null )
					{
						notice_view.onExit();
						notice_view = null;
					}

					break;
				}
			}
			if( idx == m_aryTower.size() )
			{
				m_selectedTower = null;
				if (m_towerStateView != null) {
					//[m_towerStateView removeFromSuperview];
					releaseTowerState();
				}
			}
			drawTowerRange();
		}
		
		return TouchDispatcher.kEventHandled;
	}
	
	@Override
	public boolean ccTouchesMoved(MotionEvent event) {
		
        CCPoint currentPos = Director.sharedDirector().convertCoordinate(event.getX(), event.getY());
        if(m_bDragging && m_towerTypeInDrag >= 1) {
			
			m_towerInDrag.setPos(currentPos);
			m_bBuildable = true;
			if( tsv_layer != null )
			{
				removeChild(tsv_layer, true);
				tsv_layer = null;
				tower_enable = false;
			}
			
			int count = m_points.size();
			for (int i = 0; i < count-1; i++) {
				CCPoint A = m_points.get(i);
				CCPoint B = m_points.get(i+1);
				
				if (distFromPt2LineAB(currentPos, A, B) < (m_towerInDrag.size.height+50 * m_Xrate)/2) {
					m_bBuildable = false;
					break;
				}
				
			}
			
			if (m_bBuildable) {
				for (int i = 0; i < m_aryTower.size(); i++)  {
					
					Tower objTower = (Tower)m_aryTower.get(i);
					RectF trect1 = new RectF(currentPos.x - m_towerInDrag.size.width /2, 
							   currentPos.y - m_towerInDrag.size.height /2, 
							   m_towerInDrag.size.width, m_towerInDrag.size.height);
					RectF trect2 = new RectF(objTower.pos.x - objTower.size.width /2, 
							   objTower.pos.y - objTower.size.height /2, 
							   objTower.size.width, objTower.size.height);
					if (RectF.intersects(trect1,trect2)) {
						m_bBuildable = false;
						break;
					}
				}
			}
			if (m_bBuildable) {
				if( temp_range == range_dis )
					temp_range.setVisible(false);
				temp_range = range_ena;
			} else {
				if( temp_range == range_ena )
					temp_range.setVisible(false);
				temp_range = range_dis;
			}

			drawTowerRange();
		}

        return TouchDispatcher.kEventIgnored;
	}

	@Override
    public boolean ccTouchesEnded(MotionEvent event) {
		
        CCPoint currentPos = Director.sharedDirector().convertCoordinate(event.getX(), event.getY());
		if (m_bDragging == true && m_towerTypeInDrag >= 1) {
			m_bDragging = false;
			
			m_bBuildable = true;
			if (gold_value < m_towerInDrag.cost)
				m_bBuildable = false;
			
			if (m_bBuildable) {
				for (int i = 0; i < m_points.size()-1; i++) {
					CCPoint A = m_points.get(i);
					CCPoint B = m_points.get(i+1);
					
					if (distFromPt2LineAB(currentPos, A, B) < (m_towerInDrag.size.height+50 * m_Xrate)/2) {
						m_bBuildable = false;
						break;
					}
					
				}
			}
			if (m_bBuildable) {
				for (int i = 0; i < m_aryTower.size(); i++)  {
					Tower objTower = (Tower)m_aryTower.get(i);
					RectF trect1 = new RectF(currentPos.x - m_towerInDrag.size.width /2, 
											   currentPos.y - m_towerInDrag.size.height /2, 
											   m_towerInDrag.size.width, m_towerInDrag.size.height);
					RectF trect2 = new RectF(objTower.pos.x - objTower.size.width /2, 
											   objTower.pos.y - objTower.size.height /2, 
											   objTower.size.width, objTower.size.height);
					if (RectF.intersects(trect1,trect2)) {
						m_bBuildable = false;
						break;
					}
				}
			}
			
			if (m_bBuildable && gold_value >= m_towerInDrag.cost) {
				m_towerInDrag.state = Common.ENTITY_LIVE;
				m_towerInDrag.setPos(currentPos);
				m_aryTower.add(m_towerInDrag);
				gold_value -= m_towerInDrag.cost;
				updateGold(gold_value);
				updateGameInfomations();
				
			} else {
				removeChild(m_towerInDrag.tower, true);

				String string;
				if (gold_value < m_towerInDrag.cost)
					string = "Not Enough Gold Pieces!";
				else
					string = "The tower can't be built at there!";
				showNoticeView( " ", string, " " );
			}
			m_towerTypeInDrag = -1;
			
			m_towerInDrag = null;
			drawTowerRange();
			//[self drawView:self];
		}

		return TouchDispatcher.kEventIgnored;  // TODO Auto-generated method stub
    }
	
	public void drawTowerRange() {
		float s;
		if (m_towerTypeInDrag > 0 && m_bDragging) {
			
			if (m_bBuildable){
				if( temp_range == range_dis )
					temp_range.setVisible(false);
				temp_range = range_ena;
			}
			else {
				if( temp_range == range_ena )
					temp_range.setVisible(false);
				temp_range = range_dis;
			}

			s = m_towerInDrag.range * 2 * m_Yrate / temp_range.getWidth();
			temp_range.setScale(s);
			temp_range.setPosition(m_towerInDrag.pos.x, m_towerInDrag.pos.y);
			temp_range.setVisible(true);

		} else if (m_selectedTower != null ) {
			if  (m_aryTower.indexOf(m_selectedTower) == -1) {
				m_selectedTower = null;
				return;
			}
			if( temp_range == range_dis )
				temp_range.setVisible(false);
			temp_range = range_ena;
			
			s = m_selectedTower.range * 2 * m_Yrate / temp_range.getWidth();
			temp_range.setScale(s);
			temp_range.setPosition(m_selectedTower.pos.x, m_selectedTower.pos.y);
			temp_range.setVisible(true);
		} else {
			temp_range.setVisible(false);
		}


	}
	private float distFromPt2LineAB( CCPoint pt, CCPoint A, CCPoint B ){
		float pt2A, pt2B, A2B;
		pt2A = (float)Math.sqrt((pt.x - A.x)*(pt.x -A.x) + (pt.y - A.y) * (pt.y - A.y));
		pt2B = (float)Math.sqrt((pt.x - B.x)*(pt.x -B.x) + (pt.y - B.y) * (pt.y - B.y));
		A2B = (float)Math.sqrt((A.x - B.x) * (A.x - B.x) + (A.y - B.y) * (A.y - B.y));
		if (A2B < pt2A || A2B < pt2B) {
			return pt2A < pt2B ? pt2A:pt2B;
		} else {
			float p = (pt2A + pt2B + A2B)/2;
			float s = (float)Math.sqrt(p * (p - pt2A) * (p-pt2B) * (p - A2B));
			return 2 * s / A2B;
		}
		
	}

	public void updateGameInfomations() {
		// TODO Auto-generated method stub
		gold.setString(new CCFormatter().format("Gold: %d", gold_value));
		lives.setString(new CCFormatter().format("Lives: %d", lives_value));
		level.setString(new CCFormatter().format("Level: %d", level_value));
	}

	public void destroyTower(Tower tower) {
		// TODO Auto-generated method stub
		if (m_selectedTower == tower && m_towerStateView != null) {
			//[m_towerStateView removeFromSuperview];
			drawTowerRange();
			m_selectedTower = null;
			releaseTowerState();
		}
		m_aryTower.remove(tower);
		tower.destroy();
		tower.release();
	}
	
	private void cleanArray( ArrayList<?> array )
	{
		int size = array.size();
		for( int i=0; i<size; i++ )
		{
			Object obj = array.get(i);
			if( array.equals(m_aryTower) )
				((Tower) obj).release();
			else if( array.equals(m_aryEnemy) )
				((Enemy) obj).release();
			else if( array.equals(m_aryBullet) )
				((Bullet) obj).release();
		}
	}

	private void releaseTowerState()
	{
		removeChild(m_towerStateView, true);
		m_towerStateView.removeAllChildren(true);
		m_towerStateView.onExit();
		m_towerStateView = null;
	}

}
