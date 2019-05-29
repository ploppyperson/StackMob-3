package uk.antiperson.stackmob.api.stick;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface IStickTools {
    void giveStackingStick(Player player);

    boolean isStackingStick(ItemStack stack);

    void performAction(Player player, Entity entity);

    void toggleMode(Player player);

    void updateStack(Player player, String input);

    void sendMessage(Player player, String message, int pitch);
}
