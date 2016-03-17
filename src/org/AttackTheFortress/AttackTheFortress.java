package org.AttackTheFortress;

import org.AttackTheFortress.views.LogoView;
import org.cocos2d.actions.ActionManager;
import org.cocos2d.nodes.Director;
import org.cocos2d.nodes.Scene;
import org.cocos2d.nodes.TextureManager;
import org.cocos2d.opengl.CCGLSurfaceView;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class AttackTheFortress extends Activity {
	private boolean m_bBackButton = false;
    private CCGLSurfaceView mGLSurfaceView;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        m_bBackButton = false;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mGLSurfaceView = new CCGLSurfaceView(this);
        setContentView(mGLSurfaceView);
        
        Common.round1_id = R.xml.round1;
        Common.round2_id = R.xml.round2;
        Common.round3_id = R.xml.round3;
        Common.round4_id = R.xml.round4;
        Common.round5_id = R.xml.round5;
        Common.round6_id = R.xml.round6;
        Common.towerinfo_id = R.xml.towerinfo;
        
        loadGameSetting();
    }

    @Override
    public void onStart() {
        super.onStart();
        
        DataManager.shared().onLoadMusic();
        
        // attach the OpenGL view to a window
        Director.sharedDirector().attachInView(mGLSurfaceView);      
        Scene scene = Scene.node();
        scene.addChild(new LogoView());
        // Make the Scene active
        Director.sharedDirector().runWithScene(scene);
    }

    @Override
    public void onPause() {
        super.onPause();

//        DataManager.shared().pauseMusic(0);
//        DataManager.shared().pauseMusic(1);
        Director.sharedDirector().pause();
        if(this.m_bBackButton == false)
        {
            DataManager.shared().stopMusic(0);
            DataManager.shared().stopMusic(1);
            super.onDestroy();
        }
        ActionManager.sharedManager().removeAllActions();
        TextureManager.sharedTextureManager().removeAllTextures();
    }

    @Override
    public void onResume() {
        super.onResume();

        Director.sharedDirector().resume();
    }

    @Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		m_bBackButton = true;
	}
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        DataManager.shared().stopMusic(0);
        DataManager.shared().stopMusic(1);
        ActionManager.sharedManager().removeAllActions();
        TextureManager.sharedTextureManager().removeAllTextures();
    }
    
    private void loadGameSetting()
    {
    	DataManager.shared();
    	GameSettingInfo._shard();
    	
    	SharedPreferences sp_read = this.getSharedPreferences("atf_score", 0);
//    	SharedPreferences sp_write = this.getSharedPreferences("atf_score", 0);
    	DataManager.shared().loadSetting( sp_read/*, sp_write*/ );
    }
    
}