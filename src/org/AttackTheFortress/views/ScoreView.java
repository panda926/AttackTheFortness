package org.AttackTheFortress.views;

import org.AttackTheFortress.DataManager;
import org.AttackTheFortress.GameSettingInfo;
import org.AttackTheFortress.Global;
import org.cocos2d.layers.Layer;
import org.cocos2d.menus.Menu;
import org.cocos2d.menus.MenuItem;
import org.cocos2d.menus.MenuItemImage;
import org.cocos2d.nodes.Director;
import org.cocos2d.nodes.Label;
import org.cocos2d.nodes.Scene;
import org.cocos2d.nodes.Sprite;
import org.cocos2d.nodes.Label.TextAlignment;
import org.cocos2d.transitions.FadeTransition;
import org.cocos2d.types.CCRect;

class ScoreView extends Layer 
{
	private MenuItem quit;
	private Sprite score_msg;
	private Label title, level_1, level_2, enemy_1, enemy_2, gold_1, gold_2, live_1, live_2, score_1, score_2;
	
	private float rScale_width = 0.0f;
	private float rScale_height = 0.0f;
	private float m_Xrate, m_Yrate;
	
	private float scaleFactor;

	private int m_nRound, m_nMode, m_ntotalLevels, m_nEnemiesKilled, m_nGoldPieces, m_nTotalLives, m_nTotalPoint;
	boolean m_bSucceed;

	public ScoreView()
	{
		rScale_width = Global.g_rScale_x;
		rScale_height = Global.g_rScale_y;
		
		scaleFactor = 0.41f;
		
		m_Xrate = getWidth()/1024; m_Yrate = getHeight()/768;

		Sprite bg = Sprite.sprite("gfx/Gameview/scoreviewback.png");
		bg.setScaleX(rScale_width); bg.setScaleY(rScale_height);
		bg.setPosition(getWidth()/2, getHeight()/2);
		addChild(bg, -1);
		
		quit = MenuItemImage.item("gfx/buttons/BtnQuitToMap1.png", "gfx/buttons/BtnQuitToMap2.png", this, "onQuitMap");
		quit.setScaleX(rScale_width); quit.setScaleY(rScale_height);
		quit.setPosition(getWidth()/2, 90*scaleFactor);
		
		Menu menu = Menu.menu(quit);
		menu.setPosition(0, 0);
		
		addChild(menu, 0);
	}

	public void setOption( boolean succeed, int nRound, int dft, int totalLevels, int enemiesKilled, int goldPieces, int totalLives )
	{
		scaleFactor = 0.41f;
		m_nRound = nRound;
		m_nMode = dft;
		m_bSucceed = succeed;
		m_ntotalLevels = totalLevels;
		m_nEnemiesKilled = enemiesKilled;
		m_nGoldPieces = goldPieces;
		m_nTotalLives = totalLives;
		
		m_nTotalPoint = m_ntotalLevels * 100 + m_nEnemiesKilled * 10 + m_nGoldPieces * 5 + m_nTotalLives;
		
		if( m_bSucceed )
			score_msg = Sprite.sprite("gfx/Gameview/ScoreMsg-Victory.png");
		else
			score_msg = Sprite.sprite("gfx/Gameview/ScoreMsg-Fail.png");
		score_msg.setScaleX(rScale_width); score_msg.setScaleY(rScale_height);
		score_msg.setPosition( (35+score_msg.getWidth()/2)*rScale_width, (234-score_msg.getHeight()/2)*rScale_height );
		addChild(score_msg, 0);
		
		showDatas();
	}
	
	private void showDatas()
	{
		if( m_bSucceed )
			title = Label.node("Excellent! Try next round!", 0, 0, TextAlignment.LEFT, "DroidSans", 10);
//			title = Label.label("Excellent! Try next round!", "DroidSans", 35*m_Yrate);
		else
			title = Label.node("You are DEFEATED!  Please try again...", 0, 0, TextAlignment.LEFT, "DroidSans", 10);
//			title = Label.label("You are DEFEATED!  Please try again...", "DroidSans", 35*m_Yrate);
		title.setPosition(-200, -200);
		

		float idx = 0;
		level_1 = Label.node("Total Waves Defeated", 0, 0, TextAlignment.LEFT, "DroidSans", 15*rScale_height);
//		level_1 = Label.label("Total Waves Defeated", "DroidSans", 30 * m_Yrate);
//		level_1.setContentSize(250*m_Xrate*rScale_width, 35*m_Yrate*rScale_height);
//		level_1.setPosition((550 * m_Xrate + level_1.getWidth()/2)*rScale_width, getHeight()-basey*rScale_height);
		level_1.setPosition(getWidth()/2+level_1.getBoundingBox().size.width/2, getHeight()-(110 + idx * 20)*rScale_height);
		
		String fmt = String.format("%d", m_ntotalLevels);
		level_2 = Label.node(fmt, 0, 0, TextAlignment.LEFT, "DroidSans", 15*rScale_height);
//		level_2.setContentSize(65*m_Xrate*rScale_width, 35*m_Yrate*rScale_height);
//		level_2.setPosition((850 * m_Xrate + level_2.getWidth()/2)*rScale_width, getHeight()-basey*rScale_height);
		level_2.setPosition(CCRect.maxX(level_1.getBoundingBox())+20*rScale_width, level_1.getPositionY());

		idx ++;
		enemy_1 = Label.node("Total Enemies Killed", 0, 0, TextAlignment.LEFT, "DroidSans", 15*rScale_height);
//		enemy_1 = Label.label("Total Enemies Killed", "DroidSans", 30 * m_Yrate);
//		enemy_1.setContentSize(250*m_Xrate*rScale_width, 35*m_Yrate*rScale_height);
//		enemy_1.setPosition(level_1.getPositionX(), getHeight()-(basey + 60 * scaleFactor)*rScale_height);
		enemy_1.setPosition(level_1.getPositionX(), getHeight()-(110 + idx * 20)*rScale_height);
		
		fmt = String.format("%d", m_nEnemiesKilled);
		enemy_2 = Label.node(fmt, 0, 0, TextAlignment.LEFT, "DroidSans", 15*rScale_height);
//		enemy_2.setContentSize(65*m_Xrate*rScale_width, 35*m_Yrate*rScale_height);
//		enemy_2.setPosition((850 * m_Xrate + enemy_2.getWidth()/2)*rScale_width, getHeight()-(basey + 60 * scaleFactor)*rScale_height);
		enemy_2.setPosition(level_2.getPositionX(), enemy_1.getPositionY());

		idx ++;
		gold_1 = Label.node("Gold Pieces", 0, 0, TextAlignment.LEFT, "DroidSans", 15*rScale_height);
//		gold_1 = Label.label("Gold Pieces", "DroidSans", 30 * m_Yrate);
//		gold_1.setContentSize(250*m_Xrate*rScale_width, 35*m_Yrate*rScale_height);
//		gold_1.setPosition(level_1.getPositionX(), getHeight()-(basey + 120 * scaleFactor)*rScale_height);
		gold_1.setPosition(level_1.getPositionX(), getHeight()-(110 + idx * 20)*rScale_height);
		
		fmt = String.format("%d", m_nGoldPieces);
		gold_2 = Label.node(fmt, 0, 0, TextAlignment.LEFT, "DroidSans", 15*rScale_height);
//		gold_2 = Label.label(fmt, "DroidSans", 30 * m_Yrate);
//		gold_2.setContentSize(65*m_Xrate*rScale_width, 35*m_Yrate*rScale_height);
//		gold_2.setPosition((850 * m_Xrate + gold_2.getWidth()/2)*rScale_width, getHeight()-(basey + 120 * scaleFactor)*rScale_height);
		gold_2.setPosition(level_2.getPositionX(), gold_1.getPositionY());

		idx ++;
		live_1 = Label.node("Lives", 0, 0, TextAlignment.LEFT, "DroidSans", 15*rScale_height);
//		live_1 = Label.label("Lives", "DroidSans", 30 * m_Yrate);
//		live_1.setContentSize(250*m_Xrate*rScale_width, 35*m_Yrate*rScale_height);
//		live_1.setPosition(level_1.getPositionX(), getHeight()-(basey + 180 * scaleFactor)*rScale_height);
		live_1.setPosition(level_1.getPositionX(), getHeight()-(110 + idx * 20)*rScale_height);
		
		fmt = String.format("%d", m_nTotalLives);
		live_2 = Label.node(fmt, 0, 0, TextAlignment.LEFT, "DroidSans", 15*rScale_height);
//		live_2 = Label.label(fmt, "DroidSans", 30 * m_Yrate);
//		live_2.setContentSize(65*m_Xrate*rScale_width, 35*m_Yrate*rScale_height);
//		live_2.setPosition((850 * m_Xrate + live_2.getWidth()/2)*rScale_width, getHeight()-(basey + 180 * scaleFactor)*rScale_height);
		live_2.setPosition(level_2.getPositionX(), live_1.getPositionY());

		score_1 = Label.node("Total Points", 0, 0, TextAlignment.LEFT, "DroidSans", 15*rScale_height);
//		score_1 = Label.label("Total Points", "DroidSans", 30 * m_Yrate);
//		score_1.setContentSize(250*m_Xrate*rScale_width, 35*m_Yrate*rScale_height);
//		score_1.setPosition(level_1.getPositionX(), getHeight()-(basey + 265 * scaleFactor)*rScale_height);
		score_1.setPosition(level_1.getPositionX(), getHeight()-225*rScale_height);
		
		fmt = String.format("%d", m_nTotalLives);
		score_2 = Label.node(fmt, 0, 0, TextAlignment.LEFT, "DroidSans", 15*rScale_height);
//		score_2 = Label.label(fmt, "DroidSans", 30 * m_Yrate);
//		score_2.setContentSize(65*m_Xrate*rScale_width, 35*m_Yrate*rScale_height);
//		score_2.setPosition((850 * m_Xrate + score_2.getWidth()/2)*rScale_width, getHeight()-(basey + 265 * scaleFactor)*rScale_height);
		score_2.setPosition(level_2.getPositionX(), score_1.getPositionY());

		addChild(title, 1);
		addChild(level_1, 1); addChild(level_2, 1);
		addChild(enemy_1, 1); addChild(enemy_2, 1);
		addChild(gold_1, 1); addChild(gold_2, 1);
		addChild(live_1, 1); addChild(live_2, 1);
		addChild(score_1, 1); addChild(score_2, 1);
		
		if (m_bSucceed) {
			// save score
			DataManager dataManager = DataManager.shared();
			//int totalScore = 0;
			if (GameSettingInfo.finalOccupiedRound < m_nRound)
				GameSettingInfo.finalOccupiedRound = m_nRound;
			GameSettingInfo.score4Rounds[m_nRound-1][1] += m_nTotalPoint;
			if (GameSettingInfo.score4Rounds[m_nRound-1][0] < m_nMode)
				GameSettingInfo.score4Rounds[m_nRound-1][0] = m_nMode;
			
			//dataManager.gameInfo = game_info;
			dataManager.saveSetting();
		}
	}
	
	public void onQuitMap()
	{
		DataManager dataManager = DataManager.shared();
		
		dataManager.pauseMusic(1);
		dataManager.playMusic(0);
		
		// will
    	Scene scene = Scene.node();
        scene.addChild(new MapView(), 0);
    	Director.sharedDirector().replaceScene(new FadeTransition(1.0f, scene));
	}
}
