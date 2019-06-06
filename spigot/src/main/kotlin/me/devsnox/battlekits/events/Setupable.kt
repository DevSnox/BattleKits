/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package me.devsnox.battlekits.events

import org.bukkit.plugin.Plugin

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 06.06.2019 18:20.
 * Last edit 06.06.2019
 */
interface Setupable {
    fun setup(plugin: Plugin)
}