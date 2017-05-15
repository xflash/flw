package org.xflash.gm.flw.flw02;

import org.joml.Vector3f;
import org.xflash.engine.GameItem;
import org.xflash.engine.IGameLogic;
import org.xflash.engine.Window;
import org.xflash.engine.graph.Mesh;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

/**
 */
public class DummyGame implements IGameLogic {

    private final Renderer renderer;
    private final List<GameItem> gameItems = new ArrayList<>();

    private int displxInc = 0;

    private int displyInc = 0;

    private int displzInc = 0;

    private int scaleInc = 0;


    public DummyGame() {
        renderer = new Renderer();
    }

    //                        +41788862435


    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);
        GameItem gameItem = new GameItem(
                new Mesh(new float[]{
                        // VO
                        -0.5f, 0.5f, 0.5f,
                        // V1
                        -0.5f, -0.5f, 0.5f,
                        // V2
                        0.5f, -0.5f, 0.5f,
                        // V3
                        0.5f, 0.5f, 0.5f,
                        // V4
                        -0.5f, 0.5f, -0.5f,
                        // V5
                        0.5f, 0.5f, -0.5f,
                        // V6
                        -0.5f, -0.5f, -0.5f,
                        // V7
                        0.5f, -0.5f, -0.5f,
                },
                        new float[]{
                                0.5f, 0.0f, 0.0f,
                                0.0f, 0.5f, 0.0f,
                                0.0f, 0.0f, 0.5f,
                                0.0f, 0.5f, 0.5f,
                                0.5f, 0.0f, 0.0f,
                                0.0f, 0.5f, 0.0f,
                                0.0f, 0.0f, 0.5f,
                                0.0f, 0.5f, 0.5f,
                        },
                        new int[]{
                                // Front face
                                0, 1, 3, 3, 1, 2,
                                // Top Face
                                4, 0, 3, 5, 4, 3,
                                // Right face
                                3, 2, 7, 5, 3, 7,
                                // Left face
                                6, 1, 0, 6, 0, 4,
                                // Bottom face
                                2, 1, 6, 2, 6, 7,
                                // Back face
                                7, 6, 4, 7, 4, 5,
                        }));
        gameItems.add(gameItem);
        gameItem.setPosition(0, 0, -2);
    }

    @Override
    public void input(Window window) {
        displyInc = 0;
        displxInc = 0;
        displzInc = 0;
        scaleInc = 0;
        if (window.isKeyPressed(GLFW_KEY_UP)) {
            displyInc = 1;
        } else if (window.isKeyPressed(GLFW_KEY_DOWN)) {
            displyInc = -1;
        } else if (window.isKeyPressed(GLFW_KEY_LEFT)) {
            displxInc = -1;
        } else if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
            displxInc = 1;
        } else if (window.isKeyPressed(GLFW_KEY_A)) {
            displzInc = -1;
        } else if (window.isKeyPressed(GLFW_KEY_Q)) {
            displzInc = 1;
        } else if (window.isKeyPressed(GLFW_KEY_Z)) {
            scaleInc = -1;
        } else if (window.isKeyPressed(GLFW_KEY_X)) {
            scaleInc = 1;
        }
    }

    @Override
    public void update(float interval) {
        for (GameItem gameItem : gameItems) {
            // Update position
            Vector3f itemPos = gameItem.getPosition();
            float posx = itemPos.x + displxInc * 0.01f;
            float posy = itemPos.y + displyInc * 0.01f;
            float posz = itemPos.z + displzInc * 0.01f;
            gameItem.setPosition(posx, posy, posz);

            // Update scale
            float scale = gameItem.getScale();
            scale += scaleInc * 0.05f;
            if (scale < 0) {
                scale = 0;
            }
            gameItem.setScale(scale);

            // Update rotation angle
            float rotation = gameItem.getRotation().x + 1.5f;
            if (rotation > 360) {
                rotation = 0;
            }
            gameItem.setRotation(rotation, rotation, rotation);
        }
    }

    @Override
    public void render(Window window) {
        renderer.render(window, gameItems);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        for (GameItem gameItem : gameItems) {
            gameItem.getMesh().cleanup();
        }
    }

}
