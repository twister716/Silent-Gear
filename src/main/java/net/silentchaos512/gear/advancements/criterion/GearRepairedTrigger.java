package net.silentchaos512.gear.advancements.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;

import java.util.Optional;

public class GearRepairedTrigger extends SimpleCriterionTrigger<GearRepairedTrigger.Instance> {
    @Override
    public Codec<Instance> codec() {
        return Instance.CODEC;
    }

    public void trigger(ServerPlayer player, int brokenCount, int repairedCount) {
        this.trigger(player, instance -> instance.matches(brokenCount, repairedCount));
    }

    public record Instance(
            Optional<ContextAwarePredicate> player,
            MinMaxBounds.Ints brokenCount,
            MinMaxBounds.Ints repairedCount
    ) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<Instance> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        ExtraCodecs.strictOptionalField(EntityPredicate.ADVANCEMENT_CODEC, "player").forGetter(Instance::player),
                        ExtraCodecs.strictOptionalField(MinMaxBounds.Ints.CODEC, "broken_count", MinMaxBounds.Ints.ANY).forGetter(Instance::brokenCount),
                        ExtraCodecs.strictOptionalField(MinMaxBounds.Ints.CODEC, "repaired_count", MinMaxBounds.Ints.ANY).forGetter(Instance::repairedCount)
                ).apply(instance, Instance::new)
        );

        public boolean matches(int brokenCountIn, int repairedCountIn) {
            return this.brokenCount.matches(brokenCountIn) && this.repairedCount.matches(repairedCountIn);
        }
    }
}
