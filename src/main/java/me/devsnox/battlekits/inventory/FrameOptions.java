package me.devsnox.battlekits.inventory;

public class FrameOptions {

    private boolean noItemMove;

    public FrameOptions(boolean noItemMove) {
        this.noItemMove = noItemMove;
    }

    public boolean isNoItemMove() {
        return noItemMove;
    }
}
