package com.irfan.storyapp.ui.authentication.register

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.irfan.storyapp.R
import com.irfan.storyapp.databinding.FragmentRegisterBinding
import com.irfan.storyapp.ui.authentication.login.LoginFragment
import com.irfan.storyapp.ui.main.MainActivity
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@ExperimentalPagingApi
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var loadingDialog: AlertDialog

    private val TAG = this::class.java.simpleName

    private var registerJob: Job = Job()

    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        initView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.hide()
    }

    private fun initView() {
        binding.apply {
            registerBtn.setOnClickListener {
                if (registerBtn.isEnabled) {
                    val email = binding.edRegisterEmail.text.toString().trim()
                    val password = binding.edRegisterPassword.text.toString().trim()
                    val name = binding.edRegisterName.text.toString().trim()
                    doRegister(email, name, password)
                }
            }
            labelLoginRegisterTv.setOnClickListener {
                (activity as MainActivity).moveToFragment(LoginFragment())
            }
            edRegisterName.typeFieldText = getString(R.string.name)
            edRegisterEmail.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    setStoryAuthButtonEnable()
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })
            edRegisterPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    setStoryAuthButtonEnable()
                }

                override fun afterTextChanged(p0: Editable?) {

                }
            })
            edRegisterName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    setStoryAuthButtonEnable()
                }

                override fun afterTextChanged(p0: Editable?) {

                }
            })
        }
        initLoadingDialog()
    }

    private fun doRegister(email: String, name: String, password: String) {
        setLoadingState(true)
        lifecycleScope.launchWhenResumed {
            if (registerJob.isActive) registerJob.cancel()

            registerJob = launch {
                registerViewModel.register(email, name, password).collect { result ->
                    result.onSuccess { response ->
                        setLoadingState(false)
                        response.message.let { message ->
                            AlertDialog.Builder(requireContext()).apply {
                                setTitle("Register Successfully")
                                setMessage(message)
                                setPositiveButton("OK") { _, _ ->
                                    (activity as MainActivity).moveToFragment(LoginFragment())
                                }
                                create()
                                show()
                            }
                        }

                    }
                    result.onFailure {
                        setLoadingState(false)
                        AlertDialog.Builder(requireContext()).apply {
                            setTitle("Register Failed")
                            setMessage("Please try again later !")
                            setPositiveButton("OK") { dialog, _ ->
                                dialog.cancel()
                                dialog.dismiss()
                            }
                            create()
                            show()
                        }
                    }
                }
            }
        }
    }

    private fun setStoryAuthButtonEnable() {
        binding.apply {
            registerBtn.isEnabled =
                edRegisterEmail.isValidate && edRegisterPassword.isValidate && !edRegisterName.text.isNullOrBlank()
            registerBtn.textIsEnabled =
                if (registerBtn.isEnabled) getString(R.string.str_sign_up) else getString(R.string.str_complete_form)
        }
    }

    private fun initLoadingDialog() {
        val loadingDialogBuilder = AlertDialog.Builder(requireContext()).apply {
            setTitle(getString(R.string.str_please_wait))
            setMessage(getString(R.string.str_data_being_processed) + "...")
            setCancelable(false)
        }
        loadingDialog = loadingDialogBuilder.create()
    }

    private fun setLoadingState(isLoading: Boolean) {
        if (isLoading) {
            loadingDialog.show()
            binding.registerBtn.textIsEnabled = getString(R.string.str_please_wait) + "..."
        } else {
            loadingDialog.dismiss()
            binding.registerBtn.textIsEnabled = getString(R.string.str_sign_up)
        }
    }
}