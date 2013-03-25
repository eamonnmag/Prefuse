package prefuse.render;

import prefuse.util.display.DisplayInfo;
import prefuse.visual.VisualItem;


public class MultiScaleLabelRenderer extends LabelRenderer {

    private int detailedLimit = 15;
    private int mediumLimit = 30;

    /**
     * Create a new LabelRenderer. By default the field "label" is used
     * as the field name for looking up text, and no image is used.
     */
    public MultiScaleLabelRenderer() {
        super();
    }

    /**
     * Create a new LabelRenderer. Draws a text label using the given
     * text data field and does not draw an image.
     *
     * @param textField the data field for the text label.
     */
    public MultiScaleLabelRenderer(String textField) {
        super(textField);
    }

    /**
     * Create a new LabelRenderer. Draws a text label using the given text
     * data field, and draws the image at the location reported by the
     * given image data field.
     *
     * @param textField  the data field for the text label
     * @param imageField the data field for the image location. This value
     *                   in the data field should be a URL, a file within the current classpath,
     *                   a file on the filesystem, or null for no image. If the
     *                   <code>imageField</code> parameter is null, no images at all will be
     *                   drawn.
     */
    public MultiScaleLabelRenderer(String textField, String imageField) {
        super(textField, imageField);
    }

    public void setDetailedLimit(int detailedLimit) {
        this.detailedLimit = detailedLimit;
    }

    public void setMediumLimit(int mediumLimit) {
        this.mediumLimit = mediumLimit;
    }

    /**
     * Returns a location string for the image to draw based on the current zoom level.
     * Subclasses can override this class to perform custom image selection beyond looking up the value
     * from a data field.
     *
     * @param item the item for which to select an image to draw
     * @return the location string for the image to use, or null for no image
     */
    protected String getImageLocation(VisualItem item) {
        int availableItems = DisplayInfo.getVisibleItemCount();
        // in this instance, depending on the number of available items,
        // we can search for different images to render.
        // e.g. availableItems < 10, then show detailed rendering
        // e.g. availableItems < 20, then show medium level rendering
        // e.g. availableItems > 20, then show abstract level rendering
        // maybe we should take in to consideration more variables when deciding level but for now,
        // this is maybe enough.

        Resolution resolution = getResolution(availableItems);

        String imageToRetrieve = m_imageName + resolution.getAppender();
        System.out.println("Attempting to retrieve " + imageToRetrieve);
        System.out.println("If not, we'll fall back on " + m_imageName);
        if (item.canGetString(imageToRetrieve)) {
            return item.getString(imageToRetrieve);
        } else if (item.canGetString(m_imageName)) {
            return item.getString(m_imageName);
        } else {
            return null;
        }
    }

    private Resolution getResolution(int availableItems) {
        Resolution resolution = Resolution.L;
        if (availableItems < detailedLimit) {
            resolution = Resolution.S;
        } else if (availableItems < mediumLimit) {
            resolution = Resolution.M;
        }
        return resolution;
    }


    enum Resolution {
        S("@S"), M("@M"), L("@L");

        private String appender;

        Resolution(String appender) {
            this.appender = appender;
        }

        public String getAppender() {
            return appender;
        }
    }
}
