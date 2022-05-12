package com.koisv.onlyone.commands

import com.koisv.onlyone.status
import io.github.monun.kommand.node.LiteralNode
import org.bukkit.entity.Player

object Craft {

    fun register(node: LiteralNode) {
        node.requires { status }
        node.executes {
            val host = sender as Player
            host.openWorkbench(null,true)
        }
    }
}