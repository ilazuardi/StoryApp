package com.irfan.storyapp.ui.authentication.register

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.irfan.storyapp.MainActivity
import com.irfan.storyapp.R
import com.irfan.storyapp.databinding.FragmentRegisterBinding
import com.irfan.storyapp.ui.authentication.login.LoginFragment

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var loadingDialog: AlertDialog

    private val TAG = RegisterFragment::class.java.simpleName

    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        initViewModel()
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
        registerViewModel.register(email, name, password)
    }

    private fun initViewModel() {
        registerViewModel = ViewModelProvider(this)[RegisterViewModel::class.java]

        registerViewModel.apply {
            message.observe(viewLifecycleOwner) {
                Log.d(TAG, "Message = $it")
                Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
                if (it == "User created") {
                    (activity as MainActivity).moveToFragment(LoginFragment())
                }
            }
            isLoading.observe(requireActivity()) {
                Log.d(TAG, "Loading = $it")
                if (it) {
                    loadingDialog.show()
                    binding.registerBtn.textIsEnabled = getString(R.string.str_please_wait) + "..."
                } else {
                    loadingDialog.dismiss()
                    binding.registerBtn.textIsEnabled = getString(R.string.str_sign_up)
                }

            }
            error.observe(viewLifecycleOwner) {
                Log.d(TAG, "error : $it")
                if (it) {
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
}