package ciif.mixin;

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
    public void getAsItemStack(CallbackInfoReturnable<ItemStack> cir) {
        if(ciif) {
            Optional<? extends Recipe<?>> optional = world.getRecipeManager().get(new Identifier("ciif:invisible_glow_item_frame"));
            optional.ifPresent(recipe -> cir.setReturnValue(recipe.getOutput()));
        }
    }

}
