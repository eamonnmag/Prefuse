package prefuse.util.display;

/**
 * Created with IntelliJ IDEA.
 * User: eamonnmaguire
 * Date: 05/03/2013
 * Time: 17:04
 * To change this template use File | Settings | File Templates.
 */
public class DisplayInfo {

    private static int VISIBLE_ITEMS;

    public static int getVisibleItemCount() {
        return VISIBLE_ITEMS;
    }

    public static void setVisibleItemCount(int visibleItemCount) {
        DisplayInfo.VISIBLE_ITEMS = visibleItemCount;
    }
}
