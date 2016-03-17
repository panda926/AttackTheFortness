package org.AttackTheFortress.views;

import javax.microedition.khronos.opengles.GL10;

import org.AttackTheFortress.DataManager;
import org.AttackTheFortress.Global;
import org.cocos2d.layers.Layer;
import org.cocos2d.nodes.Director;
import org.cocos2d.nodes.Scene;
import org.cocos2d.nodes.Sprite;
import org.cocos2d.transitions.FadeTransition;

public class LogoView extends Layer {

	private String rcPath;
	private float rScale_width = 0.0f;
	private float rScale_height = 0.0f;
	private int tick_counter = 0;
	
	public LogoView()
	{
		Global.init();
		
		rcPath = "gfx/logo/";
		rScale_width = Global.g_rScale_x;
		rScale_height = Global.g_rScale_y;
		
		Sprite sprite = Sprite.sprite(rcPath + "logo.png");
		sprite.setScaleX(rScale_width); sprite.setScaleY(rScale_height);
		sprite.setPosition(getWidth()/2, getHeight()/2);
		
		addChild(sprite);
		
		// ?? townInfo.xml load
		DataManager manager = DataManager.shared();
		manager.init();
		manager.loadTower();
	}
	
	public void draw(GL10 gl)
	{
		tick_counter ++;
		
		if( tick_counter > 50 )
			showMenuView();
	}
	
	private void showMenuView()
	{
    	Scene scene = Scene.node();
        scene.addChild(new MenuView(), 0);
    	Director.sharedDirector().replaceScene(new FadeTransition(1.0f, scene));
	}
}
