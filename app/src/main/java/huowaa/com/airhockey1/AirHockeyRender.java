package huowaa.com.airhockey1;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import huowaa.com.airhockey1.util.ShaderHelper;
import huowaa.com.airhockey1.util.TextResourceReader;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_SRC_ALPHA;
import static android.opengl.GLES20.GL_DST_ALPHA;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;

/**
 * Created by huowaa on 7/23/15.
 */
public class AirHockeyRender implements GLSurfaceView.Renderer {
    private static final String TAG = AirHockeyRender.class.getName();

    private static final String U_COLOR = "u_Color"; // 传递到Shader中的color变量名
    private static final String A_POSITION = "a_Position"; // 传递到Shader中的position变量名
    private static final int POSITION_COMPONENT_COUNT = 2; // 在vertex数组中两个float表示一个position
    private static final int BYTES_PER_FLOAT = 4;
    private final FloatBuffer vertexData; // 在Native层存放顶点数据的Buffer
    private final Context context;
    private int program; // Shader编译之后，java层可以访问的program对象
    private int uColorLocation; // Program中的color变量id, uniform
    private int aPositionLocation; // Program中的position变量id, attribute

    public AirHockeyRender() {
        context = null;
        vertexData = null;
    }

    public AirHockeyRender(Context context) {
        this.context = context;

        // 顶点坐标数组
        float[] tableVerticesWithTriangles = {
                // Triangle 1
                -0.5f, -0.5f,
                0.5f,  0.5f,
                -0.5f,  0.5f,

                // Triangle 2
                -0.5f, -0.5f,
                0.5f, -0.5f,
                0.5f,  0.5f,

                // Line 1
                -0.5f, 0f,
                0.5f, 0f,

                // Mallets
                0f, -0.25f,
                0f,  0.25f
        };

        // 顶点坐标数组在Native层的内存空间
        vertexData = ByteBuffer.allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT).
                order(ByteOrder.nativeOrder()).asFloatBuffer();

        vertexData.put(tableVerticesWithTriangles);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        Log.i(TAG, "onSurfaceCreated");

        // Set the background color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        // 载入shader
        String vertexShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.simple_vertex_shader);
        String fragmentShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.simple_fragment_shader);

        // 编译并链接成Program
        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);
        program = ShaderHelper.linkProgram(vertexShader, fragmentShader);

        // Validate and use program
        boolean validateStatus = ShaderHelper.validateProgram(program);
        Log.i(TAG, "Validate program status = " + validateStatus);
        glUseProgram(program);

        uColorLocation = glGetUniformLocation(program, U_COLOR);
        aPositionLocation = glGetAttribLocation(program, A_POSITION);

        // 绑定数据
        vertexData.position(0); // 先定位到定点0
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, 0, vertexData); // 遍历顶点数组设置
        glEnableVertexAttribArray(aPositionLocation);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        Log.i(TAG, "onSurfaceChanged");

        // 设置OpenGL ViewPort填满整个surface
        glViewport(0,0,width,height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        //Log.i(TAG, "onDrawFrame");

        // Clear render surface
        glClear(GL_COLOR_BUFFER_BIT);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_DST_ALPHA);

        // Draw the table
        glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 0.2f);
        glDrawArrays(GL_TRIANGLES, 0, 6); // POSITION_COMPONENT_COUNT == 2，因此会两个两个的跳

        // Draw the center dividing line
        glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        glDrawArrays(GL_LINES, 6, 2);

        // Draw the first mallet blue
        glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
        glDrawArrays(GL_POINTS, 8, 1);

        // Draw the second mallet
        glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        glDrawArrays(GL_POINTS, 9, 1);
    }
}
