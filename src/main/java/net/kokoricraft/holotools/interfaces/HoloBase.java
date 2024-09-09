package net.kokoricraft.holotools.interfaces;

import net.kokoricraft.holotools.enums.HoloActionType;

public interface HoloBase {
    void onChangeSlot(int fromSlot, int toSlot);
    void onClick();
    void onAction(HoloActionType type);
    int getSlot();
    int getMaxSlots();
    void setVisible(boolean visible);
    boolean isVisible();
}
