import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.bookmart.bookmart.AboutTab_lay.About_fragment
import com.bookmart.bookmart.AboutTab_lay.Privacy_policy
import com.bookmart.bookmart.AboutTab_lay.Term_condition

class TabPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> Privacy_policy()
            1 -> About_fragment()
            else -> Term_condition()
        }
    }

    override fun getCount(): Int {
        return 3 // The number of tabs
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "About Us"
            1 -> "Privacy & policy"
            2 -> "Term & conditions"
            else -> ""
        }
    }
}
