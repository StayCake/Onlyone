package com.koisv.onlyone.commands

import com.koisv.onlyone.status
import io.github.monun.kommand.node.LiteralNode

object Theone {
    fun register(node: LiteralNode) {
        node.requires { hasPermission(4,"onlyone.help") }
        node.executes {
            sender.sendMessage("/onlyone start -> 시1작")
            sender.sendMessage("/onlyone stop -> 멈춰!")
            sender.sendMessage("현재 ${if (status) "가동중." else "정지중."}")
        }
        node.then("start") {
            requires { hasPermission(4,"onlyone.start") }
            executes {
                status = true
                sender.sendMessage("始まるぞ～")
            }
        }
        node.then("start") {
            requires { hasPermission(4,"onlyone.stop") }
            executes {
                status = false
                sender.sendMessage("止まれ！")
            }
        }
    }
}