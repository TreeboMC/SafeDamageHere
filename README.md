# SafeDamageHere
Blocks a single type of damage (eg fall damage) in a specified region.
Damage types available at https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/event/entity/EntityDamageEvent.DamageCause.html


##Commands
  /sdh <region name> setA  -  Stand in a place and set corner A of a region.
  /sdh <region name> setB  -  Stand in a place and set corner B of a region.
  /sdh <region name> delete - Delete a region by name.
  /sdh <region name> confirm <damage type> - Save region and specify the type of damage to block.

##Permissions
  safedamagehere.updatechecker - 'Issues update check when player logs in'
  safedamagehere.reload - 'Allows a player to reload the plugin config.'
  safedamagehere.configure - 'Allows a player to create damage exclusion zones.'

All permissions default to OP
