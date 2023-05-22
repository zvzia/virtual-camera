import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

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

        ArrayList<Wall> allWalls = new ArrayList<>();
        for (Cube cube : cubes) {
            allWalls.addAll(cube.getWalls());
        }

        ArrayList<Wall> sortedWalls = customSort(allWalls);
        for (Wall wall : sortedWalls) {

            List<Point2d> wallPoints2d = new ArrayList<>();
            List<Point3d> wallPoints3d = wall.getPoints();

            for (Point3d point : wallPoints3d) {
                double px = point.x;
                double py = point.y;
                double pz = point.z;
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


    public static int intersection(Point3d p, Point3d a, Point3d b, Point3d c) {
        // obliczamy wektor normalny do płaszczyzny
        Vector3d ab = new Vector3d();
        ab.sub(b, a);
        Vector3d ac = new Vector3d();
        ac.sub(c, a);
        Vector3d n = new Vector3d();
        n.cross(ab, ac);

        // obliczamy wartość wyrażenia Ax + By + Cz + D dla punktów P i (0, 0, 0)
        double A = n.x;
        double B = n.y;
        double C = n.z;
        double D = -n.dot(new Vector3d(a));
        double distP = A * p.x + B * p.y + C * p.z + D;
        double distO = D;

        // sprawdzamy, czy prosta przecina płaszczyznę
        if (distP * distO < -1) {
            return 1;
        }

        // sprawdzamy, czy prosta zaczyna się lub kończy na płaszczyźnie
        if (distP < 1 && distP > -1) {
            return 0;
        }

        // jeśli prosta nie przecina płaszczyzny, ani nie zaczyna się ani nie kończy na płaszczyźnie, to nie ma z nią punktu wspólnego
        return -1;
    }

    public static ArrayList<Wall> customSort(ArrayList<Wall> walls) {
        ArrayList<Wall> sorted = sortHelper(walls);
        Collections.reverse(sorted);
        return sorted;
    }

    private static ArrayList<Wall> sortHelper(ArrayList<Wall> walls) {

        Wall pivot;
        if (walls.size() > 1) {
            pivot = walls.get(walls.size() / 2);
            walls.remove(walls.size() / 2);
        } else {
            return walls;
        }

        ArrayList<Wall> smaller = new ArrayList<>();
        ArrayList<Wall> bigger = new ArrayList<>();
        ArrayList<Wall> same = new ArrayList<>();

        same.add(pivot);
        for (Wall wall : walls) {
            int res = customCompare(pivot, wall);
            if (res == -1) {
                smaller.add(wall);
            } else if (res == 1) {
                bigger.add(wall);
            } else if (res == 0) {
                same.add(wall);
            }
        }

        smaller = sortHelper(smaller);
        bigger = sortHelper(bigger);

        ArrayList<Wall> sorted = new ArrayList<>();
        sorted.addAll(smaller);
        sorted.addAll(same);
        sorted.addAll(bigger);
        return sorted;
    }

    public static int customCompare(Wall o1, Wall o2) {
        int intersect = 0;
        int noIntersect = 0;
        int onPlane = 0;

        for (Point3d point : o2.getPoints()) {
            if (intersection(point, o1.points.get(0), o1.points.get(1), o1.points.get(2)) == 1) {
                intersect++;
            }

            if (intersection(point, o1.points.get(0), o1.points.get(1), o1.points.get(2)) == -1) {
                noIntersect++;
            }

            if (intersection(point, o1.points.get(0), o1.points.get(1), o1.points.get(2)) == 0) {
                onPlane++;
            }
        }

        if (intersect == 4) {
            return 1;
        } else if (noIntersect == 4) {
            return -1;
        } else if (onPlane == 4) {
            return 0;
        } else if (intersect == 2 && onPlane == 2) {
            return 1;
        } else if (noIntersect == 2 && onPlane == 2) {
            return -1;
        } else {
            System.err.println("blad, przecina: " + intersect + ", nie przecina: " + noIntersect + ", lezy na:" + onPlane);
            return 2;
        }


    }
}
