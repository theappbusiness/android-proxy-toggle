package com.kinandcarta.create.proxytoggle.feature.manager.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
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

    private var dialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupIcons()

        observeProxyState()
        observeProxyEvent()
    }

    private fun observeProxyEvent() {
        viewModel.proxyEvent.observe(viewLifecycleOwner, Observer { proxyEvent ->
            hideErrors()
            when (proxyEvent) {
                is ProxyManagerEvent.InvalidAddress -> showInvalidAddressError()
                is ProxyManagerEvent.InvalidPort -> showInvalidPortError()
            }
        })
    }

    private fun observeProxyState() {
        viewModel.proxyState.observe(viewLifecycleOwner, Observer { proxyState ->
            when (proxyState) {
                is ProxyState.Enabled -> showProxyEnabled(proxyState.address, proxyState.port)
                is ProxyState.Disabled -> showProxyDisabled()
            }
        })
    }

    @SuppressWarnings("MagicNumber")
    private fun setupIcons() {
        // Info icon
        binding.info.setOnClickListener {
            dialog?.dismiss()
            dialog = AlertDialog.Builder(requireContext())
                .setMessage(R.string.dialog_message_information)
                .setPositiveButton(getString(R.string.dialog_action_close)) { _, _ -> }
                .show()
        }

        // Theme mode icon
//        binding.themeMode.extendTouchArea(20.px)
        binding.themeMode.setOnClickListener { viewModel.toggleTheme() }
    }

    private fun showProxyEnabled(proxyAddress: String, proxyPort: String) {
        hideErrors()
        with(binding) {
            inputLayoutAddress.editText?.setText(proxyAddress)
            inputLayoutPort.editText?.setText(proxyPort)
            inputLayoutAddress.isEnabled = false
            inputLayoutPort.isEnabled = false
            status.text = getString(R.string.proxy_status_enabled)
            toggle.isActivated = true
            toggle.contentDescription = getString(R.string.a11y_disable_proxy)
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
            toggle.contentDescription = getString(R.string.a11y_enable_proxy)
            toggle.setOnClickListener {
                viewModel.enableProxy(
                    inputLayoutAddress.editText?.text?.toString().orEmpty(),
                    inputLayoutPort.editText?.text?.toString().orEmpty()
                )
            }
        }
    }

    private fun showInvalidAddressError() {
        binding.inputLayoutAddress.apply {
            error = getString(R.string.error_invalid_address)
            requestFocusFromTouch()
        }
    }

    private fun showInvalidPortError() {
        binding.inputLayoutPort.apply {
            error = getString(R.string.error_invalid_port)
            requestFocusFromTouch()
        }
    }

    private fun hideErrors() {
        with(binding) {
            inputLayoutAddress.error = null
            inputLayoutPort.error = null
        }
    }
}
