package net.kokoricraft.holotools.utils.enums;

import net.kokoricraft.holotools.HoloTools;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Utils {
    private final HoloTools plugin;

    public Utils(HoloTools plugin){
        this.plugin = plugin;
    }

    public int getTextLength(String text){
        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for(char c : text.toCharArray()){
            if(c == '§'){
                previousCode = true;
                continue;
            }else if(previousCode == true){
                previousCode = false;
                if(c == 'l' || c == 'L'){
                    isBold = true;
                    continue;
                }else isBold = false;
            }else{
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }
        return messagePxSize;
    }

    public String getItemStackDecoded(ItemStack itemstack) {
        ItemStack[] items = new ItemStack[]{itemstack};
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(items.length);

            for (ItemStack item : items) {
                dataOutput.writeObject(item);
            }

            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception ex) {
            throw new IllegalStateException("Error transforming itemstack to base64.", ex);
        }
    }

    public ItemStack getItemStackEncoded(String base64coded) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(base64coded));
        BukkitObjectInputStream dataInput;
        ItemStack[] items = null;
        try {
            dataInput = new BukkitObjectInputStream(inputStream);
            items = new ItemStack[dataInput.readInt()];

            for (int i = 0; i < items.length; ++i) {
                try {
                    items[i] = (ItemStack) dataInput.readObject();
                } catch (ClassNotFoundException ex) {
                    throw new IOException("Error transforming base64 to itemstack.", ex);
                } catch (IOException ignored) {
                }
            }

            dataInput.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        assert items != null;
        return items[0];
    }
}
