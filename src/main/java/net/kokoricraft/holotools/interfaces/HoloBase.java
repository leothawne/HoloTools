package net.kokoricraft.holotools.interfaces;

public interface HoloBase {
    void onChangeSlot(int fromSlot, int toSlot);
    void onClick();
    int getSlot();
    int getMaxSlots();
    void setVisible(boolean visible);
    boolean isVisible();
}
