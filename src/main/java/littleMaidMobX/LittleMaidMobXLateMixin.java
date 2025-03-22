package littleMaidMobX;

import com.gtnewhorizon.gtnhmixins.ILateMixinLoader;
import com.gtnewhorizon.gtnhmixins.LateMixin;
import cpw.mods.fml.common.Loader;

import java.util.*;

@LateMixin
public class LittleMaidMobXLateMixin implements ILateMixinLoader {
    @Override
    public String getMixinConfig() {
        return "mixins.lmmx.late.json";
    }

    @Override
    public List<String> getMixins(Set<String> loadedMods) {
        List<String> mixinList = new ArrayList<>();
        mixinList.add("EventShieldMixin");
        if (Loader.isModLoaded("backhand")){
            mixinList.add("InventoryLittleMaidMixin");
            mixinList.add("ContainerInventoryMixin");
            mixinList.add("EntityLittleMaidMixin");
        }
        return mixinList;
    }
}
