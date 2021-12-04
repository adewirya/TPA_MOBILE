package edu.bluejack21_1.SunibTinder

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.os.HandlerCompat.postDelayed
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import edu.bluejack21_1.SunibTinder.databinding.FragmentChatBinding
import edu.bluejack21_1.SunibTinder.databinding.FragmentProfileBinding
import kotlinx.coroutines.delay

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
    private var matchList : List<String> = mutableListOf<String>()
    private var listUrl = mutableListOf<String>()
    private var listName = mutableListOf<String>()
    private var listMsg = mutableListOf<String>()




    private lateinit var recyclerView: RecyclerView
    private lateinit var pd : ProgressDialog

    private lateinit var chatSnapshot : QuerySnapshot
    private lateinit var chatTask : Task<QuerySnapshot>

    private lateinit var adapter : BimbingAdapter
    private var isLoading : Boolean = false

    private var start = 0
    private var end = 3

    private fun addRange(
        newUrls : MutableList<String>,
        newName : MutableList<String>,
        newMsg : MutableList<String>,
        adapter : RecyclerView.Adapter<*>
    ){
        val size = newUrls.size
        if (newUrls.size == 0){
            adapter.notifyItemChanged(size)
        } else{
            listUrl.addAll(newUrls)
            listName.addAll(newName)
            listMsg.addAll(newName)
            adapter.notifyItemChanged(size)
            adapter.notifyItemRangeInserted(size+1, newUrls.size - 1)
        }
    }

    private fun loadNewItem(adapter : RecyclerView.Adapter<*>){
        if (::chatTask.isInitialized && !chatTask.isComplete || ::chatSnapshot.isInitialized && chatSnapshot.isEmpty){
            return
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            var query = db.collection("users").document(docId)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private fun searchOnce(callback: (Boolean) -> Unit){
        var imageUrl : Uri
        db.collection("users").document(docId).get().addOnSuccessListener {
                e ->
            imageUrl = Uri.parse(e["Profile"].toString())
            Picasso.get().load(imageUrl).into(pp)
            pd.setMessage("Percentage : 25%")
            if (e["Match"] != null){
                matchList = e["Match"] as List<String>
            }
        }.addOnCompleteListener{
            Log.w("datatest", matchList.toString())
            callback(true)
        }
    }

    private fun getData(startPoint : Int, endPoint : Int, callback : (Boolean) -> Unit){
        pd.setTitle("Getting Data")
        pd.setMessage("Percentage : 0%")
        pd.show()

        var endPoints = endPoint

        if (matchList.isNotEmpty()){
            if (endPoints > matchList.size-1){
                endPoints = matchList.size-1
            }
        }

        pd.setMessage("Percentage : 50%")
            val temp = mutableListOf<String>()
        (startPoint..endPoints ).forEach{

            db.collection("users").document(matchList[it]).get().addOnSuccessListener { e->
                Log.w("datatest", matchList[it])
                listUrl.add(e["Profile"].toString())
                if (it == endPoints){
                    callback(true)
                }
            }
        }


            pd.setMessage("Percentage : 75%")
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
        pd = ProgressDialog(this.requireContext())


        pp.setOnClickListener{
                view ->
            view.findNavController().navigate(R.id.fragment_profile)
        }


        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)

        searchOnce {
            bl ->
            if (bl){
                getData(start,end) { e->
                    if (e){
                        assignData()
                    }
                }
            }
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled (recyclerView: RecyclerView, dx : Int, dy : Int){

                    Log.w("teshoho", "masuk scroll + ${end}")

                    val visibleItemCount = LinearLayoutManager(activity).childCount
                    val pastVisibleItem = LinearLayoutManager(activity).findFirstCompletelyVisibleItemPosition()
                    val total = adapter.itemCount

                    if (!isLoading && end < (matchList.size)){
//                        if(visibleItemCount + pastVisibleItem >= total){
                            if (visibleItemCount < matchList.size){
                                assignData()
                            }
//                        }
                    }
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        return v!!.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun assignData(){
        Log.w("teshoho" , "$listUrl, ${listUrl.size} ")
        isLoading = true
        Handler().postDelayed({
             if (::adapter.isInitialized && end <= (matchList.size-1)){
                     start += 4
                     end += 4
                     Log.w("teshaha", "masuk kedua + ${end} < ${matchList.size - 1}")
//                 if (end < (matchList.size-1)){
                     getData(start,end){
                             e->
                         if (e){
                             adapter.listImgUrl = listUrl
                             adapter.notifyDataSetChanged()
                             pd.dismiss()
                         }
                     }
//                 }
             } else {
                 Log.w("teshaha", "masuk awal")
                 adapter =  BimbingAdapter()
                 adapter.listDocIds = matchList as MutableList<String>
                 adapter.listImgUrl = listUrl
                 recyclerView.adapter = adapter
             }
        }, 5000)
        isLoading = false
        pd.setMessage("Percentage : 100%")
        pd.dismiss()
    }

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