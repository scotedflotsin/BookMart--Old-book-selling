import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.bookmart.bookmart.Adapters.HistoryFragment_Adapters.Ads_History_Adapter
import com.bookmart.bookmart.Model.HistoryFragment_Models.Ads_medium_History_Models
import com.bookmart.bookmart.Model.HomeFragment_Models.TopAds_Model
import com.bookmart.bookmart.R
import com.bookmart.bookmart.databinding.FragmentHistoryFragmentsBinding

class HistoryFragment:Fragment(R.layout.fragment_history_fragments) {
    val binding by lazy {
        FragmentHistoryFragmentsBinding.inflate(layoutInflater)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager=LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        binding.recyclerView.layoutManager=layoutManager


        var data=ArrayList<Ads_medium_History_Models>()

        data.add(Ads_medium_History_Models(R.drawable.bookdemo,"Rs math", "$345","2023","10th"))
        data.add(Ads_medium_History_Models(R.drawable.bookdemo,"Rs math", "$345","2023","10th"))
        data.add(Ads_medium_History_Models(R.drawable.bookdemo,"Rs math", "$345","2023","10th"))
        data.add(Ads_medium_History_Models(R.drawable.bookdemo,"Rs math", "$345","2023","10th"))
        data.add(Ads_medium_History_Models(R.drawable.bookdemo,"Rs math", "$345","2023","10th"))

        var adapter=Ads_History_Adapter(data)
        binding.recyclerView.adapter=adapter


    }

}