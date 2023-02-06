import java.util.*;

public class Main {
    private static final double EPSILSON = 0.001;

    public static class Point {
        double x;
        double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        // copy constructor
        public Point(Point other) {
            this.x = other.x;
            this.y = other.y;
        }

        public boolean equals(Point other) {
            return nearlyEqual(this.x, other.x) && nearlyEqual(this.y, other.y);
        }

        @Override public String toString() {
            return round(x) + " " + round(y) ;
        }
    }

    public static boolean nearlyEqual(double a, double b) {
        return Math.abs(a - b) < EPSILSON;
    }

    public static double round(double a) {
        return Math.round(a*10)/10.0;
    }

    public static boolean listsHaveChanged(List<Point> a, List<Point> b) {
        if (a.size() != b.size()) return true;
        for (int i = 0; i < a.size(); i++) {
            if (!a.get(i).equals(b.get(i))) return true;
        }
        return false;
    }


    public static Point getCenter(List<Point> stops) {
        double x = 0;
        double y = 0;
        for (int i = 0; i < stops.size(); i++) {
            x += stops.get(i).x;
            y += stops.get(i).y;
        }
        x /= stops.size();
        y /= stops.size();
        Point point = new Point(x, y);
        System.out.println("Center = " + point );
        return point;
    }

    public static double getDistance(Point a, Point b) {
        return Math.sqrt(Math.pow(a.x - b.x,2) + Math.pow(a.y - b.y,2));
    }

    public static int closestStopIndex(Point lmk, List<Point> stops) {
        int closestIndex = 0;
        double distance = Double.MAX_VALUE;
        double xDistance = Double.MAX_VALUE;
        double yDistance = Double.MAX_VALUE;

        for (int i = 0; i < stops.size(); i++) {
            double tempDistance = getDistance(lmk, stops.get(i));
            double tempXdistance = Math.abs(lmk.x - stops.get(i).x);
            double tempYdistance = Math.abs(lmk.y - stops.get(i).y);
            if (tempDistance < distance) {
                closestIndex = i;
                distance = tempDistance;
                xDistance = Math.abs(lmk.x - stops.get(i).x);
                yDistance = Math.abs(lmk.y - stops.get(i).y);
            }
            if (tempDistance == distance) {
                if (tempXdistance < xDistance) {
                    closestIndex = i;
                    distance = tempDistance;
                    xDistance = Math.abs(lmk.x - stops.get(i).x);
                    yDistance = Math.abs(lmk.y - stops.get(i).y);
                }
                if (tempXdistance == xDistance) {
                    if (tempYdistance < yDistance) {
                        closestIndex = i;
                        distance = tempDistance;
                        xDistance = Math.abs(lmk.x - stops.get(i).x);
                        yDistance = Math.abs(lmk.y - stops.get(i).y);
                    }
                }
            }
        }
        return closestIndex;
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int trials = Integer.parseInt(scan.nextLine());
        for (int i = 0; i < trials; i++) {
            String line = scan.nextLine();
            String[] tokens = line.split(" ");
            int lm = Integer.parseInt(tokens[0]);
            int st = Integer.parseInt(tokens[1]);
            ArrayList<Point> landmarks = new ArrayList<>();
            ArrayList<Point> busStops = new ArrayList<>();
            for (int j = 0; j < lm; j++) {
                line = scan.nextLine();
                tokens = line.split(" ");
                landmarks.add( new Point(Double.parseDouble(tokens[0]), Double.parseDouble(tokens[1])) );
            }
            for (int j = 0; j < st; j++) {
                line = scan.nextLine();
                tokens = line.split(" ");
                busStops.add ( new Point(Double.parseDouble(tokens[0]), Double.parseDouble(tokens[1])) );
            }
            System.out.println("Trial #" + i);
            System.out.println("Landmarks = " + landmarks);
            System.out.println("Bus Stops = " + busStops);
            boolean hasChanged = true;
            while (hasChanged) {
                hasChanged = false;
                Map<Point, List<Point>> clusters = new HashMap<>();
                // put the bus stops into the cluster Map
                for (int j = 0; j < busStops.size(); j++) {
                    clusters.put(busStops.get(j), new ArrayList<Point>());
                }
                // add each landmark to the closest bus stop
                for (int j = 0; j < landmarks.size(); j++) {
                    int index = closestStopIndex(landmarks.get(j), busStops);
                    clusters.get(busStops.get(index)).add(landmarks.get(j));
                }

                // calculate the new centroids and see if any have moved

                if (listsHaveChanged(busStops, newBusStops)) {
                    hasChanged = true;
                }
            }
            // if we get here, the clusters have not changed
            for(Point stop : busStops) {
                double x = round(stop.x);
                double y = round(stop.y);
                System.out.println(String.format("%.1f %.1f", x, y));
            }

        }
    }
}


