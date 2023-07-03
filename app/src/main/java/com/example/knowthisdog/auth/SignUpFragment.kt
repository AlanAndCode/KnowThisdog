package com.example.knowthisdog.auth

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.knowthisdog.R
import com.example.knowthisdog.databinding.FragmentLoginBinding
import com.example.knowthisdog.databinding.FragmentSignUpBinding


class SignUpFragment : Fragment() {
    interface  SignUpFragmentActions{
        fun  onSignUpFieldsValidated(email: String, password: String,
       passwordConfirmation: String)
    }

    private lateinit var signUpFragmentActions: SignUpFragmentActions


    override fun onAttach(context: Context){
        super.onAttach(context)
        signUpFragmentActions = try {
            context as SignUpFragmentActions
        } catch (e: ClassCastException) {
            throw ClassCastException("#$context must implement LoginFragemetActions")
        }
    }

 private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentSignUpBinding.inflate(inflater)
        setupSignUpButton()
        return binding.root
    }

    private fun setupSignUpButton(){
        binding.signUpButton.setOnClickListener{
            validateFields()
        }
    }

    private fun validateFields() {
        binding.emailInput.error = ""
        binding.passwordInput.error = ""
        binding.confirmPasswordInput.error = ""
        val email = binding.emailEdit.text.toString()

        if (!isValidEmail(email)){
            binding.emailInput.error = getString(R.string.email_not_valid)
                return
        }
        val password = binding.passwordEdit.text.toString()

        if (password.isEmpty()){
            binding.passwordInput.error =getString(R.string.pasword_empty)
            return
        }

        val passwordConfirmation = binding.passwordEdit.text.toString()

        if (passwordConfirmation.isEmpty()){
            binding.confirmPasswordInput.error =getString(R.string.pasword_empty)
            return
        }

        if (password != passwordConfirmation){
            binding.confirmPasswordInput.error = " Pawords do not match"
            return
        }
        signUpFragmentActions.onSignUpFieldsValidated(email, password, passwordConfirmation)
    }



    private fun isValidEmail(email: String?): Boolean {
        return !email.isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}