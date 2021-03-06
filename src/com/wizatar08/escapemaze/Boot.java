package com.wizatar08.escapemaze;

import com.wizatar08.escapemaze.helpers.Clock;
import com.wizatar08.escapemaze.visuals.Drawer;
import com.wizatar08.escapemaze.helpers.Lang;
import com.wizatar08.escapemaze.menus.MenuRun;
import com.wizatar08.escapemaze.render.Renderer;
import com.wizatar08.escapemaze.visuals.Tex;
import org.lwjgl.opengl.Display;

/**
 * Where all the code is set up and run
 */
public class Boot {
    MenuRun menu;

    /**
     * Main method.
     * Holds the loop that runs all the game code.
     */
    public Boot() {
        Lang.start();
        Renderer.begin(); // Setup display and drawing tools
        Drawer.createFont();

        menu = new MenuRun();

        while(!Display.isCloseRequested()) { // While the program has not been requested to close
            Clock.update();
            menu.update();
            Renderer.loop();
            Display.update();
            Clock.FPS = 60;
            Display.sync(Clock.FPS); // Set FPS
        }

        Display.destroy(); // Destroy (close) display
    }

    /**
     * First class to run. Calls the Boot class
     * @param args
     */
    public static void main(String[] args) {
        new Boot();
    }
}