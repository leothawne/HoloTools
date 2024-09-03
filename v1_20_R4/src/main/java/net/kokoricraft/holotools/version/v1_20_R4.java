package net.kokoricraft.holotools.version;

import com.google.common.collect.ImmutableList;
import com.mojang.math.Transformation;
import io.netty.channel.*;
import net.kokoricraft.holotools.events.InventoryUpdateEvent;
import net.kokoricraft.holotools.utils.objects.HoloColor;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.util.Brightness;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.item.ItemDisplayContext;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R4.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_20_R4.util.CraftChatMessage;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.inventory.ItemStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class v1_20_R4 implements Compat{

    @Override
    public HoloTextDisplay createTextDisplay(List<Player> players, Location location, float yaw, float pitch) {
        return new HoloDisplayText(players, location, yaw, pitch);
    }

    @Override
    public HoloItemDisplay createItemDisplay(List<Player> players, Location location, float yaw, float pitch) {
        return new HoloDisplayItem(players, location, yaw, pitch);
    }

    @Override
    public void initPacketsRegister(Player player){
        try{
            ChannelPipeline pipeline = getPipeline((CraftPlayer) player);

            pipeline.addBefore("packet_handler", player.getName(), new ChannelDuplexHandler(){
                @Override
                public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                    if(msg instanceof Packet<?> packet){
                        String name = packet.getClass().getName();
                        if(name.endsWith("PacketPlayOutSetSlot") || name.endsWith("ClientboundContainerSetSlotPacket")){
                            onPacketSend(player, packet);
                        }
                    }
                    super.write(ctx, msg, promise);
                }
            });
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }

    private ChannelPipeline getPipeline(CraftPlayer player){
        return getChannel(player).pipeline();
    }

    public Channel getChannel(CraftPlayer player){
        try{
            ServerCommonPacketListenerImpl serverCommonPacketListener = player.getHandle().c;

            Field networkManagerField = ServerCommonPacketListenerImpl.class.getDeclaredField("e");// connection
            networkManagerField.setAccessible(true);

            NetworkManager networkManager = (NetworkManager) networkManagerField.get(serverCommonPacketListener);

            return networkManager.n;
        }catch (Exception ignore){}
        return null;
    }

    private void onPacketSend(Player player, Packet<?> packet) {
        InventoryUpdateEvent event = new InventoryUpdateEvent(player);
        Bukkit.getPluginManager().callEvent(event);
    }

    @Override
    public void removePlayers() {
        Bukkit.getOnlinePlayers().forEach(player -> getPipeline((CraftPlayer) player).remove(((CraftPlayer) player).getName()));
    }

    public static class HoloDisplayText implements HoloTextDisplay{
        private final List<Player> players;
        private final Display.TextDisplay textDisplay;
        private Location location;
        private final Packet<?> spawnPacket;

        public HoloDisplayText(List<Player> players, Location location, float yaw, float pitch){
            this.players = players;
            this.location = location;
            WorldServer world = ((CraftWorld) Objects.requireNonNull(location.getWorld())).getHandle();
            this.textDisplay = new Display.TextDisplay(EntityTypes.bb, world);
            spawnPacket =  new PacketPlayOutSpawnEntity(textDisplay.al(), textDisplay.cz(), location.getX(), location.getY(), location.getZ(), yaw, pitch, textDisplay.ak(), 0, textDisplay.ds(), textDisplay.cs());

            players.forEach(player -> {
                ((CraftPlayer)player).getHandle().c.b(spawnPacket);
            });
        }

        @Override
        public void update(Location location) {
            if(players.isEmpty()) return;
            this.location = location;
            textDisplay.o(location.getX(), location.getY(), location.getZ()); //setPosRaw
            textDisplay.a_(location.getX(), location.getY(), location.getZ());
            PacketPlayOutEntityTeleport teleport = new PacketPlayOutEntityTeleport(textDisplay);
            players.forEach(player -> {
                ((CraftPlayer)player).getHandle().c.b(teleport);
            });

            internalUpdate();
        }

        @Override
        public void remove() {
            if(players.isEmpty()) return;
            PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(textDisplay.al()); //textDisplay.al() = getId()
            players.forEach(player -> {
                ((CraftPlayer)player).getHandle().c.b(destroy);
            });
        }

        @Override
        public void setText(String text) {
            textDisplay.c(CraftChatMessage.fromString(text, true)[0]);
        }

        @Override
        public void setColor(HoloColor color) {
            int colorValue = color == null ? -1 : color.asARGB();
            textDisplay.ap().a(Display.TextDisplay.aP, colorValue); //CraftTextDisplay.setBackgroundColor
        }

        @Override
        public void setGlowing(boolean glowing) {
            textDisplay.i(glowing);
        }

        @Override
        public void setScale(float x, float y, float z) {
            Transformation nms = Display.a(textDisplay.ap()); // ar = getDataWatcher
            Transformation transformation = new Transformation(nms.d(), nms.e(), new Vector3f(x, y, z), nms.g());
            textDisplay.a(transformation);
        }

        @Override
        public void setRotation(float x, float y, float z) {
            Transformation nms = Display.a(textDisplay.ap());
            Quaternionf quaternionf = new Quaternionf();
            quaternionf.rotateXYZ((float) Math.toRadians(x), (float) Math.toRadians(y), (float) Math.toRadians(z));
            Transformation transformation = new Transformation(nms.d(), quaternionf, nms.f(), nms.g());
            textDisplay.a(transformation);
        }

        @Override
        public void setRotation(float v, float v2) {
            if(Float.isNaN(v))
                v = 0.0F;

            if(Float.isNaN(v2))
                v2 = 0.0F;

            textDisplay.t(v % 360.0F);
            textDisplay.u(v2 % 360.0F);
        }

        @Override
        public void setSeeThrough(boolean seeThrough) {
            setFlag(2, seeThrough);
        }

        @Override
        public void setLineWidth(int width) {
            textDisplay.ap().a(Display.TextDisplay.aO, width);
        }

        @Override
        public void setOpacity(byte opacity) {
            textDisplay.c(opacity);
        }

        @Override
        public void setShadowed(boolean shadow) {
            setFlag(1, shadow);
        }

        @Override
        public void setAlignment(TextDisplay.TextAlignment alignment) {
            switch (alignment) {
                case LEFT:
                    setFlag(8, true);
                    setFlag(16, false);
                    return;
                case RIGHT:
                    setFlag(8, false);
                    setFlag(16, true);
                    return;
                case CENTER:
                    setFlag(8, false);
                    setFlag(16, false);
            }
        }

        @Override
        public void setTranslation(float x, float y, float z) {
            Transformation nms = Display.a(textDisplay.ap());
            Transformation transformation = new Transformation(new Vector3f(x, y, z), nms.e(), nms.f(), nms.g());
            textDisplay.a(transformation);
        }

        @Override
        public void update() {
            internalUpdate();
        }


        @Override
        public Location getLocation() {
            return location;
        }

        @Override
        public void setBrightness(org.bukkit.entity.Display.Brightness bukkitBrightness) {
            Brightness brightness = new Brightness(bukkitBrightness.getBlockLight(), bukkitBrightness.getSkyLight());
            textDisplay.a(brightness);
        }

        @Override
        public void mount(Player target) {
            Entity entityPlayer = ((CraftPlayer)target).getHandle();
            List<Entity> list = new ArrayList<>(entityPlayer.p);
            if(!list.contains(textDisplay))
                list.add(textDisplay);

            entityPlayer.p = ImmutableList.copyOf(list);
            PacketPlayOutMount packet = new PacketPlayOutMount(entityPlayer);

            players.forEach(player -> ((CraftPlayer)player).getHandle().c.b(packet));
        }

        @Override
        public void setBillboard(org.bukkit.entity.Display.Billboard billboard) {
            textDisplay.a(Display.BillboardConstraints.valueOf(billboard.name()));
        }

        public void internalUpdate(){
            PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(textDisplay.al(), textDisplay.ap().c());
            players.forEach(player -> {
                ((CraftPlayer)player).getHandle().c.b(metadata);
            });
        }

        public void setFlag(int flag, boolean set){
            byte flagBits = textDisplay.y();
            if (set) {
                flagBits = (byte)(flagBits | flag);
            } else {
                flagBits = (byte)(flagBits & (~flag));
            }
            textDisplay.d(flagBits);
        }
    }

    public static class HoloDisplayItem implements HoloItemDisplay{

        private List<Player> players = new ArrayList<>();
        private final Display.ItemDisplay itemDisplay;
        private Location location;
        private final Packet<?> spawnPacket;

        public HoloDisplayItem(List<Player> players, Location location, float yaw, float pitch){
            this.players = players;
            this.location = location;
            WorldServer world = ((CraftWorld) Objects.requireNonNull(location.getWorld())).getHandle();
            this.itemDisplay = new Display.ItemDisplay(EntityTypes.ah, world);
            spawnPacket = new PacketPlayOutSpawnEntity(itemDisplay.al(), itemDisplay.cz(), location.getX(), location.getY(), location.getZ(), pitch, yaw, itemDisplay.ak(), 0, itemDisplay.ds(), itemDisplay.cs());

            players.forEach(player -> {
                ((CraftPlayer)player).getHandle().c.b(spawnPacket);
            });
        }

        @Override
        public void update(Location location) {
            if(players.isEmpty()) return;
            this.location = location;
            itemDisplay.o(location.getX(), location.getY(), location.getZ()); //setPosRaw
            itemDisplay.a_(location.getX(), location.getY(), location.getZ());
            PacketPlayOutEntityTeleport teleport = new PacketPlayOutEntityTeleport(itemDisplay);
            players.forEach(player -> {
                ((CraftPlayer)player).getHandle().c.b(teleport);
            });

            internalUpdate();
        }

        @Override
        public void remove() {
            if(players.isEmpty()) return;
            PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(itemDisplay.al());
            players.forEach(player -> {
                ((CraftPlayer)player).getHandle().c.b(destroy);
            });
        }

        @Override
        public void setGlowing(boolean glowing) {
            itemDisplay.i(glowing);
        }

        @Override
        public void setScale(float x, float y, float z) {
            Transformation nms = Display.a(itemDisplay.ap());
            Transformation transformation = new Transformation(nms.d(), nms.e(), new Vector3f(x, y, z), nms.g());
            itemDisplay.a(transformation);
        }

        @Override
        public void setRotation(float x, float y, float z) {
            Transformation nms = Display.a(itemDisplay.ap());
            Quaternionf quaternionf = new Quaternionf();
            quaternionf.rotateXYZ((float) Math.toRadians(x), (float) Math.toRadians(y), (float) Math.toRadians(z));
            Transformation transformation = new Transformation(nms.d(), quaternionf, nms.f(), nms.g());
            itemDisplay.a(transformation);
        }

        @Override
        public void setTranslation(float x, float y, float z) {
            Transformation nms = Display.a(itemDisplay.ap());
            Transformation transformation = new Transformation(new Vector3f(x, y, z), nms.e(), nms.f(), nms.g());
            itemDisplay.a(transformation);
        }

        @Override
        public void setRotation(float v, float v2) {
            if(Float.isNaN(v))
                v = 0.0F;

            if(Float.isNaN(v2))
                v2 = 0.0F;

            itemDisplay.t(v % 360.0F);
            itemDisplay.u(v2 % 360.0F);
        }

        @Override
        public void update() {
            internalUpdate();
        }

        @Override
        public void mount(Player target) {
            Entity entityPlayer = ((CraftPlayer)target).getHandle();
            List<Entity> list = new ArrayList<>(entityPlayer.p);
            if(!list.contains(itemDisplay))
                list.add(itemDisplay);

            entityPlayer.p = ImmutableList.copyOf(list);
            PacketPlayOutMount packet = new PacketPlayOutMount(entityPlayer);

            players.forEach(player -> {
                ((CraftPlayer)player).getHandle().c.b(packet);
            });
        }

        @Override
        public void setItemStack(ItemStack itemStack) {
            net.minecraft.world.item.ItemStack nmsgItemStack = CraftItemStack.asNMSCopy(itemStack);
            itemDisplay.a(nmsgItemStack);
            PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(itemDisplay.al(), itemDisplay.ap().c());
            players.forEach(player -> ((CraftPlayer)player).getHandle().c.b(metadata));
        }

        @Override
        public Location getLocation() {
            return location;
        }

        @Override
        public void setItemDisplayTransform(ItemDisplay.ItemDisplayTransform transform) {
            itemDisplay.a(ItemDisplayContext.k.apply(transform.ordinal()));
        }

        @Override
        public void setBillboard(org.bukkit.entity.Display.Billboard billboard) {
            itemDisplay.a(Display.BillboardConstraints.valueOf(billboard.name()));
        }

        @Override
        public void setViewRange(float range) {
            itemDisplay.b(range);
        }

        @Override
        public void setBrightness(org.bukkit.entity.Display.Brightness bukkitBrightness) {
            Brightness brightness = new Brightness(bukkitBrightness.getBlockLight(), bukkitBrightness.getSkyLight());
            itemDisplay.a(brightness);
        }

        public void internalUpdate(){
            PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(itemDisplay.al(), itemDisplay.ap().c());
            players.forEach(player -> {
                ((CraftPlayer)player).getHandle().c.b(metadata);
            });
        }
    }
}
