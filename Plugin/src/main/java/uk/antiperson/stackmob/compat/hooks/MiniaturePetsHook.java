package uk.antiperson.stackmob.compat.hooks;

import com.kirelcodes.miniaturepets.api.APIUtils;
import org.bukkit.entity.Entity;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.compat.HookManager;
import uk.antiperson.stackmob.compat.PluginHook;
import uk.antiperson.stackmob.api.compat.PluginCompat;
import uk.antiperson.stackmob.api.compat.Testable;

public class MiniaturePetsHook extends PluginHook implements Testable {

    public MiniaturePetsHook(HookManager hm, StackMob sm){
        super(hm, sm, PluginCompat.MINIATUREPETS);
    }

    @Override
    public void enable(){
        if(getStackMob().getCustomConfig().getBoolean("check.is-miniature-pet")){
            if(isMiniPetCorrectVersion()){
                getHookManager().registerHook(getPluginCompat(), this);
            }else{
                getStackMob().getLogger().warning("A version of MiniaturePets has been detected that is not supported!");
                getStackMob().getLogger().warning("MiniaturePets related mob checks will not work!");
            }
        }
    }

    @Override
    public boolean cantStack(Entity entity){
        return isMiniPet(entity);
    }

    private boolean isMiniPet(Entity entity){
        return APIUtils.isEntityMob(entity);
    }

    public boolean isMiniPetCorrectVersion(){
        try {
            Class.forName("com.kirelcodes.miniaturepets.APIUtils");
            return true;
        }catch (ClassNotFoundException e){
            return false;
        }
    }
}
