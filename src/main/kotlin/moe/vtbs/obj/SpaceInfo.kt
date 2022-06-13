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
package moe.vtbs.obj

import com.google.gson.JsonObject
import moe.vtbs.lang.annotation.Blocked
import moe.vtbs.util.Image

/**
 *  空间信息
 *
 * @author 一七年夏
 * @since 2022-06-12 23:55
 */
class SpaceInfo(val json: JsonObject) {
    val data get() = json["data"].asJsonObject
    val mid get() = data["mid"].asInt
    val name get() = data["name"].asString
    val sex get() = data["sex"].asString
    val face get() = data["face"].asString
    val faceNft get() = data["faceNft"].asInt
    val faceNftType get() = data["faceNftType"].asInt
    val sign get() = data["sign"].asString
    val rank get() = data["rank"].asInt
    val level get() = data["level"].asInt
    val joinTime get() = data["jointime"].asInt
    val moral get() = data["moral"].asInt
    val silence get() = data["silence"].asInt
    val coins get() = data["coins"].asInt
    val fansBadge get() = data["fans_badge"].asBoolean
    val fansMedal get() = data["fans_medal"]
    val official get() = data["official"]
    val vip get() = data["vip"]
    val pendant get() = data["pendant"]
    val nameplate get() = data["nameplate"]
    val userHonourInfo get() = data["user_honour_info"]
    val isFollowed get() = data["is_followed"].asBoolean
    val topPhoto get() = data["top_photo"].asString

    @Blocked
    fun getTopPhotoImage() = Image.getImage(topPhoto)
    suspend fun getTopPhotoImageAsync() = Image.getImageAsync(topPhoto)
    val theme get() = data["theme"]
    val sysNotice get() = data["sys_notice"]
    val liveRoom = LiveRoom(data["live_room"].asJsonObject)
    val birthday get() = data["birthday"].asString
    val school get() = data["school"]
    val profession get() = data["profession"]
    val tags get() = data["tags"]
    val series get() = data["series"]
    val isSeniorMember get() = data["is_senior_member"]

    class LiveRoom(val json: JsonObject) {
        val roomStatus get() = json["roomStatus"].asInt == 1
        val liveStatus get() = json["liveStatus"].asInt == 1
        val url get() = json["url"].asString
        val title get() = json["title"].asString
        val cover get() = json["cover"].asString

        @Blocked
        fun getCoverImage() = Image.getImage(cover)
        suspend fun getCoverImageAsync() = Image.getImageAsync(cover)
        val roomid get() = json["roomid"]
        val roundStatus get() = json["roundStatus"]
        val broadcastType get() = json["broadcast_type"]
        val watchedShow get() = json["watched_show"]
    }
}