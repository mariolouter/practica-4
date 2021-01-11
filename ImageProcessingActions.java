public class ImageProcessingActions {

    public static void modificarBrilloPressed(ImageProcessingGUI gui, int value) {
        int[][] img = gui.getImage();
        img = ImageUtils.brillo(img, value);
        gui.setImage(img);

    }

    public static void binarizarPressed(ImageProcessingGUI gui, int value) {
        int[][] img = gui.getImage();
        img = ImageUtils.binarizar(img, value);
        gui.setImage(img);
    }

    public static void entrelazarPressed(ImageProcessingGUI gui) {
        // COMPLETAR
    }

    public static void pixelarPressed(ImageProcessingGUI gui, int value) {
        int[][] img = gui.getImage();
        int w = value;
        img = ImageUtils.pixelar(img, w);
        gui.setImage(img);

    }

    public static void sobelPressed(ImageProcessingGUI gui) {
        // COMPLETAR
    }

}