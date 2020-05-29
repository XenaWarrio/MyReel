package dx.queen.myreel.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dx.queen.myreel.R
import dx.queen.myreel.databinding.FragmentChatsBinding
import dx.queen.myreel.viewModel.ChatsViewModel
import dx.queen.myreel.viewModel.rvChats.ChatsAdapter
import dx.queen.myreel.viewModel.rvChats.ChatsItem

class ChatsFragment : Fragment() {
    private lateinit var binding: FragmentChatsBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_chats, container, false
        )
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProvider(this).get(ChatsViewModel::class.java)
        binding.chatsViewModel = viewModel

        val adapter = ChatsAdapter()
        binding.rvChatsRow.adapter = adapter
        binding.rvChatsRow.layoutManager = LinearLayoutManager(context)

        val chatOnclick = Observer<ChatsItem> {
            val ac = activity as MainActivity
            ac.openCertainChat(it)
        }

        adapter.clickOnItem.observe(viewLifecycleOwner, chatOnclick)
        adapter.updateList(viewModel.listOfChats)
        viewModel.fetchCurrentUser()// TODO delete after testing
    }

}