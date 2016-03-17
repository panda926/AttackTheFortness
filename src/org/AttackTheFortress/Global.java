package org.AttackTheFortress;

import org.cocos2d.nodes.Director;
import org.cocos2d.types.CCSize;

public class Global {

	public static float g_rScale_x = 0.0f;
	public static float g_rScale_y = 0.0f;
	
	private static final float DEFAULT_WIDTH = 480.0f;
	private static final float DEFAULT_HEIGHT = 320.0f;
	
	public static void init()
	{
		CCSize winSize = Director.sharedDirector().winSize();
		
		g_rScale_x = winSize.width / DEFAULT_WIDTH;
		g_rScale_y = winSize.height / DEFAULT_HEIGHT;
	}
}
