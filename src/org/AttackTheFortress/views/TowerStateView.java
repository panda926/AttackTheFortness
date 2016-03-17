package org.AttackTheFortress.views;

import org.AttackTheFortress.DataManager;
import org.AttackTheFortress.Global;
import org.AttackTheFortress.TowerInfo;
import org.AttackTheFortress.entity.Tower;
import org.cocos2d.events.TouchDispatcher;
import org.cocos2d.layers.Layer;
import org.cocos2d.menus.Menu;
import org.cocos2d.menus.MenuItemLabel;
import org.cocos2d.nodes.CocosNode;
import org.cocos2d.nodes.Director;
import org.cocos2d.nodes.Label;
import org.cocos2d.nodes.Sprite;
import org.cocos2d.types.CCColor3B;
import org.cocos2d.types.CCPoint;
import org.cocos2d.types.CCRect;
import org.cocos2d.utils.CCFormatter;

import android.view.MotionEvent;

public class TowerStateView extends Layer {
	
	private float rScale_width = 0.0f;
	private float rScale_height = 0.0f;
	
	private Tower m_tower;
	private TowerInfo m_towerInfo;
	private Sprite health, range, speed, damage, sell;
	private MenuItemLabel m_btnHealth, m_btnRange, m_btnSpeed, m_btnDamage, m_btnSell;
	private Label lblHealthValue, lblRangeValue, lblSpeedValue, lblDamageValue;
	
	private static final int tag_bk = 1;
	
	private GameView m_view;

	public TowerStateView( Tower tower, CCPoint pt, GameView view ) {
		// TODO Auto-generated constructor stub
		setIsTouchEnabled(true);
		
		rScale_width = Global.g_rScale_x;
		rScale_height = Global.g_rScale_y;
		
		m_view = view;
		
		Sprite bk = Sprite.sprite("gfx/Towerstatview/towerstatview.png");
		bk.setScaleX(rScale_width); bk.setScaleY(rScale_height);

		float x, y, old_x, old_y;
		old_x = x = pt.x; old_y = y = pt.y;
		x += bk.getWidth()/2*rScale_width; y -= bk.getHeight()/2*rScale_height;
		if( old_x+bk.getWidth() > getWidth() )
		{
			x = old_x - bk.getWidth()*rScale_width/2;
			old_x -= bk.getWidth()*rScale_width;
		}
		if( old_y-bk.getHeight() < 0 )
		{
			y = old_y + bk.getHeight()*rScale_height/2.0f;
			old_y += bk.getHeight()*rScale_height;
		}
		bk.setPosition(x, y);
		addChild(bk, -1, tag_bk);
		
		//initRect( rect );
		m_tower = tower;
		DataManager dataManager = DataManager.shared();
		m_towerInfo = dataManager.getTowerInforsAt(m_tower.type - 1);
		
		int idx = 0;
		health = Sprite.sprite("gfx/Towerstatview/btnUpgrade.png");
		health.setScaleX(rScale_width); health.setScale(rScale_height);
		health.setPosition(old_x+73*rScale_width, old_y-46*rScale_height-idx*20*rScale_height);
//		health.setContentSize(93*scaleFactor, 32*scaleFactor);
//		health.setPosition(110*scaleFactor+health.getWidth()/2, 245*scaleFactor-health.getHeight()/2);
		addChild(health, 0); idx ++;
		
		damage = Sprite.sprite("gfx/Towerstatview/btnUpgrade.png");
		damage.setScaleX(rScale_width); damage.setScale(rScale_height);
		damage.setPosition(old_x+73*rScale_width, old_y-46*rScale_height-idx*20*rScale_height);
//		damage.setContentSize(93*scaleFactor, 32*scaleFactor);
//		damage.setPosition(110*scaleFactor+damage.getWidth()/2, 200*scaleFactor-damage.getHeight()/2);
		addChild(damage, 0); idx ++;
			
		speed = Sprite.sprite("gfx/Towerstatview/btnUpgrade.png");
		speed.setScaleX(rScale_width); speed.setScale(rScale_height);
		speed.setPosition(old_x+73*rScale_width, old_y-46*rScale_height-idx*20*rScale_height);
//		speed.setContentSize(93*scaleFactor, 32*scaleFactor);
//		speed.setPosition(110*scaleFactor+speed.getWidth()/2, 157*scaleFactor-speed.getHeight()/2);
		addChild(speed, 0); idx ++;
			
		range = Sprite.sprite("gfx/Towerstatview/btnUpgrade.png");
		range.setScaleX(rScale_width); range.setScale(rScale_height);
		range.setPosition(old_x+73*rScale_width, old_y-46*rScale_height-idx*20*rScale_height);
//		range.setContentSize(93*scaleFactor, 32*scaleFactor);
//		range.setPosition(110*scaleFactor+range.getWidth()/2, 115*scaleFactor-range.getHeight()/2);
		addChild(range, 0);
			
		sell = Sprite.sprite("gfx/Towerstatview/btnSell.png");
		sell.setScaleX(rScale_width); sell.setScale(rScale_height);
		sell.setPosition(old_x+58*rScale_width, range.getPositionY()-26*rScale_height);
//		sell.setContentSize(165*scaleFactor, 32*scaleFactor);
//		sell.setPosition(37*scaleFactor+sell.getWidth()/2, 75*scaleFactor-sell.getHeight()/2);
		addChild(sell, 0);
		
		Label lblHealth, lblDamage, lblSpeed, lblRange, lblSell;
		String str = new CCFormatter().format("+%d   %dG", m_towerInfo.max_life/10, m_towerInfo.cost/10);
		lblHealth = Label.label(str, "DroidSans", 8);
		m_btnHealth = MenuItemLabel.item(lblHealth, this, "onHealth");
		m_btnHealth.setScaleX(rScale_width); m_btnHealth.setScaleY(rScale_height);
		m_btnHealth.setPosition(health.getPositionX(), health.getPositionY());
		
		str = new CCFormatter().format("+%d   %dG", m_towerInfo.upgrade_damage[m_tower.upDamageLevel][0], m_towerInfo.upgrade_damage[m_tower.upDamageLevel][1]);
		lblDamage = Label.label(str, "DroidSans", 8);
		m_btnDamage = MenuItemLabel.item(lblDamage, this, "onDamage");
		m_btnDamage.setScaleX(rScale_width); m_btnDamage.setScaleY(rScale_height);
		m_btnDamage.setPosition(m_btnHealth.getPositionX(), damage.getPositionY());
		
		str = new CCFormatter().format("+%d   %dG", m_towerInfo.upgrade_speed[m_tower.upSpeedLevel][0], m_towerInfo.upgrade_speed[m_tower.upSpeedLevel][1]);
		lblSpeed = Label.label(str, "DroidSans", 8);
		m_btnSpeed = MenuItemLabel.item(lblSpeed, this, "onSpeed");
		m_btnSpeed.setScaleX(rScale_width); m_btnSpeed.setScaleY(rScale_height);
		m_btnSpeed.setPosition(m_btnHealth.getPositionX(), speed.getPositionY());
		
		str = new CCFormatter().format("+%d   %dG", m_towerInfo.upgrade_range[m_tower.upRangeLevel][0], m_towerInfo.upgrade_range[m_tower.upRangeLevel][1]);
		lblRange = Label.label(str, "DroidSans", 8);
		m_btnRange = MenuItemLabel.item(lblRange, this, "onRange");
		m_btnRange.setScaleX(rScale_width); m_btnRange.setScaleY(rScale_height);
		m_btnRange.setPosition(m_btnHealth.getPositionX(), range.getPositionY());
		
		float money = (float)(m_tower.cost * 0.75 * m_tower.health / m_tower.maxLife);
		str = new CCFormatter().format("sell 75%s for %dG", "%", (int)money);
		lblSell = Label.label(str, "DroidSans", 8);
		m_btnSell = MenuItemLabel.item(lblSell, this, "onSell");
		m_btnSell.setScaleX(rScale_width); m_btnSell.setScaleY(rScale_height);
		m_btnSell.setPosition(sell.getPositionX(), sell.getPositionY());
		
		Menu menu = Menu.menu(m_btnHealth, m_btnDamage, m_btnSpeed, m_btnRange, m_btnSell);
		menu.setPosition(0, 0);
		addChild(menu, 1);
		
		String label = String.format("%d", m_tower.health);
		lblHealthValue = Label.label(label, "DroidSans", 8);
		lblHealthValue.setScaleX(rScale_width); lblHealthValue.setScaleY(rScale_height);
		lblHealthValue.setPosition(CCRect.minX(health.getBoundingBox())-CCRect.width(lblHealthValue.getBoundingBox())/2, health.getPositionY());
		//lblHealthValue.setPosition(health.getPositionX()-health.getWidth()/2-lblHealthValue.getWidth()/2*rScale_width, health.getPositionY());
		lblHealthValue.setColor(new CCColor3B(50, 50, 50));
		addChild(lblHealthValue, 0);

		label = String.format("%d", m_tower.damage);
		lblDamageValue = Label.label(label, "DroidSans", 8);
		lblDamageValue.setScaleX(rScale_width); lblDamageValue.setScaleY(rScale_height);
		lblDamageValue.setPosition(lblHealthValue.getPositionX(), damage.getPositionY());
		lblDamageValue.setColor(new CCColor3B(50, 50, 50));
		addChild(lblDamageValue, 0);

		label = String.format("%d", m_tower.speed);
		lblSpeedValue = Label.label(label, "DroidSans", 8);
		lblSpeedValue.setScaleX(rScale_width); lblSpeedValue.setScaleY(rScale_height);
		lblSpeedValue.setPosition(lblHealthValue.getPositionX(), speed.getPositionY());
		lblSpeedValue.setColor(new CCColor3B(50, 50, 50));
		addChild(lblSpeedValue, 0);

		label = String.format("%d", m_tower.range);
		lblRangeValue = Label.label(label, "DroidSans", 8);
		lblRangeValue.setScaleX(rScale_width); lblRangeValue.setScaleY(rScale_height);
		lblRangeValue.setPosition(lblHealthValue.getPositionX(), range.getPositionY());
		lblRangeValue.setColor(new CCColor3B(50, 50, 50));
		addChild(lblRangeValue, 0);
	}
	
	public void setString( String string )
	{
		
	}
	
	public void setTower( Tower tower )
	{
		m_tower = tower;
		DataManager dataManager = DataManager.shared();
		m_towerInfo = dataManager.getTowerInforsAt(m_tower.type-1);
		updateTowerCostWithPlus(0);
		updateAllVariables();
	}

	private void updateAllVariables() {
		// TODO Auto-generated method stub
		m_btnHealth.setString(new CCFormatter().format("+%d  %dG", m_towerInfo.max_life/10, m_towerInfo.cost/10));

		int index;
		index = (m_tower.upDamageLevel > 3) ? 3 : m_tower.upDamageLevel;
		m_btnDamage.setString(new CCFormatter().format("+%d  %dG", m_towerInfo.upgrade_damage[index][0], m_towerInfo.upgrade_damage[index][1]));

		index = (m_tower.upSpeedLevel > 3) ? 3 : m_tower.upSpeedLevel;
		m_btnSpeed.setString(new CCFormatter().format("+%d  %dG", m_towerInfo.upgrade_speed[index][0], m_towerInfo.upgrade_speed[index][1]));

		index = (m_tower.upRangeLevel > 3) ? 3 : m_tower.upRangeLevel;
		m_btnRange.setString(new CCFormatter().format("+%d  %dG", m_towerInfo.upgrade_speed[index][0], m_towerInfo.upgrade_range[index][1]));

		lblHealthValue.setString(new CCFormatter().format("%d", m_tower.health));
		lblDamageValue.setString(new CCFormatter().format("%d", m_tower.damage));
		lblSpeedValue.setString(new CCFormatter().format("%d", m_tower.speed));
		lblRangeValue.setString(new CCFormatter().format("%d", m_tower.range));
		
		if (m_tower.upRangeLevel > 3) {
			m_btnRange.setIsEnabled(false);
		} else {
			m_btnRange.setIsEnabled(true);
		}
		if (m_tower.upDamageLevel > 3) {
			m_btnDamage.setIsEnabled(false);
		} else {
			m_btnDamage.setIsEnabled(true);
		}
		if (m_tower.upSpeedLevel > 3) {
			m_btnSpeed.setIsEnabled(false);
		} else {
			m_btnSpeed.setIsEnabled(true);
		}
		
		if (m_tower.health >= m_towerInfo.max_life) {
			m_btnHealth.setIsEnabled(false);
		} else {
			m_btnHealth.setIsEnabled(true);
		}
	}

	private void updateTowerCostWithPlus(int plus) {
		// TODO Auto-generated method stub
		m_tower.cost += plus;
		
		float money = (float)(m_tower.cost * 0.75 * m_tower.health / m_tower.maxLife);
		m_btnSell.setString(new CCFormatter().format("sell 75%s for %dG","%",(int)money));
	}
	
	public void onHealth()
	{
		if( m_view.gold_value < m_towerInfo.cost/10 )
			return;
		m_tower.health += m_towerInfo.max_life/10;
		m_tower.updateHealth();
		m_view.gold_value -= m_towerInfo.cost/10;
		updateAllVariables();
		m_view.updateGameInfomations();
	}
	
	public void onDamage()
	{
		if( m_view.gold_value < m_towerInfo.upgrade_damage[m_tower.upDamageLevel][1] )
			return;
		m_tower.damage += m_towerInfo.upgrade_damage[m_tower.upDamageLevel][0];
		m_view.gold_value -= m_towerInfo.upgrade_damage[m_tower.upDamageLevel][1];
		updateTowerCostWithPlus( m_towerInfo.upgrade_damage[m_tower.upDamageLevel][1] );
		
		m_tower.upDamageLevel ++;
		updateAllVariables();
		m_view.updateGameInfomations();
	}
	
	public void onSpeed()
	{
		if (m_view.gold_value < m_towerInfo.upgrade_speed[m_tower.upSpeedLevel][1])
			return;
		
		m_tower.speed += m_towerInfo.upgrade_speed[m_tower.upSpeedLevel][0];
		m_view.gold_value -= m_towerInfo.upgrade_speed[m_tower.upSpeedLevel][1];
		updateTowerCostWithPlus(m_towerInfo.upgrade_speed[m_tower.upSpeedLevel][1]);
		m_tower.upSpeedLevel ++;
		updateAllVariables();
		m_view.updateGameInfomations();
	}
	
	public void onRange()
	{
		if (m_view.gold_value < m_towerInfo.upgrade_range[m_tower.upRangeLevel][1])
			return;
		
		m_tower.range += m_towerInfo.upgrade_range[m_tower.upRangeLevel][0];
		m_view.gold_value -= m_towerInfo.upgrade_range[m_tower.upRangeLevel][1];
		updateTowerCostWithPlus(m_towerInfo.upgrade_range[m_tower.upRangeLevel][1]);
		m_tower.upRangeLevel ++;
		updateAllVariables();
		m_view.updateGameInfomations();
		//[gameView setNeedsDisplay];
		m_view.drawTowerRange();
		//[gameView drawView:self];
	}
	
	public void onSell()
	{
		float money = (float)(m_tower.cost * 0.75 * m_tower.health / m_tower.maxLife);
		m_view.gold_value += (int)money;
		
		m_view.destroyTower(m_tower);
		m_tower = null;
		
		m_view.updateGameInfomations();
		remove();
	}

	private void remove() {
		// TODO Auto-generated method stub
		this.removeAllChildren(true);
		m_view.removeChild(this, true);
		//m_view.removeChild(m_view.temp_range, true);
		m_view.temp_range.setVisible(false);
		//m_view.temp_range = null;
	}

	@Override
	public boolean ccTouchesBegan(MotionEvent event) {
		
		CCPoint pt = Director.sharedDirector().convertCoordinate(event.getX(), event.getY());
		CocosNode node = getChild(tag_bk);
		CCRect rect = node.getBoundingBox();
		if( !CCRect.containsPoint(rect, pt) )
		{
			m_view.ccTouchesBegan(event);
		}
		
		return TouchDispatcher.kEventHandled;
	}
}
