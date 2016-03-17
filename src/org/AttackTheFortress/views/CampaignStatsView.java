package org.AttackTheFortress.views;

import org.AttackTheFortress.GameSettingInfo;
import org.AttackTheFortress.Global;
import org.cocos2d.layers.Layer;
import org.cocos2d.menus.Menu;
import org.cocos2d.menus.MenuItem;
import org.cocos2d.menus.MenuItemImage;
import org.cocos2d.nodes.Label;
import org.cocos2d.nodes.Sprite;
import org.cocos2d.nodes.Label.TextAlignment;
import org.cocos2d.utils.CCFormatter;

public class CampaignStatsView extends Layer {
	
	private float rScale_width, rScale_height;
	private float scaleFactor;
	
	private Label[] m_lblRound = {null, null, null, null, null, null};
	private Label m_lblTotalScore;
	private MapView map_view;
	
	private final int MAX_MAP = 6;

	public CampaignStatsView( MapView view ) {
		// TODO Auto-generated constructor stub
		map_view = view;
		
		rScale_width = Global.g_rScale_x;
		rScale_height = Global.g_rScale_y;

		scaleFactor = 0.41f;
		
		Sprite bgSprite = Sprite.sprite("gfx/roundselectview/cmpnStats.png");
		bgSprite.setScaleX(rScale_width); bgSprite.setScaleY(rScale_height);
		bgSprite.setPosition(getWidth()/2, getHeight()/2);
		addChild(bgSprite, -1);

		MenuItem close = MenuItemImage.item("gfx/buttons/BtnClose1.png", "gfx/buttons/BtnClose2.png", this, "onCampaignClose");
		close.setScaleX(rScale_width); close.setScaleY(rScale_height);
		close.setPosition(150*rScale_width, 70*rScale_height);
		
		Menu menu = Menu.menu(close);
		menu.setPosition(0, 0);
		
		addChild(menu, 0);
		
		showDatas();
	}
	
	private void showDatas()
	{
		String[] mode_str = {"Easy", "Normal", "Hard"};
		for( int i=0; i<MAX_MAP; i++ )
		{
			if ( GameSettingInfo.score4Rounds[i][1] <= 0)
				continue;
			String fmt = String.format("%d", GameSettingInfo.score4Rounds[i][1]);
			m_lblRound[i] = Label.node(fmt, 0, 0, TextAlignment.LEFT, "DroidSans", 20 * scaleFactor);
			m_lblRound[i].setPosition(270*rScale_width + 58*rScale_width * (i%3), 170*rScale_height - 90*rScale_height*(i/3));
			m_lblRound[i].setScaleX(rScale_width); m_lblRound[i].setScaleY(rScale_height);
			addChild(m_lblRound[i], 1);
			
			Label label = Label.node(mode_str[GameSettingInfo.score4Rounds[i][0] - 1], 0, 0, TextAlignment.LEFT, "DroidSans", 20 * scaleFactor);
			label.setPosition(270*rScale_width + 58*rScale_width * (i%3), 162*rScale_height - 90*rScale_height*(i/3));
			label.setScaleX(rScale_width); label.setScaleY(rScale_height);
			addChild(label, 1);
		}
		int totalScore = 0;
		for (int i = 0; i < 6; i++)
			totalScore += GameSettingInfo.score4Rounds[i][1];

		boolean allOccupied = true, allhardMode = true;
		for (int i = 0; i <6; i++) {
			if (GameSettingInfo.score4Rounds[i][0] < 2) {
				allOccupied = false;
			} else 
				//totalScore += 50000;//original
				totalScore += 10000;
			
			if (GameSettingInfo.score4Rounds[i][0] < 3)
				allhardMode = false;
		}
		if (allOccupied)
			//totalScore += 75000; //original
			totalScore += 15000;
		
		//if (totalScore >= 600000)//original
		//	totalScore += 100000;
		
		if (allhardMode)
			totalScore += 200000;

		String str = new CCFormatter().format("%d", totalScore);
		m_lblTotalScore = Label.node(str, 0, 0, TextAlignment.LEFT, "DroidSans", 50 * scaleFactor);
		m_lblTotalScore.setScaleX(rScale_width); m_lblTotalScore.setScaleY(rScale_height);
		m_lblTotalScore.setPosition(getWidth()*150/480, getHeight()/2+2*rScale_height);
		addChild(m_lblTotalScore, 1);
	}
	
	public void onCampaignClose()
	{
		onExit();
	}

	@Override
	public void onExit()
	{
		map_view.enableButtons(true);
		this.removeAllChildren(true);
		super.onExit();
	}
}
