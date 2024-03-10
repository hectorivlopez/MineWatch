import javax.swing.*;

public class Window extends JFrame {
    public int titleBarHeight;
    public int width;
    public int height;
    public boolean isMacOS;
    public DrawPanel bgPanel;

    public Window() {
        this.isMacOS = System.getProperty("os.name").toLowerCase().contains("mac");
        if (isMacOS) {
            final JRootPane rootPane = this.getRootPane();
            rootPane.putClientProperty("apple.awt.fullWindowContent", true);
            rootPane.putClientProperty("apple.awt.transparentTitleBar", true);
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            ;
        }

        initComponents();

        setVisible(true);
    }

    private void initComponents() {
        titleBarHeight = 28;
        width = 760;
        height = width;

        setSize(width, height);
        setResizable(false);
        setLocationRelativeTo(null);

        bgPanel = new DrawPanel(width, height);
        getContentPane().add(bgPanel);



        JMenuBar menuBar = new JMenuBar();
        ButtonGroup slicesOptions = new ButtonGroup();

        JMenu slicesMenu = new JMenu("Slices");

        JRadioButtonMenuItem yesOption = new JRadioButtonMenuItem("Yes", true);
        yesOption.addActionListener(e -> {
            bgPanel.slices = true;
        });
        slicesMenu.add(yesOption);
        slicesOptions.add(yesOption);

        JRadioButtonMenuItem noOption = new JRadioButtonMenuItem("No", true);
        noOption.addActionListener(e -> {
            bgPanel.slices = false;
        });
        slicesMenu.add(noOption);
        slicesOptions.add(noOption);

        menuBar.add(slicesMenu);

        setJMenuBar(menuBar);

    }






}


