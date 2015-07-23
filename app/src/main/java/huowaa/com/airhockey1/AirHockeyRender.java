package huowaa.com.airhockey1;

import android.content.Context;
import android.opengl.GLSurfaceView;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by huowaa on 7/23/15.
 */
public class AirHockeyRender implements GLSurfaceView.Renderer {
    private static final String U_COLOR = "u_color";
    private static final String A_POSITION = "a_position";
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int BYTES_PER_FLOAT = 4;
    private final FloatBuffer vertexData;
    private final Context context;
    private int program;
    private int uColorLocation;
    private int aPositionLocation;






    public AirHockeyRender(AirHockeyActivity airHockeyActivity) {

    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i2) {

    }

    @Override
    public void onDrawFrame(GL10 gl10) {

    }
}
