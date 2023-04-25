import java.util.ArrayList;
import java.util.List;

public class Wall {
    ArrayList<Point3d> points;

    public Wall(Point3d p1, Point3d p2, Point3d p3, Point3d p4) {
        points = new ArrayList<>();
        this.points.add(p1);
        this.points.add(p2);
        this.points.add(p3);
        this.points.add(p4);
    }

    public ArrayList<Point3d> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<Point3d> points) {
        this.points = points;
    }

    public double computeSumDist() {
        double result = 0;
        for (Point3d point : points) {
            result = result + Math.sqrt((point.x * point.x) + (point.y * point.y) + (point.z * point.z));
        }
        return result;
    }
}
