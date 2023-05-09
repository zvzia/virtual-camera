import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public class Plane {
    public enum Side {FRONT, BACK, COPLANAR};

    private Point3d point;
    private Vector3d normal;

    public Plane(Point3d point, Vector3d normal) {
        this.point = point;
        this.normal = normal;
    }

    public Point3d getPoint() {
        return point;
    }

    public Vector3d getNormal() {
        return normal;
    }

    public Side getSide(Point3d point) {
        double distance = getDistance(point);
        if (distance > 0) {
            return Side.FRONT;
        } else if (distance < 0) {
            return Side.BACK;
        } else {
            return Side.COPLANAR;
        }
    }

    public double getDistance(Point3d point) {
        Vector3d pointVector = new Vector3d(point);
        pointVector.sub(this.point);
        return normal.dot(pointVector);
    }
}
