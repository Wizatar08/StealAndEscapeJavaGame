package com.wizatar08.escapemaze.game.events;

import com.wizatar08.escapemaze.game.game_entities.items.Item;
import com.wizatar08.escapemaze.game.game_entities.items.ItemType;
import com.wizatar08.escapemaze.helpers.Timer;
import com.wizatar08.escapemaze.helpers.visuals.Drawer;
import com.wizatar08.escapemaze.menus.Game;

import java.util.ArrayList;
import java.util.Collections;

public class ItemTouchAndRemoveEvent extends Event {
    private Item centerItem;
    private ArrayList<ItemType> itemsNeeded;
    private Timer timeTillTrigger;

    public ItemTouchAndRemoveEvent(Item centerItem, int timeTillTrigger, ItemType... otherItems) {
        super();
        this.centerItem = centerItem;
        this.timeTillTrigger = new Timer(Timer.TimerModes.COUNT_DOWN, timeTillTrigger);
        this.timeTillTrigger.pause();
        itemsNeeded = new ArrayList<ItemType>();
        Collections.addAll(itemsNeeded, otherItems);
    }

    @Override
    public boolean isOccuring() {
        return true;
    }

    @Override
    public void update() {
        timeTillTrigger.update();
        ArrayList<ItemType> types = new ArrayList<ItemType>(itemsNeeded);
        ArrayList<Item> items = new ArrayList<>();
        for (Item item1 : getGameController().getItems()) {
            if (Drawer.checkCollision(item1.getX(), item1.getY(), item1.getWidth(), item1.getHeight(), centerItem.getX(), centerItem.getY(), centerItem.getWidth(), centerItem.getHeight()) && itemsNeeded.contains(item1.getType())) {
                types.remove(item1.getType());
                items.add(item1);
            }
        }
        if (types.size() <= 0) {
            timeTillTrigger.unpause();
            if (timeTillTrigger.getTotalSeconds() <= 0) {
                centerItem.onTrigger(items);
            }
        } else {
            timeTillTrigger.pause();
            timeTillTrigger.setTime(10);
        }
        System.out.println(timeTillTrigger.getTotalSeconds() + ", " + types.size() + ", " + timeTillTrigger.isPaused());
    }
}
