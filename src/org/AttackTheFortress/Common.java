package org.AttackTheFortress;

public class Common {

	public static final int SOUND_TOWER_EXPLODE = 50;
	public static final int SOUND_ENEMY_LAUNCH = 51;
	public static final int SOUND_TOWER_LAUNCH = 52;
	
	public static boolean sound_state = true;
	
	public static int round1_id;
	public static int round2_id;
	public static int round3_id;
	public static int round4_id;
	public static int round5_id;
	public static int round6_id;
	public static int towerinfo_id;
	
    private static Common _sharedCommon = null;
	public static Common sharedCommon()
	{
		if( _sharedCommon == null )
		{
			_sharedCommon = new Common();
		}
		return _sharedCommon;
	}
	
	//public class Entity_State{
		public static final int ENTITY_DESTROIED = 0;
		public static final int ENTITY_IN_DESTROY = 1;
		public static final int ENTITY_CREATED = 2;
		public static final int ENTITY_IN_DRAGGING = 3;
		public static final int ENTITY_LIVE = 4;
		public static final int ENTITY_SELECTED = 5;
	//}
	
	//public class Game_State{
		public static final int GAME_INITIAL = 0;
		public static final int GAME_LOADED = 1;
		public static final int GAME_LEVEL_START = 2;
		public static final int GAME_LEVEL_END = 3;
		public static final int GAME_LEVEL_LOADED = 4;
		public static final int GAME_ROUND_FAILED = 5;
	//}
	
}
