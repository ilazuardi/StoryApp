package com.irfan.storyapp.ui.authentication.login

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
import com.irfan.storyapp.databinding.FragmentLoginBinding
import com.irfan.storyapp.ui.authentication.register.RegisterFragment
import com.irfan.storyapp.ui.home.HomeFragment
import com.irfan.storyapp.ui.main.MainActivity
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@ExperimentalPagingApi
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val TAG = this::class.java.simpleName

    private lateinit var loadingDialog: AlertDialog
    private var loginJob: Job = Job()

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.hide()
        initView()
    }

    private fun initView() {
        binding.apply {
            loginBtn.setOnClickListener {
                if (loginBtn.isEnabled) {
                    val email = binding.edLoginEmail.text.toString().trim()
                    val password = binding.edLoginPassword.text.toString().trim()
                    doLogin(email, password)
                }
            }
            labelRegisterLoginTv.setOnClickListener {
                (activity as MainActivity).moveToFragment(RegisterFragment())
            }
            edLoginEmail.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    setStoryAuthButtonEnable()
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })
            edLoginPassword.addTextChangedListener(object : TextWatcher {
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

    private fun doLogin(email: String, password: String) {
        setLoadingState(true)
        lifecycleScope.launchWhenResumed {
            if (loginJob.isActive) loginJob.cancel()

            loginJob = launch {
                loginViewModel.login(email, password).collect { result ->
                    result.onSuccess { response ->
                        setLoadingState(false)
                        response.loginResult.token.let { token ->
                            loginViewModel.saveUserToken(token)
                            AlertDialog.Builder(requireContext()).apply {
                                setTitle("Login Successfully")
                                setMessage("Logged in as ${response.loginResult.name}")
                                setPositiveButton("OK") { _, _ ->
                                    (activity as MainActivity).moveToFragment(HomeFragment())
                                }
                                create()
                                show()
                            }
                        }

                    }
                    result.onFailure {
                        setLoadingState(false)
                        AlertDialog.Builder(requireContext()).apply {
                            setTitle(getString(R.string.str_login_failed))
                            setMessage("Please try again later !")
                            setPositiveButton("OK") { dialog, _ ->
                                dialog.cancel()
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
            loginBtn.isEnabled = edLoginEmail.isValidate && edLoginPassword.isValidate
            loginBtn.textIsEnabled =
                if (loginBtn.isEnabled) getString(R.string.str_sign_in) else getString(R.string.str_complete_form)
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
            binding.loginBtn.textIsEnabled = getString(R.string.str_please_wait) + "..."
        } else {
            loadingDialog.dismiss()
            binding.loginBtn.textIsEnabled = getString(R.string.str_sign_in)
        }
    }
}