package com.wizatar08.escapemaze.game.game_entities.items;

import com.wizatar08.escapemaze.game.game_entities.items.subclasses.*;
import com.wizatar08.escapemaze.game.game_entities.items.subclasses.durability.RechargableBattery;
import com.wizatar08.escapemaze.game.game_entities.items.subclasses.durability.RefuelableGasCan;
import com.wizatar08.escapemaze.visuals.Tex;

import java.util.*;

public enum ItemType {
    // IDTYPE: 3
    NULL("300000", "null", new Builder()),
    RED_KEY("300101", "red_key", new Builder().weight(0.1f).pixelLength(32).className(Key.class)),
    ORANGE_KEY("300102", "orange_key", new Builder().weight(0.1f).pixelLength(32).className(Key.class)),
    YELLOW_KEY("300103", "yellow_key", new Builder().weight(0.1f).pixelLength(32).className(Key.class)),
    GREEN_KEY("300104", "green_key", new Builder().weight(0.1f).pixelLength(32).className(Key.class)),
    BLUE_KEY("300105", "blue_key", new Builder().weight(0.1f).pixelLength(32).className(Key.class)),
    DARK_BLUE_KEY("300106", "dark_blue_key", new Builder().pixelLength(32).weight(0.1f).className(Key.class)),
    PURPLE_KEY("300107", "purple_key", new Builder().weight(0.1f).pixelLength(32).className(Key.class)),
    PINK_KEY("300108", "pink_key", new Builder().weight(0.1f).pixelLength(32).className(Key.class)),
    DIAMOND("300201", "diamond", new Builder().weight(0.2f).pixelLength(48).required()),
    LARGE_DIAMOND("300202", "large_diamond", new Builder().weight(0.65f).required()), // Must always be surrounded by lasers
    RUBY("300203", "ruby", new Builder().weight(0.3f).pixelLength(32).required()),
    LASER_DEACTIVATOR("300301", "laser_deactivator", new Builder().weight(0.15f).pixelLength(40).className(LaserDeactivator.class)),
    PASS_1("300401", "pass_level_1", new Builder().weight(0.15f).pass(1).className(Pass.class)),
    PASS_2("300402", "pass_level_2", new Builder().weight(0.15f).pass(2).className(Pass.class)),
    PASS_3("300403", "pass_level_3", new Builder().weight(0.15f).pass(3).className(Pass.class)),
    BASIC_BATTERY("300501", "battery", new Builder().weight(0.3f).powerSource().className(RechargableBattery.class, new Object[]{40, new Tex("tile_overlays/battery_plugged")})),
    BASIC_GAS_CAN("300601", "gas_can", new Builder().weight(0.4f).gasSource().className(RefuelableGasCan.class, new Object[]{40, new Tex("tile_overlays/gas_can_refuel")})),
    BOOSTER("300701", "booster", new Builder().weight(0.6f).className(Booster.class)),
    ADMIN_ACCESSOR("300801", "admin_pass", new Builder().weight(0.2f)),
    HOVERING_DEVICE("300901", "hovering_device", new Builder().weight(0.3f).className(HoveringDevice.class)),
    SMALL_EMP("301001", "small_emp", new Builder().weight(0.2f).className(SmallEMP.class)),
    EMP("301002", "emp", new Builder().weight(1.3f).className(EMP.class)),
    HACKED_COMPUTER("301101", "hacked_computer", new Builder().weight(0.9f).className(HackedComputer.class)),
    INSTRUCTIONS("301201", "instructions", new Builder().weight(0 /*0.5f*/).pixelLength(32)),
    PARTS("301301", "robot_parts", new Builder().weight(0 /*1.5f*/).className(RobotPartsItem.class)),
    MINI_GENERATOR("301401", "mini_generator", new Builder().weight(0 /*1.35f*/).pixelLength(32)),
    HELPER_BOT("301501", "helper_bot_unpowered", new Builder().weight(0 /*1.35f*/).className(HelperBot.class, new Object[]{60, new Tex("game/items/helper_bot_powered"), new Tex("game/items/helper_bot_active")})),
    WIRES("301601", "wires", new Builder().weight(0.1f).pixelLength(32)),
    METAL_SHEET("301701", "metal_sheet", new Builder().weight(0.3f)),
    SERVO_MOTOR("301801", "servo_motor", new Builder().weight(0.14f).pixelLength(24)),
    BUCKET("301901", "bucket", new Builder().weight(0.11f).className(Bucket.class).pixelLength(12));

    /* IDEAS FOR ITEMS:
     * - DONE: Pass: Can unlock vaults
     * - DONE*: Admin accessor: Can unlock special things. *Make sure to add other tiles/items that require the admin accessor
     * - DONE: Gas can: Can enable other items to be used. Has only certain amount of time before gas runs out.
     * - DONE: Simple battery: Can enable other items to be used Has only certain amount of time before electricity runs out.
     * - DONE: Hovering Device: Must have gas can to use. Can let the player fly, avoiding certain security issues (pressure plates)
     * - DONE: Booster: Must have extra battery to use. Increases player speed. Uses 2% battery per second.
     * - DONE: Small EMP: Must have 10% extra battery to use. Will shut down the nearest enemy robot or camera for 10 seconds.
     * - DONE: EMP: Must have 40% extra battery to use. Shuts down all enemy robots and cameras for 20 seconds (cannot move or sound alarm). Afterwards, alarm will activate. One time use.
     * - DONE: Hacked computer: Immediately turns off alarm no matter where you are. One time use. Requires admin accessor
     * - Smoke machine: Must have 30% gas can to use. This halves the vision of all robots for 20 seconds.
     * - Gas spot: Must have 40% gas can to use. Puts gas on the ground. The next enemy to step in it gets debuffed (loses half its speed and vision). Does not work on cameras or atoms bots. This clears the gas spot.
     * - Blockade: Item cannot be picked up, but instead pushed (This is considered an item because it shouldn't spawn as a tile). This prevents enemies from seeing you through it. If an enemy bumps into it, alarm will be set off.
     * - Shutoff switch: All doors become unlocked. Must have EMP and admin accessor to use
     */


    // Initialize variables
    private String id;
    private String texture;
    private float weight;
    private boolean required, isPass, isAdminAccessor, power, gas;
    private Class<? extends Item> className;
    private int passLevel, pixelLength;
    private Object[] classArgs;

    private static HashMap<String, ItemType> ITEM_IDS;

    ItemType(String id, String texture, Builder builder) {
        createIdMapAndArrays();
        addToMap(id, this);
        this.id = id;
        this.texture = texture;
        this.pixelLength = builder.getPixelLength();
        this.weight = builder.getWeight();
        this.required = builder.getRequired();
        this.className = builder.getClassName();
        this.passLevel = builder.isPass();
        this.isPass = passLevel > 0;
        this.power = builder.isPower();
        this.gas = builder.isGas();
        this.isAdminAccessor = builder.isAdminAccessor();
        this.classArgs = builder.getClassArgs();
    }

    private void createIdMapAndArrays() {
        if (ITEM_IDS == null) ITEM_IDS = new HashMap<>();
    }

    private void addToMap(String id, ItemType type) {
        ITEM_IDS.put(id, type);
    }
    // Getters
    public String getId() {
        return id;
    }
    public String getTexture() {
        return texture;
    }

    public float getWeight() {
        return weight;
    }
    public boolean isRequired() {
        return required;
    }
    public Class<? extends Item> getClassname() {
        return className;
    }
    public int passLevel() {
        return passLevel;
    }
    public boolean isPass() {
        return isPass;
    }
    public boolean isAdminAccessor() {
        return isAdminAccessor;
    }
    public boolean isPower() {
        return power;
    }
    public boolean isGas() {
        return gas;
    }
    public Object[] getClassArgs() {
        return classArgs;
    }
    public int getPixelLength() {
        return pixelLength;
    }

    public static ItemType getType(String type) {
        if (ITEM_IDS.get(type) != null) {
            return ITEM_IDS.get(type);
        } else {
            return NULL;
        }
    }

    /**
     * Item builder class
     */
    private static class Builder {
        private float weight;
        private boolean required, isAdminAccessor, power, gas;
        private Class<? extends Item> className;
        private int passLevel, pixelLength;
        private Object[] classArgs;

        private Builder() {
            this.weight = 0.0f;
            this.required = false;
            this.className = null;
            this.passLevel = 0;
            this.power = false;
            this.gas = false;
            this.isAdminAccessor = false;
            this.classArgs = null;
            this.pixelLength = 64;
        }

        public Builder weight(float weight) {
            this.weight = weight;
            return this;
        }

        public Builder required() {
            this.required = true;
            return this;
        }

        public Builder className(Class<? extends Item> clazz) {
            this.className = clazz;
            return this;
        }

        public Builder className(Class<? extends Item> clazz, Object[] args) {
            this.className = clazz;
            this.classArgs = args;
            return this;
        }

        public Builder pass(int level) {
            this.passLevel = level;
            return this;
        }

        public Builder powerSource() {
            this.power = true;
            return this;
        }

        public Builder gasSource() {
            this.gas = true;
            return this;
        }

        public Builder adminPass() {
            isAdminAccessor = true;
            return this;
        }

        public Builder pixelLength(int length) {
            pixelLength = length;
            return this;
        }

        public float getWeight() {
            return weight;
        }
        public boolean getRequired() {
            return required;
        }
        public Class<? extends Item> getClassName() {
            return className;
        }
        public int isPass() {
            return passLevel;
        }
        public boolean isGas() {
            return gas;
        }
        public boolean isPower() {
            return power;
        }
        public boolean isAdminAccessor() {
            return isAdminAccessor;
        }
        public Object[] getClassArgs() {
            return classArgs;
        }
        public int getPixelLength() {
            return pixelLength;
        }
    }
}