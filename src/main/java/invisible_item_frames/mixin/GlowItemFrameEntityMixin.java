package invisible_item_frames.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.GlowItemFrameEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(value = GlowItemFrameEntity.class)
public abstract class GlowItemFrameEntityMixin extends ItemFrameEntityMixin {

    public GlowItemFrameEntityMixin(EntityType<? extends ItemFrameEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "getAsItemStack", at = @At("HEAD"), cancellable = true)
    protected void invisible_item_frames$getAsItemStackGlowItemFrame(CallbackInfoReturnable<ItemStack> cir) {
        if(invisible_item_frame) {
            Optional<? extends Recipe<?>> optional = world.getRecipeManager().get(new Identifier("invisible_item_frames:glow_item_frame_invisible"));
            optional.ifPresent(recipe -> cir.setReturnValue(recipe.getOutput(world.getRegistryManager())));
        }
    }

}
