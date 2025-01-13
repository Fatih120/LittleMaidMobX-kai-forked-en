package littleMaidMobX;

import com.gtnewhorizon.gtnhmixins.ILateMixinLoader;
import com.gtnewhorizon.gtnhmixins.LateMixin;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@LateMixin
public class LittleMaidMobXLateMixin implements ILateMixinLoader {
    @Override
    public String getMixinConfig() {
        return "mixins.lmmx.late.json";
    }

    @Override
    public List<String> getMixins(Set<String> loadedMods) {
        return Arrays.asList("EventShieldMixin");
    }
}
