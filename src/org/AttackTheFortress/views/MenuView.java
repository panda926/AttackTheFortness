package org.AttackTheFortress.views;

import org.AttackTheFortress.DataManager;
import org.AttackTheFortress.Global;
import org.cocos2d.layers.Layer;
import org.cocos2d.menus.Menu;
import org.cocos2d.menus.MenuItem;
import org.cocos2d.menus.MenuItemImage;
import org.cocos2d.nodes.Director;
import org.cocos2d.nodes.Scene;
import org.cocos2d.nodes.Sprite;
import org.cocos2d.transitions.FadeTransition;

public class MenuView extends Layer {

	private String bgPath, btnPath;
	private float rScale_width = 0.0f;
	private float rScale_height = 0.0f;
	
	public MenuView() {
		// TODO Auto-generated constructor stub
		bgPath = "gfx/menuview/";
		btnPath = "gfx/buttons/";

		rScale_width = Global.g_rScale_x;
		rScale_height = Global.g_rScale_y;
		
		DataManager.shared().playMusic(0);
		
		loadResource();
		
	}
	
	private void loadResource()
	{
		// background
		Sprite bgSprite = Sprite.sprite(bgPath + "menuviewback.png");
		bgSprite.setScaleX(rScale_width); bgSprite.setScaleY(rScale_height);
		bgSprite.setPosition(getWidth()/2, getHeight()/2);
		addChild(bgSprite, -1);
		
		// play button
        MenuItem item = MenuItemImage.item(btnPath + "BtnPlay1.png", btnPath + "BtnPlay2.png", this, "showMapView");
        Menu menu = Menu.menu(item);
        menu.setPosition(0, 0);
        item.setScaleX(rScale_width); item.setScaleY(rScale_height);
        item.setPosition(235*rScale_width, 97*rScale_height);
        addChild(menu);
	}

	public void showMapView()
	{
    	Scene scene = Scene.node();
        scene.addChild(new MapView(), 0);
    	Director.sharedDirector().replaceScene(new FadeTransition(1.0f, scene));
	}
}
