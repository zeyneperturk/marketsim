package com.ctis487.marketsim.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.net.toUri
import com.ctis487.marketsim.R
import com.ctis487.marketsim.databinding.UserProfileBinding
import com.ctis487.marketsim.model.User

class UserProfileView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private var profileEditListener: OnProfileEditListener? = null
    private val binding: UserProfileBinding

    init {
        orientation = VERTICAL

        binding = UserProfileBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )

        binding.imgEdit.setOnClickListener {
            profileEditListener?.onEditClicked(this)
        }
    }

    fun setUser(user: User) {
        binding.username = user.username
        binding.firstName = user.firstName
        binding.lastName = user.lastName

        if (user.iconUrl.isNotEmpty()) {
            binding.imgUser.setImageURI(user.iconUrl.toUri())
        } else {
            binding.imgUser.setImageResource(R.drawable.ic_user_placeholder)
        }

        binding.executePendingBindings()
    }

    fun showEditIcon(show: Boolean) {
        binding.imgEdit.visibility = if (show) View.VISIBLE else View.GONE
    }

    interface OnProfileEditListener {
        fun onEditClicked(view: View)
    }

    fun setOnProfileEditListener(listener: OnProfileEditListener) {
        profileEditListener = listener
    }
}