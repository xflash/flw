package org.xflash.gm.flw.flw02;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.xflash.engine.GameItem;
import org.xflash.engine.IGameLogic;
import org.xflash.engine.MouseInput;
import org.xflash.engine.Window;
import org.xflash.engine.graph.*;

import static org.lwjgl.glfw.GLFW.*;

/**
 */
public class DummyGame implements IGameLogic {

    private static final float MOUSE_SENSITIVITY = 0.2f;
    private static final float CAMERA_POS_STEP = 0.05f;
    private final Vector3f cameraInc;
    private final Renderer renderer;
    private final Camera camera;
    private GameItem[] gameItems;
    private Vector3f ambientLight;
    private PointLight[] pointLightList;
    private SpotLight[] spotLightList;
    private DirectionalLight directionalLight;
    private float lightAngle;
    private float spotAngle = 0;

    private float spotInc = 1;

    public DummyGame() {
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f(0.0f, 0.0f, 0.0f);
        lightAngle = -90;
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);

        float reflectance = 1f;
        Mesh mesh = OBJLoader.loadMesh("/models/bunny.obj");
        Material material = new Material(new Vector4f(0.2f, 0.5f, 0.5f, 0), reflectance);

//        Mesh mesh = OBJLoader.loadMesh("/models/cube.obj");
//        Texture texture = new Texture("/textures/grassblock.png");
//        Material material = new Material(texture, reflectance);
        mesh.setMaterial(material);
        //gameItem.setPosition(0, 0, -2);
//        gameItem.setScale(0.1f);
        //gameItem.setPosition(0, 0, -2);
        //gameItem.setPosition(0, 0, -0.2f);
        gameItems = new GameItem[]{
                new GameItem(mesh)
                    .setScale(0.5f)
                    .setPosition(0, 0, -2),
        };

        ambientLight = new Vector3f(0.3f, 0.3f, 0.3f);

        // Point Light

        PointLight pointLight = new PointLight(
                new Vector3f(1, 1, 1),
                new Vector3f(0, 0, 1),
                1.0f);
        pointLight.setAttenuation(new PointLight.Attenuation(0.0f, 0.0f, 1.0f));
        pointLightList = new PointLight[]{pointLight};

        // Spot Light
        pointLight = new PointLight(
                new Vector3f(1, 1, 1),
                new Vector3f(0, 0.0f, 10f),
                1.0f);
        pointLight.setAttenuation(new PointLight.Attenuation(0.0f, 0.0f, 0.02f));

        SpotLight spotLight = new SpotLight(pointLight,
                new Vector3f(0, 0, -1),
                (float) Math.cos(Math.toRadians(180)));
        spotLightList = new SpotLight[]{spotLight, new SpotLight(spotLight)};
//        spotLightList = new SpotLight[]{};

        directionalLight = new DirectionalLight(
                new Vector3f(1, 1, 1),
                new Vector3f(-1, 0, 0), 1.0f);
    }

    @Override
    public void input(Window window, MouseInput mouseInput) {
        cameraInc.set(0, 0, 0);
        if (window.isKeyPressed(GLFW_KEY_W)) {
            cameraInc.z = -1;
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            cameraInc.z = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            cameraInc.x = -1;
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            cameraInc.x = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_Z)) {
            cameraInc.y = -1;
        } else if (window.isKeyPressed(GLFW_KEY_X)) {
            cameraInc.y = 1;
        }
        if(spotLightList.length>=1) {
            float lightPos = spotLightList[0].getPointLight().getPosition().z;
            if (window.isKeyPressed(GLFW_KEY_N)) {
                this.spotLightList[0].getPointLight().getPosition().z = lightPos + 0.1f;
            } else if (window.isKeyPressed(GLFW_KEY_M)) {
                this.spotLightList[0].getPointLight().getPosition().z = lightPos - 0.1f;
            }
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        // Update camera position
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);

        // Update camera based on mouse
        if (mouseInput.isRightButtonPressed()) {
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
        }

        // Update spot light direction
        spotAngle += spotInc * 0.05f;
        if (spotAngle > 2) {
            spotInc = -1;
        } else if (spotAngle < -2) {
            spotInc = 1;
        }
        if(spotLightList.length>=1) {
            spotLightList[0].getConeDirection().y =
                    (float) Math.sin(Math.toRadians(spotAngle));
        }

        // Update directional light direction, intensity and colour
        lightAngle += 1.1f;
        if (lightAngle > 90) {
            directionalLight.setIntensity(0);
            if (lightAngle >= 360) {
                lightAngle = -90;
            }
        } else if (lightAngle <= -80 || lightAngle >= 80) {
            float factor = 1 - (float) (Math.abs(lightAngle) - 80) / 10.0f;
            directionalLight.setIntensity(factor);
            directionalLight.getColor().y = Math.max(factor, 0.9f);
            directionalLight.getColor().z = Math.max(factor, 0.5f);
        } else {
            directionalLight.setIntensity(1);
            directionalLight.getColor().x = 1;
            directionalLight.getColor().y = 1;
            directionalLight.getColor().z = 1;
        }
        double angRad = Math.toRadians(lightAngle);
        directionalLight.getDirection().x = (float) Math.sin(angRad);
        directionalLight.getDirection().y = (float) Math.cos(angRad);
    }

    @Override
    public void render(Window window) {
        renderer.render(window, camera, gameItems, ambientLight,
                pointLightList, spotLightList, directionalLight);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        for (GameItem gameItem : gameItems) {
            gameItem.getMesh().cleanUp();
        }
    }

}
