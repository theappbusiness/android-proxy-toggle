package com.kinandcarta.create.proxytoggle.feature.manager.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.kinandcarta.create.proxytoggle.R
import com.kinandcarta.create.proxytoggle.databinding.FragmentProxyManagerBinding
import com.kinandcarta.create.proxytoggle.feature.manager.viewmodel.ProxyManagerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProxyManagerFragment : Fragment() {

    companion object {
        fun newInstance() = ProxyManagerFragment()
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
            inputLayoutAddress.editText?.setText(proxyAddress)
            inputLayoutPort.editText?.setText(proxyPort)
            inputLayoutAddress.isEnabled = false
            inputLayoutPort.isEnabled = false
            status.text = getString(R.string.proxy_status_enabled)
            toggle.isActivated = true
            toggle.setOnClickListener {
                viewModel.disableProxy()
            }
        }
    }

    private fun showProxyDisabled() {
        with(binding) {
            val lastUsedProxy = viewModel.lastUsedProxy
            if (lastUsedProxy.isEnabled) {
                inputLayoutAddress.editText?.setText(lastUsedProxy.address)
                inputLayoutPort.editText?.setText(lastUsedProxy.port)
            }
            inputLayoutAddress.isEnabled = true
            inputLayoutPort.isEnabled = true
            status.text = getString(R.string.proxy_status_disabled)
            toggle.isActivated = false
            toggle.setOnClickListener {
                viewModel.enableProxy(
                    inputLayoutAddress.editText?.text?.toString() ?: "",
                    inputLayoutPort.editText?.text?.toString() ?: ""
                )
            }
        }
    }
}
