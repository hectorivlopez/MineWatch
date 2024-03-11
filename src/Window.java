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

        JRadioButtonMenuItem yesSlicesOption = new JRadioButtonMenuItem("Yes", true);
        yesSlicesOption.addActionListener(e -> {
            bgPanel.slices = true;
        });
        slicesMenu.add(yesSlicesOption);
        slicesOptions.add(yesSlicesOption);

        JRadioButtonMenuItem noSlicesOption = new JRadioButtonMenuItem("No", true);
        noSlicesOption.addActionListener(e -> {
            bgPanel.slices = false;
        });
        slicesMenu.add(noSlicesOption);
        slicesOptions.add(noSlicesOption);

        yesSlicesOption.setSelected(true);

        menuBar.add(slicesMenu);


        ButtonGroup steve3Options = new ButtonGroup();

        JMenu steve3Menu = new JMenu("Steve 3");

        JRadioButtonMenuItem yesSteve3Option = new JRadioButtonMenuItem("Yes", true);
        yesSteve3Option.addActionListener(e -> {
            bgPanel.steve3 = true;
        });
        steve3Menu.add(yesSteve3Option);
        steve3Options.add(yesSteve3Option);

        JRadioButtonMenuItem noSteve3Option = new JRadioButtonMenuItem("No", true);
        noSteve3Option.addActionListener(e -> {
            bgPanel.steve3 = false;
        });
        steve3Menu.add(noSteve3Option);
        steve3Options.add(noSteve3Option);

        noSteve3Option.setSelected(true);

        menuBar.add(steve3Menu);

        setJMenuBar(menuBar);

    }






}


