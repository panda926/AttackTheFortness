package org.AttackTheFortress.views;

import org.AttackTheFortress.Global;
import org.cocos2d.layers.Layer;
import org.cocos2d.menus.Menu;
import org.cocos2d.menus.MenuItem;
import org.cocos2d.menus.MenuItemImage;
import org.cocos2d.nodes.Label;
import org.cocos2d.nodes.Label.TextAlignment;
import org.cocos2d.nodes.Sprite;
import org.cocos2d.types.CCColor3B;
import org.cocos2d.types.CCRect;

public class NoticeView extends Layer {

	private float rScale_width = 0.0f;
	private float rScale_height = 0.0f;
	private float offset_x, offset_y;
	
	private Label title = null;
	private Label content = null;
	private Label info = null;
	private Sprite bg;
	
	public NoticeView() {
		// TODO Auto-generated constructor stub
		rScale_width = Global.g_rScale_x;
		rScale_height = Global.g_rScale_y;

		bg = Sprite.sprite("gfx/Gameview/noticeview.png");
		bg.setScaleX(rScale_width); bg.setScaleY(rScale_height);
		bg.setPosition(getWidth()/2, 5*rScale_height+bg.getHeight()*rScale_height/2);
		addChild(bg, -1);

		MenuItem close = MenuItemImage.item("gfx/buttons/close.png", "gfx/buttons/close.png", this, "noticeClose");
		close.setScaleX(rScale_width); close.setScaleY(rScale_height);
		//float y = 5*rScale_height + (bg.getHeight()-15)*rScale_height;
		float x = CCRect.maxX(bg.getBoundingBox())-CCRect.width(bg.getBoundingBox())/32;
		float y = CCRect.maxY(bg.getBoundingBox())-CCRect.height(bg.getBoundingBox())/6;
		close.setPosition(x, y);

		offset_x = getWidth()/2-bg.getWidth()/2; offset_y = 5*rScale_height;
		Menu menu = Menu.menu(close);
		menu.setPosition(0, 0);
		addChild(menu, 0);
	}
	
	public void addTitle( String string )
	{
		title = Label.node(string, 200*rScale_width, 12*rScale_height, TextAlignment.LEFT, "DroidSans", 10);
		title.setScaleX(rScale_width); title.setScaleY(rScale_height);
		title.setPosition(offset_x + 86*rScale_width + 80*rScale_width, offset_y + (bg.getHeight()-18)*rScale_height);
        addChild(title, 0);
	}
	
	public void addContent( String string )
	{
		content = Label.node(string, 200*rScale_width, 12*rScale_height, TextAlignment.LEFT, "DroidSans", 10);
		content.setColor(new CCColor3B(50, 50, 50));
		content.setScaleX(rScale_width); content.setScaleY(rScale_height);
		content.setPosition(offset_x + 86*rScale_width + 80*rScale_width, offset_y + (bg.getHeight()-33)*rScale_height);
//			content.setPosition(offset_x + 114*rScale_width, offset_y + (bg.getHeight()-32)*rScale_height);
        addChild(content, 0);
	}
	
	public void addInfo( String string )
	{
		info = Label.node(string, 200*rScale_width, 12*rScale_height, TextAlignment.LEFT, "DroidSans", 10);
        info.setColor(new CCColor3B(50, 50, 50));
		info.setScaleX(rScale_width); info.setScaleY(rScale_height);
		info.setPosition(offset_x + 86*rScale_width + 80*rScale_width, offset_y + (bg.getHeight()-43)*rScale_height);
//			info.setPosition(offset_x + 98*rScale_width, offset_y + (bg.getHeight()-43)*rScale_height);
        addChild(info, 0);
	}

	public void noticeClose()
	{
		onExit();
	}
	
	@Override
	public void onExit()
	{
		this.removeAllChildren(true);
		super.onExit();
	}
}
