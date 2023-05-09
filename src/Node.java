import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import java.util.ArrayList;
import java.util.List;

public class Node {
    private Plane plane;
    private Node front;
    private Node back;
    private List<Wall> walls;

    public Node(List<Wall> walls) {
        this.walls = walls;
        buildBSP();
    }

    private void buildBSP() {
        if (walls.isEmpty()) {
            return;
        }

        Wall firstWall = walls.get(0);
        Vector3d normal = computeNormal(firstWall.getPoints().get(0), firstWall.getPoints().get(1), firstWall.getPoints().get(2));
        Point3d point = firstWall.getPoints().get(0);
        plane = new Plane(point, normal);

        List<Wall> frontWalls = new ArrayList<>();
        List<Wall> backWalls = new ArrayList<>();

        for (int i = 1; i < walls.size(); i++) {
            Wall wall = walls.get(i);
            Plane.Side side = plane.getSide(wall.getPoints().get(0));
            switch (side) {
                case FRONT:
                    frontWalls.add(wall);
                    break;
                case BACK:
                    backWalls.add(wall);
                    break;
                case COPLANAR:
                    if (plane.getSide(wall.getPoints().get(1)) == Plane.Side.FRONT) {
                        frontWalls.add(wall);
                    } else {
                        backWalls.add(wall);
                    }
                    break;
            }
        }

        if (!frontWalls.isEmpty()) {
            front = new Node(frontWalls);
        }
        if (!backWalls.isEmpty()) {
            back = new Node(backWalls);
        }
    }

    private Vector3d computeNormal(Point3d p1, Point3d p2, Point3d p3) {
        Vector3d v1 = new Vector3d();
        Vector3d v2 = new Vector3d();
        v1.sub(p2, p1);
        v2.sub(p3, p1);
        Vector3d normal = new Vector3d();
        normal.cross(v1, v2);
        normal.normalize();
        return normal;
    }

    public Plane getPlane() {
        return plane;
    }

    public Node getFront() {
        return front;
    }

    public Node getBack() {
        return back;
    }

    public List<Wall> getWalls() {
        return walls;
    }
}
