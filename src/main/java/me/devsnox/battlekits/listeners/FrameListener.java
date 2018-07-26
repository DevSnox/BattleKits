package me.devsnox.battlekits.listeners;

import me.devsnox.battlekits.inventory.Frame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.ArrayList;

public class FrameListener implements Listener {

    private ArrayList<Frame> frames;

    public FrameListener() {
        this.frames = new ArrayList<>();
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        for(Frame frame : this.frames) {
            if(frame.inventory.getName().equals(event.getClickedInventory().getName())) {
                frame.onClick(event);
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        for(Frame frame : this.frames) {
            if(frame.inventory.getName().equals(event.getInventory().getName())) {
                frame.onClose(event);
            }
        }
    }

    public void addFrame(Frame frame) {
        this.frames.add(frame);
    }
}
