package hisui.milcc.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
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

@Mixin(MobEntity.class)
abstract class MilkAnythingMixin extends LivingEntity {
	protected MilkAnythingMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@ModifyReturnValue(at = @At("HEAD"), method = "interactMob")
	private ActionResult milcc$milkAnythingMixin(ActionResult original, PlayerEntity player, Hand hand) {
		if(original == ActionResult.PASS) {
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
				return ActionResult.success(this.getWorld().isClient());
			}
		}
		return original;
	}
}