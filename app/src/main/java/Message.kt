class Message {
    constructor() //empty for firebase

    constructor(messageText: String, sId: String, rId: String, d: String){
        text = messageText
        senderId = sId
        receiverId = rId
        date = d
    }
    var receiverId : String? = null
    var senderId : String? = null
    var text: String? = null
    var date : String? = null
    var timestamp: Long = System.currentTimeMillis()
}