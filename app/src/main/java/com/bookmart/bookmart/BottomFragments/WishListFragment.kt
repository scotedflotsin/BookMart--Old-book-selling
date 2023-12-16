import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bookmart.bookmart.Adapters.WishlistFragment_Adapters.WishList_Adapter
import com.bookmart.bookmart.Model.HomeFragment_Models.TopAds_Model
import com.bookmart.bookmart.Model.WishListFragment_Models.Wishlist_Model
import com.bookmart.bookmart.R
import com.bookmart.bookmart.databinding.FragmentWishListBinding

class WishListFragment:Fragment(R.layout.fragment_wish_list){
    val binding by lazy {
        FragmentWishListBinding.inflate(layoutInflater)
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
        binding.recininininin.layoutManager=layoutManager

        var data= ArrayList<Wishlist_Model>()

        data.add(Wishlist_Model(R.drawable.bookdemo,"Rs math", "$345","2023","10th"))
        data.add(Wishlist_Model(R.drawable.bookdemo,"Rs math", "$345","2023","10th"))
        data.add(Wishlist_Model(R.drawable.bookdemo,"Rs math", "$345","2023","10th"))
        data.add(Wishlist_Model(R.drawable.bookdemo,"Rs math", "$345","2023","10th"))
        data.add(Wishlist_Model(R.drawable.bookdemo,"Rs math", "$345","2023","10th"))


        var adapter=WishList_Adapter(data)

        binding.recininininin.adapter=adapter



    }



}