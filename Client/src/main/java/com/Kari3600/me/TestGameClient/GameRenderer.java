package com.Kari3600.me.TestGameClient;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashSet;
import java.util.concurrent.CompletableFuture;

import javax.swing.JFrame;

import com.Kari3600.me.TestGameCommon.Champions.Champion;
import com.Kari3600.me.TestGameClient.util.Object2D;
import com.Kari3600.me.TestGameClient.util.Object3D;
import com.Kari3600.me.TestGameCommon.util.Vector2;
import com.Kari3600.me.TestGameCommon.util.Vector3;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.GLRunnable;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

public class GameRenderer implements GLEventListener {

    private final GLU glu = new GLU();
    private final TextRenderer textRenderer = new TextRenderer(new Font("SansSerif", Font.BOLD, 12));
    private final JFrame frame;
    private final GLCanvas canvas;
    private GameEngineClient ge = Main.getGameEngine();
    private HashSet<Object3D> objects3D = new HashSet<Object3D>();
    private HashSet<Object2D> objects2D = new HashSet<Object2D>();
    private int cameraDistance = 45000;
    private Vector3 cameraOffset = new Vector3(0,cameraDistance,-cameraDistance/2);
    //private TextureInputStream mapTextureIS = ModelLoader.getTextureIS("map");
    private Texture mapTexture;
    private boolean fullscreen = false;
    private Dimension screenSize = new Dimension(1280,720);
    private Dimension viewportSize;

    public Dimension getWindowSize() {
        return screenSize;
    }
    public Dimension getViewportSize() {
        return viewportSize;
    }

    public JFrame getFrame() {
        return frame;
    }

    public GLCanvas getCanvas() {
        return canvas;
    }

    public TextRenderer getTextRenderer() {
        return textRenderer;
    }

    public void addObject(Object3D object) {
        objects3D.add(object);
        object.loadTexture();
    }

    public void removeObject(Object3D object) {
        object.unloadTexture();
        objects3D.remove(object);
    }

    public void addObject(Object2D object) {
        objects2D.add(object);
    }

    public void removeObject(Object2D object) {
        objects2D.remove(object);
    }

    public CompletableFuture<Vector3> toWorldLocation(Vector2 location) {
        CompletableFuture<Vector3> future = new CompletableFuture<>();

        canvas.invoke(false, new GLRunnable() {
            @Override
            public boolean run(GLAutoDrawable drawable) {
                future.complete(toWorldLocation(drawable, location));
                return true;
            }
        });

        return future;
    }

    public Vector3 toWorldLocation(GLAutoDrawable drawable, Vector2 location) {
        GL2 gl = drawable.getGL().getGL2();

        IntBuffer viewport = IntBuffer.allocate(4);
        FloatBuffer modelview = FloatBuffer.allocate(16);
        FloatBuffer projection = FloatBuffer.allocate(16);
        gl.glGetIntegerv(GL.GL_VIEWPORT, viewport);
        gl.glGetFloatv(GL2.GL_MODELVIEW_MATRIX, modelview);
        gl.glGetFloatv(GL2.GL_PROJECTION_MATRIX, projection);

        int x = (int) (viewport.get(2)*(location.x/canvas.getWidth()));
        int y = (int) (viewport.get(3)*(1-location.y/canvas.getHeight()));

        System.out.println("Real coords: "+location.x+", "+location.y);
        System.out.println("Custom coords: "+x+", "+y);

        FloatBuffer nearPoint = FloatBuffer.allocate(3);
        FloatBuffer farPoint = FloatBuffer.allocate(3);
                
        glu.gluUnProject((float) x, (float) y, 0.0f, modelview, projection, viewport, nearPoint);
        glu.gluUnProject((float) x, (float) y, 1.0f, modelview, projection, viewport, farPoint);

        float[] rayOrigin = {nearPoint.get(0), nearPoint.get(1), nearPoint.get(2)};
        float[] rayDirection = {
            farPoint.get(0) - nearPoint.get(0),
            farPoint.get(1) - nearPoint.get(1),
            farPoint.get(2) - nearPoint.get(2)
        };

        float length = (float) Math.sqrt(rayDirection[0] * rayDirection[0] +
                                         rayDirection[1] * rayDirection[1] +
                                         rayDirection[2] * rayDirection[2]);
        rayDirection[0] /= length;
        rayDirection[1] /= length;
        rayDirection[2] /= length;

        float t = -rayOrigin[1] / rayDirection[1];
        float intersectX = rayOrigin[0] + t * rayDirection[0];
        float intersectZ = rayOrigin[2] + t * rayDirection[2];

        System.out.println("Intersection point on y=0 plane: (" + intersectX + ", 0, " + intersectZ + ")");

        return new Vector3(intersectX, 0 ,intersectZ);
    }

    public Vector2 toScreenLocation(GLAutoDrawable drawable, Vector3 location) {
        GL2 gl = drawable.getGL().getGL2();

        int[] viewport = new int[4];
        double[] modelview = new double[16];
        double[] projection = new double[16];
        double[] winCoords = new double[3];
        
        gl.glGetIntegerv(GL2.GL_VIEWPORT, viewport, 0);
        gl.glGetDoublev(GL2.GL_MODELVIEW_MATRIX, modelview, 0);
        gl.glGetDoublev(GL2.GL_PROJECTION_MATRIX, projection, 0);

        glu.gluProject(location.x, location.y, location.z, modelview, 0, projection, 0, viewport, 0, winCoords, 0);

        return new Vector2(winCoords[0],winCoords[1]);
    }



    @Override
    public void init(GLAutoDrawable drawable) {
        System.out.println("Renderer init");
        final GL2 gl = drawable.getGL().getGL2();
        System.out.println("Viewport size: " + viewportSize.getWidth() + "x" + viewportSize.getHeight());
        glu.gluPerspective( 45.0f, screenSize.getWidth()/screenSize.getHeight(), 1.0, cameraDistance*2 );
        gl.glShadeModel( GL2.GL_SMOOTH );
        gl.glClearColor( 0f, 0f, 0f, 0f );
        gl.glClearDepth( 1.0f );
        gl.glEnable( GL2.GL_DEPTH_TEST );
        gl.glEnable(GL2.GL_TEXTURE_2D);
        gl.glDepthFunc( GL2.GL_LEQUAL );
        gl.glHint( GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST );

        for (Object3D obj : objects3D) {
            obj.loadTexture();
        }
        try {
            mapTexture = TextureIO.newTexture(new File("Client/src/main/resources/map.png"),false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        // Cleanup code here
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT );
        gl.glLoadIdentity();
        Champion character = ge.getPlayerCharacter();
        Vector3 position;
        if (character == null) {
            position = new Vector3(0,0,0);
        } else {
            position = character.getPosition();
        }
        glu.gluLookAt(position.x+cameraOffset.x,position.y+cameraOffset.y,position.z+cameraOffset.z,position.x,position.y,position.z,0,1,0);
        mapTexture.enable(gl);
        mapTexture.bind(gl);
        gl.glColor3f(1.0f, 1.0f, 1.0f);
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2d(0,1);
        gl.glVertex3d(200000.0,0.0,200000.0);
        gl.glTexCoord2d(1,1);
        gl.glVertex3d(-200000.0,0.0,200000.0);
        gl.glTexCoord2d(1,0);
        gl.glVertex3d(-200000.0,0.0,-200000.0);
        gl.glTexCoord2d(0,0);
        gl.glVertex3d(200000.0,0.0,-200000.0);
        gl.glEnd();

        for (Object3D obj : objects3D) {
            gl.glPushMatrix();
            obj.render(drawable);
            gl.glPopMatrix();
        }
        for (Object2D obj : objects2D) {
            obj.preRender(drawable);
        }

        gl.glPushAttrib(GL2.GL_ENABLE_BIT);

        gl.glDisable(GL2.GL_TEXTURE_2D);
        gl.glDisable(GL2.GL_LIGHTING);

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glPushMatrix();
        gl.glLoadIdentity();
        gl.glOrtho(0, (int) viewportSize.getWidth(), 0, (int) viewportSize.getHeight(), -1, 1);  // Set up a 2D orthographic projection

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glPushMatrix();
        gl.glLoadIdentity();

        for (Object2D obj : objects2D) {
            gl.glPushMatrix();
            obj.render(drawable);
            gl.glPopMatrix();
        }

        gl.glPopAttrib();

        gl.glPopMatrix();
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glPopMatrix();
        gl.glMatrixMode(GL2.GL_MODELVIEW);

        gl.glFlush();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        System.out.println(String.format("Resized %d %d %d %d",x,y,width,height));
        GL2 gl = drawable.getGL().getGL2();
      
        if( height <= 0 )
            height = 1;
			
        final float h = ( float ) width / ( float ) height;
        gl.glMatrixMode( GL2.GL_PROJECTION );
        gl.glLoadIdentity();
        //gl.glViewport( 0, 0, width, height );
        gl.glViewport( 0, 0, (int) viewportSize.getWidth(), (int) viewportSize.getHeight() );
		glu.gluPerspective( 45.0f, h, 1.0, cameraDistance*2 );

        gl.glMatrixMode( GL2.GL_MODELVIEW );
        gl.glLoadIdentity();
    }

    public GameRenderer() {
        //System.setProperty("jogl.debug", "true");
         // Create a GLProfile
        GLProfile profile = GLProfile.getDefault();
        GLCapabilities capabilities = new GLCapabilities(profile);
        capabilities.setDepthBits(24);
        capabilities.setDoubleBuffered(true);
        capabilities.setHardwareAccelerated(true);

        // Create a canvas
        canvas = new GLCanvas(capabilities);
        canvas.addGLEventListener(this);
        if (!fullscreen) {
            canvas.setPreferredSize(screenSize);
        }
        canvas.addKeyListener(new KeyHandler(this));
        canvas.addMouseListener(new MouseHandler(this));

        // Create a frame
        frame = new JFrame("Test Game");
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());
        frame.add(canvas, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        if (fullscreen) {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setUndecorated(true);
            screenSize = frame.getSize();
        } else {
            frame.pack();
        }
        frame.setVisible(true);
        AffineTransform t = canvas.getGraphicsConfiguration().getDefaultTransform();
        float sx = (float) t.getScaleX();
        float sy = (float) t.getScaleY();
        viewportSize = new Dimension((int) (canvas.getWidth() * sx),(int) (canvas.getHeight() * sy));

        System.out.println("GLCanvas size: " + canvas.getWidth() + "x" + canvas.getHeight());

        // Start the animator
        FPSAnimator animator = new FPSAnimator(canvas, 60);
        animator.start();
    }
}
