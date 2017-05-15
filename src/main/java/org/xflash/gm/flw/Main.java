package org.xflash.gm.flw;

import org.xflash.engine.GameEngine;
import org.xflash.engine.IGameLogic;
import org.xflash.gm.flw.flw02.DummyGame;

/**
 */
public class Main {

    public static void main(String[] args) {
        try {
            boolean vSync = true;
            IGameLogic gameLogic = new DummyGame();
            GameEngine gameEng = new GameEngine("GAME", 600, 480, vSync, gameLogic);
            gameEng.start();
        } catch (Exception excp) {
            excp.printStackTrace();
            System.exit(-1);
        }
    }
}
