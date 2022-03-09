package theintership.my.signin_signup

import androidx.lifecycle.ViewModel
import theintership.my.model.user_info

class viewModel_Signin_Signup : ViewModel() {

    var user_info: user_info = user_info(
        email = "",
        fullname = "",
        phone = "",
        sex = "",
        pronoun = "",
        gender = "",
        age = 1,
        birthday = "",
        create_at = "",
        last_login = "",
        lastname = "",
        firstname = ""
    )
    var account_user = ""
    var password_user = ""
    var list_account = mutableListOf<String>()
    var list_phone_number = mutableListOf<String>()
    var list_email_address = mutableListOf<String>()

    fun set_user_info_fullname(fullname: String) {
        user_info.fullname = fullname
    }

    fun set_user_info_lastname(lastname: String) {
        user_info.lastname = lastname
    }

    fun set_user_info_firstname(firstname: String) {
        user_info.firstname = firstname
    }

    fun set_user_info_create_at(create_at: String) {
        user_info.create_at = create_at
    }

    fun set_user_info_last_login(last_login: String) {
        user_info.last_login = last_login
    }

    fun set_user_info_phone(phone: String) {
        user_info.phone = phone
    }

    fun set_user_info_email(email: String) {
        user_info.email = email
    }

    fun set_user_info_sex(sex: String) {
        user_info.sex = sex
    }

    fun set_user_info_age(age: Int) {
        user_info.age = age
    }

    fun set_user_info_birthday(birthday: String) {
        user_info.birthday = birthday
    }

    fun set_user_info_pronoun(pronoun: String) {
        user_info.pronoun = pronoun
    }

    fun set_user_info_gender(gender: String) {
        user_info.gender = gender
    }
}