package org.AttackTheFortress;


public class GameSettingInfo {

	public static int finalOccupiedRound;		// final unlocked round
	public static int[][] score4Rounds = {{0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}};		// score per round(1~6)
	public static int highScore;
	
	private static GameSettingInfo _gmSetInfo = null;
	
	public static GameSettingInfo _shard(){
		if( _gmSetInfo == null )
		{
			_gmSetInfo = new GameSettingInfo();

			finalOccupiedRound = 0;
			highScore = 0;
		}
		return _gmSetInfo;
	}
}
