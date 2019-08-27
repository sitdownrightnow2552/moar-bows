package net.Indyuce.moarbows.bow;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.Indyuce.moarbows.api.ArrowData;
import net.Indyuce.moarbows.api.LinearValue;
import net.Indyuce.moarbows.api.MoarBow;
import net.Indyuce.moarbows.api.modifier.DoubleModifier;

public class Ice_Bow extends MoarBow {
	public Ice_Bow() {
		super(new String[] { "Shoots ice arrows that cause an ice", "explosion upon landing, temporarily", "slowing every nearby entity." }, 0, "snow_shovel", new String[] { "ICE,ICE,ICE", "ICE,BOW,ICE", "ICE,ICE,ICE" });

		addModifier(new DoubleModifier("cooldown", new LinearValue(0, 0)), new DoubleModifier("amplifier", new LinearValue(2, .4)), new DoubleModifier("duration", new LinearValue(5, 1)));
	}

	@Override
	public boolean canShoot(EntityShootBowEvent event, ArrowData data) {
		return true;
	}

	@Override
	public void whenHit(EntityDamageByEntityEvent event, ArrowData data, Entity target) {
		whenLand(data);
	}

	@Override
	public void whenLand(ArrowData data) {
		int duration = (int) (data.getDouble("duration") * 20);
		int amplifier = (int) data.getDouble("amplifier");

		data.getArrow().remove();
		data.getArrow().getWorld().spawnParticle(Particle.EXPLOSION_LARGE, data.getArrow().getLocation(), 0);
		data.getArrow().getWorld().spawnParticle(Particle.SNOW_SHOVEL, data.getArrow().getLocation(), 48, 0, 0, 0, .2);
		data.getArrow().getWorld().spawnParticle(Particle.FIREWORKS_SPARK, data.getArrow().getLocation(), 24, 0, 0, 0, .2);
		data.getArrow().getWorld().playSound(data.getArrow().getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 3, 1);
		for (Entity ent : data.getArrow().getNearbyEntities(5, 5, 5))
			if (ent instanceof LivingEntity) {
				((LivingEntity) ent).removePotionEffect(PotionEffectType.SLOW);
				((LivingEntity) ent).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, duration, amplifier));
			}
	}
}
