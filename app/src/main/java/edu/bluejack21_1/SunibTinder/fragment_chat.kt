package edu.bluejack21_1.SunibTinder

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import edu.bluejack21_1.SunibTinder.databinding.FragmentChatBinding
import edu.bluejack21_1.SunibTinder.databinding.FragmentProfileBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragment_chat.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragment_chat : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    val db = Firebase.firestore


    private var v: FragmentChatBinding? = null
    private val binding get() = v!!
    private lateinit var sharedPref : SharedPrefConfig
    private lateinit var docId : String
    private lateinit var pp: ImageView
    private lateinit var matchList : List<String>
    private lateinit var listUrl : MutableList<String>
    private lateinit var listName : MutableList<String>
    private lateinit var listMsg : MutableList<String>

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private fun getData(callback : (Boolean) -> Unit){
        var imageUrl : Uri
        db.collection("users").document(docId).get().addOnSuccessListener {
                e ->
            imageUrl = Uri.parse(e["Profile"].toString())
            Picasso.get().load(imageUrl).into(pp)

            if (e["Match"] != null){
                matchList = e["Match"] as List<String>
            }

            for (i in matchList){
                db.collection("users").document(i).get().addOnSuccessListener {
                        e->
//                    Log.w("teshoho", "hoh " + e["Profile"].toString())
                    listName.add(e["FullName"].toString())
                    listMsg.add(e["FullName"].toString())
                    val temp = e["Profile"].toString()
                    listUrl.add(temp)
                }.addOnCompleteListener{
                    callback(true)
                }
            }

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        sharedPref = SharedPrefConfig(this.requireContext())
        docId = sharedPref.getString("Uid").toString()

        // Inflate the layout for this fragment
        v = FragmentChatBinding.inflate(inflater, container, false)
        pp = binding.imageView8

        listUrl = mutableListOf<String>()
        listMsg = mutableListOf<String>()
        listName = mutableListOf<String>()

        pp.setOnClickListener{
                view ->
            view.findNavController().navigate(R.id.fragment_profile)
        }

        getData { e->
            if (e){
//                Log.w("teshoho" , "$listUrl $listName")
                recyclerView = binding.recyclerView

                recyclerView.layoutManager = LinearLayoutManager(activity)
                val adapter = BimbingAdapter()

                adapter.listDocIds = matchList as MutableList<String>
                adapter.listImgUrl = listUrl
                adapter.listName = listName
                adapter.listMsg = listMsg

                recyclerView.adapter = adapter
            }
        }


        return v!!.root
    }


    var page = 1
    var isLoading =false
    var limit = 5

    fun getPage(){
        val start  = (page-1) * limit
        val end = (page) * limit

        for (i in start..end){

        }
    }
//
//    class RecyclerAdapter(val activity: Home ) : RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>(){
//
//
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
//            return RecyclerViewHolder(LayoutInflater.from(activity).inflate(R.layout.rv_child_chat,parent,false))
//        }
//
//        override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
//            holder.chatObj.text = activity.chatList[position]
//        }
//
//        override fun getItemCount(): Int {
//            return activity.chatList.size
//        }
//
//
//        class RecyclerViewHolder(v : View) : RecyclerView.ViewHolder(v){
//            val chatObj = v.findViewById<Button>(R.id.chatComponent)
//        }
//
//    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fragment_chat.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            fragment_chat().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}