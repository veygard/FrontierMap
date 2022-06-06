package com.veygard.frontiermap.presentation.screens

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.veygard.frontiermap.R
import com.veygard.frontiermap.databinding.FragmentScreenErrorBinding

class ErrorScreenFragment : Fragment() {
    private val binding: FragmentScreenErrorBinding by viewBinding()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_screen_error, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setReconnectListener()
    }

    private fun setReconnectListener() {
        binding.reconnectButton.setOnClickListener {
            if(isInternetAvailable()){
                this.findNavController().navigate(R.id.action_errorScreenFragment_to_mainScreenFragmen)
            }else{
                Toast.makeText(requireContext(), getString(R.string.critical_error_no_internet_error), Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun isInternetAvailable(): Boolean {
        var status = false
        requireContext().let {
            val cm =
                it.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (cm.activeNetwork != null && cm.getNetworkCapabilities(cm.activeNetwork) != null) {
                status = true
            }
        }
        return status
    }
}