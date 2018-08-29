package net.Indyuce.mb.bow;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.Indyuce.mb.Eff;
import net.Indyuce.mb.MoarBows;
import net.Indyuce.mb.api.MoarBow;
import net.Indyuce.mb.util.Utils;
import net.Indyuce.mb.util.VersionUtils;

public class Autobow extends MoarBow {
	public Autobow() {
		super(new String[] { "Shoots a flurry of arrows.", "The number depends on the", "bow pull force." }, 0, 8.0, "crit", new String[] { "BOW,BOW,BOW", "BOW,NETHER_STAR,BOW", "BOW,BOW,BOW" });
	}
	
	@Override
	public boolean shoot(EntityShootBowEvent e, Arrow a, Player p, ItemStack i) {
		e.setCancelled(true);
		new BukkitRunnable() {
			double ti = 0;

			public void run() {
				ti++;
				if (ti > 20 * e.getForce())
					cancel();
				
				if (!Utils.consumeAmmo(p, new ItemStack(Material.ARROW)))
					return;

				Location loc = p.getEyeLocation().clone();
				Eff.CRIT.display(.2f, .2f, .2f, 0, 6, loc, 100);
				VersionUtils.sound(p.getLocation(), "ENTITY_ARROW_SHOOT", 1, 1.5f);
				loc.setPitch(loc.getPitch() + new Random().nextInt(3) - 1);
				loc.setYaw(loc.getYaw() + new Random().nextInt(3) - 1);
				p.launchProjectile(Arrow.class).setVelocity(loc.getDirection().multiply(3.3 * e.getForce()));
			}
		}.runTaskTimer(MoarBows.plugin, 0, 2);
		return false;
	}
}