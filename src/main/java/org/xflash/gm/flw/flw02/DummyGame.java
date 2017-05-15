package org.xflash.gm.flw.flw02;

import org.xflash.engine.IGameLogic;
import org.xflash.engine.Window;
import org.xflash.engine.graph.Mesh;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

/**
 */
public class DummyGame implements IGameLogic {

    private final Renderer renderer;
    private int direction = 0;
    private float color = 0.0f;
    private Mesh mesh;

    public DummyGame() {
        renderer = new Renderer();
    }

    @Override
    public void init() throws Exception {
        mesh = new Mesh(new float[]{
                -0.5f, 0.5f, 0.0f, //0
                -0.5f, -0.5f, 0.0f, //1
                0.5f, -0.5f, 0.0f, //2
                0.5f, 0.5f, 0.0f, //3
        },
                new float[]{
                        0.5f, 0.0f, 0.0f,
                        0.0f, 0.5f, 0.0f,
                        0.0f, 0.0f, 0.5f,
                        0.0f, 0.5f, 0.5f,
                },
                new int[]{
                        0, 1, 3,
                        3, 1, 2,
                });

        renderer.init();

    }

    @Override
    public void input(Window window) {
        if (window.isKeyPressed(GLFW_KEY_UP)) {
            direction = 1;
        } else if (window.isKeyPressed(GLFW_KEY_DOWN)) {
            direction = -1;
        } else {
            direction = 0;
        }
    }

    @Override
    public void update(float interval) {
        color += direction * 0.01f;
        if (color > 1) {
            color = 1.0f;
        } else if (color < 0) {
            color = 0.0f;
        }
    }

    @Override
    public void render(Window window) {
        window.setClearColor(color, color, color, 0.0f);
        renderer.render(window, mesh);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        mesh.cleanup();
    }

}
