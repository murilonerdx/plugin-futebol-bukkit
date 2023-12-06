package org.utils.objects;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class PunishPlayer {
    private final UUID player;
    private final List<ItemStack> inventory; // Mudança para uma lista de ItemStack
    private final Date punishTime;
    private final boolean isTemporary;
    private final String punishType;

    public PunishPlayer(UUID player, Inventory inv, Date punishTime, boolean isTemporary, String punishType) {
        this.player = player;
        this.inventory = copyInventory(inv); // Copiando o inventário
        this.punishTime = punishTime;
        this.isTemporary = isTemporary;
        this.punishType = punishType;
    }

    private List<ItemStack> copyInventory(Inventory inv) {
        List<ItemStack> copiedInventory = new ArrayList<>();
        for (ItemStack item : inv.getContents()) {
            if (item != null) {
                copiedInventory.add(new ItemStack(item));
            }
        }
        return copiedInventory;
    }

    public UUID getPlayer() {
        return player;
    }



    public List<InventorySave> getInventory() {
        List<InventorySave> inventorySaveList = new ArrayList<>();
        inventory.forEach(t -> inventorySaveList.add(new InventorySave(t.getType().name(), t.getAmount())));
        return inventorySaveList;
    }

    public Date getPunishTime() {
        return punishTime;
    }

    public boolean isTemporary() {
        return isTemporary;
    }

    public String getPunishType() {
        return punishType;
    }
}

