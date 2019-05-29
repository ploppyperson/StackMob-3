package uk.antiperson.stackmob.checks.trait;

import org.bukkit.Material;
import org.bukkit.entity.Drowned;
import org.bukkit.entity.Entity;
import uk.antiperson.stackmob.api.checks.TraitManager;
import uk.antiperson.stackmob.api.checks.ApplicableTrait;

import java.util.Arrays;
import java.util.List;

public class DrownedTrait implements ApplicableTrait {

    private List<Material> materials = Arrays.asList(Material.NAUTILUS_SHELL, Material.TRIDENT);
    public String getConfigPath(){
        return "compare.drowned-hand-item";
    }

    @Override
    public boolean checkTrait(Entity original, Entity nearby) {
        if(original instanceof Drowned){
            Drowned oriDrowned = (Drowned) original;
            Drowned nearDrowned = (Drowned) nearby;
            if(materials.contains(oriDrowned.getEquipment().getItemInMainHand().getType()) ||
                    materials.contains(nearDrowned.getEquipment().getItemInMainHand().getType())){
                if(oriDrowned.getEquipment().getItemInMainHand().getType() !=
                        nearDrowned.getEquipment().getItemInMainHand().getType()){
                    return true;
                }
            }
            if(materials.contains(oriDrowned.getEquipment().getItemInOffHand().getType()) ||
                    materials.contains(nearDrowned.getEquipment().getItemInOffHand().getType())){
                return oriDrowned.getEquipment().getItemInOffHand().getType() !=
                        nearDrowned.getEquipment().getItemInOffHand().getType();
            }
        }
        return false;
    }

    @Override
    public void applyTrait(Entity original, Entity spawned) {
        if(original instanceof Drowned){
            Drowned oriDrowned = (Drowned) original;
            Drowned spawnDrowned = (Drowned) spawned;
            if(materials.contains(oriDrowned.getEquipment().getItemInMainHand().getType())){
                spawnDrowned.getEquipment().setItemInMainHand(oriDrowned.getEquipment().getItemInMainHand());
            }
            if(materials.contains(oriDrowned.getEquipment().getItemInOffHand().getType())){
                spawnDrowned.getEquipment().setItemInOffHand(oriDrowned.getEquipment().getItemInOffHand());
            }
        }
    }
}
