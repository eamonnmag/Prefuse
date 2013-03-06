package prefuse.util.display;

/**
 * Created with IntelliJ IDEA.
 * User: eamonnmaguire
 * Date: 05/03/2013
 * Time: 17:04
 * To change this template use File | Settings | File Templates.
 */
public class DisplayZoomInfo {

    private static int VISIBLE_ITEMS;

    public static int getCurrentZoom() {
        return VISIBLE_ITEMS;
    }

    public static void setVisibleItemCount(int visibleItemCount) {
        System.out.println("Numer of visible items is " + visibleItemCount);
        DisplayZoomInfo.VISIBLE_ITEMS = visibleItemCount;
    }
}
