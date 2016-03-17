package org.AttackTheFortress.views;

import javax.microedition.khronos.opengles.GL10;

import org.AttackTheFortress.GameSettingInfo;
import org.AttackTheFortress.Global;
import org.cocos2d.layers.Layer;
import org.cocos2d.menus.Menu;
import org.cocos2d.menus.MenuItem;
import org.cocos2d.menus.MenuItemImage;
import org.cocos2d.nodes.Director;
import org.cocos2d.nodes.Scene;
import org.cocos2d.nodes.Sprite;
import org.cocos2d.nodes.TextureManager;
import org.cocos2d.nodes.TextureNode;
import org.cocos2d.transitions.FadeTransition;

public class MapView extends Layer {

	private MenuItem item_1, item_2, item_3, item_4, item_5, item_6;
	private MenuItem item_7, item_8;
	private MenuItem[] mapButtons = {null, null, null, null, null, null};
	private TextureNode[] unlock_nodes = {null, null, null, null, null, null};
	private TextureNode[] lock_nodes = {null, null, null, null, null, null};
	
	private Layer mode_layer, score_layer;
	
	private String bgPath, btnPath;
	private String map_no, map_pr;
	private String btn_no, btn_pr;
	private float rScale_width = 0.0f;
	private float rScale_height = 0.0f;
	
	private int map_idx;
	
	public MapView() {
		// TODO Auto-generated constructor stub
		bgPath = "gfx/roundselectview/";
		btnPath = "gfx/buttons/";

		rScale_width = Global.g_rScale_x;
		rScale_height = Global.g_rScale_y;
		
		map_idx = -1;
		loadResource();
	}
	
	private void setMapString( int id )
	{
		map_no = String.format("%sBtnMap%d_%d.png", bgPath, id, 1);
		map_pr = String.format("%sBtnMap%d_%d.png", bgPath, id, 2);
	}
	
	private void setButtonString( String name )
	{
		btn_no = String.format("%s%s%d.png", btnPath, name, 1);
		btn_pr = String.format("%s%s%d.png", btnPath, name, 2);
	}

	private void loadResource()
	{
		// background
		Sprite bgSprite = Sprite.sprite(bgPath + "mapselectview.png");
		bgSprite.setScaleX(rScale_width); bgSprite.setScaleY(rScale_height);
		bgSprite.setPosition(getWidth()/2, getHeight()/2);
		addChild(bgSprite, -1);
		
		/* map */
		setMapString(1);
		// map_1
		item_1 = MenuItemImage.item(map_no, map_pr, this, "showGameView1");
		item_1.setPosition(89*rScale_width, 102.5f*rScale_height);
		item_1.setScaleX(rScale_width); item_1.setScaleY(rScale_height);
		
		setMapString(2);
		// map_2
		item_2 = MenuItemImage.item(map_no, map_pr, this, "showGameView2");
		item_2.setPosition(246.5f*rScale_width, 81*rScale_height);
		item_2.setScaleX(rScale_width); item_2.setScaleY(rScale_height);
		
		setMapString(3);
		// map_3
		item_3 = MenuItemImage.item(map_no, map_pr, this, "showGameView3");
		item_3.setPosition(383.5f*rScale_width, 91*rScale_height);
		item_3.setScaleX(rScale_width); item_3.setScaleY(rScale_height);

		setMapString(4);
		// map_4
		item_4 = MenuItemImage.item(map_no, map_pr, this, "showGameView4");
		item_4.setPosition(134*rScale_width, 218*rScale_height);
		item_4.setScaleX(rScale_width); item_4.setScaleY(rScale_height);

		setMapString(5);
		// map_5
		item_5 = MenuItemImage.item(map_no, map_pr, this, "showGameView5");
		item_5.setPosition(396.5f*rScale_width, 240*rScale_height);
		item_5.setScaleX(rScale_width); item_5.setScaleY(rScale_height);

		setMapString(6);
		// map_6
		item_6 = MenuItemImage.item(map_no, map_pr, this, "showGameView6");
		item_6.setPosition(272*rScale_width, 212*rScale_height);
		item_6.setScaleX(rScale_width); item_6.setScaleY(rScale_height);

		Menu menu1 = Menu.menu(item_1, item_2, item_3, item_4, item_5, item_6);
		menu1.setPosition(0, 0);
		addChild(menu1, 0);
		
		lock_nodes[0] = new TextureNode();
		lock_nodes[0].setTexture(TextureManager.sharedTextureManager().addImage("gfx/roundselectview/lock.png"));
		lock_nodes[0].setScaleX(rScale_width); lock_nodes[0].setScaleY(rScale_height);
		lock_nodes[0].setPosition(89*rScale_width, 102.5f*rScale_height);
		
		lock_nodes[1] = new TextureNode();
		lock_nodes[1].setTexture(TextureManager.sharedTextureManager().addImage("gfx/roundselectview/lock.png"));
		lock_nodes[1].setScaleX(rScale_width); lock_nodes[1].setScaleY(rScale_height);
		lock_nodes[1].setPosition(246.5f*rScale_width, 81*rScale_height);
		
		lock_nodes[2] = new TextureNode();
		lock_nodes[2].setTexture(TextureManager.sharedTextureManager().addImage("gfx/roundselectview/lock.png"));
		lock_nodes[2].setScaleX(rScale_width); lock_nodes[2].setScaleY(rScale_height);
		lock_nodes[2].setPosition(383.5f*rScale_width, 91*rScale_height);
		
		lock_nodes[3] = new TextureNode();
		lock_nodes[3].setTexture(TextureManager.sharedTextureManager().addImage("gfx/roundselectview/lock.png"));
		lock_nodes[3].setScaleX(rScale_width); lock_nodes[3].setScaleY(rScale_height);
		lock_nodes[3].setPosition(134*rScale_width, 218*rScale_height);
		
		lock_nodes[4] = new TextureNode();
		lock_nodes[4].setTexture(TextureManager.sharedTextureManager().addImage("gfx/roundselectview/lock.png"));
		lock_nodes[4].setScaleX(rScale_width); lock_nodes[4].setScaleY(rScale_height);
		lock_nodes[4].setPosition(396.5f*rScale_width, 240*rScale_height);
		
		lock_nodes[5] = new TextureNode();
		lock_nodes[5].setTexture(TextureManager.sharedTextureManager().addImage("gfx/roundselectview/lock.png"));
		lock_nodes[5].setScaleX(rScale_width); lock_nodes[5].setScaleY(rScale_height);
		lock_nodes[5].setPosition(272*rScale_width, 212*rScale_height);
		
		
		unlock_nodes[0] = new TextureNode();
		unlock_nodes[0].setTexture(TextureManager.sharedTextureManager().addImage("gfx/roundselectview/unlock.png"));
		unlock_nodes[0].setScaleX(rScale_width); unlock_nodes[0].setScaleY(rScale_height);
		unlock_nodes[0].setPosition(89*rScale_width, 102.5f*rScale_height);
		
		unlock_nodes[1] = new TextureNode();
		unlock_nodes[1].setTexture(TextureManager.sharedTextureManager().addImage("gfx/roundselectview/unlock.png"));
		unlock_nodes[1].setScaleX(rScale_width); unlock_nodes[1].setScaleY(rScale_height);
		unlock_nodes[1].setPosition(246.5f*rScale_width, 81*rScale_height);
		
		unlock_nodes[2] = new TextureNode();
		unlock_nodes[2].setTexture(TextureManager.sharedTextureManager().addImage("gfx/roundselectview/unlock.png"));
		unlock_nodes[2].setScaleX(rScale_width); unlock_nodes[2].setScaleY(rScale_height);
		unlock_nodes[2].setPosition(383.5f*rScale_width, 91*rScale_height);
		
		unlock_nodes[3] = new TextureNode();
		unlock_nodes[3].setTexture(TextureManager.sharedTextureManager().addImage("gfx/roundselectview/unlock.png"));
		unlock_nodes[3].setScaleX(rScale_width); unlock_nodes[3].setScaleY(rScale_height);
		unlock_nodes[3].setPosition(134*rScale_width, 218*rScale_height);
		
		unlock_nodes[4] = new TextureNode();
		unlock_nodes[4].setTexture(TextureManager.sharedTextureManager().addImage("gfx/roundselectview/unlock.png"));
		unlock_nodes[4].setScaleX(rScale_width); unlock_nodes[4].setScaleY(rScale_height);
		unlock_nodes[4].setPosition(396.5f*rScale_width, 240*rScale_height);
		
		unlock_nodes[5] = new TextureNode();
		unlock_nodes[5].setTexture(TextureManager.sharedTextureManager().addImage("gfx/roundselectview/unlock.png"));
		unlock_nodes[5].setScaleX(rScale_width); unlock_nodes[5].setScaleY(rScale_height);
		unlock_nodes[5].setPosition(272*rScale_width, 212*rScale_height);
		
		mapButtons[0] = item_1; mapButtons[1] = item_2; mapButtons[2] = item_3;
		mapButtons[3] = item_4; mapButtons[4] = item_5; mapButtons[5] = item_6;
		
		/* button */
		setButtonString("BtnExit");
		item_7 = MenuItemImage.item(btn_no, btn_pr, this, "exitToMenu");
		item_7.setPosition(75.5f*rScale_width, 281.5f*rScale_height);
		item_7.setScaleX(rScale_width); item_7.setScaleY(rScale_height);
		
		setButtonString("BtnCampaignStats");
		item_8 = MenuItemImage.item(btn_no, btn_pr, this, "campaignStats");
		item_8.setPosition(384*rScale_width, 38.5f*rScale_height);
		item_8.setScaleX(rScale_width); item_8.setScaleY(rScale_height);
		
		Menu menu2 = Menu.menu(item_7, item_8);
		menu2.setPosition(0, 0);
		addChild(menu2, 0);
		
		for( int i=0; i<GameSettingInfo.finalOccupiedRound; i++ )
		{
			addChild(unlock_nodes[i], 1);
			mapButtons[i].setIsEnabled(true);
		}
		for( int i=GameSettingInfo.finalOccupiedRound + 1; i<6; i++ )
		{
			addChild(lock_nodes[i], 1);
			mapButtons[i].setIsEnabled(false);
		}
	}
	
	public void exitToMenu()
	{
    	Scene scene = Scene.node();
        scene.addChild(new MenuView(), 0);
    	Director.sharedDirector().replaceScene(new FadeTransition(1.0f, scene));
	}
	
	public void campaignStats()
	{
		Layer cam_layer = new CampaignStatsView( this );
		addChild(cam_layer, 1);

		enableButtons( false );
	}
	
	public void showGameView1()
	{
		map_idx = 0;
		showModeLayer();
	}
	
	public void showGameView2()
	{
		map_idx = 1;
		showModeLayer();
	}
	
	public void showGameView3()
	{
		map_idx = 2;
		showModeLayer();
	}
	
	public void showGameView4()
	{
		map_idx = 3;
		showModeLayer();
	}
	
	public void showGameView5()
	{
		map_idx = 4;
		showModeLayer();
	}
	
	public void showGameView6()
	{
		map_idx = 5;
		showModeLayer();
	}
	
	private void showModeLayer()
	{
		mode_layer = Layer.node();
		
		Sprite bgSprite = Sprite.sprite(bgPath + "gamemodeview.png");
		bgSprite.setScaleX(rScale_width); bgSprite.setScaleY(rScale_height);
		bgSprite.setPosition(mode_layer.getWidth()/2, mode_layer.getHeight()/2);
		mode_layer.addChild(bgSprite, -1);
		
		setButtonString("BtnNormal");
		MenuItem normal = MenuItemImage.item(btn_no, btn_pr, this, "onNormal");
		normal.setScaleX(rScale_width); normal.setScaleY(rScale_height);
		normal.setPosition(mode_layer.getWidth()/2, mode_layer.getHeight()/2);
		
		setButtonString("BtnClose");
		MenuItem close = MenuItemImage.item(btn_no, btn_pr, this, "onClose");
		close.setScaleX(rScale_width); close.setScaleY(rScale_height);
		float close_y = (normal.getPositionY() - normal.getHeight()/2 - 86*rScale_height) / 2;
		close.setPosition(mode_layer.getWidth()/2, 86*rScale_height + close_y);

		setButtonString("BtnEasy");
		MenuItem easy = MenuItemImage.item(btn_no, btn_pr, this, "onEasy");
		easy.setScaleX(rScale_width); easy.setScaleY(rScale_height);
		float easy_x = (normal.getPositionX() - normal.getWidth()/2 - 78*rScale_width) / 2;
		easy.setPosition(78*rScale_width + easy_x, mode_layer.getHeight()/2);
		
		setButtonString("BtnHard");
		MenuItem hard = MenuItemImage.item(btn_no, btn_pr, this, "onHard");
		hard.setScaleX(rScale_width); hard.setScaleY(rScale_height);
		float hard_x = (402*rScale_width - normal.getPositionX() - normal.getWidth()/2) / 2;
		hard.setPosition(normal.getPositionX() + normal.getWidth()/2 + hard_x, mode_layer.getHeight()/2);
		
		Menu menu = Menu.menu(normal, close, easy, hard);
		menu.setPosition(0, 0);
		mode_layer.addChild(menu);
		
		addChild(mode_layer, 1);
		
		enableButtons( false );
	}
	
	public void onEasy()
	{
    	Scene scene = Scene.node();
        scene.addChild(new GameView(map_idx, 1), 0);
    	Director.sharedDirector().replaceScene(new FadeTransition(1.0f, scene));
	}
	
	public void onNormal()
	{
    	Scene scene = Scene.node();
        scene.addChild(new GameView(map_idx, 2), 0);
    	Director.sharedDirector().replaceScene(new FadeTransition(1.0f, scene));
	}
	
	public void onHard()
	{
    	Scene scene = Scene.node();
        scene.addChild(new GameView(map_idx, 3), 0);
    	Director.sharedDirector().replaceScene(new FadeTransition(1.0f, scene));
	}
	
	public void onClose()
	{
		removeChild(mode_layer, true);
		enableButtons( true );
	}
	
	public void onScoreClose()
	{
		removeChild(score_layer, true);
		enableButtons( true );
	}
	
	public void enableButtons( boolean state )
	{
		item_1.setIsEnabled(state);
		item_2.setIsEnabled(state);
		item_3.setIsEnabled(state);
		item_4.setIsEnabled(state);
		item_5.setIsEnabled(state);
		item_6.setIsEnabled(state);
		item_7.setIsEnabled(state);
		item_8.setIsEnabled(state);
	}
	
	public void draw( GL10 gl )
	{
		
	}

}
