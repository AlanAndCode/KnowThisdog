package com.example.knowthisdog.auth

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.knowthisdog.R


class LoginFragment : Fragment() {

    interface  LoginFragmentActions{
        fun  onRegisterButtonClick()
    }

    private lateinit var loginFragmentActions: LoginFragmentActions


    override fun onAttach(context: Context){
        super.onAttach(context)
        loginFragmentActions = try {
            context as LoginFragmentActions
        } catch (e: ClassCastException) {
            throw ClassCastException("#$context must implement LoginFragemetActions")
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }


}