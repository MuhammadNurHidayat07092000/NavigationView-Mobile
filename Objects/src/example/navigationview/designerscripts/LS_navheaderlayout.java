package example.navigationview.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_navheaderlayout{

public static void LS_general(java.util.LinkedHashMap<String, anywheresoftware.b4a.keywords.LayoutBuilder.ViewWrapperAndAnchor> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
if ((anywheresoftware.b4a.keywords.LayoutBuilder.getScreenSize()>6.5d)) { 
;
views.get("label1").vw.setLeft((int)((24d * scale)));
views.get("label1").vw.setWidth((int)((views.get("panel1").vw.getWidth())-(24d * scale) - ((24d * scale))));
views.get("label2").vw.setLeft((int)((24d * scale)));
views.get("label2").vw.setWidth((int)((views.get("panel1").vw.getWidth())-(24d * scale) - ((24d * scale))));
;};

}
}