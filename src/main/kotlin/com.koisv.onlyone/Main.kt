package com.koisv.onlyone

import com.koisv.onlyone.commands.Craft
import com.koisv.onlyone.commands.Theone
import io.github.monun.kommand.kommand
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.logging.Level

var status : Boolean = false

class Main : JavaPlugin() {

    companion object {
        lateinit var craftLoc: File
            private set
        lateinit var craft: YamlConfiguration
            private set
    }

    override fun onEnable() {
        if (server.minecraftVersion.split(".")[1].toInt() < 18) {
            pluginLoader.disablePlugin(this)
            logger.log(Level.WARNING,"1.18 이상에서만 사용 가능 합니다.")
        }
        logger.log(Level.INFO,"Starting Up... | v${description.version}")
        server.pluginManager.registerEvents(Events(),this)

        craftLoc = File(dataFolder,"craftTable.yml")
        craft = YamlConfiguration.loadConfiguration(craftLoc)
        if (!craftLoc.canRead()) {
            craft.save(craftLoc)
        }

        kommand {
            register("onlyone") {
                Theone.register(this)
            }
            register("조합","craft","whgkq","ㅊㄱㅁㄽ") {
                Craft.register(this)
            }
        }
    }

    override fun onDisable() {
        logger.log(Level.INFO,"Shutting Down... | v${description.version}")
    }
}