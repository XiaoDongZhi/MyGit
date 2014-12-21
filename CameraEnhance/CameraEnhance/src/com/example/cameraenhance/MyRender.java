package com.example.cameraenhance;
 
 
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView.Renderer;
 
public class MyRender implements Renderer 
{   
    float[] verteices = new float[]
            {
                0.1f,0.6f,0.0f,
                -0.3f,0.0f,0.0f,
                0.3f,0.1f,0.0f
            };
    int[] colors = new int[]
            {
                65535,0,0,0,
                0,65535,0,0,
                0,0,65535,0
            };
     
    FloatBuffer vBuffer = MemUtil.makeFloatBuffer(verteices);
    IntBuffer cBuffer = MemUtil.makeIntBuffer(colors);
     
    @Override
    public void onDrawFrame(GL10 gl) 
    {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT|GL10.GL_DEPTH_BUFFER_BIT);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glTranslatef(0.0f, 0.0f, -1.0f);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vBuffer);
        gl.glColorPointer(4, GL10.GL_FIXED, 0, cBuffer);
        gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 3);
        gl.glFinish();
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
    }
 
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) 
    {
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        float ratio = (float)width/height;
        gl.glFrustumf(-ratio, ratio, -1, 1, 1, 9);
    }
 
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) 
    {
        gl.glDisable(GL10.GL_DITHER);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
        gl.glClearColor(0, 0, 0, 0);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);
    }
 
}