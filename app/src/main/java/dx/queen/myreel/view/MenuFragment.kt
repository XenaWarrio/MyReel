package dx.queen.myreel.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dx.queen.myreel.R
import dx.queen.myreel.databinding.FragmentMenuBinding
import dx.queen.myreel.viewModel.MenuViewModel
import androidx.lifecycle.Observer

class MenuFragment : Fragment() {

    lateinit var menuBinder : FragmentMenuBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        menuBinder = DataBindingUtil.inflate(
            inflater, R.layout.fragment_menu, container , false)
        val v = menuBinder.root
        menuBinder.lifecycleOwner = this
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ac = activity as MainActivity
        val menuViewModel = ViewModelProvider(this).get(MenuViewModel::class.java)
        menuBinder.menu = menuViewModel

        val  observerChat = Observer<String>{
            ac.navigateFromMenuToChats()
        }
        val observerSearch = Observer<String>{
            ac.navigateFromMenuToSearch()
        }
        val observerPersonalInformation = Observer<String>{
            ac.navigateFromMenuToPersonalInformation()
        }
        val observerExit = Observer<String>{
            ac.navigateFromMenuToRegistration()
        }

        menuViewModel.chat.observe(viewLifecycleOwner, observerChat)
        menuViewModel.search.observe(viewLifecycleOwner, observerSearch)
        menuViewModel.personalInformation.observe(viewLifecycleOwner, observerPersonalInformation)
        menuViewModel.exit.observe(viewLifecycleOwner, observerExit)
    }
}