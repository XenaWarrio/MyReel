package dx.queen.myreel.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import dx.queen.myreel.R
import dx.queen.myreel.databinding.FragmentChatBinding
import dx.queen.myreel.viewModel.ChatViewModel
import dx.queen.myreel.viewModel.ViewModelFactory
import dx.queen.myreel.viewModel.rvChat.ChatAdapter
import dx.queen.myreel.viewModel.rvChats.ChatsItem

class ChatFragment : Fragment() {
    lateinit var binding: FragmentChatBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_chat, container, false
        )
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val chatsItem = requireArguments().getParcelable<ChatsItem>("chatsItem")
        binding.username.text = chatsItem!!.name
        Picasso.get().load(chatsItem.imageUrl).into(binding.companionImage)

        val viewModel = ViewModelProvider(
            this,
            ViewModelFactory(chatsItem.companionId)
        ).get(ChatViewModel::class.java)
        binding.viewModel = viewModel

        binding.btSendMessage.isEnabled = false
        binding.etMessage.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString() != " ") {
                    binding.btSendMessage.isEnabled = true
                }
            }
        })

        val adapter = ChatAdapter()
        binding.rvChat.layoutManager = LinearLayoutManager(context)
        binding.rvChat.adapter = adapter

        val clearFieldObserver = Observer<String> {

            binding.etMessage.text.clear()
            binding.rvChat.scrollToPosition(adapter.itemCount - 1)
        }
        viewModel.clearTextField.observe(viewLifecycleOwner, clearFieldObserver)
    }
}