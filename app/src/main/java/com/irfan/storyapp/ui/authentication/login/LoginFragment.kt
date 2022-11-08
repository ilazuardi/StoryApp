package com.irfan.storyapp.ui.authentication.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.irfan.storyapp.MainActivity
import com.irfan.storyapp.R
import com.irfan.storyapp.data.model.local.UserSession
import com.irfan.storyapp.data.preferences.UserPreferences
import com.irfan.storyapp.databinding.FragmentLoginBinding
import com.irfan.storyapp.ui.authentication.register.RegisterFragment
import com.irfan.storyapp.ui.home.HomeFragment

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val TAG = LoginFragment::class.java.simpleName

    private lateinit var loadingDialog: AlertDialog

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var userPreferences: UserPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        userPreferences = UserPreferences(requireContext())
        initViewModel()
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
        loginViewModel.apply {
            login(email, password)
            user.observe(viewLifecycleOwner) {
                if (it != null) {
                    val currentUser = UserSession(
                        it.name,
                        it.userId,
                        it.token,
                        true
                    )
                    userPreferences.setUser(currentUser)

                    AlertDialog.Builder(requireContext()).apply {
                        setTitle("Login Successfully")
                        setMessage("Logged in as ${currentUser.name}")
                        setPositiveButton("OK") { _, _ ->
                            (activity as MainActivity).moveToFragment(HomeFragment())
                        }
                        create()
                        show()
                    }
                }
            }
        }
    }

    private fun initViewModel() {
        loginViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[LoginViewModel::class.java]

        loginViewModel.apply {
            message.observe(viewLifecycleOwner) {
                Log.d(TAG, "Message = $it")
            }
            isLoading.observe(viewLifecycleOwner) {
                if (it) {
                    loadingDialog.show()
                    binding.loginBtn.textIsEnabled = getString(R.string.str_please_wait) + "..."
                } else {
                    loadingDialog.dismiss()
                    binding.loginBtn.textIsEnabled = getString(R.string.str_sign_in)
                }
            }
            error.observe(viewLifecycleOwner) {
                if (it) {
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
}