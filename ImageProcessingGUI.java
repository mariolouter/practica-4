import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageProcessingGUI extends JPanel implements ActionListener {
    //atributos
    static String pixelarString = "Pixelar";
    static String brilloString = "Brillo";
    static String binarizarString = "Binarizar";
    static String entrelazarString = "Entrelazar";
    static String sobelString = "Sobel";
    static String originalString = "Original";
    protected String imageFileName = null;
    protected JLabel picture;
    protected int value = 0;
    private int[][] img = null;

    //constructor
    public ImageProcessingGUI() {
        super(new BorderLayout());

        //creaciÃ³n de botones.
        JRadioButton originalButton = new JRadioButton(originalString);
        originalButton.setMnemonic(KeyEvent.VK_A);
        originalButton.setActionCommand(originalString);
        originalButton.setSelected(true);

        JRadioButton brilloButton = new JRadioButton(brilloString);
        brilloButton.setMnemonic(KeyEvent.VK_B);
        brilloButton.setActionCommand(brilloString);

        JRadioButton pixelarButton = new JRadioButton(pixelarString);
        pixelarButton.setMnemonic(KeyEvent.VK_C);
        pixelarButton.setActionCommand(pixelarString);

        JRadioButton binarizarButton = new JRadioButton(binarizarString);
        binarizarButton.setMnemonic(KeyEvent.VK_C);
        binarizarButton.setActionCommand(binarizarString);

        JRadioButton entrelazarButton = new JRadioButton(entrelazarString);
        entrelazarButton.setMnemonic(KeyEvent.VK_D);
        entrelazarButton.setActionCommand(entrelazarString);

        JRadioButton sobelButton = new JRadioButton(sobelString);
        sobelButton.setMnemonic(KeyEvent.VK_R);
        sobelButton.setActionCommand(sobelString);


        //agrupar botones.
        ButtonGroup group = new ButtonGroup();
        group.add(originalButton);
        group.add(brilloButton);
        group.add(pixelarButton);
        group.add(binarizarButton);
        group.add(entrelazarButton);
        group.add(sobelButton);

        //activar el manejador de senyales para los botones.
        originalButton.addActionListener(this);
        brilloButton.addActionListener(this);
        pixelarButton.addActionListener(this);
        binarizarButton.addActionListener(this);
        entrelazarButton.addActionListener(this);
        sobelButton.addActionListener(this);

        picture = new JLabel();
        picture.setPreferredSize(new Dimension(446, 482));


        //poner los botones en el panel principal
        JPanel radioPanel = new JPanel(new GridLayout(0, 1));
        radioPanel.add(originalButton);
        radioPanel.add(brilloButton);
        radioPanel.add(binarizarButton);
        radioPanel.add(pixelarButton);
        radioPanel.add(entrelazarButton);
        radioPanel.add(sobelButton);

        //anyadir boton cargar imagen
        JButton openButton = new JButton("Cargar imagen...");
        openButton.setActionCommand("openFile");
        openButton.addActionListener(this);


        //anyadir los bloques a la pantalla principal
        add(radioPanel, BorderLayout.LINE_START);
        add(picture, BorderLayout.CENTER);
        add(openButton, BorderLayout.PAGE_END);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        //manejador de senyales para cuando se modifique el tamanyo de la ventana
        addComponentListener(new ComponentListener() {
            public void componentHidden(ComponentEvent evt) {
            }

            public void componentShown(ComponentEvent evt) {
            }

            public void componentMoved(ComponentEvent evt) {
            }

            public void componentResized(ComponentEvent evt) {
                Component c = (Component) evt.getSource();

                Dimension newSize = c.getSize();
                BufferedImage image;

                if (img != null)
                    image = matrizToImagen(img);
                else
                    image = new BufferedImage(446, 482, java.awt.Image.SCALE_SMOOTH);

                ImageIcon imageIcon = new ImageIcon(image);
                Image image_aux = imageIcon.getImage();
                Image image_aux2 = image_aux.getScaledInstance(newSize.width - 150, (int) (newSize.height * (image.getHeight() / (double) image.getWidth())), java.awt.Image.SCALE_SMOOTH);
                imageIcon = new ImageIcon(image_aux2);
                picture.setIcon(imageIcon);
            }
        });

    }

    public int[][] getImage() {
        return img;
    }

    protected int elegirValor(int ini, int fi, int orig) {
        value = orig;
        JOptionPane optionPane = new JOptionPane();
        JSlider slider = new JSlider(JSlider.HORIZONTAL, ini, fi, orig);
        slider.setMajorTickSpacing(100);
        slider.setMinorTickSpacing(10);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                JSlider slider = (JSlider) evt.getSource();
                if (!slider.getValueIsAdjusting())
                    value = slider.getValue();
            }
        });


        optionPane.setMessage(new Object[]{"Select a value: ", slider});
        optionPane.setMessageType(JOptionPane.QUESTION_MESSAGE);
        optionPane.setOptionType(JOptionPane.OK_CANCEL_OPTION);
        JDialog dialog = optionPane.createDialog(this, "Choose a level");
        dialog.setVisible(true);
        return value;

    }

    protected double elegirValor(double ini, double fi, double orig) {
        value = (int) (orig * 100);
        JOptionPane optionPane = new JOptionPane();
        int iniI = (int) (ini * 100);
        int fiI = (int) (fi * 100);
        int origI = (int) (orig * 100);
        JSlider slider = new JSlider(JSlider.HORIZONTAL, iniI, fiI, origI);
        slider.setMajorTickSpacing(100);
        slider.setMinorTickSpacing(10);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                JSlider slider = (JSlider) evt.getSource();
                if (!slider.getValueIsAdjusting()) {
                    value = slider.getValue();

                }
            }
        });

        optionPane.setMessage(new Object[]{"Select a value: ", slider});
        optionPane.setMessageType(JOptionPane.QUESTION_MESSAGE);
        optionPane.setOptionType(JOptionPane.OK_CANCEL_OPTION);
        JDialog dialog = optionPane.createDialog(this, "Choose a level");
        dialog.setVisible(true);
        return value / 100.0;

    }

    public void actionPerformed(ActionEvent e) {

        try {
            switch (e.getActionCommand()) {

                case "openFile":
                    JFileChooser fc = new JFileChooser();
                    int returnVal = fc.showOpenDialog(null);

                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        imageFileName = fc.getSelectedFile().getAbsolutePath();
                    }
                    img = cargarImagenDeFichero(imageFileName);
                    setImage(img);
                    break;
                case "Original":
                    img = cargarImagenDeFichero(imageFileName);
                    setImage(img);
                    break;
                case "Brillo":
                    value = elegirValor(-255, 255, 0);
                    ImageProcessingActions.modificarBrilloPressed(this, value);
                    break;
                case "Binarizar":
                    value = elegirValor(0, 255, 150);
                    ImageProcessingActions.binarizarPressed(this, value);
                    break;
                case "Entrelazar":
                    ImageProcessingActions.entrelazarPressed(this);
                    break;
                case "Pixelar":
                    value = elegirValor(0, 25, 5);
                    ImageProcessingActions.pixelarPressed(this, value);
                    break;
                case "Sobel":
                    value = elegirValor(0, 255,255);
                    ImageProcessingActions.sobelPressed(this, value);
                    break;
            }

        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("ERROR " + ex);
            JOptionPane.showMessageDialog(null, "Array index out of bounds", "Error ", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalArgumentException ex) {
            System.out.println("ERROR " + ex);
            JOptionPane.showMessageDialog(null, "Pixel value out of expected range [0-255]", "Error ", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            System.out.println("ERROR " + ex);
            JOptionPane.showMessageDialog(null, "Image not uploaded", "Error ", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void setImage(int[][] img) {
        this.img = img;
        BufferedImage image = matrizToImagen(img);
        ImageIcon imageIcon = new ImageIcon(image);

        Image image_aux = imageIcon.getImage();
        Dimension size = this.getSize();
        Image image_aux2 = image_aux.getScaledInstance(size.width - 150, (int) (size.height * (image.getHeight() / (double) image.getWidth())), java.awt.Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(image_aux2);

        picture.setIcon(imageIcon);
    }

    /*
    public abstract void changeBrightPressed(int value);

    public abstract void binarizePressed (int value);

    public abstract void entrelazarPressed ();

    public abstract void pixelarPressed (int value);

    public abstract void sobelPressed ();
    */

    protected static void createAndShowGUI() {
        JFrame frame = new JFrame("PrÃ¡ctica 5: Matrices");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JComponent newContentPane = new ImageProcessingGUI();
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);
        frame.pack();
        frame.setVisible(true);
    }


    /**
     * Convierte una matriz de enteros entre 0 y 255 a una BufferedImage
     */
    public static BufferedImage matrizToImagen(int[][] m) {
        int ancho = m[0].length;
        int alto = m.length;
        BufferedImage image = new BufferedImage(ancho, alto, BufferedImage.TYPE_BYTE_GRAY);
        for (int i = 0; i < alto; i++) {
            for (int j = 0; j < ancho; j++) {
                int gray = m[i][j];
                Color c = new Color(gray, gray, gray);
                image.setRGB(j, i, c.getRGB());
            }
        }
        return image;
    }


    /* Convierte la imagen inputImage a escala de grises
     **/
    protected static BufferedImage getGrayScale(BufferedImage inputImage) {
        BufferedImage img = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(),
                BufferedImage.TYPE_BYTE_GRAY);
        Graphics g = img.getGraphics();
        g.drawImage(inputImage, 0, 0, null);
        g.dispose();
        return img;
    }


    /*
     * Lee del fichero fileName una imagen, la convierte a escala de grises y la
     * almacena en una matriz de enteros entre 0 y 255; 0 es el negro y 255 el blanco
     * @param String fileName, nombre del fichero donde se encuentra la imagen
     * @return int[][], matriz de valores entre 0 y 255 que representa la imagen original en blanco y negro
     **/
    public int[][] cargarImagenDeFichero(String fileName) {
        int[][] img;
        try {
            BufferedImage imageRGB = ImageIO.read(new File(fileName));
            BufferedImage image = getGrayScale(imageRGB);
            int alto = image.getHeight();
            int ancho = image.getWidth();
            img = new int[alto][ancho];

            for (int i = 0; i < alto; i++) {
                for (int j = 0; j < ancho; j++) {
                    int pix = image.getRGB(j, i);
                    Color c = new Color(pix);
                    int r = c.getRed(), g = c.getGreen(), b = c.getBlue();
                    img[i][j] = (r + g + b) / 3;
                }
            }
            //mejorarContraste(img);
            return img;
        } catch (IOException e) {
            return null;
        }
    }

    private static void mejorarContraste(int[][] img) {
        int alto = img.length;
        int ancho = img[0].length;

        // Buscar maximo y minimo
        int min = img[0][0];
        int max = img[0][0];
        for (int i = 0; i < alto; i++) {
            for (int j = 0; j < ancho; j++) {
                if (img[i][j] < min) min = img[i][j];
                if (img[i][j] > min) max = img[i][j];
            }
        }

        // Estirar histograma
        for (int i = 0; i < alto; i++) {
            for (int j = 0; j < ancho; j++) {
                img[i][j] = img[i][j] * 255 / (max - min);
                if (img[i][j] < 0) img[i][j] = 0;
                if (img[i][j] > 255) img[i][j] = 255;
            }
        }
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

}