import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

public class Cube {
    ArrayList<Point3d> points;
    ArrayList<Wall>  walls;

    ArrayList<Point2d> points2d;

    public Cube(ArrayList<Point3d> points) {
        this.points = points;
        walls = new ArrayList<>();
        // Dodanie ścian do listy
        addWall(0, 1, 5, 4); // Górna ściana
        addWall(1, 2, 6, 5); // Dolna ściana
        addWall(2, 6, 7, 3); // Ściana boczna
        addWall(3, 7, 4, 0); // Ściana boczna
        addWall(0, 1, 2, 3); // Ściana boczna
        addWall(4, 5, 6, 7); // Ściana boczna

    }

    private void addWall(int a, int b, int c, int d) {
        Point3d p1 = points.get(a);
        Point3d p2 = points.get(b);
        Point3d p3 = points.get(c);
        Point3d p4 = points.get(d);
        walls.add(new Wall(p1, p2, p3, p4));
    }

    public void translate(double x, double y, double z ) {
        for (Point3d point : points) {
            point.setX(point.getX() + x);
            point.setY(point.getY() + y);
            point.setZ(point.getZ() + z);

        }
    }

    public void rotate(double k, char axis) {
        double sin = Math.sin(k);
        double cos = Math.cos(k);

        for (Point3d point : points) {
            double x = point.getX();
            double y = point.getY();
            double z = point.getZ();

            switch (axis) {
                case 'x':
                    point.setY(y * cos - z * sin);
                    point.setZ(z * cos + y * sin);
                    break;
                case 'y':
                    point.setX(x * cos + z * sin);
                    point.setZ(z * cos - x * sin);
                    break;
                case 'z':
                    point.setX(x * cos - y * sin);
                    point.setY(y * cos + x * sin);
                    break;
            }
        }
    }

    public ArrayList<Point3d> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<Point3d> points) {
        this.points = points;
    }

    public ArrayList<Wall> getWalls() {
        return walls;
    }

    public void setWalls(ArrayList<Wall> walls) {
        this.walls = walls;
    }

    public ArrayList<Point2d> getPoints2d() {
        return points2d;
    }

    public void setPoints2d(ArrayList<Point2d> points2d) {
        this.points2d = points2d;
    }


    public double computeSumDist() {
        double result = 0;
        for (Point3d point : points) {
            result = result + Math.sqrt((point.x * point.x) + (point.y * point.y) + (point.z * point.z));
        }
        return result;
    }
}
