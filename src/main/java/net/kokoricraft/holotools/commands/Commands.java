package net.kokoricraft.holotools.commands;

import com.google.common.base.Strings;
import com.google.gson.JsonObject;
import net.kokoricraft.holotools.HoloTools;
import net.kokoricraft.holotools.managers.HoloManager;
import net.kokoricraft.holotools.objects.holocrafter.HoloCrafter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Display;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


public class Commands implements CommandExecutor {
    private final HoloTools plugin;

    public Commands(HoloTools plugin){
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if(args.length == 0){
            player.sendMessage("Ingresa argumentos!");
            return true;
        }

        switch (args[0].toLowerCase()){
            case "test" ->{
                ItemStack itemStack = plugin.getConfigManager().CRAFTER_ITEM.getItem();
                player.getInventory().addItem(itemStack);
            }
            case "test2" ->{
                ItemStack itemStack = plugin.getConfigManager().WARDROBE_ITEM.getItem();
                player.getInventory().addItem(itemStack);
            }
            case "open" ->{
                plugin.getManager().openHaloCrafting(player);
                player.sendMessage("Debio abrir v:");
            }
            case "wardrobe" ->{

            }
            case "holo" ->{
                HoloCrafter holoCrafter = new HoloCrafter(player, new HashMap<>(), player.getInventory().getItemInMainHand());
                holoCrafter.setVisible(true);
                player.sendMessage("holo");
            }
            case "text" ->{
                player.getWorld().spawn(player.getLocation().clone().add(0, 1, 0), TextDisplay.class, display ->{
                    display.setText("Horizontal");
                    display.setBillboard(Display.Billboard.HORIZONTAL);

                });

                player.getWorld().spawn(player.getLocation().clone().add(0, 2, 0), TextDisplay.class, display ->{
                    display.setText("Vertical");
                    display.setBillboard(Display.Billboard.VERTICAL);
                });

                player.getWorld().spawn(player.getLocation().clone().add(0, 3, 0), TextDisplay.class, display ->{
                    display.setText("FIXED");
                    display.setBillboard(Display.Billboard.FIXED);
                });

                player.getWorld().spawn(player.getLocation().clone().add(0, 4, 0), TextDisplay.class, display ->{
                    display.setText("CENTER");
                    display.setBillboard(Display.Billboard.CENTER);
                });

                player.sendMessage("aaaaaaa");
            }
            case "awa" ->{
                Interaction interaction = player.getWorld().spawn(player.getLocation(), Interaction.class, entity ->{
                   entity.setInteractionWidth(3);
                   entity.setInteractionHeight(3);
                   entity.setResponsive(true);
                });

                Bukkit.getScheduler().runTaskLater(plugin, interaction::remove, 20 * 20);
            }
        }

        return true;
    }

    public static String serializeItemStack(ItemStack itemStack) throws Exception {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        GZIPOutputStream gzipStream = new GZIPOutputStream(byteStream);
        BukkitObjectOutputStream objectStream = new BukkitObjectOutputStream(gzipStream);

        objectStream.writeObject(itemStack);
        objectStream.close();

        return Base64.getEncoder().encodeToString(byteStream.toByteArray());
    }

    public static ItemStack deserializeItemStack(String serializedItem) throws Exception {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(Base64.getDecoder().decode(serializedItem));
        GZIPInputStream gzipStream = new GZIPInputStream(byteStream);
        BukkitObjectInputStream objectStream = new BukkitObjectInputStream(gzipStream);

        ItemStack itemStack = (ItemStack) objectStream.readObject();
        objectStream.close();

        return itemStack;
    }

    public static ItemStack createTestItemStack() {
        ItemStack itemStack = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = itemStack.getItemMeta();

        if (meta != null) {
            // Establecer un nombre largo y llamativo
            meta.setDisplayName("§6Espada de la Eternidad de los Ancestros de los Reyes del Apocalipsis");

            // Establecer un lore extenso
            meta.setLore(Arrays.asList(
                    "§7Forjada en el corazón de la montaña eterna,",
                    "§7con el poder de los dragones y el fuego celestial.",
                    "§7Este artefacto antiguo ha sido bendecido",
                    "§7por los dioses y lleva consigo la esencia de la inmortalidad.",
                    "§7Solo los valientes pueden empuñarla y sobrevivir a su poder."
            ));

            // Agregar encantamientos múltiples
            meta.addEnchant(Enchantment.DAMAGE_ALL, 5, true); // Daño de espada (Sharpness V)
            meta.addEnchant(Enchantment.FIRE_ASPECT, 2, true); // Aspecto ígneo (Fire Aspect II)
            meta.addEnchant(Enchantment.LOOT_BONUS_MOBS, 3, true); // Botín (Looting III)
            meta.addEnchant(Enchantment.DURABILITY, 3, true); // Inquebrantable (Unbreaking III)
            meta.addEnchant(Enchantment.KNOCKBACK, 2, true); // Retroceso (Knockback II)

            // Agregar datos adicionales al PersistentDataContainer
            NamespacedKey customKey1 = new NamespacedKey("myplugin", "custom_data_1");
            NamespacedKey customKey2 = new NamespacedKey("myplugin", "custom_data_2");
            NamespacedKey customKey3 = new NamespacedKey("myplugin", "custom_data_3");
            NamespacedKey customKey4 = new NamespacedKey("myplugin", "custom_data_4");
            NamespacedKey customKey5 = new NamespacedKey("myplugin", "custom_data_5");
            NamespacedKey customKey6 = new NamespacedKey("myplugin", "custom_data_6");
            NamespacedKey customKey7 = new NamespacedKey("myplugin", "custom_data_7");
            NamespacedKey customKey8 = new NamespacedKey("myplugin", "custom_data_8");
            NamespacedKey customKey9 = new NamespacedKey("myplugin", "custom_data_9");

            meta.getPersistentDataContainer().set(customKey1, PersistentDataType.STRING, "Valor de datos personalizados 1");
            meta.getPersistentDataContainer().set(customKey2, PersistentDataType.INTEGER, 123456);
            meta.getPersistentDataContainer().set(customKey3, PersistentDataType.STRING, "Valor de datos personalizados 1");
            meta.getPersistentDataContainer().set(customKey4, PersistentDataType.STRING, "Valor de datos personalizados 1");
            meta.getPersistentDataContainer().set(customKey5, PersistentDataType.STRING, "Valor de datos personalizados 1");
            meta.getPersistentDataContainer().set(customKey6, PersistentDataType.STRING, "Valor de datos personalizados 1");
            meta.getPersistentDataContainer().set(customKey7, PersistentDataType.STRING, "Valor de datos personalizados 1");
            meta.getPersistentDataContainer().set(customKey8, PersistentDataType.STRING, "Valor de datos personalizados 1");
            meta.getPersistentDataContainer().set(customKey9, PersistentDataType.STRING, Strings.repeat("Valor de datos personalizados 1", 40));

            itemStack.setItemMeta(meta);
        }

        return itemStack;
    }
}
