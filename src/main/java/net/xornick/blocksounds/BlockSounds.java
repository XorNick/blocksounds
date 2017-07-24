package net.xornick.blocksounds;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.EnumMap;
import java.util.Map;

public final class BlockSounds extends JavaPlugin implements Listener {

    private Map<Material, Sound> soundMap = new EnumMap<Material, Sound>(Material.class);

    @Override
    public void onEnable() {
        saveDefaultConfig();
        fillSoundMap();
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (soundMap.containsKey(block.getType())) {
            Sound toPlay = soundMap.get(block.getType());
            player.playSound(block.getLocation(), toPlay, 1F, 1F);
        }
    }

    private void fillSoundMap() {
        soundMap.clear();

        for (String mat : getConfig().getKeys(false)) {
            Material material = Material.valueOf(mat.toUpperCase());
            Sound sound = Sound.valueOf(getConfig().getString(mat));

            if (material == null) {
                getLogger().warning("Material " + mat + " is invalid and was skipped.");
            } else if (sound == null) {
                getLogger().warning("Sound for material " + mat + " is invalid and was skipped.");
            } else {
                soundMap.put(material, sound);
            }
        }
    }
}
