import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GameScreen extends JFrame implements ActionListener {
    // CONFIG
    private final double DefaultPipeVelocity = 2;

    private final Image PipeImage;
    private final Image AllBackgroundImage;
    private final double WidthScreen;
    private final double HeightScreen;
    private final Timer Timer;
    private final Timer PipesTimer;
    private final double DeathTop;
    private final double DeathBottom;
    private final Random Random = new Random();
    private NeuralNetwork neuralNetwork;

    private int pipesCount = 0;
    private Map<Integer, java.util.List<Pipe>> pipesMap;
    private boolean gameOver;
    private boolean inGame;

    private final Bird bird;

    public GameScreen() {
        Image backgroundImage = new ImageIcon("src/main/java/assets/background.png").getImage();
        Image BaseImage = new ImageIcon("src/main/java/assets/base.png").getImage();
        PipeImage = new ImageIcon("src/main/java/assets/pipe.png").getImage();
        AllBackgroundImage = createBackgroundImage(backgroundImage, BaseImage);

        WidthScreen = AllBackgroundImage.getWidth(null);
        HeightScreen = AllBackgroundImage.getHeight(null);
        DeathTop = 0;
        DeathBottom = HeightScreen - BaseImage.getHeight(null);

        bird = new Bird(WidthScreen / 4, HeightScreen / 2);
        gameOver = false;
        pipesMap = new HashMap<>();

        Timer = new Timer(16, this);
        PipesTimer = new Timer(2000, e -> createPipes());

        inGame = false;

        Timer.start();
        PipesTimer.start();

        neuralNetwork = new NeuralNetwork(bird, pipesMap);

        setResizable(false);
        setSize((int) WidthScreen, (int) HeightScreen);
        setVisible(true);

        initializeComponents();
    }

    private Image createBackgroundImage(Image backgroundImage, Image baseImage) {
        BufferedImage combinedBackground = new BufferedImage(
                backgroundImage.getWidth(null) * 2,
                backgroundImage.getHeight(null),
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics g = combinedBackground.getGraphics();
        g.drawImage(backgroundImage, 0, 0, null);
        g.drawImage(baseImage, 0, backgroundImage.getHeight(null) - baseImage.getHeight(null), null);
        g.drawImage(backgroundImage, backgroundImage.getWidth(null), 0, null);
        g.drawImage(baseImage, backgroundImage.getWidth(null), backgroundImage.getHeight(null) - baseImage.getHeight(null), null);
        g.dispose();

        return combinedBackground;
    }

    private void initializeComponents() {
        JPanel panel = getJPanel();
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_SPACE:
                        bird.jump();
                        break;
                    case KeyEvent.VK_P:
                        inGame = !inGame;
                        break;
                    case KeyEvent.VK_R:
                        restart();
                        break;
                    case KeyEvent.VK_ESCAPE:
                        System.exit(0);
                        break;
                    default:
                        break;
                }
            }
        });

        setContentPane(panel);
    }

    private JPanel getJPanel() {
        return  new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                super.requestFocus();

                g.drawImage(AllBackgroundImage, 0, 0, this);
                if (inGame) bird.update();
                bird.draw(g);

                for (java.util.List<Pipe> pipes : pipesMap.values()) {
                    for (Pipe pipe : pipes) {
                        if (inGame) pipe.update();

                        pipe.drawPipe(g);
                    }
                }
                neuralNetwork.getNextAction(g);
            }
        };
    }

    public void verifyCollision() {
        if (bird.getY() <= DeathTop || bird.getY() + bird.getHeight() >= DeathBottom) {
            gameOver();
        }

        Map<Integer, java.util.List<Pipe>> possiblesPipes = getPossiblesPipes();

        Rectangle birdRect = new Rectangle((int) bird.getX(), (int) bird.getY(), (int) bird.getWidth(), (int) bird.getHeight());

        for (java.util.List<Pipe> pipes : possiblesPipes.values()) {
            for (Pipe pipe : pipes) {
                Rectangle pipeRect = new Rectangle((int) pipe.getX(), (int) pipe.getY(), (int) pipe.getWidth(), (int) pipe.getHeight());
                if (birdRect.intersects(pipeRect)) {
                    gameOver();
                    return;
                }
            }
        }
    }

    public void gameOver() {
        gameOver = true;
        inGame = false;
    }

    public void restart(){
        gameOver = false;
        inGame = true;
        bird.setPosition(WidthScreen / 4, HeightScreen / 2);
        pipesMap.clear();
        PipesTimer.start();
        Timer.start();
    }

    public void createPipes() {
        if (pipesMap == null) {
            pipesMap = new  HashMap<>();
        }

        double spacing = (bird.getHeight() * 3 + Random.nextDouble(bird.getHeight() * 4));

        pipesMap.put(pipesCount, new ArrayList<>());
        // TOP
        pipesMap.get(pipesCount).add(new Pipe(
                rotateImage(PipeImage),
                WidthScreen,
                -(PipeImage.getHeight(null) - HeightScreen / 2) - spacing / 2,
                PipeImage.getWidth(null),
                PipeImage.getHeight(null),
                DefaultPipeVelocity
        ));

        // BOTTOM
        pipesMap.get(pipesCount).add(new Pipe(
                PipeImage,
                WidthScreen,
                HeightScreen / 2 + spacing / 2,
                PipeImage.getWidth(null),
                PipeImage.getHeight(null),
                DefaultPipeVelocity
        ));

        pipesCount++;
    }

    private Image rotateImage(Image image) {
        int w = image.getWidth(null);
        int h = image.getHeight(null);

        BufferedImage rotated = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotated.createGraphics();

        g2d.rotate(Math.toRadians(180), w / 2.0, h / 2.0);

        g2d.drawImage(image, 0, 0, null);

        g2d.dispose();
        return rotated;
    }

    private Map<Integer, java.util.List<Pipe>> getPossiblesPipes() {
        Map<Integer, java.util.List<Pipe>> result = new HashMap<>();

        for (Map.Entry<Integer, java.util.List<Pipe>> entry : pipesMap.entrySet()) {
            double pipesX = entry.getValue().get(entry.getValue().size() - 1).getX();
            double birdX = bird.getX();

            if (pipesX + PipeImage.getWidth(null) > birdX) {
                result.put(entry.getKey(), entry.getValue());
            } else {
                result.remove(entry.getKey());
            }
        }

        return result;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        bird.flap();
        verifyCollision();
        repaint();
        revalidate();
    }
}
