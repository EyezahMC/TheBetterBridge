/*
 * TheBridge - Defend your base and try to wipe out the others
 * Copyright (C)  2021  Plugily Projects - maintained by Tigerpanzer_02, 2Wild4You and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package plugily.projects.thebridge.events;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;
import plugily.projects.commonsbox.minecraft.compat.xseries.XMaterial;
import plugily.projects.commonsbox.minecraft.compat.xseries.XMaterialUtil;
import plugily.projects.thebridge.Main;
import plugily.projects.thebridge.arena.Arena;
import plugily.projects.thebridge.arena.ArenaRegistry;
import plugily.projects.thebridge.arena.ArenaState;
import plugily.projects.thebridge.arena.base.BaseMenuHandler;

public class FuckErrorsEvents implements Listener {
	private final Main plugin;

	public FuckErrorsEvents(Main plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	// runs last
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInvClick(InventoryClickEvent event) {
		HumanEntity entity = event.getWhoClicked();

		if (entity instanceof Player) {
			Player player = (Player) entity;

			if (ArenaRegistry.isInArena(player)) {
				Arena arena = ArenaRegistry.getArena(player);

				if (arena.getArenaState() == ArenaState.STARTING || arena.getArenaState() == ArenaState.WAITING_FOR_PLAYERS) {
					ItemStack stack = event.getCurrentItem();
					XMaterial material = XMaterial.matchXMaterial(stack);

					if (material.getLegacy()[0].equals("WOOL")) {
						String colour = material.name().substring(0, material.name().length() - 5);
						arena.getBases().stream()
							.filter(b -> b.getMaterialColor().toUpperCase().equals(colour))
							.findFirst()
							.ifPresent(base -> BaseMenuHandler.handleInvClick(event, base, arena, this.plugin));
					}
				}
			}
		}
	}
}
