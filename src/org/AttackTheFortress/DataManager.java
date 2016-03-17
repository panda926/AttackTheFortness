package org.AttackTheFortress;

import java.util.ArrayList;

import org.cocos2d.nodes.Director;
import org.cocos2d.nodes.Sprite;
import org.cocos2d.utils.CCFormatter;
import org.xmlpull.v1.XmlPullParser;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;
import android.util.Log;

public class DataManager {
	
	private static DataManager 	_shared = null;
	private TowerInfo[] 		m_towerInfos;
	public ArrayList<Sprite> 	aryBlow;
	private XmlPullParser 		parser;
	private int 				m_curTowerIndx;
	private int					m_upgradeItemIndx;		//0:damage, 1:speed, 2:range
	private int					m_subIndx;
	
	public final int			SOUND_ENEMY_LAUNCH = 7;
	public final int			SOUND_TOWER_LAUNCH = 8;
	public final int 			SOUND_EXPLODE = 9;
	
	public static final String final_round = "finalround";			
	public static final String score_round[][] = {{"adt_0_0", "adt_0_1"}, {"adt_1_0", "adt_1_1"}, {"adt_2_0", "adt_2_1"}, 
												  {"adt_3_0", "adt_3_1"}, {"adt_4_0", "adt_4_1"}, {"adt_5_0", "adt_5_1"}};			
	public static final String high_score = "highscore";			
	
	public MediaPlayer m_mainMusic;
	public MediaPlayer m_backMusic;
	public MediaPlayer[] m_crySound = {null, null, null, null, null, null, null};
	public MediaPlayer m_explodeSound;
	public MediaPlayer m_enemySound;
	public MediaPlayer m_towerSound;
	
	private SharedPreferences m_sp;
    private static final String LOG_TAG = DataManager.class.getSimpleName();

	public static DataManager shared()
	{
		if( _shared == null )
		{
			_shared = new DataManager();
		}
		return _shared;
	}
	
	private DataManager()
	{
		init();
	}
	
	public void init()
	{
		m_towerInfos = new TowerInfo[6];
		for( int i=0; i<6; i++ )
			m_towerInfos[i] = new TowerInfo();
		
		loadSetting();
		
		aryBlow = new ArrayList<Sprite>();
		for( int i=0; i<22; i++ )
			aryBlow.add(Sprite.sprite(new CCFormatter().format("gfx/Blow/blow%d.png", i+1)));
	}
	
	public TowerInfo getTowerInforsAt( int i )
	{
		return m_towerInfos[i];
	}
	
	void loadSetting()
	{
	}
	
    public void onLoadMusic()
    {
    	m_mainMusic = MediaPlayer.create(Director.sharedDirector().getActivity().getApplicationContext(), R.raw.main);
    	m_backMusic = MediaPlayer.create(Director.sharedDirector().getActivity().getApplicationContext(), R.raw.back);
    	m_crySound[0] = MediaPlayer.create(Director.sharedDirector().getActivity().getApplicationContext(), R.raw.cry1);
    	m_crySound[1] = MediaPlayer.create(Director.sharedDirector().getActivity().getApplicationContext(), R.raw.cry2);
    	m_crySound[2] = MediaPlayer.create(Director.sharedDirector().getActivity().getApplicationContext(), R.raw.cry3);
    	m_crySound[3] = MediaPlayer.create(Director.sharedDirector().getActivity().getApplicationContext(), R.raw.cry4);
    	m_crySound[4] = MediaPlayer.create(Director.sharedDirector().getActivity().getApplicationContext(), R.raw.cry5);
    	m_crySound[5] = MediaPlayer.create(Director.sharedDirector().getActivity().getApplicationContext(), R.raw.cry6);
    	m_crySound[6] = MediaPlayer.create(Director.sharedDirector().getActivity().getApplicationContext(), R.raw.cry7);
    	m_explodeSound = MediaPlayer.create(Director.sharedDirector().getActivity().getApplicationContext(), R.raw.explode);
    	m_enemySound = MediaPlayer.create(Director.sharedDirector().getActivity().getApplicationContext(), R.raw.enemy_launch);
    	m_towerSound = MediaPlayer.create(Director.sharedDirector().getActivity().getApplicationContext(), R.raw.tower_launch);
    }


    public void playMusic( int idx )
	{
    	if( !Common.sound_state )
    		return ;
    	if( idx == 0 )
    	{
    		m_mainMusic.start();
    		m_mainMusic.setLooping(true);
    	}
    	else
    	{
    		m_backMusic.start();
    		m_backMusic.setLooping(true);
    	}
	}
    
    public void pauseMusic( int idx )
    {
    	if( idx == 0 )
    		m_mainMusic.pause();
    	else
    		m_backMusic.pause();
    }
    
    public void stopMusic( int idx )
    {
    	if( idx == 0 )
    	{
			m_mainMusic.stop();
    	}
    	else
    	{
			m_backMusic.stop();
    	}
    }
    
    public void playSound( int idx )
    {
    	if( !Common.sound_state )
    		return ;
    	if( idx == SOUND_ENEMY_LAUNCH )
    		m_enemySound.start();
    	else if( idx == SOUND_TOWER_LAUNCH )
    		m_towerSound.start();
    	else if( idx == SOUND_EXPLODE )
    		m_explodeSound.start();
    	else
    		m_crySound[idx-1].start();
    }
    
    public void stopSound( int idx )
    {
    }
	
	public void loadTower()
	{
		m_curTowerIndx = 0;
		
		Activity cur = Director.sharedDirector().getActivity();
		parser = cur.getResources().getXml(Common.towerinfo_id);
		
		try{
			while( parser.next() != XmlPullParser.END_DOCUMENT )
			{
				String name = parser.getName();
				if( name == null )
					continue;
				if( name.equals("Tower") && parser.getAttributeCount() != -1 )
				{
					String str = parser.getAttributeValue(0);
					m_curTowerIndx = Integer.valueOf(str).intValue();
				}
				else if( name.equals("Property") && parser.getAttributeCount() != -1 )
				{
					m_towerInfos[m_curTowerIndx-1].max_life = Integer.valueOf(parser.getAttributeValue(0)).intValue();
					m_towerInfos[m_curTowerIndx-1].speed = Integer.valueOf(parser.getAttributeValue(1)).intValue();
					m_towerInfos[m_curTowerIndx-1].damage = Integer.valueOf(parser.getAttributeValue(2)).intValue();
					float temp = Integer.valueOf(parser.getAttributeValue(3)).intValue() * 1.28f;
					m_towerInfos[m_curTowerIndx-1].range = (int) temp;
					m_towerInfos[m_curTowerIndx-1].cost = Integer.valueOf(parser.getAttributeValue(4)).intValue();
				}
				else if( name.equals("speed_upgrade") && parser.getAttributeCount() != -1 )
				{
					if (m_upgradeItemIndx != 1) {
						m_upgradeItemIndx = 1;
						m_subIndx = 0;
					}
					m_towerInfos[m_curTowerIndx-1].upgrade_speed[m_subIndx][0] = Integer.valueOf(parser.getAttributeValue(0)).intValue();
					m_towerInfos[m_curTowerIndx-1].upgrade_speed[m_subIndx][1] = Integer.valueOf(parser.getAttributeValue(1)).intValue();
					m_subIndx ++;
				}
				else if( name.equals("damage_upgrade") && parser.getAttributeCount() != -1 )
				{
					if (m_upgradeItemIndx != 0) {
						m_upgradeItemIndx = 0;
						m_subIndx = 0;
					}
					m_towerInfos[m_curTowerIndx-1].upgrade_damage[m_subIndx][0] = Integer.valueOf(parser.getAttributeValue(0)).intValue();
					m_towerInfos[m_curTowerIndx-1].upgrade_damage[m_subIndx][1] = Integer.valueOf(parser.getAttributeValue(1)).intValue();
					m_subIndx ++;
				}
				else if( name.equals("range_upgrade") && parser.getAttributeCount() != -1 )
				{
					if (m_upgradeItemIndx != 2) {
						m_upgradeItemIndx = 2;
						m_subIndx = 0;
					}
					float temp = Integer.valueOf(parser.getAttributeValue(0)).intValue() * 1.28f;
					m_towerInfos[m_curTowerIndx-1].upgrade_range[m_subIndx][0] = (int) temp;
					m_towerInfos[m_curTowerIndx-1].upgrade_range[m_subIndx][1] = Integer.valueOf(parser.getAttributeValue(1)).intValue();
					m_subIndx ++;
				}
			}
		}catch (Exception e){
			Log.e("ReadXMLResourceFile", e.getMessage(), e);
		}
	}

	private void validateSetting()
	{
		GameSettingInfo._shard();
		if (GameSettingInfo.finalOccupiedRound < 0 || GameSettingInfo.finalOccupiedRound > 6)
			GameSettingInfo.finalOccupiedRound = 0;
		if (GameSettingInfo.highScore < 0)
			GameSettingInfo.highScore = 0;
		for (int i = 0; i < 6; i++) {
			if (GameSettingInfo.score4Rounds[i][0] < 0 || GameSettingInfo.score4Rounds[i][0] > 3)
				GameSettingInfo.score4Rounds[i][0] = 0;
			if (GameSettingInfo.score4Rounds[i][1] < 0)
				GameSettingInfo.score4Rounds[i][1] = 0;
		}
	}
	
	public void loadSetting( SharedPreferences sp_read/*, SharedPreferences sp_write*/ )
	{
//		m_sp = sp_write;
		m_sp = sp_read;
    	GameSettingInfo.finalOccupiedRound = sp_read.getInt(DataManager.final_round, 0);
    	Log.i(LOG_TAG, new CCFormatter().format("%d", GameSettingInfo.finalOccupiedRound));
    	for( int i=0; i<6; i++ )
    	{
    	   	GameSettingInfo.score4Rounds[i][0] = sp_read.getInt(DataManager.score_round[i][0], 0);
        	Log.i(LOG_TAG, new CCFormatter().format("%d", GameSettingInfo.score4Rounds[i][0]));
    	   	GameSettingInfo.score4Rounds[i][1] = sp_read.getInt(DataManager.score_round[i][1], 0);
        	Log.i(LOG_TAG, new CCFormatter().format("%d", GameSettingInfo.score4Rounds[i][1]));
    	}
     	GameSettingInfo.highScore = sp_read.getInt(DataManager.high_score, 0);
    	Log.i(LOG_TAG, new CCFormatter().format("%d", GameSettingInfo.highScore));
    	
    	validateSetting();
	}
	
	public void saveSetting()
	{
    	validateSetting();
        Editor ed = m_sp.edit();
    	Log.i(LOG_TAG, new CCFormatter().format("%d", GameSettingInfo.finalOccupiedRound));
        ed.putInt(DataManager.final_round, GameSettingInfo.finalOccupiedRound);
    	for( int i=0; i<6; i++ )
    	{
        	Log.i(LOG_TAG, new CCFormatter().format("%d", GameSettingInfo.score4Rounds[i][0]));
        	Log.i(LOG_TAG, new CCFormatter().format("%d", GameSettingInfo.score4Rounds[i][1]));
    		ed.putInt(DataManager.score_round[i][0], GameSettingInfo.score4Rounds[i][0]);
    		ed.putInt(DataManager.score_round[i][1], GameSettingInfo.score4Rounds[i][1]);
    	}
    	Log.i(LOG_TAG, new CCFormatter().format("%d", GameSettingInfo.highScore));
        ed.putInt(DataManager.high_score, GameSettingInfo.highScore);
        ed.commit();
	}
}
