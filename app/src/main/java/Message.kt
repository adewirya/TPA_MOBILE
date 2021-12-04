import android.net.Uri
import java.net.URI

class Message {
    constructor() //empty for firebase

    constructor(messageText: String, sId: String, rId: String, d: String, ii: String){
        text = messageText
        senderId = sId
        receiverId = rId
        date = d
        isImage = ii
    }
    var receiverId : String? = null
    var senderId : String? = null
    var text: String? = null
    var date : String? = null
    var isImage : String? = null
    var timestamp: Long = System.currentTimeMillis()
}