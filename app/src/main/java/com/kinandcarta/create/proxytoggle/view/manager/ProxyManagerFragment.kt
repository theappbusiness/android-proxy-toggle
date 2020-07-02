package com.kinandcarta.create.proxytoggle.view.manager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.kinandcarta.create.proxytoggle.databinding.FragmentProxyManagerBinding
import com.kinandcarta.create.proxytoggle.feature.manager.view.ProxyState
import com.kinandcarta.create.proxytoggle.feature.manager.viewmodel.ProxyManagerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProxyManagerFragment : Fragment() {

    companion object {
        fun newInstance() =
            ProxyManagerFragment()

        const val FI_ADDRESS = "192.168.1.215"
        const val FI_PORT = "8888"
    }

    private val binding by lazy { FragmentProxyManagerBinding.inflate(layoutInflater) }
    private val viewModel: ProxyManagerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.proxyState.observe(viewLifecycleOwner, Observer { proxyState ->
            when (proxyState) {
                is ProxyState.Enabled -> showProxyEnabled(proxyState.address, proxyState.port)
                is ProxyState.Disabled -> showProxyDisabled()
            }
        })
    }

    private fun showProxyEnabled(proxyAddress: String, proxyPort: String) {
        with(binding) {
            address.text = proxyAddress
            port.text = proxyPort
            status.text = "Enabled"
            button.text = "Disable proxy"
            button.setOnClickListener { viewModel.disableProxy() }
        }
    }

    private fun showProxyDisabled() {
        with(binding) {
            status.text = "Disabled"
            button.text = "Enable proxy"
            button.setOnClickListener {
                viewModel.enableProxy(
                    FI_ADDRESS,
                    FI_PORT
                )
            }
        }
    }
}
