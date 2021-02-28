package com.wizatar08.escapemaze.menus;

import com.wizatar08.escapemaze.map.TileType;
import com.wizatar08.escapemaze.map.TileMap;
import com.wizatar08.escapemaze.helpers.ExternalMapHandler;
import com.wizatar08.escapemaze.helpers.ui.UI;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.opengl.Texture;

import javax.swing.*;

import static com.wizatar08.escapemaze.helpers.Drawer.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import static com.wizatar08.escapemaze.render.Renderer.*;

public class Editor {
    // Initialize variables
    private Texture background;
    private TileMap map;
    private UI editorUI;
    private ArrayList<UI.Menu> menus;
    public static int displacementX;
    public static int displacementY;
    private TileType tileSelected;
    private int menuIndex;
    private boolean isSelectingEnemy;
    private int[][] positions;
    private int index;
    private String id;
    private boolean buttonPressed;

    public Editor() {
        editorUI = new UI();
        menus = new ArrayList<>();
        tileSelected = null;
        menuIndex = 0;
        isSelectingEnemy = false;
        positions = new int[1024][2];
        buttonPressed = false;
        createMenus();
        try {
            map = ExternalMapHandler.LoadMap("map.wtremm");
        } catch (NullPointerException e) {
            map = new TileMap();
        }
        displacementX = 0;
        displacementY = 0;
        background = LoadPNG("backgrounds/main_menu");
    }

    private void changeMapSize(int width, int height) {
        TileMap oldMap = map;
        map = new TileMap(width, height);
        for(int i = 0; i < map.getMapAsArray().length; i++){
            for(int j = 0; j < map.getMapAsArray()[i].length; j++){
                map.setTile(i, j, TileType.DEFAULT_FLOOR);
                try {
                    map.setTile(i, j, oldMap.getTile(i, j).getType());
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
        }
    }

    private void detectKey() {
        while (Keyboard.next()) {
            if (keyDown(Keyboard.KEY_S)) {
                ExternalMapHandler.SaveMap("map.wtremm", map);
            }
            if (keyDown(Keyboard.KEY_EQUALS)) {
                try {
                    int size = Integer.parseInt(JOptionPane.showInputDialog("Change width of map:"));
                    if (size < 1) {
                        JOptionPane.showMessageDialog(null, "Width of map cannot be less than 1.");
                    } else if (size > 48) {
                        JOptionPane.showMessageDialog(null, "Width of map cannot be greater than 48.");
                    } else changeMapSize(size, map.getTilesHigh());
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null,"Inputted number is not an integer.");
                }
            }
            if (keyDown(Keyboard.KEY_MINUS)) {
                try {
                    int size = Integer.parseInt(JOptionPane.showInputDialog("Change height of map:"));
                    if (size < 1) {
                        JOptionPane.showMessageDialog(null, "Width of map cannot be less than 1.");
                    } else if (size > 48) {
                        JOptionPane.showMessageDialog(null, "Width of map cannot be greater than 48.");
                    } else changeMapSize(map.getTilesWide(), size);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null,"Inputted number is not an integer.");
                }
            }
            if (keyDown(Keyboard.KEY_E)) {
                if (!isSelectingEnemy) {
                    id = JOptionPane.showInputDialog("Enter enemy ID:");
                    index = 0;
                    isSelectingEnemy = true;
                } else {
                    writeCoords();
                    JOptionPane.showMessageDialog(null, "Coordinates have been written to a .json file. Copy it and paste it into your respective file inside level_enemies.");
                    isSelectingEnemy = false;
                }
            }
            if (keyDown(Keyboard.KEY_D)) {
                menuIndex++;
                int max = 0;
                for (int i = 0; i < editorUI.getMenuList().size(); i++) {
                    max++;
                }
                if (menuIndex >= max) {
                    menuIndex = 0;
                }
            }
        }
        if (keyDown(Keyboard.KEY_UP)) displacementY -= 1;
        if (keyDown(Keyboard.KEY_DOWN)) displacementY += 1;
        if (keyDown(Keyboard.KEY_LEFT)) displacementX += 1;
        if (keyDown(Keyboard.KEY_RIGHT)) displacementX -= 1;
    }

    private boolean keyDown(int key) {
        return (Keyboard.getEventKey() == key) && (Keyboard.getEventKeyState());
    }

    private void detectIfButtonDown() {
        boolean isOverButton = false;
        if (Mouse.isButtonDown(0)) {
            if (!isSelectingEnemy) {
                for (int i = 0; i < TileType.TILE_TYPES.size(); i++) {
                    int ceil = (int) Math.ceil(i / (3 * 11));
                    for (int j = 0; j < editorUI.getMenu("Tiles" + ceil).getButtons().size(); j++) {
                        if (editorUI.getMenu("Tiles" + ceil).isButtonClicked(editorUI.getMenu("Tiles" + ceil).getButtons().get(j).getName())) {
                            tileSelected = TileType.TILE_IDS.get(editorUI.getMenu("Tiles" + ceil).getButtons().get(j).getName());
                            isOverButton = true;
                        }
                    }
                }
                if (!isOverButton && tileSelected != null) {
                    map.setTile((int) Math.floor(((Mouse.getX() - displacementX) / TILE_SIZE)), (int) Math.floor(((HEIGHT - Mouse.getY() - 1 - displacementY) / TILE_SIZE)), tileSelected);
                }
            } else {
                if (buttonPressed == false) {
                    buttonPressed = true;
                    int xPlace = (int) Math.floor((Mouse.getX() - displacementX) / TILE_SIZE);
                    int yPlace = (int) Math.floor(((HEIGHT - Mouse.getY() - 1 - displacementY) / TILE_SIZE));
                    positions[index][0] = xPlace;
                    positions[index][1] = yPlace;
                    index++;
                }
            }
        } else {
            buttonPressed = false;
        }
    }

    private void showMenu() {
        for (int i = 0; i < editorUI.getMenuList().size(); i++) {
            if (editorUI.getMenu(editorUI.getMenuList().get(i).getName()).getName().equals("Tiles" + menuIndex)) {
                editorUI.getMenu(editorUI.getMenuList().get(i).getName()).showMenu(true);
            } else {
                editorUI.getMenu(editorUI.getMenuList().get(i).getName()).showMenu(false);
            }
        }
    }

    private void writeCoords() {
        try {
            File file = new File("enemyFile.json");
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write("{");
            bw.newLine();
            bw.write("  \"id\": " + id + ",");
            bw.newLine();
            String coords = "[";
            int[][] newArray = new int[index][2];
            for (int i = 0; i < index; i++) {
                newArray[i][0] = positions[i][0];
                newArray[i][1] = positions[i][1];
                coords += ("[" + newArray[i][0] + ", " + newArray[i][1] + "]");
                if (i != (index - 1)) {
                    coords += ", ";
                }
            }
            coords += "]";
            bw.write("  \"paths\": " + coords);
            bw.newLine();
            bw.write("}");
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setEnemy() {
        drawQuadTex(LoadPNG("tiles/selectors/safe_space_selector"), (int) Math.floor((Mouse.getX() - displacementX) / TILE_SIZE) * TILE_SIZE + displacementX, (int) Math.floor(((HEIGHT - Mouse.getY() - 1 - displacementY) / TILE_SIZE)) * TILE_SIZE +  displacementY, TILE_SIZE, TILE_SIZE);
    }

    public void update() {
        detectKey();
        detectIfButtonDown();
        showMenu();
        draw();
        if (isSelectingEnemy) {
            setEnemy();
        }
    }

    private void draw() {
        drawQuadTex(background, 0, 0, WIDTH * 2, HEIGHT);
        map.draw();
        editorUI.draw();
    }


    private void createMenus() {
        int w = 3;
        int h = 11;
        for (int i = 0; i < TileType.TILE_TYPES.size(); i++) {
            int ceil = (int) Math.ceil(i / (w * h));
            if (editorUI.getMenu("Tiles" + ceil) == null) editorUI.createMenu("Tiles" + ceil, WIDTH - (int) (64 * 3.3), 20, 220, 704, 3, 11);
            Texture[] textures = new Texture[TileType.TILE_TYPES.get(i).getOverlayTex().length + 1];
            int ind = 0;
            textures[0] = LoadPNG("tiles/" + TileType.TILE_TYPES.get(i).getTexture());
            for (int j = 0; j < TileType.TILE_TYPES.get(i).getOverlayTex().length; j++) {
                textures[j + 1] = TileType.TILE_TYPES.get(i).getOverlayTex()[j];
            }
            System.out.println("Textures length: " + textures.length + ", " + textures.length + ", " + TileType.TILE_TYPES.get(i).getOverlayTex().length);
            editorUI.getMenu("Tiles" + ceil).addButton(TileType.TILE_TYPES.get(i).getId(), textures, TileType.TILE_TYPES.get(i).getOverlayTexRot());
        }
    }

}
