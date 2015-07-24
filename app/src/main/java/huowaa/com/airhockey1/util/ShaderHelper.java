package huowaa.com.airhockey1.util;

/**
 * Created by huowaa on 7/23/15.
 */

import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.GL_VALIDATE_STATUS;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteProgram;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glValidateProgram;


public class ShaderHelper {

    public static int compileVertexShader(String shaderCode) {
        return compileShader(GL_VERTEX_SHADER, shaderCode);
    }

    public static int compileFragmentShader(String shaderCode) {
        return compileShader(GL_FRAGMENT_SHADER, shaderCode);
    }

    private static int compileShader(int type, String shaderCode) {

        // 创建一个shader对象
        final int shaderObjectId = glCreateShader(type);

        if (shaderObjectId == 0) {
            return 0;
        }

        // 传入Shader source
        glShaderSource(shaderObjectId, shaderCode);

        // Compile
        glCompileShader(shaderObjectId);

        // Get compilation status
        final int[] compileStatus = new int[1];
        glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0);

        // Verify compilation status
        if (compileStatus[0] == 0) {
            // If failed, delete the shader object
            glDeleteShader(shaderObjectId);
        }

        return shaderObjectId;
    }

    public static int linkProgram(int vertexShaderId, int fragmentShaderId) {
        // Create a new program object
        final int programObjectId = glCreateProgram();
        if (programObjectId == 0) {
            return 0;
        }

        // Attach shader to the program
        glAttachShader(programObjectId, vertexShaderId);
        glAttachShader(programObjectId, fragmentShaderId);
        glLinkProgram(programObjectId);

        // Get and verify the link status
        final int[] linkStatus = new int[1];
        glGetProgramiv(programObjectId, GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] == 0) {
            // If failed, delete
            glDeleteProgram(programObjectId);
        }

        return programObjectId;
    }

    public static boolean validateProgram(int programId) {
        glValidateProgram(programId);

        final int[] validateStatus = new int[1];
        glGetProgramiv(programId, GL_VALIDATE_STATUS, validateStatus, 0);

        return validateStatus[0] != 0;
    }
}
