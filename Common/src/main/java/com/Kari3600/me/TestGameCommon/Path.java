package com.Kari3600.me.TestGameCommon;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.Kari3600.me.TestGameCommon.util.Vector2;
import com.Kari3600.me.TestGameCommon.util.Vector3;

public class Path {

    class PathTile {
        final int x;
        final int y;
        PathTile parent = null;
        final int destinationX;
        final int destinationY;
        float startDistance = 0;
        float endDistance;
        public void getNeighbours(PriorityQueue<PathTile> available, HashMap<Integer,PathTile> explored) {
            for (int dx=-1;dx<2;dx++) {
                for (int dy=-1;dy<2;dy++) {
                    if (dy==0 && dx==0) continue;
                    if (x+dx<0 || x+dx>=map.getWidth() || y+dy<0 || y+dy>=map.getHeight()) continue;
                    if (map.getRGB(x+dx,y+dy) == 0xFF000000) continue;
                    float newDist = (float) Math.sqrt(Math.abs(dx)+Math.abs(dy));
                    PathTile oldPathTile = explored.get(x+dx+(y+dy)*map.getHeight());
                    if (oldPathTile != null) {
                        if (oldPathTile.startDistance >= this.startDistance+newDist) {
                            //System.out.println("Updating dicovered tile "+(x+dx)+", "+(y+dy));
                            oldPathTile.parent = this;
                            oldPathTile.startDistance = this.startDistance+newDist;
                        }
                        continue;
                    }
                    PathTile newPathTile = new PathTile(x+dx,y+dy,this,newDist);
                    boolean success = false;
                    for (PathTile pTile : available.toArray(new PathTile[]{})) {
                        if (pTile.equals(newPathTile)) {
                            if (pTile.startDistance >= this.startDistance+newDist) {
                                //System.out.println("Updating available tile "+(x+dx)+", "+(y+dy));
                                available.remove(pTile);
                                pTile.parent = this;
                                pTile.startDistance = this.startDistance+newDist;
                                available.add(pTile);
                            }
                            success = true;
                            break;
                        }
                    }
                    if (success) continue;
                    //System.out.println("Adding available tile "+(x+dx)+", "+(y+dy));
                    available.add(newPathTile);
                }
            }
        }
        public float getDistance() {
            return startDistance + endDistance;
        }
        private void calcEndDistance() {
            //Euler distance
            double max = Math.max(Math.abs(x-destinationX),Math.abs(y-destinationY));
            double min = Math.min(Math.abs(x-destinationX),Math.abs(y-destinationY));
            
            if (max == 0) {
                this.endDistance = 0;
                return;
            }
            this.endDistance = (float) (max*Math.sqrt(1+(min/max)*(min/max)));
            

            //Chebyshev-like distance

            //this.endDistance = (float) (max-min+min*Math.sqrt(2));

        }
        @Override
        public boolean equals(Object o) {
            if (o == null) return false;
            if (o.getClass() != this.getClass()) return false;
            PathTile t = (PathTile) o;
            if (t.x == x && t.y == y) return true;
            return false;
        }
        public PathTile(Vector2 v, Vector2 dest) {
            this.x = (int) v.x;
            this.y = (int) v.y;
            this.destinationX = (int) dest.x;
            this.destinationY = (int) dest.y;
            calcEndDistance();
        }
        public PathTile(int x, int y, PathTile parent, float dist) {
            this.x = x;
            this.y = y;
            this.parent = parent;
            this.startDistance = parent.startDistance + dist;
            this.destinationX = parent.destinationX;
            this.destinationY = parent.destinationY;
            calcEndDistance();
        }
    }

    class ImageDisplay extends JPanel {

        private BufferedImage image;
        private float scale;

        public ImageDisplay(BufferedImage image, float scale) {
            this.image = image;
            this.scale = scale;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) {
                // Draw the image on the panel
                g.drawImage(image, 0, 0, (int) (image.getWidth()*scale), (int) (image.getHeight()*scale), this);
            }
        }
    }

    private static final File mapFile = new File("src/main/resources/map.png");
    private static final BufferedImage map = getMap();
    private static final float mapSize = 400000;

    private static BufferedImage getMap() {
        try {
            return ImageIO.read(mapFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Vector2 toMapCoordinated(Vector2 v) {
        return v.multiply(-1.0F/mapSize).add(0.5F, 0.5F).multiply(map.getWidth());
    }

    public static Vector2 fromMapCoordinates(Vector2 v) {
        return v.multiply(1.0F/map.getWidth()).add(-0.5F, -0.5F).multiply(-mapSize);
    }

    private final MovingEntity entity;
    private final Vector3 destination;
    private PathTile tile;

    private void recalculate() {
        final PathTile source = new PathTile(toMapCoordinated(destination.to2D()),toMapCoordinated(entity.getPosition().to2D()));
        boolean success = false;
        PathTile currTile = source;
        PriorityQueue<PathTile> available = new PriorityQueue<PathTile>(Comparator.comparingDouble(t -> t.getDistance()));
        HashMap<Integer,PathTile> explored = new HashMap<Integer,PathTile>();

        int c = 0;

        //System.out.println("Destination: "+source.destinationX+", "+source.destinationY);

        while (!success && c<5000) {
            //System.out.println("Exploring cell "+currTile.x+", "+currTile.y+" (Distance: "+currTile.getDistance()+")");
            currTile.getNeighbours(available,explored);
            currTile = available.poll();
            if (currTile == null) {
                //System.err.println("No current tile.");
                return;
            }
            explored.put(currTile.x+currTile.y*map.getHeight(),currTile);
            if (currTile.x == currTile.destinationX && currTile.y == currTile.destinationY) {
                success = true;
            }
            c++;
        }
        //drawPath(currTile,available,explored);
        tile = currTile.parent;
    }

    public Vector3[] progress(Vector3 position, float distance) {
        if (tile == null) {
            return null;
        }
        while (true) {
            Vector3 destination = Path.fromMapCoordinates(new Vector2(tile.x,tile.y)).to3D();
            Vector3 lVector = destination.substract(position);
            distance -= lVector.length();
            //System.out.println("Distance: "+distance);
            if (distance < 0) {
                return new Vector3[] {position.lerp(destination, distance+(float)lVector.length()),lVector};
            } else {
                position = destination;
                popTile();
                if (tile == null) {
                    return new Vector3[] {destination,lVector};
                }
            }
            
        }
    }

    private void drawPath(PathTile tile, PriorityQueue<PathTile> avaliable, HashMap<Integer,PathTile> explored) {
        JFrame frame = new JFrame("Display BufferedImage");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(map.getWidth(), map.getHeight());

        for (PathTile t : avaliable) {
            map.setRGB(t.x, t.y, 0xFF00FF00); 
        }

        for (int v : explored.keySet()) {
            map.setRGB(v%map.getHeight(), v/map.getHeight(), 0xFF0000FF);
        }

        PathTile testTile = tile;
        while (testTile != null) {
            map.setRGB(testTile.x, testTile.y, 0xFFFF0000);
            testTile = testTile.parent;
        }

        map.setRGB(tile.destinationX, tile.destinationY, 0xFFFF00FF);

        BufferedImage focusedRegion = map.getSubimage(212, 100, 300, 300);

        float scale = 3;

        frame.setSize((int) (focusedRegion.getWidth()*scale), (int) (focusedRegion.getHeight()*scale));
        // Add the image panel to the frame
        ImageDisplay imagePanel = new ImageDisplay(focusedRegion,scale);
        frame.add(imagePanel);

        // Make the frame visible
        frame.setVisible(true);
    }

    public PathTile getTile() {
        return tile;
    }

    public PathTile popTile() {
        tile = tile.parent;
        return tile;
    }

    public Path(MovingEntity entity, Vector3 destination) {
        this.entity = entity;
        this.destination = destination;
        recalculate();
    }
}
