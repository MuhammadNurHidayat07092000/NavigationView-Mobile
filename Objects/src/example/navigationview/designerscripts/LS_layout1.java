package example.navigationview.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_layout1{

public static void LS_general(java.util.LinkedHashMap<String, anywheresoftware.b4a.keywords.LayoutBuilder.ViewWrapperAndAnchor> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
//BA.debugLineNum = 5;BA.debugLine="If Landscape Then"[layout1/General script]
if ((BA.ObjectToBoolean( String.valueOf(!anywheresoftware.b4a.keywords.LayoutBuilder.isPortrait())))) { 
;
//BA.debugLineNum = 6;BA.debugLine="ToolBar.Height = 48dip"[layout1/General script]
views.get("toolbar").vw.setHeight((int)((48d * scale)));
//BA.debugLineNum = 7;BA.debugLine="Else"[layout1/General script]
;}else{ 
;
//BA.debugLineNum = 8;BA.debugLine="ToolBar.Height = 56dip"[layout1/General script]
views.get("toolbar").vw.setHeight((int)((56d * scale)));
//BA.debugLineNum = 9;BA.debugLine="End If"[layout1/General script]
;};
//BA.debugLineNum = 11;BA.debugLine="If ActivitySize > 6.5 Then"[layout1/General script]
if ((anywheresoftware.b4a.keywords.LayoutBuilder.getScreenSize()>6.5d)) { 
;
//BA.debugLineNum = 12;BA.debugLine="ToolBar.Height = 64dip"[layout1/General script]
views.get("toolbar").vw.setHeight((int)((64d * scale)));
//BA.debugLineNum = 13;BA.debugLine="End If"[layout1/General script]
;};
//BA.debugLineNum = 15;BA.debugLine="TabLayout.Top = ToolBar.Bottom"[layout1/General script]
views.get("tablayout").vw.setTop((int)((views.get("toolbar").vw.getTop() + views.get("toolbar").vw.getHeight())));
//BA.debugLineNum = 16;BA.debugLine="VP.SetTopAndBottom(TabLayout.Bottom, 100%y)"[layout1/General script]
views.get("vp").vw.setTop((int)((views.get("tablayout").vw.getTop() + views.get("tablayout").vw.getHeight())));
views.get("vp").vw.setHeight((int)((100d / 100 * height) - ((views.get("tablayout").vw.getTop() + views.get("tablayout").vw.getHeight()))));

}
}