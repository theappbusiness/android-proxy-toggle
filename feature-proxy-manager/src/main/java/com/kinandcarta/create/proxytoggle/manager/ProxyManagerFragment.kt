package com.kinandcarta.create.proxytoggle.manager

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.kinandcarta.create.proxytoggle.R

class ProxyManagerFragment : Fragment(R.layout.fragment_proxy_manager) {

    companion object {
        fun newInstance() =
            ProxyManagerFragment()
    }

    private lateinit var viewModel: ProxyManagerViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProxyManagerViewModel::class.java)
        // TODO Use the ViewModel
    }

}