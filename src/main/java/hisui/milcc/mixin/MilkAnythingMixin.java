package hisui.milcc.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
abstract class MilkAnythingMixin extends LivingEntity {
	protected MilkAnythingMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(at = @At("HEAD"), method = "interactMob", cancellable = true)
	private void milcc$milkAnythingMixin(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
		ItemStack itemStack = player.getStackInHand(hand);
		if (itemStack.isOf(Items.BUCKET) && !this.isBaby()) {
			player.playSound(SoundEvents.ENTITY_COW_MILK, 1.0f, 1.0f);
			ItemStack outputStack = new ItemStack(Items.MILK_BUCKET);

			NbtList lore = outputStack.getOrCreateSubNbt("display").getList("Lore", NbtElement.STRING_TYPE);
			lore.add(NbtString.of(Text.Serializer.toJson(Text.empty().append(this.getName()).setStyle(Style.EMPTY.withColor(Formatting.GRAY)))));
			lore.add(NbtString.of("Hello"));
			outputStack.getOrCreateSubNbt("display").put("Lore", lore);

			ItemStack itemStack2 = ItemUsage.exchangeStack(itemStack, player, outputStack);


			player.setStackInHand(hand, itemStack2);
			cir.setReturnValue(ActionResult.success(this.getWorld().isClient));
		}
	}
}