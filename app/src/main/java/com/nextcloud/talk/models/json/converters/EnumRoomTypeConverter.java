/*
 * Nextcloud Talk application
 *
 * @author Mario Danic
 * Copyright (C) 2017 Mario Danic (mario@lovelyhq.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.nextcloud.talk.models.json.converters;

import com.bluelinelabs.logansquare.typeconverters.IntBasedTypeConverter;
import com.nextcloud.talk.models.json.conversations.Conversation;

public class EnumRoomTypeConverter extends IntBasedTypeConverter<Conversation.ConversationType> {
    @Override
    public Conversation.ConversationType getFromInt(int i) {
        switch (i) {
            case 2:
                return Conversation.ConversationType.ROOM_PUBLIC_CALL;
            case 3:
                return Conversation.ConversationType.ROOM_GROUP_CALL;
            // ***************************
            // Module : Room Type
            case 22:
                return Conversation.ConversationType.ROOM_GROUP_COMMITTEE_CALL;
            case 222:
                return Conversation.ConversationType.ROOM_PUBLIC_COMMITTEE_CALL;
            case 33:
                return Conversation.ConversationType.ROOM_GROUP_PLENARY_CALL;
            case 333:
                return Conversation.ConversationType.ROOM_PUBLIC_PLENARY_CALL;
            case 20:
                return Conversation.ConversationType.ROOM_BREAKOUT_CALL;
            // ***************************
            default:
                return Conversation.ConversationType.DUMMY;
        }
//        switch (i) {
//            case 1:
//                return Conversation.ConversationType.ROOM_TYPE_ONE_TO_ONE_CALL;
//            case 2:
//                return Conversation.ConversationType.ROOM_GROUP_CALL;
//            case 3:
//                return Conversation.ConversationType.ROOM_PUBLIC_CALL;
//            case 4:
//                return Conversation.ConversationType.ROOM_SYSTEM;
//            case 222:
//                return Conversation.ConversationType.ROOM_PLENARY_CALL;
//            case 22:
//                return Conversation.ConversationType.ROOM_COMMITTEE_CALL;
//            default:
//                return Conversation.ConversationType.DUMMY;
//        }
    }

    @Override
    public int convertToInt(Conversation.ConversationType object) {
        switch (object) {
            case ROOM_TYPE_ONE_TO_ONE_CALL:
                return 1;
            case ROOM_GROUP_CALL: //This is the STAFF_CALL
                return 2;
            case ROOM_PUBLIC_CALL:
                return 3;
            case ROOM_SYSTEM:
                return 4;
            // ***************************
            // Module : Room Type
            case ROOM_GROUP_COMMITTEE_CALL:
                return 22;
            case ROOM_PUBLIC_COMMITTEE_CALL:
                return 222;
            case ROOM_GROUP_PLENARY_CALL:
                return 33;
            case ROOM_PUBLIC_PLENARY_CALL:
                return 333;
            case ROOM_BREAKOUT_CALL:
                return 20;
            // ***************************
            default:
                return 0;
        }
    }
}
