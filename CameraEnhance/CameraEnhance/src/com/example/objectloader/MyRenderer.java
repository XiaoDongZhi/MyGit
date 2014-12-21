package com.example.objectloader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.os.Debug;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class MyRenderer extends GLSurfaceView implements Renderer
{

	/** Triangle instance */
	private OBJParser parser;
	private TDModel model;

	/* Rotation values */
	private float xrot; // X Rotation
	private float yrot; // Y Rotation

	/* Rotation speed values */

	private float xspeed; // X Rotation Speed ( NEW )
	private float yspeed; // Y Rotation Speed ( NEW )

	private float z = 50.0f;

	private float oldX;
	private float oldY;
	private final float TOUCH_SCALE = 0.4f; // Proved to be good for normal
											// rotation ( NEW )

	private float[] lightAmbient = { 1.0f, 1.0f, 1.0f, 1.0f };
	private float[] lightDiffuse = { 1.0f, 1.0f, 1.0f, 1.0f };
	private float[] lightPosition = { 0.0f, -3.0f, 2.0f, 1.0f };
	private FloatBuffer lightAmbientBuffer;
	private FloatBuffer lightDiffuseBuffer;
	private FloatBuffer lightPositionBuffer;

	private static class ConfigChooser implements
			GLSurfaceView.EGLConfigChooser
	{
		public ConfigChooser(int r, int g, int b, int a, int depth, int stencil)
		{
			mRedSize = r;
			mGreenSize = g;
			mBlueSize = b;
			mAlphaSize = a;
			mDepthSize = depth;
			mStencilSize = stencil;
		}

		private EGLConfig getMatchingConfig(EGL10 egl, EGLDisplay display,
				int[] configAttribs)
		{
			// Get the number of minimally matching EGL configurations
			int[] num_config = new int[1];
			egl.eglChooseConfig(display, configAttribs, null, 0, num_config);

			int numConfigs = num_config[0];
			if (numConfigs <= 0)
				throw new IllegalArgumentException("No matching EGL configs");

			// Allocate then read the array of minimally matching EGL configs
			EGLConfig[] configs = new EGLConfig[numConfigs];
			egl.eglChooseConfig(display, configAttribs, configs, numConfigs,
					num_config);

			// Now return the "best" one
			return chooseConfig(egl, display, configs);
		}

		public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display)
		{
			// This EGL config specification is used to specify 2.0
			// rendering. We use a minimum size of 4 bits for
			// red/green/blue, but will perform actual matching in
			// chooseConfig() below.
			final int EGL_OPENGL_ES2_BIT = 0x0004;
			final int[] s_configAttribs_gl20 = { EGL10.EGL_RED_SIZE, 4,
					EGL10.EGL_GREEN_SIZE, 4, EGL10.EGL_BLUE_SIZE, 4,
					EGL10.EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT,
					EGL10.EGL_NONE };

			return getMatchingConfig(egl, display, s_configAttribs_gl20);
		}

		public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display,
				EGLConfig[] configs)
		{
			for (EGLConfig config : configs)
			{
				int d = findConfigAttrib(egl, display, config,
						EGL10.EGL_DEPTH_SIZE, 0);
				int s = findConfigAttrib(egl, display, config,
						EGL10.EGL_STENCIL_SIZE, 0);

				// We need at least mDepthSize and mStencilSize bits
				if (d < mDepthSize || s < mStencilSize)
					continue;

				// We want an *exact* match for red/green/blue/alpha
				int r = findConfigAttrib(egl, display, config,
						EGL10.EGL_RED_SIZE, 0);
				int g = findConfigAttrib(egl, display, config,
						EGL10.EGL_GREEN_SIZE, 0);
				int b = findConfigAttrib(egl, display, config,
						EGL10.EGL_BLUE_SIZE, 0);
				int a = findConfigAttrib(egl, display, config,
						EGL10.EGL_ALPHA_SIZE, 0);

				if (r == mRedSize && g == mGreenSize && b == mBlueSize
						&& a == mAlphaSize)
					return config;
			}

			return null;
		}

		private int findConfigAttrib(EGL10 egl, EGLDisplay display,
				EGLConfig config, int attribute, int defaultValue)
		{

			if (egl.eglGetConfigAttrib(display, config, attribute, mValue))
				return mValue[0];

			return defaultValue;
		}

		// Subclasses can adjust these values:
		protected int mRedSize;
		protected int mGreenSize;
		protected int mBlueSize;
		protected int mAlphaSize;
		protected int mDepthSize;
		protected int mStencilSize;
		private int[] mValue = new int[1];
	}

	public MyRenderer(Context ctx)
	{
		super(ctx);

		parser = new OBJParser(ctx);
		model = parser.parseOBJ("/sdcard/cube.obj");
		Debug.stopMethodTracing();
		
		this.requestFocus();
		this.setFocusableInTouchMode(true);
		this.setEGLConfigChooser(new ConfigChooser(8, 8, 8, 8, 16, 0));
		this.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		this.setZOrderOnTop(true);
		this.setRenderer(this);
        
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(lightAmbient.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		lightAmbientBuffer = byteBuf.asFloatBuffer();
		lightAmbientBuffer.put(lightAmbient);
		lightAmbientBuffer.position(0);

		byteBuf = ByteBuffer.allocateDirect(lightDiffuse.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		lightDiffuseBuffer = byteBuf.asFloatBuffer();
		lightDiffuseBuffer.put(lightDiffuse);
		lightDiffuseBuffer.position(0);

		byteBuf = ByteBuffer.allocateDirect(lightPosition.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		lightPositionBuffer = byteBuf.asFloatBuffer();
		lightPositionBuffer.put(lightPosition);
		lightPositionBuffer.position(0);
	}

	/**
	 * The Surface is created/init()
	 */
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, lightAmbientBuffer);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, lightDiffuseBuffer);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, lightPositionBuffer);
		gl.glEnable(GL10.GL_LIGHT0);

		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
		gl.glClearDepthf(1.0f);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);

		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
	}

	/**
	 * Here we do our drawing
	 */
	public void onDrawFrame(GL10 gl)
	{
		// Clear Screen And Depth Buffer
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		gl.glEnable(GL10.GL_LIGHTING);
		gl.glTranslatef(-25.0f, 10.0f, -z); // Move down 1.2 Unit And Into The
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
	    gl.glEnableClientState(GL10.GL_COLOR_ARRAY);// Screen 6.0
		gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f); // X
		gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f); // Y
		model.draw(gl); // Draw the square
		gl.glLoadIdentity();

		xrot += xspeed;
		yrot += yspeed;

	}

	/**
	 * If the surface changes, reset the view
	 */
	public void onSurfaceChanged(GL10 gl, int width, int height)
	{
		if (height == 0)
		{ // Prevent A Divide By Zero By
			height = 1; // Making Height Equal One
		}

		gl.glViewport(0, 0, width, height); // Reset The Current Viewport
		gl.glMatrixMode(GL10.GL_PROJECTION); // Select The Projection Matrix
		gl.glLoadIdentity(); // Reset The Projection Matrix

		// Calculate The Aspect Ratio Of The Window
		GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f,
				500.0f);

		gl.glMatrixMode(GL10.GL_MODELVIEW); // Select The Modelview Matrix
		gl.glLoadIdentity(); // Reset The Modelview Matrix
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		//
		float x = event.getX();
		float y = event.getY();

		// If a touch is moved on the screen
		if (event.getAction() == MotionEvent.ACTION_MOVE)
		{
			// Calculate the change
			float dx = x - oldX;
			float dy = y - oldY;
			// Define an upper area of 10% on the screen
			int upperArea = this.getHeight() / 10;

			// Zoom in/out if the touch move has been made in the upper
			if (y < upperArea)
			{
//				z -= dx * TOUCH_SCALE / 2;

				// Rotate around the axis otherwise
			} else
			{
				xrot += dy * TOUCH_SCALE;
				yrot += dx * TOUCH_SCALE;
			}

			// A press on the screen
		} else if (event.getAction() == MotionEvent.ACTION_UP)
		{

		}

		// Remember the values
		oldX = x;
		oldY = y;

		// We handled the event
		return true;
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event)
	{
		//
		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
		{

		} else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)
		{

		} else if (keyCode == KeyEvent.KEYCODE_DPAD_UP)
		{
			z -= 3;

		} else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
		{
			z += 3;

		} else if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER)
		{

		}

		// We handled the event
		return true;
	}
}