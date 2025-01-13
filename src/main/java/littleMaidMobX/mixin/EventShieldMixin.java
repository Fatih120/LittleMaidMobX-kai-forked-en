package littleMaidMobX.mixin;

import com.meteor.extrabotany.common.event.EventShield;
import littleMaidMobX.LMM_IEntityLittleMaidAvatarBase;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EventShield.class, remap = false)
public class EventShieldMixin {

    @Inject(method = "onPlayerAttacked",
            at = @At(value = "INVOKE",
                    target = "Lcom/meteor/extrabotany/common/core/handler/PropertyHandler;getShieldAmount(Lnet/minecraft/entity/player/EntityPlayer;)F",
            ordinal = 0),
            remap = false,
            cancellable = true)
    private void playerAttacked(LivingHurtEvent event, CallbackInfo ci){
        if (event.entity instanceof LMM_IEntityLittleMaidAvatarBase){
            ci.cancel();
        }
    }
    @Inject(method = "onEntityConstructing",
            at = @At("HEAD"),
            remap = false,
            cancellable = true)
    private void entityConstructing(EntityEvent.EntityConstructing event, CallbackInfo ci){
        if (event.entity instanceof LMM_IEntityLittleMaidAvatarBase){
            ci.cancel();
        }
    }
}
