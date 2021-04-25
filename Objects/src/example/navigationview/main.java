package example.navigationview;


import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class main extends android.support.v7.app.AppCompatActivity implements B4AActivity{
	public static main mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = false;
	public static final boolean includeTitle = true;
    public static WeakReference<Activity> previousOne;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isFirst) {
			processBA = new BA(this.getApplicationContext(), null, null, "example.navigationview", "example.navigationview.main");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (main).");
				p.finish();
			}
		}
        processBA.runHook("oncreate", this, null);
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		mostCurrent = this;
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
        WaitForLayout wl = new WaitForLayout();
        if (anywheresoftware.b4a.objects.ServiceHelper.StarterHelper.startFromActivity(processBA, wl, true))
		    BA.handler.postDelayed(wl, 5);

	}
	static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
            
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "example.navigationview", "example.navigationview.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "example.navigationview.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (main) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (main) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
        try {
            if (processBA.subExists("activity_actionbarhomeclick")) {
                Class.forName("android.app.ActionBar").getMethod("setHomeButtonEnabled", boolean.class).invoke(
                    getClass().getMethod("getActionBar").invoke(this), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (processBA.runHook("oncreateoptionsmenu", this, new Object[] {menu}))
            return true;
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
        
		return true;
	}   
 @Override
 public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (item.getItemId() == 16908332) {
        processBA.raiseEvent(null, "activity_actionbarhomeclick");
        return true;
    }
    else
        return super.onOptionsItemSelected(item); 
}
@Override
 public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    super.onPrepareOptionsMenu(menu);
    processBA.runHook("onprepareoptionsmenu", this, new Object[] {menu});
    return true;
    
 }
 protected void onStart() {
    super.onStart();
    processBA.runHook("onstart", this, null);
}
 protected void onStop() {
    super.onStop();
    processBA.runHook("onstop", this, null);
}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       if (processBA.subExists("activity_windowfocuschanged"))
           processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEventFromUI(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return main.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeydown", this, new Object[] {keyCode, event}))
            return true;
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK &&
					android.os.Build.VERSION.SDK_INT >= 18) {
				HandleKeyDelayed hk = new HandleKeyDelayed();
				hk.kc = keyCode;
				BA.handler.post(hk);
				return true;
			}
			else {
				boolean res = new HandleKeyDelayed().runDirectly(keyCode);
				if (res)
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private class HandleKeyDelayed implements Runnable {
		int kc;
		public void run() {
			runDirectly(kc);
		}
		public boolean runDirectly(int keyCode) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true) {
                return true;
            }
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
            return false;
		}
		
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeyup", this, new Object[] {keyCode, event}))
            return true;
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
		this.setIntent(intent);
        processBA.runHook("onnewintent", this, new Object[] {intent});
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null) //workaround for emulator bug (Issue 2423)
            return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        BA.LogInfo("** Activity (main) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        processBA.setActivityPaused(true);
        mostCurrent = null;
        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        processBA.runHook("onpause", this, null);
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
        processBA.runHook("ondestroy", this, null);
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
        processBA.runHook("onresume", this, null);
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
			if (mostCurrent == null || mostCurrent != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (main) Resume **");
		    processBA.raiseEvent(mostCurrent._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
        processBA.runHook("onactivityresult", this, new Object[] {requestCode, resultCode});
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}
    public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        for (int i = 0;i < permissions.length;i++) {
            Object[] o = new Object[] {permissions[i], grantResults[i] == 0};
            processBA.raiseEventFromDifferentThread(null,null, 0, "activity_permissionresult", true, o);
        }
            
    }

public anywheresoftware.b4a.keywords.Common __c = null;
public static String _ip = "";
public de.amberhome.viewpager.AHViewPager _vp = null;
public de.amberhome.viewpager.AHPageContainer _pc = null;
public de.amberhome.objects.TabLayoutWrapper _tablayout = null;
public de.amberhome.objects.appcompat.ACToolbarLightWrapper _toolbar = null;
public de.amberhome.objects.NavigationDrawerWrapper _navdrawer = null;
public anywheresoftware.b4a.objects.LabelWrapper _headerlabel = null;
public anywheresoftware.b4a.samples.httputils2.httputils2service _httputils2service = null;
public example.navigationview.mn_data _mn_data = null;
public example.navigationview.mn_datainput _mn_datainput = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
vis = vis | (mn_data.mostCurrent != null);
vis = vis | (mn_datainput.mostCurrent != null);
return vis;}
public static String  _activity_create(boolean _firsttime) throws Exception{
anywheresoftware.b4a.object.XmlLayoutBuilder _xml = null;
de.amberhome.objects.appcompat.ACSubMenuWrapper _submenu = null;
int _i = 0;
anywheresoftware.b4a.objects.PanelWrapper _p = null;
String _pagename = "";
de.amberhome.objects.appcompat.AppCompatBase _ac = null;
 //BA.debugLineNum = 34;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 35;BA.debugLine="Dim xml As XmlLayoutBuilder";
_xml = new anywheresoftware.b4a.object.XmlLayoutBuilder();
 //BA.debugLineNum = 37;BA.debugLine="NavDrawer.Initialize2(\"NavDrawer\", Activity, NavD";
mostCurrent._navdrawer.Initialize2(mostCurrent.activityBA,"NavDrawer",(anywheresoftware.b4a.objects.PanelWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.PanelWrapper(), (android.view.ViewGroup)(mostCurrent._activity.getObject())),mostCurrent._navdrawer.getDefaultDrawerWidth(),mostCurrent._navdrawer.GRAVITY_START);
 //BA.debugLineNum = 38;BA.debugLine="IP=\"192.168.43.31\"";
_ip = "192.168.43.31";
 //BA.debugLineNum = 39;BA.debugLine="Activity.LoadLayout(\"layout1\")";
mostCurrent._activity.LoadLayout("layout1",mostCurrent.activityBA);
 //BA.debugLineNum = 40;BA.debugLine="ToolBar.Title=\"Sistem Informasi Mahasiswa\"";
mostCurrent._toolbar.setTitle(BA.ObjectToCharSequence("Sistem Informasi Mahasiswa"));
 //BA.debugLineNum = 41;BA.debugLine="NavDrawer.InitDrawerToggle";
mostCurrent._navdrawer.InitDrawerToggle();
 //BA.debugLineNum = 43;BA.debugLine="ToolBar.InitMenuListener";
mostCurrent._toolbar.InitMenuListener();
 //BA.debugLineNum = 44;BA.debugLine="NavDrawer.NavigationView.LoadLayout( \"navheaderLa";
mostCurrent._navdrawer.getNavigationView().LoadLayout(mostCurrent.activityBA,"navheaderLayout.bal",mostCurrent._navdrawer.getDefaultHeaderHeight());
 //BA.debugLineNum = 46;BA.debugLine="NavDrawer.NavigationView.Menu.AddWithGroup2(1, 1,";
mostCurrent._navdrawer.getNavigationView().getMenu().AddWithGroup2((int) (1),(int) (1),(int) (1),BA.ObjectToCharSequence("Home"),_xml.GetDrawable("ic_home_black_24dp")).setChecked(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 48;BA.debugLine="NavDrawer.NavigationView.Menu.AddWithGroup2(2, 10";
mostCurrent._navdrawer.getNavigationView().getMenu().AddWithGroup2((int) (2),(int) (10),(int) (1000),BA.ObjectToCharSequence("Data Mahasiswa"),_xml.GetDrawable("ic_settings_black_24dp"));
 //BA.debugLineNum = 49;BA.debugLine="NavDrawer.NavigationView.Menu.AddWithGroup2(2, 11";
mostCurrent._navdrawer.getNavigationView().getMenu().AddWithGroup2((int) (2),(int) (11),(int) (1000),BA.ObjectToCharSequence("Input Mahasiswa"),_xml.GetDrawable("ic_input_black_24dp"));
 //BA.debugLineNum = 50;BA.debugLine="NavDrawer.NavigationView.Menu.AddWithGroup2(2, 12";
mostCurrent._navdrawer.getNavigationView().getMenu().AddWithGroup2((int) (2),(int) (12),(int) (1100),BA.ObjectToCharSequence("Feedback"),_xml.GetDrawable("ic_feedback_black_24dp"));
 //BA.debugLineNum = 51;BA.debugLine="NavDrawer.NavigationView.Menu.AddWithGroup2(2, 13";
mostCurrent._navdrawer.getNavigationView().getMenu().AddWithGroup2((int) (2),(int) (13),(int) (1200),BA.ObjectToCharSequence("Bantuan"),_xml.GetDrawable("ic_help_black_24dp"));
 //BA.debugLineNum = 52;BA.debugLine="NavDrawer.NavigationView.Menu.AddWithGroup2(2, 14";
mostCurrent._navdrawer.getNavigationView().getMenu().AddWithGroup2((int) (2),(int) (14),(int) (1200),BA.ObjectToCharSequence("Keluar"),_xml.GetDrawable("ic_close_black_24dp"));
 //BA.debugLineNum = 56;BA.debugLine="NavDrawer.NavigationView.Menu.SetGroupCheckable(2";
mostCurrent._navdrawer.getNavigationView().getMenu().SetGroupCheckable((int) (2),anywheresoftware.b4a.keywords.Common.True,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 58;BA.debugLine="NavDrawer.AddSecondaryDrawer(150dip, NavDrawer.GR";
mostCurrent._navdrawer.AddSecondaryDrawer(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (150)),mostCurrent._navdrawer.GRAVITY_END);
 //BA.debugLineNum = 59;BA.debugLine="NavDrawer.SecondaryNavigationView.LoadLayout(\"nav";
mostCurrent._navdrawer.getSecondaryNavigationView().LoadLayout(mostCurrent.activityBA,"navHeaderLayout2",anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (100)));
 //BA.debugLineNum = 60;BA.debugLine="HeaderLabel.Text = \"Fist Header\"";
mostCurrent._headerlabel.setText(BA.ObjectToCharSequence("Fist Header"));
 //BA.debugLineNum = 61;BA.debugLine="NavDrawer.SecondaryNavigationView.LoadLayout(\"nav";
mostCurrent._navdrawer.getSecondaryNavigationView().LoadLayout(mostCurrent.activityBA,"navHeaderLayout2",anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 62;BA.debugLine="HeaderLabel.Text = \"Second Header\"";
mostCurrent._headerlabel.setText(BA.ObjectToCharSequence("Second Header"));
 //BA.debugLineNum = 63;BA.debugLine="NavDrawer.SecondaryNavigationView.ItemIconColor =";
mostCurrent._navdrawer.getSecondaryNavigationView().setItemIconColor(anywheresoftware.b4a.keywords.Common.Colors.Red);
 //BA.debugLineNum = 64;BA.debugLine="NavDrawer.SecondaryNavigationView.SetItemTextColo";
mostCurrent._navdrawer.getSecondaryNavigationView().SetItemTextColors(anywheresoftware.b4a.keywords.Common.Colors.Gray,anywheresoftware.b4a.keywords.Common.Colors.Red,anywheresoftware.b4a.keywords.Common.Colors.Red,anywheresoftware.b4a.keywords.Common.Colors.LightGray);
 //BA.debugLineNum = 66;BA.debugLine="Dim subMenu As ACSubMenu";
_submenu = new de.amberhome.objects.appcompat.ACSubMenuWrapper();
 //BA.debugLineNum = 67;BA.debugLine="subMenu = NavDrawer.SecondaryNavigationView.Menu.";
_submenu = mostCurrent._navdrawer.getSecondaryNavigationView().getMenu().AddSubMenu((int) (3),(int) (20),(int) (20),BA.ObjectToCharSequence("Pages"));
 //BA.debugLineNum = 68;BA.debugLine="PC.Initialize";
mostCurrent._pc.Initialize(mostCurrent.activityBA);
 //BA.debugLineNum = 69;BA.debugLine="For i = 0 To 2";
{
final int step26 = 1;
final int limit26 = (int) (2);
_i = (int) (0) ;
for (;(step26 > 0 && _i <= limit26) || (step26 < 0 && _i >= limit26) ;_i = ((int)(0 + _i + step26))  ) {
 //BA.debugLineNum = 70;BA.debugLine="Dim p As Panel";
_p = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 71;BA.debugLine="p.Initialize(\"\")";
_p.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 72;BA.debugLine="p.Color = Colors.RGB(Rnd(0,256), Rnd(0,256), Rnd";
_p.setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB(anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (256)),anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (256)),anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (256))));
 //BA.debugLineNum = 73;BA.debugLine="Dim pageName As String";
_pagename = "";
 //BA.debugLineNum = 74;BA.debugLine="pageName = \"Page \" & i";
_pagename = "Page "+BA.NumberToString(_i);
 //BA.debugLineNum = 75;BA.debugLine="PC.AddPage(p, pageName)";
mostCurrent._pc.AddPage((android.view.View)(_p.getObject()),_pagename);
 //BA.debugLineNum = 76;BA.debugLine="subMenu.AddWithGroup2(3, 100+i, 100+i, pageName,";
_submenu.AddWithGroup2((int) (3),(int) (100+_i),(int) (100+_i),BA.ObjectToCharSequence(_pagename),_xml.GetDrawable("ic_bookmark_black_24dp"));
 }
};
 //BA.debugLineNum = 78;BA.debugLine="NavDrawer.SecondaryNavigationView.CheckedItem = 1";
mostCurrent._navdrawer.getSecondaryNavigationView().setCheckedItem((int) (100));
 //BA.debugLineNum = 79;BA.debugLine="subMenu.SetGroupCheckable(3, True, True)";
_submenu.SetGroupCheckable((int) (3),anywheresoftware.b4a.keywords.Common.True,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 80;BA.debugLine="VP.PageContainer = PC";
mostCurrent._vp.setPageContainer(mostCurrent._pc);
 //BA.debugLineNum = 82;BA.debugLine="Dim Ac As AppCompat";
_ac = new de.amberhome.objects.appcompat.AppCompatBase();
 //BA.debugLineNum = 83;BA.debugLine="TabLayout.Color = Ac.GetThemeAttribute(\"colorPrim";
mostCurrent._tablayout.setColor(_ac.GetThemeAttribute(mostCurrent.activityBA,"colorPrimary"));
 //BA.debugLineNum = 84;BA.debugLine="TabLayout.SetViewPager(VP)";
mostCurrent._tablayout.SetViewPager((android.support.v4.view.ViewPager)(mostCurrent._vp.getObject()));
 //BA.debugLineNum = 85;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
 //BA.debugLineNum = 97;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 98;BA.debugLine="If KeyCode = KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 99;BA.debugLine="Log(\"Close the drawers on back key press\")";
anywheresoftware.b4a.keywords.Common.Log("Close the drawers on back key press");
 //BA.debugLineNum = 100;BA.debugLine="If NavDrawer.IsDrawerOpen2(NavDrawer.GRAVITY_STA";
if (mostCurrent._navdrawer.IsDrawerOpen2(mostCurrent._navdrawer.GRAVITY_START) || mostCurrent._navdrawer.IsDrawerOpen2(mostCurrent._navdrawer.GRAVITY_END)) { 
 //BA.debugLineNum = 101;BA.debugLine="NavDrawer.CloseDrawers";
mostCurrent._navdrawer.CloseDrawers();
 //BA.debugLineNum = 102;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 };
 };
 //BA.debugLineNum = 106;BA.debugLine="Return False";
if (true) return anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 107;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 91;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 93;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 87;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 89;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 22;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 24;BA.debugLine="Private VP As AHViewPager";
mostCurrent._vp = new de.amberhome.viewpager.AHViewPager();
 //BA.debugLineNum = 25;BA.debugLine="Private PC As AHPageContainer";
mostCurrent._pc = new de.amberhome.viewpager.AHPageContainer();
 //BA.debugLineNum = 26;BA.debugLine="Private TabLayout As DSTabLayout";
mostCurrent._tablayout = new de.amberhome.objects.TabLayoutWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Private ToolBar As ACToolBarLight";
mostCurrent._toolbar = new de.amberhome.objects.appcompat.ACToolbarLightWrapper();
 //BA.debugLineNum = 29;BA.debugLine="Private NavDrawer As DSNavigationDrawer";
mostCurrent._navdrawer = new de.amberhome.objects.NavigationDrawerWrapper();
 //BA.debugLineNum = 30;BA.debugLine="Private HeaderLabel As Label";
mostCurrent._headerlabel = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 31;BA.debugLine="End Sub";
return "";
}
public static String  _navdrawer_drawerclosed(int _drawergravity) throws Exception{
 //BA.debugLineNum = 142;BA.debugLine="Sub NavDrawer_DrawerClosed (DrawerGravity As Int)";
 //BA.debugLineNum = 143;BA.debugLine="LogColor(\"NavDrawer closed\", Colors.Green)";
anywheresoftware.b4a.keywords.Common.LogColor("NavDrawer closed",anywheresoftware.b4a.keywords.Common.Colors.Green);
 //BA.debugLineNum = 144;BA.debugLine="End Sub";
return "";
}
public static String  _navdrawer_draweropened(int _drawergravity) throws Exception{
 //BA.debugLineNum = 146;BA.debugLine="Sub NavDrawer_DrawerOpened (DrawerGravity As Int)";
 //BA.debugLineNum = 147;BA.debugLine="LogColor(\"NavDrawer opened\", Colors.Green)";
anywheresoftware.b4a.keywords.Common.LogColor("NavDrawer opened",anywheresoftware.b4a.keywords.Common.Colors.Green);
 //BA.debugLineNum = 148;BA.debugLine="End Sub";
return "";
}
public static String  _navdrawer_drawerslide(float _position,int _drawergravity) throws Exception{
 //BA.debugLineNum = 150;BA.debugLine="Sub NavDrawer_DrawerSlide (Position As Float, Draw";
 //BA.debugLineNum = 151;BA.debugLine="LogColor(\"NavDrawer Slide: \" & Position, Colors.B";
anywheresoftware.b4a.keywords.Common.LogColor("NavDrawer Slide: "+BA.NumberToString(_position),anywheresoftware.b4a.keywords.Common.Colors.Blue);
 //BA.debugLineNum = 152;BA.debugLine="End Sub";
return "";
}
public static String  _navdrawer_navigationitemselected(de.amberhome.objects.appcompat.ACMenuItemWrapper _menuitem,int _drawergravity) throws Exception{
int _tanya = 0;
 //BA.debugLineNum = 155;BA.debugLine="Sub NavDrawer_NavigationItemSelected (MenuItem As";
 //BA.debugLineNum = 156;BA.debugLine="If MenuItem.Id = 1 Then'jika klik home";
if (_menuitem.getId()==1) { 
 //BA.debugLineNum = 157;BA.debugLine="Msgbox(\"Home\",\"Di Klik\")";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("Home"),BA.ObjectToCharSequence("Di Klik"),mostCurrent.activityBA);
 }else if(_menuitem.getId()==10) { 
 //BA.debugLineNum = 159;BA.debugLine="StartActivity(mn_Data)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._mn_data.getObject()));
 }else if(_menuitem.getId()==11) { 
 //BA.debugLineNum = 161;BA.debugLine="StartActivity(mn_dataInput)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._mn_datainput.getObject()));
 }else if(_menuitem.getId()==14) { 
 //BA.debugLineNum = 163;BA.debugLine="Dim Tanya As Int = Msgbox2(\"Anda Yakin Keluar Ap";
_tanya = anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Anda Yakin Keluar Aplikasi ..?"),BA.ObjectToCharSequence("Informasi"),"Ya","Tidak","",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"tanya.png").getObject()),mostCurrent.activityBA);
 //BA.debugLineNum = 164;BA.debugLine="If Tanya = DialogResponse.Positive Then";
if (_tanya==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 165;BA.debugLine="ExitApplication";
anywheresoftware.b4a.keywords.Common.ExitApplication();
 };
 };
 //BA.debugLineNum = 169;BA.debugLine="NavDrawer.NavigationView.CheckedItem = MenuItem.I";
mostCurrent._navdrawer.getNavigationView().setCheckedItem(_menuitem.getId());
 //BA.debugLineNum = 171;BA.debugLine="If MenuItem.Id >= 100 Then";
if (_menuitem.getId()>=100) { 
 //BA.debugLineNum = 172;BA.debugLine="VP.GotoPage(MenuItem.Id - 100, True)";
mostCurrent._vp.GotoPage((int) (_menuitem.getId()-100),anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 175;BA.debugLine="NavDrawer.CloseDrawer2(DrawerGravity)";
mostCurrent._navdrawer.CloseDrawer2(_drawergravity);
 //BA.debugLineNum = 176;BA.debugLine="End Sub";
return "";
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        anywheresoftware.b4a.samples.httputils2.httputils2service._process_globals();
main._process_globals();
mn_data._process_globals();
mn_datainput._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 18;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 19;BA.debugLine="Public IP As String";
_ip = "";
 //BA.debugLineNum = 20;BA.debugLine="End Sub";
return "";
}
public static String  _toolbar_navigationitemclick() throws Exception{
 //BA.debugLineNum = 181;BA.debugLine="Sub Toolbar_NavigationItemClick";
 //BA.debugLineNum = 182;BA.debugLine="If NavDrawer.IsDrawerOpen Then";
if (mostCurrent._navdrawer.IsDrawerOpen()) { 
 //BA.debugLineNum = 183;BA.debugLine="NavDrawer.CloseDrawer";
mostCurrent._navdrawer.CloseDrawer();
 }else {
 //BA.debugLineNum = 185;BA.debugLine="NavDrawer.OpenDrawer";
mostCurrent._navdrawer.OpenDrawer();
 };
 //BA.debugLineNum = 187;BA.debugLine="End Sub";
return "";
}
public static String  _vp_pagechanged(int _position) throws Exception{
int _i = 0;
 //BA.debugLineNum = 111;BA.debugLine="Sub VP_PageChanged (Position As Int)";
 //BA.debugLineNum = 112;BA.debugLine="Log(\"PageChanged: \" & Position)";
anywheresoftware.b4a.keywords.Common.Log("PageChanged: "+BA.NumberToString(_position));
 //BA.debugLineNum = 115;BA.debugLine="For i = 0 To 2";
{
final int step2 = 1;
final int limit2 = (int) (2);
_i = (int) (0) ;
for (;(step2 > 0 && _i <= limit2) || (step2 < 0 && _i >= limit2) ;_i = ((int)(0 + _i + step2))  ) {
 //BA.debugLineNum = 116;BA.debugLine="If i = Position Then";
if (_i==_position) { 
 //BA.debugLineNum = 117;BA.debugLine="NavDrawer.SecondaryNavigationView.Menu.FindItem";
mostCurrent._navdrawer.getSecondaryNavigationView().getMenu().FindItem((int) (_i+100)).setChecked(anywheresoftware.b4a.keywords.Common.True);
 }else {
 //BA.debugLineNum = 119;BA.debugLine="NavDrawer.SecondaryNavigationView.Menu.FindItem";
mostCurrent._navdrawer.getSecondaryNavigationView().getMenu().FindItem((int) (_i+100)).setChecked(anywheresoftware.b4a.keywords.Common.False);
 };
 }
};
 //BA.debugLineNum = 122;BA.debugLine="End Sub";
return "";
}
public static String  _vp_pagecreated(int _position,Object _page) throws Exception{
 //BA.debugLineNum = 124;BA.debugLine="Sub VP_PageCreated (Position As Int, Page As Objec";
 //BA.debugLineNum = 125;BA.debugLine="Log(\"PageCreated: \" & Position)";
anywheresoftware.b4a.keywords.Common.Log("PageCreated: "+BA.NumberToString(_position));
 //BA.debugLineNum = 126;BA.debugLine="End Sub";
return "";
}
public static String  _vp_pagedestroyed(int _position,Object _page) throws Exception{
 //BA.debugLineNum = 128;BA.debugLine="Sub VP_PageDestroyed (Position As Int, Page As Obj";
 //BA.debugLineNum = 129;BA.debugLine="Log(\"PageDestroyed: \" & Position)";
anywheresoftware.b4a.keywords.Common.Log("PageDestroyed: "+BA.NumberToString(_position));
 //BA.debugLineNum = 130;BA.debugLine="End Sub";
return "";
}
public static String  _vp_pagescrolled(int _position,float _positionoffset,int _positionoffsetpixels) throws Exception{
 //BA.debugLineNum = 132;BA.debugLine="Sub VP_PageScrolled (Position As Int, PositionOffs";
 //BA.debugLineNum = 134;BA.debugLine="End Sub";
return "";
}
public static String  _vp_pagescrollstatechanged(int _state) throws Exception{
 //BA.debugLineNum = 136;BA.debugLine="Sub VP_PageScrollStateChanged (State As Int)";
 //BA.debugLineNum = 137;BA.debugLine="Log(\"StateChange: \" & State)";
anywheresoftware.b4a.keywords.Common.Log("StateChange: "+BA.NumberToString(_state));
 //BA.debugLineNum = 138;BA.debugLine="End Sub";
return "";
}
}
