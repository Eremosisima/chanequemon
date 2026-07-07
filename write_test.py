import os

bp = os.path.expandvars(r'%LOCALAPPDATA%\Packages\Microsoft.MinecraftUWP_8wekyb3d8bbwe\LocalState\games\com.mojang\development_behavior_packs\chanequemon_test')

script_content = """import { world } from "@minecraft/server";

world.afterEvents.playerSpawn.subscribe(function(event) {
  if (event.initialSpawn) {
    event.player.sendMessage("\\u00a7a[TEST] Script loaded!");
  }
});

world.afterEvents.chatSend.subscribe(function(event) {
  if (event.message === "!test") {
    event.sender.sendMessage("\\u00a7a[TEST] It works!");
  }
});
"""

with open(os.path.join(bp, 'scripts', 'main.js'), 'w', encoding='utf-8') as f:
    f.write(script_content)
print('Script written OK')
