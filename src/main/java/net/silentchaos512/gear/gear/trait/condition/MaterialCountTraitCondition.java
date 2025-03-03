package net.silentchaos512.gear.gear.trait.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.silentchaos512.gear.api.traits.ITraitCondition;
import net.silentchaos512.gear.api.traits.TraitConditionSerializer;
import net.silentchaos512.gear.api.traits.TraitInstance;
import net.silentchaos512.gear.api.util.GearComponentInstance;
import net.silentchaos512.gear.api.util.PartGearKey;
import net.silentchaos512.gear.gear.trait.Trait;
import net.silentchaos512.gear.util.TextUtil;

import java.util.List;

public record MaterialCountTraitCondition(int requiredCount) implements ITraitCondition {
    public static final MapCodec<MaterialCountTraitCondition> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    ExtraCodecs.POSITIVE_INT.fieldOf("count").forGetter(c -> c.requiredCount)
            ).apply(instance, MaterialCountTraitCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, MaterialCountTraitCondition> STREAM_CODEC = StreamCodec.of(
            (buf, con) -> ByteBufCodecs.BYTE.encode(buf, (byte) con.requiredCount),
            buf -> new MaterialCountTraitCondition(ByteBufCodecs.BYTE.decode(buf))
    );
    public static final TraitConditionSerializer<MaterialCountTraitCondition> SERIALIZER = new TraitConditionSerializer<>(CODEC, STREAM_CODEC);

    @Override
    public TraitConditionSerializer<?> serializer() {
        return SERIALIZER;
    }

    @Override
    public boolean matches(Trait trait, PartGearKey key, List<? extends GearComponentInstance<?>> components) {
        int count = 0;
        for (GearComponentInstance<?> comp : components) {
            for (TraitInstance inst : comp.getTraits(key)) {
                if (inst.getTrait() == trait) {
                    count++;
                    break;
                }
            }
        }
        return count >= this.requiredCount;
    }

    @Override
    public MutableComponent getDisplayText() {
        return TextUtil.translate("trait.condition", "material_count", this.requiredCount);
    }
}
