package net.kokoricraft.holotools.objects.tooltip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay.TextAlignment;
import org.bukkit.inventory.ItemStack;

import com.google.common.base.Strings;

import net.kokoricraft.holotools.HoloTools;
import net.kokoricraft.holotools.utils.objects.HoloColor;
import net.kokoricraft.holotools.version.HoloTextDisplay;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class TooltipDisplay {
    private final static float size = 0.2f;
    private final static HoloColor background_color = HoloColor.fromARGB(255, 16, 0, 16);
    private final static HoloColor outline_color = HoloColor.fromHex("#270066", 255);
    //private final static HoloColor white = HoloColor.fromARGB(255, 255, 255, 255);
    private final HoloTools plugin = HoloTools.getInstance();
    private final Map<TooltipLayerType, HoloTextDisplay> entities = new HashMap<>();
    private final Display.Billboard billboard = Display.Billboard.VERTICAL;
    private final Player player;
    private final List<Player> viewers;
    private ItemStack itemStack;
    private final static float translation_per_row = -0.1f;
    private final static float translation_per_length = 0.01f;
    private final static float z_player = -0.8f;
    private float x = 0f;
    private float y = -0.4f;
    public TooltipDisplay(Player player) {
        this.player = player;
        this.viewers = new ArrayList<>();
        viewers.add(player);
        this.itemStack = player.getInventory().getItemInMainHand();
    }

    public void setItemStack(ItemStack itemStack) {
        if(this.itemStack.equals(itemStack)){
            setVisible(true);
            return;
        }

        this.itemStack = itemStack;
        if(this.itemStack == null)
            this.itemStack = new ItemStack(Material.AIR);

        update();
        setVisible(true);
    }

    public void spawn(){
        Location location = player.getLocation();

        List<BaseComponent> textList = plugin.getCompatManager().getCompat().getToolTip(itemStack, player, false);

        int max_length = 0;

        for(BaseComponent component : textList){
            String text = component.toPlainText();
            int length = plugin.getUtils().getTextLength(text);
            if(length > max_length)
                max_length = length;
        }

        max_length = max_length / 2;

        int rows = textList.size();

        float y_translation = (translation_per_row * rows) - translation_per_row;
        float x_translation = (translation_per_length * max_length) - translation_per_length;

        HoloTextDisplay background1 = plugin.getCompatManager().createTextDisplay(viewers, location, 0, 0);
        background1.setScale(1 * size, (1 + (.38f / rows))* size, 1 * size);
        background1.setText(getTooltip(2));
        background1.setColor(background_color);
        background1.setAlignment(TextAlignment.LEFT);
        background1.setTextOpacity((byte) 10);
        background1.setLineWidth(999999999);
        background1.setBillboard(billboard);
        background1.setTranslation(x + (x_translation * size), (-0.048f * size) + (y_translation * size) + y, 0.00f + z_player);
        background1.setBrightness(new Display.Brightness(15, 15));
        entities.put(TooltipLayerType.BACKGROUND1, background1);

        HoloTextDisplay background2 = plugin.getCompatManager().createTextDisplay(viewers, location, 0, 0);
        background2.setScale(1 * size, (1 + (.18f / rows)) * size, 1 * size);
        background2.setText(getTooltip(3));
        background2.setColor(background_color);
        background2.setAlignment(TextAlignment.LEFT);
        background2.setTextOpacity((byte) 10);
        background2.setLineWidth(999999999);
        background2.setBillboard(billboard);
        background2.setTranslation(x + (x_translation * size), (-0.024f * size) + (y_translation * size) + y, 0.003f + z_player);
        background2.setBrightness(new Display.Brightness(15, 15));
        entities.put(TooltipLayerType.BACKGROUND2, background2);

        HoloTextDisplay outline1 = plugin.getCompatManager().createTextDisplay(viewers, location, 0, 0);
        outline1.setScale(1 * size, (1 + (.18f / rows)) * size, 1 * size);
        outline1.setText(getTooltip(2));
        outline1.setColor(outline_color);
        outline1.setAlignment(TextAlignment.LEFT);
        outline1.setTextOpacity((byte) 10);
        outline1.setLineWidth(999999999);
        outline1.setBillboard(billboard);
        outline1.setTranslation(x + (x_translation * size), (-0.024f * size) + (y_translation * size) + y, 0.006f + z_player);
        outline1.setBrightness(new Display.Brightness(15, 15));
        entities.put(TooltipLayerType.OUTLINE1, outline1);

        HoloTextDisplay outline2 = plugin.getCompatManager().createTextDisplay(viewers, location, 0, 0);
        outline2.setScale(1 * size, 1 * size, 1 * size);
        outline2.setText(getTooltip(1));
        outline2.setColor(background_color);
        outline2.setAlignment(TextAlignment.LEFT);
        outline2.setTextOpacity((byte) 10);
        outline2.setLineWidth(999999999);
        outline2.setBillboard(billboard);
        outline2.setTranslation(x + (x_translation * size), (y_translation * size) + y, 0.009f+ z_player);
        outline2.setBrightness(new Display.Brightness(15, 15));
        entities.put(TooltipLayerType.OUTLINE2, outline2);

        HoloTextDisplay text = plugin.getCompatManager().createTextDisplay(viewers, location, 0, 0);
        text.setScale(1 * size, 1 * size, 1 * size);
        text.setText(getTooltip(0));
        text.setColor(background_color);
        text.setAlignment(TextAlignment.LEFT);
        text.setLineWidth(999999999);
        text.setBillboard(billboard);
        text.setTranslation(x + (x_translation * size), (y_translation * size) + y, 0.012f + z_player);
        text.setBrightness(new Display.Brightness(15, 15));
        entities.put(TooltipLayerType.TEXT, text);

        background1.update();
        background2.update();
        outline1.update();
        outline2.update();
        text.update();

        background1.mount(player);
        background2.mount(player);
        outline1.mount(player);
        outline2.mount(player);
        text.mount(player);
    }

    public void update(){
        if (entities.isEmpty()) {
            spawn();
            return;
        }

        List<BaseComponent> textList = plugin.getCompatManager().getCompat().getToolTip(itemStack, player, false);
        int max_length = 0;

        for(BaseComponent component : textList){
            String text = component.toPlainText();
            int length = plugin.getUtils().getTextLength(text);
            if(length > max_length)
                max_length = length;
        }

        max_length = max_length / 2;

        int rows = textList.size();

        float y_translation = (translation_per_row * rows) - translation_per_row;
        float x_translation = (translation_per_length * max_length) - translation_per_length;

        HoloTextDisplay background1 = entities.get(TooltipLayerType.BACKGROUND1);
        background1.setScale(1 * size, (1 + (.38f / rows))* size, 1 * size);
        background1.setText(getTooltip(2));
        background1.setTranslation(x + (x_translation * size), (-0.048f * size) + (y_translation * size) + y, 0.00f + z_player);

        HoloTextDisplay background2 = entities.get(TooltipLayerType.BACKGROUND2);
        background2.setScale(1 * size, (1 + (.18f / rows)) * size, 1 * size);
        background2.setText(getTooltip(3));
        background2.setTranslation(x + (x_translation * size), (-0.024f * size) + (y_translation * size) + y, 0.003f + z_player);

        HoloTextDisplay outline1 = entities.get(TooltipLayerType.OUTLINE1);
        outline1.setScale(1 * size, (1 + (.18f / rows)) * size, 1 * size);
        outline1.setText(getTooltip(2));
        outline1.setTranslation(x + (x_translation * size), (-0.024f * size) + (y_translation * size) + y, 0.006f + z_player);

        HoloTextDisplay outline2 = entities.get(TooltipLayerType.OUTLINE2);
        outline2.setText(getTooltip(1));
        outline2.setTranslation(x + (x_translation * size), (y_translation * size) + y, 0.009f+ z_player);

        HoloTextDisplay text = entities.get(TooltipLayerType.TEXT);
        text.setText(getTooltip(0));
        text.setTranslation(x + (x_translation * size), (y_translation * size) + y, 0.012f + z_player);

        background1.update();
        background2.update();
        outline1.update();
        outline2.update();
        text.update();
    }

    public List<BaseComponent> getTooltip(int separation){
        String separator = separation == 0 ? "" : Strings.repeat(".", separation);
        List<BaseComponent> baseComponents = plugin.getCompatManager().getCompat().getToolTip(itemStack, player, false);
        List<BaseComponent> components = new ArrayList<>();

        for (int i = 0; i < baseComponents.size(); i++) {
            BaseComponent component = baseComponents.get(i);
            BaseComponent point = new TextComponent(separator);
            point.addExtra(component);

            if (i < baseComponents.size() - 1){
                point.addExtra(separator);
                point.addExtra("\n");
            }

            components.add(point);
        }
        return components;
    }

    public void remove(){
        entities.values().forEach(HoloTextDisplay::remove);
        entities.clear();
    }

    public void setVisible(boolean visible){
        if(plugin.getCompatManager().VERSION.contains("1_19")){
            if(visible){
                update();
            }else {
                remove();
            }
            return;
        }

        entities.values().forEach(text -> {
            text.setViewRange(visible ? 60 : 0);
            text.update();
        });
    }

    public void setY(float y){
        this.y = y;
    }

    public void setX(float x){
        this.x = x;
    }
}
