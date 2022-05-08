package com.koisv.onlyone

import com.koisv.onlyone.commands.Theone
import io.github.monun.kommand.kommand
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Level

var status : Boolean = false

class Main : JavaPlugin() {

    override fun onEnable() {
        if (server.minecraftVersion.split(".")[1].toInt() < 18) {
            pluginLoader.disablePlugin(this)
            logger.log(Level.WARNING,"1.18 이상에서만 사용 가능 합니다.")
        }
        logger.log(Level.INFO,"v${description.version} Starting Up...")
        server.pluginManager.registerEvents(Events(),this)
        kommand {
            register("onlyone") {
                Theone.register(this)
            }
        }
    }

    override fun onDisable() {
        logger.log(Level.INFO,"v${description.version} Shutting Down...")
    }
}