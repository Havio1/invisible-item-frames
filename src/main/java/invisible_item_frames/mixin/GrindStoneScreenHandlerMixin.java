package invisible_item_frames.mixin;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.Items;
import net.minecraft.screen.GrindstoneScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GrindstoneScreenHandler.class)
public abstract class GrindStoneScreenHandlerMixin extends ScreenHandler {

    protected GrindStoneScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
    }

    @Inject(method = "onContentChanged", at = @At("HEAD"), cancellable = true)
    public void contentChanged(Inventory inventory, CallbackInfo ci) {
        if(inventory.count(Items.ITEM_FRAME) > 0 || inventory.count(Items.GLOW_ITEM_FRAME) > 0) {
            ci.cancel();
        }
    }

}
