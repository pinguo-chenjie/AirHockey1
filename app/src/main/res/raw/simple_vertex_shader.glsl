attribute vec4 a_Position; // Java层传递到Native层的顶点变量

void main()
{
    gl_Position = a_Position;    // 处理的当前顶点
    gl_PointSize = 10.0;
}