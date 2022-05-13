package theintership.my.main_interface.message.model

data class item_in_chat_person(
    val account_ref : String?,
    val link_avatar : String?,
    val text : String?,
    val key : String?,
    var status : Boolean
) {
}