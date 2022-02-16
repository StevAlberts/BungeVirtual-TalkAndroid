package com.nextcloud.talk.models.kikaoutitilies

data class FetchedVotes (
    val vote: Vote
)

data class Vote (
    val id: String,
    val type: String,
    val description: String,
    val owner: String,
    val created: String,
    val expire: String,
    val deleted: String,
    val access: String,
    val anonymous: String,
    val allowMaybe: String,
    val voteLimit: String,
    val showResults: String,
    val adminAccess: String,
    val important: String,
    val optionLimit: String,
    val allowComment: String,
    val hideBookedUp: String,
    val allowProposals: String,
    val useNo: String,
    val proposalsExpire: String,
    val title: String,
    val voteType: String,
    val notifMins: String,
    val openingTime: String,
    val meetingName: String,
    val meetingID: String,
    val miscSettings: String,
    val voteCleared: String,
    val serverTime: Long
)
