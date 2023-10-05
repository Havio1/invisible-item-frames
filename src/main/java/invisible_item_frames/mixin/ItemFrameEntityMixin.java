package invisible_item_frames.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(value = ItemFrameEntity.class)
public abstract class ItemFrameEntityMixin extends AbstractDecorationEntity {

    @Shadow
    public abstract void writeCustomDataToNbt(NbtCompound nbt);

    @Shadow
    public abstract ItemStack getHeldItemStack();

    @Unique
    protected boolean invisible_item_frame;

    protected ItemFrameEntityMixin(EntityType<? extends AbstractDecorationEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "dropHeldStack", at = @At("HEAD"))
    public void onDropStack(Entity entity, boolean alwaysDrop, CallbackInfo ci) {
        if (invisible_item_frame) {
            setInvisible(false);
        }
    }

    @Inject(method = "onPlace", at = @At("HEAD"))
    public void onPlace(CallbackInfo ci) {
        invisible_item_frame = isInvisible();
        if (invisible_item_frame && getHeldItemStack().isEmpty()) {
            setInvisible(false);
        }
    }

    @Inject(method = "interact", at = @At("TAIL"))
    public void interact(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (invisible_item_frame && !getHeldItemStack().isEmpty() && !isInvisible()) {
            setInvisible(true);
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("invisible_item_frames", invisible_item_frame);
    }

    @Inject(method = "getAsItemStack", at = @At("HEAD"), cancellable = true)
    public void getAsItemStack(CallbackInfoReturnable<ItemStack> cir) {
        if(invisible_item_frame) {
            Optional<? extends Recipe<?>> optional = world.getRecipeManager().get(new Identifier("ciif:invisible_item_frame"));
            optional.ifPresent(recipe -> cir.setReturnValue(recipe.getOutput()));
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        invisible_item_frame = nbt.getBoolean("invisible_item_frames");
    }
}
