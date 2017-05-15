package org.xflash.gm.flw.flw02;


import org.xflash.engine.GameItem;
import org.xflash.engine.Utils;
import org.xflash.engine.Window;
import org.xflash.engine.graph.ShaderProgram;
import org.xflash.engine.graph.Transformation;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;


public class Renderer {


    /**
     * Field of View in Radians
     */
    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.f;
    private final Transformation transformation;
    private ShaderProgram shaderProgram;

    public Renderer() {
        transformation = new Transformation();
    }

    public void init(Window window) throws Exception {
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Utils.loadResource("/vertex.vs"));
        shaderProgram.createFragmentShader(Utils.loadResource("/fragment.fs"));
        shaderProgram.link();

        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("worldMatrix");

        window.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(Window window, List<GameItem> gameItems) {
        clear();

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shaderProgram.bind();

        // Update projection Matrix
        shaderProgram.setUniform("projectionMatrix",
                transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR));

        // Render each gameItem
        for (GameItem gameItem : gameItems) {
            // Set world matrix for this item
            shaderProgram.setUniform("worldMatrix",
                    transformation.getWorldMatrix(gameItem.getPosition(), gameItem.getRotation(), gameItem.getScale()));
            // Render the mesh for this game item
            gameItem.getMesh().render();
        }

        shaderProgram.unbind();
    }


    public void cleanup() {
        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }

    }
}