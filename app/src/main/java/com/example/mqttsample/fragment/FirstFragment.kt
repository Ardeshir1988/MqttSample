package com.example.mqttsample.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mqttsample.MainViewModel
import com.example.mqttsample.R
import com.example.mqttsample.printToast
import kotlinx.android.synthetic.main.fragment_first.*
import java.util.*

class FirstFragment : Fragment() {

    private lateinit var viewModel: MainViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity(), defaultViewModelProviderFactory).get(
            MainViewModel::class.java
        )


        first_btn_login.setOnClickListener {

            if (checkInputs())
                connectToBroker()
            else
                printToast(requireContext(), "Please Enter right values for fields....")
        }

    }


    private fun connectToBroker() {
        showViews(first_pb, first_tv_pb)
        blockInputs()
        // Get IP-Adresse:
        var address = "tcp://" + first_et_ip.text.toString()
        val port = first_et_port.text.toString()
        if (port != "")
            address = "$address:$port"
        val clientID = UUID.randomUUID().toString();

        viewModel.initClient(address, clientID) { status ->
            if (status) {
                val pwd = first_et_pwd.text.toString()
                val user = first_et_name.text.toString()
                // Connection:
                viewModel.connectClient(user, pwd) { status ->
                    hideViews(first_pb, first_tv_pb)
                    if (status) {
                        unblockInputs()
                        findNavController().navigate(R.id.action_first_secpnd)
                    } else {
                        unblockInputs()
                        printToast(
                            requireContext(),
                            "Connection could not be established..."
                        )
                    }

                }
            } else {
                unblockInputs()
            }
        }


    }


    private fun blockInputs() {
        first_et_name.isEnabled = false
        first_et_pwd.isEnabled = false
        first_et_port.isEnabled = false
        first_et_ip.isEnabled = false
        first_btn_login.isEnabled = false
    }

    private fun unblockInputs() {
        first_et_name.isEnabled = true
        first_et_pwd.isEnabled = true
        first_et_port.isEnabled = true
        first_et_ip.isEnabled = true
        first_btn_login.isEnabled = true
    }


    private fun checkInputs(): Boolean {
        var status = true
        if (first_et_name.text.isNullOrEmpty())
            status = false
        else if (first_et_pwd.text.isNullOrEmpty())
            status = false
        else if (first_et_port.text.isNullOrEmpty())
            status = false
        else if (first_et_ip.text.isNullOrEmpty())
            status = false
        return status
    }


    private fun hideViews(vararg views: View) {
        for (i in views)
            i.visibility = View.GONE
    }

    private fun showViews(vararg views: View) {
        for (i in views)
            i.visibility = View.VISIBLE
    }
}