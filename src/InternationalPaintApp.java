import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.util.Locale;
import java.util.ResourceBundle;

public class InternationalPaintApp extends JFrame {

    private Locale currentLocale = new Locale("fr");
    private ResourceBundle messages;

    private JMenuBar menuBar;
    private JMenu menuFile, menuOptions, menuLanguage;
    private JMenuItem itemClear, itemExit, itemLangFR, itemLangEN;

    private JToolBar toolBar;
    private JRadioButton rbPencil, rbEraser, rbLine, rbRectangle, rbSquare, rbEllipse, rbCircle, rbStar;
    private ButtonGroup toolGroup;
    private JButton btnChooseColor;
    private JLabel lblColorPreview, lblThickness;
    private JSpinner spinnerThickness;

    private CanvasPanel canvasPanel;

    private Color currentColor = Color.BLACK;
    private float currentThickness = 2.0f;

    public enum ToolType { PENCIL, ERASER, LINE, RECTANGLE, SQUARE, ELLIPSE, CIRCLE, STAR }
    private ToolType currentTool = ToolType.PENCIL;

    public InternationalPaintApp() {
        loadResourceBundle();

        setTitle(messages.getString("app.title"));
        setSize(1050, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        canvasPanel = new CanvasPanel();
        add(canvasPanel, BorderLayout.CENTER);

        buildMenuBar();
        buildToolBar();

        setVisible(true);
    }

    private void loadResourceBundle() {
        messages = ResourceBundle.getBundle("messages", currentLocale);
    }

    private void buildMenuBar() {
        if (menuBar == null) {
            menuBar = new JMenuBar();
            setJMenuBar(menuBar);
        } else {
            menuBar.removeAll();
        }

        menuFile = new JMenu(messages.getString("menu.file"));
        itemClear = new JMenuItem(messages.getString("menu.file.clear"));
        itemClear.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        itemClear.addActionListener(e -> confirmAndClear());

        itemExit = new JMenuItem(messages.getString("menu.file.exit"));
        itemExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
        itemExit.addActionListener(e -> System.exit(0));

        menuFile.add(itemClear);
        menuFile.addSeparator();
        menuFile.add(itemExit);

        menuOptions = new JMenu(messages.getString("menu.options"));
        menuLanguage = new JMenu(messages.getString("menu.options.language"));

        itemLangFR = new JMenuItem("Français (FR)");
        itemLangFR.addActionListener(e -> switchLanguage(new Locale("fr")));

        itemLangEN = new JMenuItem("English (EN)");
        itemLangEN.addActionListener(e -> switchLanguage(new Locale("en")));

        menuLanguage.add(itemLangFR);
        menuLanguage.add(itemLangEN);
        menuOptions.add(menuLanguage);

        menuBar.add(menuFile);
        menuBar.add(menuOptions);

        menuBar.revalidate();
        menuBar.repaint();
    }

    private void buildToolBar() {
        if (toolBar == null) {
            toolBar = new JToolBar();
            toolBar.setFloatable(false);
            add(toolBar, BorderLayout.NORTH);
        } else {
            toolBar.removeAll();
        }

        rbPencil = new JRadioButton(messages.getString("tool.pencil"), currentTool == ToolType.PENCIL);
        rbEraser = new JRadioButton(messages.getString("tool.eraser"), currentTool == ToolType.ERASER);
        rbLine = new JRadioButton(messages.getString("shape.line"), currentTool == ToolType.LINE);
        rbRectangle = new JRadioButton(messages.getString("shape.rectangle"), currentTool == ToolType.RECTANGLE);
        rbSquare = new JRadioButton(messages.getString("shape.square"), currentTool == ToolType.SQUARE);
        rbEllipse = new JRadioButton(messages.getString("shape.ellipse"), currentTool == ToolType.ELLIPSE);
        rbCircle = new JRadioButton(messages.getString("shape.circle"), currentTool == ToolType.CIRCLE);
        rbStar = new JRadioButton(messages.getString("shape.star"), currentTool == ToolType.STAR);

        rbPencil.addActionListener(e -> currentTool = ToolType.PENCIL);
        rbEraser.addActionListener(e -> currentTool = ToolType.ERASER);
        rbLine.addActionListener(e -> currentTool = ToolType.LINE);
        rbRectangle.addActionListener(e -> currentTool = ToolType.RECTANGLE);
        rbSquare.addActionListener(e -> currentTool = ToolType.SQUARE);
        rbEllipse.addActionListener(e -> currentTool = ToolType.ELLIPSE);
        rbCircle.addActionListener(e -> currentTool = ToolType.CIRCLE);
        rbStar.addActionListener(e -> currentTool = ToolType.STAR);

        toolGroup = new ButtonGroup();
        toolGroup.add(rbPencil);
        toolGroup.add(rbEraser);
        toolGroup.add(rbLine);
        toolGroup.add(rbRectangle);
        toolGroup.add(rbSquare);
        toolGroup.add(rbEllipse);
        toolGroup.add(rbCircle);
        toolGroup.add(rbStar);

        toolBar.add(rbPencil);
        toolBar.add(rbEraser);
        toolBar.add(rbLine);
        toolBar.add(rbRectangle);
        toolBar.add(rbSquare);
        toolBar.add(rbEllipse);
        toolBar.add(rbCircle);
        toolBar.add(rbStar);
        toolBar.addSeparator();

        btnChooseColor = new JButton(messages.getString("btn.color"));
        btnChooseColor.addActionListener(e -> {
            Color color = JColorChooser.showDialog(this, messages.getString("btn.color"), currentColor);
            if (color != null) {
                currentColor = color;
                lblColorPreview.setBackground(currentColor);
            }
        });

        lblColorPreview = new JLabel("     ");
        lblColorPreview.setOpaque(true);
        lblColorPreview.setBackground(currentColor);
        lblColorPreview.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        toolBar.add(btnChooseColor);
        toolBar.add(Box.createHorizontalStrut(5));
        toolBar.add(new JLabel(messages.getString("label.color")));
        toolBar.add(Box.createHorizontalStrut(5));
        toolBar.add(lblColorPreview);
        toolBar.addSeparator();

        lblThickness = new JLabel(messages.getString("label.thickness"));
        spinnerThickness = new JSpinner(new SpinnerNumberModel((int) currentThickness, 1, 30, 1));
        spinnerThickness.setMaximumSize(new Dimension(50, 30));
        spinnerThickness.addChangeListener(e -> currentThickness = ((Integer) spinnerThickness.getValue()).floatValue());

        toolBar.add(lblThickness);
        toolBar.add(Box.createHorizontalStrut(5));
        toolBar.add(spinnerThickness);

        toolBar.revalidate();
        toolBar.repaint();
    }

    private void switchLanguage(Locale newLocale) {
        this.currentLocale = newLocale;
        loadResourceBundle();
        setTitle(messages.getString("app.title"));
        buildMenuBar();
        buildToolBar();
    }

    private void confirmAndClear() {
        int response = JOptionPane.showConfirmDialog(
                this,
                messages.getString("dialog.clear.message"),
                messages.getString("dialog.clear.title"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (response == JOptionPane.YES_OPTION) {
            canvasPanel.clearCanvas();
        }
    }

    private class CanvasPanel extends JPanel {
        private BufferedImage bufferedImage;
        private Graphics2D g2dBuffer;

        private Point startPoint = null;
        private Point currentPoint = null;
        private Point lastFreehandPoint = null;

        public CanvasPanel() {
            setBackground(Color.WHITE);

            MouseAdapter mouseHandler = new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    startPoint = e.getPoint();
                    currentPoint = startPoint;
                    lastFreehandPoint = e.getPoint();

                    if (currentTool == ToolType.PENCIL || currentTool == ToolType.ERASER) {
                        drawFreehandSegment(lastFreehandPoint, lastFreehandPoint);
                    }
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    currentPoint = e.getPoint();

                    if (currentTool == ToolType.PENCIL || currentTool == ToolType.ERASER) {
                        drawFreehandSegment(lastFreehandPoint, currentPoint);
                        lastFreehandPoint = currentPoint;
                    }
                    repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (currentTool != ToolType.PENCIL && currentTool != ToolType.ERASER) {
                        if (startPoint != null && currentPoint != null) {
                            drawShapeToBuffer(startPoint, e.getPoint(), currentTool, currentColor, currentThickness);
                        }
                    }
                    startPoint = null;
                    currentPoint = null;
                    lastFreehandPoint = null;
                    repaint();
                }
            };

            addMouseListener(mouseHandler);
            addMouseMotionListener(mouseHandler);
        }

        private void initBuffer() {
            if (bufferedImage == null || bufferedImage.getWidth() != getWidth() || bufferedImage.getHeight() != getHeight()) {
                BufferedImage newImage = new BufferedImage(Math.max(1, getWidth()), Math.max(1, getHeight()), BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = newImage.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRect(0, 0, getWidth(), getHeight());

                if (bufferedImage != null) {
                    g2.drawImage(bufferedImage, 0, 0, null);
                }
                g2.dispose();
                bufferedImage = newImage;
                g2dBuffer = bufferedImage.createGraphics();
                g2dBuffer.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            }
        }

        private void drawFreehandSegment(Point p1, Point p2) {
            initBuffer();
            g2dBuffer.setStroke(new BasicStroke(currentThickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            if (currentTool == ToolType.ERASER) {
                g2dBuffer.setColor(Color.WHITE);
            } else {
                g2dBuffer.setColor(currentColor);
            }
            g2dBuffer.drawLine(p1.x, p1.y, p2.x, p2.y);
            repaint();
        }

        private void drawShapeToBuffer(Point p1, Point p2, ToolType tool, Color color, float thickness) {
            initBuffer();
            g2dBuffer.setColor(color);
            g2dBuffer.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            Shape shape = createShape(p1, p2, tool);
            if (shape != null) {
                g2dBuffer.draw(shape);
            }
        }

        private Shape createShape(Point p1, Point p2, ToolType tool) {
            int x = Math.min(p1.x, p2.x);
            int y = Math.min(p1.y, p2.y);
            int width = Math.abs(p1.x - p2.x);
            int height = Math.abs(p1.y - p2.y);

            switch (tool) {
                case LINE:
                    return new Line2D.Double(p1.x, p1.y, p2.x, p2.y);
                case RECTANGLE:
                    return new Rectangle2D.Double(x, y, width, height);
                case SQUARE: {
                    int size = Math.min(width, height);
                    int sqX = p2.x < p1.x ? p1.x - size : p1.x;
                    int sqY = p2.y < p1.y ? p1.y - size : p1.y;
                    return new Rectangle2D.Double(sqX, sqY, size, size);
                }
                case ELLIPSE:
                    return new Ellipse2D.Double(x, y, width, height);
                case CIRCLE: {
                    int diameter = Math.min(width, height);
                    int cX = p2.x < p1.x ? p1.x - diameter : p1.x;
                    int cY = p2.y < p1.y ? p1.y - diameter : p1.y;
                    return new Ellipse2D.Double(cX, cY, diameter, diameter);
                }
                case STAR: {
                    return createStarShape(x + width / 2.0, y + height / 2.0, Math.min(width, height) / 2.0, Math.min(width, height) / 4.0, 5);
                }
                default:
                    return null;
            }
        }

        private Shape createStarShape(double centerX, double centerY, double outerRadius, double innerRadius, int numPoints) {
            Path2D path = new Path2D.Double();
            for (int i = 0; i < 2 * numPoints; i++) {
                double r = (i % 2 == 0) ? outerRadius : innerRadius;
                double angle = i * Math.PI / numPoints - Math.PI / 2;
                double px = centerX + r * Math.cos(angle);
                double py = centerY + r * Math.sin(angle);
                if (i == 0) {
                    path.moveTo(px, py);
                } else {
                    path.lineTo(px, py);
                }
            }
            path.closePath();
            return path;
        }

        public void clearCanvas() {
            if (bufferedImage != null) {
                g2dBuffer.setColor(Color.WHITE);
                g2dBuffer.fillRect(0, 0, getWidth(), getHeight());
                repaint();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            initBuffer();

            g.drawImage(bufferedImage, 0, 0, null);

            if (startPoint != null && currentPoint != null && currentTool != ToolType.PENCIL && currentTool != ToolType.ERASER) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(currentColor);
                g2d.setStroke(new BasicStroke(currentThickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                Shape previewShape = createShape(startPoint, currentPoint, currentTool);
                if (previewShape != null) {
                    g2d.draw(previewShape);
                }
                g2d.dispose();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InternationalPaintApp());
    }
}