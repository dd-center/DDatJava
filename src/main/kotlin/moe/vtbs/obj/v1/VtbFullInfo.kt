/*
 * Copyright (C) 2022 一七年夏
 * 
 * The part of program is free: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published 
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see https://www.gnu.org/licenses/
 */
package moe.vtbs.obj.v1

/**
 *  Vtuber的完整信息
 *
 * @author 一七年夏
 * @since 2022-07-05 19:02
 */
class VtbFullInfo {
    var mid = 0
    var userName = ""
    var video = 0
    var roomID = 0
    var sign = ""
    var notice = ""
    var face = ""
    var rise = 0
    var topPhoto = ""
    var archiveView = 0
    var follower = 0
    var liveStatus = 0
    var recordNum = 0
    var guardNum = 0
    var lastLive = 0L
    var guardChange = 0
    var guardType = intArrayOf()
    var areaRank = 0
    var online = 0
    var title = ""
    var time = 0L
    var liveStartTime = 0L
    var uuid = ""
    var vdb = VtuberDatabase()

    class VtuberDatabase {
        var uuid = ""
        var type = ""
        var bot = false
        var accounts = Accounts()
        var group = ""
        var name = Name()

        class Accounts {
            var id = ""
            var type = ""
            var platform = ""
        }

        class Name {
            var extra: Any? = null
            var cn = ""
            var default = ""
        }
    }
}