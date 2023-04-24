import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.List;
import javax.swing.*;

public class Main extends JPanel implements KeyListener {
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 800;
    private static int CAMERA_DISTANCE = 500;

    private ArrayList<Cube> cubes;

    public Main(ArrayList<Cube> cubes) {
        this.cubes = cubes;
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        addKeyListener(this);
        setFocusable(true);
    }

    @Override
    public void paintComponent(Graphics g) {


        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(WIDTH / 2, HEIGHT / 2);

        Collections.sort(cubes, new Comparator<Cube>() {
            @Override
            public int compare(Cube o1, Cube o2) {
                return Double.compare(o2.computeSumDist(), o1.computeSumDist());
            }
        });


        for (Cube cube : cubes) {
            ArrayList<Wall> cubeWalls = (ArrayList<Wall>) cube.getWalls();

            Collections.sort(cubeWalls, new Comparator<Wall>() {
                @Override
                public int compare(Wall o1, Wall o2) {
                    return Double.compare(o2.computeSumDist(), o1.computeSumDist());
                }
            });


            for (Wall wall : cubeWalls) {

                List<Point2d> wallPoints2d = new ArrayList<>();
                List<Point3d> wallPoints3d = wall.getPoints();

                for (Point3d point : wallPoints3d) {
                    double px = point.getX();
                    double py = point.getY();
                    double pz = point.getZ();
                    double ppx = px * CAMERA_DISTANCE / pz;
                    double ppy = py * CAMERA_DISTANCE / pz;
                    wallPoints2d.add(new Point2d(ppx, ppy));
                }

                int[] xPoints = new int[4];
                int[] yPoints = new int[4];
                boolean visible = true;
                for (int i = 0; i < wallPoints2d.size(); i++) {
                    xPoints[i] = (int) wallPoints2d.get(i).x;
                    yPoints[i] = (int) wallPoints2d.get(i).y;
                    if (wallPoints3d.get(i).z <= 0) {
                        visible = false;
                    }
                }

                if (visible) {
                    g2d.setColor(Color.GRAY);
                    g2d.fillPolygon(xPoints, yPoints, 4);
                }

                Point2d startPoint = wallPoints2d.get(0);
                double pzStart = wallPoints3d.get(0).z;
                double pzEnd;

                g2d.setColor(Color.BLACK);

                for (int i = 1; i < wallPoints3d.size(); i++) {
                    Point2d endPoint = wallPoints2d.get(i);
                    pzEnd = wallPoints3d.get(i).z;

                    if (pzStart > 0 && pzEnd > 0) {
                        g2d.drawLine((int) startPoint.getX(), (int) startPoint.getY(),
                                (int) endPoint.getX(), (int) endPoint.getY());
                    }
                    startPoint = endPoint;
                    pzStart = pzEnd;

                }

                Point2d endPoint = wallPoints2d.get(0);
                pzEnd = wallPoints3d.get(0).z;
                if (pzStart > 0 && pzEnd > 0) {
                    g2d.drawLine((int) startPoint.getX(), (int) startPoint.getY(),
                            (int) endPoint.getX(), (int) endPoint.getY());
                }
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        for (Cube cube : cubes) {
            switch (keyCode) {
                case KeyEvent.VK_UP:
                    cube.translate(0, 0, -10);
                    break;
                case KeyEvent.VK_DOWN:
                    cube.translate(0, 0, 10);
                    break;
                case KeyEvent.VK_LEFT:
                    cube.translate(10, 0, 0);
                    break;
                case KeyEvent.VK_RIGHT:
                    cube.translate(-10, 0, 0);
                    break;
                case KeyEvent.VK_PAGE_UP:
                    cube.translate(0, 10, 0);
                    break;
                case KeyEvent.VK_PAGE_DOWN:
                    cube.translate(0, -10, 0);
                    break;
                case KeyEvent.VK_W:
                    cube.rotate(-0.1, 'x');
                    break;
                case KeyEvent.VK_S:
                    cube.rotate(0.1, 'x');
                    break;
                case KeyEvent.VK_D:
                    cube.rotate(-0.1, 'y');
                    break;
                case KeyEvent.VK_A:
                    cube.rotate(0.1, 'y');
                    break;
                case KeyEvent.VK_PERIOD:
                    cube.rotate(-0.1, 'z');
                    break;
                case KeyEvent.VK_COMMA:
                    cube.rotate(0.1, 'z');
                    break;
            }
        }
        switch (keyCode) {
            case KeyEvent.VK_EQUALS:
                zoom(10);
                break;
            case KeyEvent.VK_MINUS:
                zoom(-10);
                break;
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public void zoom(double z) {
        CAMERA_DISTANCE += z;
    }

    public static void main(String[] args) {
        ArrayList<Cube> cubes = getDefaultCubes();
        JFrame frame = new JFrame("Cube Renderer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Main renderer = new Main(cubes);
        frame.getContentPane().add(renderer);


        frame.pack();
        frame.setVisible(true);
    }

    public static ArrayList<Cube> getDefaultCubes() {
        ArrayList<Cube> cubes = new ArrayList<>();

        ArrayList<Point3d> points = new ArrayList<>();
        points.add(new Point3d(50, -50, 100));
        points.add(new Point3d(50, -50, 150));
        points.add(new Point3d(100, -50, 150));
        points.add(new Point3d(100, -50, 100));
        points.add(new Point3d(50, 50, 100));
        points.add(new Point3d(50, 50, 150));
        points.add(new Point3d(100, 50, 150));
        points.add(new Point3d(100, 50, 100));
        cubes.add(new Cube(points));

        ArrayList<Point3d> points2 = new ArrayList<>();
        points2.add(new Point3d(-50, -50, 100));
        points2.add(new Point3d(-50, -50, 150));
        points2.add(new Point3d(-100, -50, 150));
        points2.add(new Point3d(-100, -50, 100));
        points2.add(new Point3d(-50, 50, 100));
        points2.add(new Point3d(-50, 50, 150));
        points2.add(new Point3d(-100, 50, 150));
        points2.add(new Point3d(-100, 50, 100));
        cubes.add(new Cube(points2));

        ArrayList<Point3d> points3 = new ArrayList<>();
        points3.add(new Point3d(-50, -50, 200));
        points3.add(new Point3d(-50, -50, 250));
        points3.add(new Point3d(-100, -50, 250));
        points3.add(new Point3d(-100, -50, 200));
        points3.add(new Point3d(-50, 50, 200));
        points3.add(new Point3d(-50, 50, 250));
        points3.add(new Point3d(-100, 50, 250));
        points3.add(new Point3d(-100, 50, 200));
        cubes.add(new Cube(points3));

        ArrayList<Point3d> points4 = new ArrayList<>();
        points4.add(new Point3d(50, -50, 200));
        points4.add(new Point3d(50, -50, 250));
        points4.add(new Point3d(100, -50, 250));
        points4.add(new Point3d(100, -50, 200));
        points4.add(new Point3d(50, 50, 200));
        points4.add(new Point3d(50, 50, 250));
        points4.add(new Point3d(100, 50, 250));
        points4.add(new Point3d(100, 50, 200));
        cubes.add(new Cube(points4));

        return cubes;
    }
}